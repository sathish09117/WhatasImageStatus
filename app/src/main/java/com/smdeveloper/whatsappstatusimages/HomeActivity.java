package com.smdeveloper.whatsappstatusimages;

import android.content.Intent;
import android.support.multidex.MultiDex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;

import android.view.View;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smdeveloper.whatsappstatusimages.Inteface.ItemClickListener;
import com.smdeveloper.whatsappstatusimages.Model.Category;
import com.smdeveloper.whatsappstatusimages.ViewHolders.MenuViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference category;


    FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;

    RecyclerView recycler_menu;
    private AdView mAdView;

    FloatingActionMenu fab_menu;
    FloatingActionButton image_upload;


    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        MultiDex.install(this);



        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");



        //Load for Menu
        recycler_menu = (RecyclerView)findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplication(),2);
        //recycler_menu.setLayoutManager(gridLayoutManager);
        loadMenu();


        fab_menu = (FloatingActionMenu)findViewById(R.id.fab_menu);
        image_upload = (FloatingActionButton)findViewById(R.id.image_upload);
        image_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent upload = new Intent(HomeActivity.this,UploadWallpaperActivity.class);
                startActivity(upload);
            }
        });


    }

    private void loadMenu() {
        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(
                Category.class,R.layout.menu_item,MenuViewHolder.class,category) {
            @Override
            protected void populateViewHolder(final MenuViewHolder viewHolder, final Category model, int position) {
                viewHolder.txtMenuName.setText(model.getName());
                Picasso.with(getBaseContext())
                        .load(model.getImage())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(viewHolder.imageView, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(getBaseContext())
                                        .load(model.getImage())
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
                final Category clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongVlick) {

                        Intent BackgroundAc = new Intent(HomeActivity.this,ListWallPaperActivity.class);
                        BackgroundAc.putExtra("CategoryId",adapter.getRef(position).getKey());
                        startActivity(BackgroundAc);
                    }
                });
            }
        };
        recycler_menu.setAdapter(adapter);
    }

}
