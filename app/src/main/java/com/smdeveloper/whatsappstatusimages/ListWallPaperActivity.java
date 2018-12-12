package com.smdeveloper.whatsappstatusimages;

import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smdeveloper.whatsappstatusimages.Common.Common;
import com.smdeveloper.whatsappstatusimages.Inteface.ItemClickListener;
import com.smdeveloper.whatsappstatusimages.Model.Backgrounds;
import com.smdeveloper.whatsappstatusimages.ViewHolders.BackgroundViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ListWallPaperActivity extends AppCompatActivity {


    DatabaseReference background;

    RecyclerView recycler_Background;
    // RecyclerView.LayoutManager layoutManager;

    String categoryId="";

    AdView mAdView;


    FirebaseRecyclerAdapter<Backgrounds,BackgroundViewHolder> BackgroundAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_wall_paper);

        MultiDex.install(this);


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);





        //Init Firebase

        background = FirebaseDatabase.getInstance().getReference().child("Backgrounds");




        //Load for Menu
        recycler_Background = findViewById(R.id.recycler_back);
        recycler_Background.setHasFixedSize(true);
        //layoutManager = new LinearLayoutManager(this);
        //recycler_Background.setLayoutManager(layoutManager);
        GridLayoutManager Backgrid = new GridLayoutManager(getApplication(),3);
        recycler_Background.setLayoutManager(Backgrid);

        //Get Intent here
        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty())
        {
            loadBackground();
        }
        else
        {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }


    }

    private void loadBackground()
    {
        BackgroundAdapter = new FirebaseRecyclerAdapter<Backgrounds, BackgroundViewHolder>(
                Backgrounds.class,
                R.layout.user_single_list,
                BackgroundViewHolder.class,
                background) {
            @Override
            protected void populateViewHolder(final BackgroundViewHolder viewHolder, final Backgrounds model, int position) {

                String back_id = getRef(position).getKey();
                background.child(back_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Picasso.with(getBaseContext())
                                .load(model.getImagelink())
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .into(viewHolder.imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Picasso.with(getBaseContext())
                                                .load(model.getImagelink())
                                                .error(R.drawable.ic_terrain_black_24dp)
                                                .into(viewHolder.imageView, new Callback() {
                                                    @Override
                                                    public void onSuccess() {

                                                    }

                                                    @Override
                                                    public void onError() {
                                                        Log.e("ERROR_SMTT","Couldn't fetch image");

                                                    }
                                                });
                                    }
                                });

                        viewHolder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongVlick) {
                                Intent WallPaperintent = new Intent(ListWallPaperActivity.this,ViewWallpaperActivity.class);
                                Common.Selected_background = model;
                                startActivity(WallPaperintent);
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

             }
        };
        recycler_Background.setAdapter(BackgroundAdapter);
    }
}
