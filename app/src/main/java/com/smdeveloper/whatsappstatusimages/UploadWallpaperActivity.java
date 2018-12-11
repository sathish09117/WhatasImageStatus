package com.smdeveloper.whatsappstatusimages;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.smdeveloper.whatsappstatusimages.Common.Common;
import com.smdeveloper.whatsappstatusimages.Model.Backgrounds;
import com.smdeveloper.whatsappstatusimages.Model.Category;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UploadWallpaperActivity extends AppCompatActivity {

    ImageView imageViewPreview;
    Button btn_upload,btn_browser;
    MaterialSpinner spinner;

    //MaterialSpinner Data
    Map<String,String> spinnerData = new HashMap<>();

    private Uri filePath;
    String categoryIdSelect="";

    //FirebaseStoreage
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_wallpaper);
        MultiDex.install(this);

        //Init
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //View
        imageViewPreview = (ImageView)findViewById(R.id.image_preview);
        btn_browser  = (Button)findViewById(R.id.btn_browse);
        btn_upload = (Button)findViewById(R.id.btn_upload);
        spinner = (MaterialSpinner)findViewById(R.id.spinner);

        loadCategoryToSpinner();


        btn_browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedIndex() == 0)
                    Toast.makeText(UploadWallpaperActivity.this, "Please Choose Category", Toast.LENGTH_SHORT).show();
                else
                    uploadImage();
            }
        });

    }

    private void uploadImage() {

        if (filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            StorageReference reference = storageReference.child(new StringBuilder("images/").append(UUID.randomUUID().toString())
            .toString());

            reference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            saveUrlToCategroy(categoryIdSelect,taskSnapshot.getDownloadUrl().toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UploadWallpaperActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded : "+(int)progress+"%" );
                        }
                    });
        }


    }

    private void saveUrlToCategroy(String categoryIdSelect, String imageLink) {
        FirebaseDatabase.getInstance()
                .getReference(Common.STR_BACKGROUNDS)
                .push()
                .setValue(new Backgrounds(imageLink,categoryIdSelect))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UploadWallpaperActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            filePath = data.getData();
            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imageViewPreview.setImageBitmap(bitmap);
                btn_upload.setEnabled(true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture:"),Common.PICK_IMAGE_REQUEST);
    }

    private void loadCategoryToSpinner() {
        FirebaseDatabase.getInstance()
                .getReference(Common.STR_CATEGORY_BACKGROUND)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapShot:dataSnapshot.getChildren())
                        {
                            Category item = postSnapShot.getValue(Category.class);
                            String key = postSnapShot.getKey();

                            spinnerData.put(key,item.getName());
                        }
                        //Because Material Spinner will not receive hint so we need custom hint
                        //this is my tip
                        Object[] valueArray = spinnerData.values().toArray();
                        List<Object> valueList  = new ArrayList<>();
                        valueList.add(0,"Category");//We will add first item is Hint:D
                        valueList.addAll(Arrays.asList(valueArray));
                        spinner.setItems(valueList);
                        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                                //when will choose category,we will get categoryId (key)
                                Object[] keyArray = spinnerData.keySet().toArray();
                                List<Object> keyList  = new ArrayList<>();
                                keyList.add(0,"Category_key");//We will add first item is Hint:D
                                keyList.addAll(Arrays.asList(keyArray));
                                categoryIdSelect = keyList.get(position).toString();//assign key when user choose category

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
 