package com.smdeveloper.whatsappstatusimages;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import android.widget.RelativeLayout;

import com.github.clans.fab.FloatingActionMenu;
import com.smdeveloper.whatsappstatusimages.Common.Common;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.disposables.CompositeDisposable;

public class ViewWallpaperActivity extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton fabWallPaper;
    ImageView imageViewWallpaper;
    RelativeLayout rootLayout;

    String backgroundsId="";

    FloatingActionMenu viewFabMenu;
    com.github.clans.fab.FloatingActionButton fab_share,fab_setWall;
    //Room Database

    CompositeDisposable compositeDisposable;

        private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());

            try {
                wallpaperManager.setBitmap(bitmap);
                Snackbar.make(rootLayout,"Wallpaper Was Set",Snackbar.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wallpaper);

        MultiDex.install(this);

        rootLayout =(RelativeLayout) findViewById(R.id.root_layout);
        imageViewWallpaper = (ImageView)findViewById(R.id.image_thumb);
        Picasso.with(this)
                .load(Common.Selected_background.getImagelink())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(imageViewWallpaper);

        viewFabMenu = (FloatingActionMenu)findViewById(R.id.view_fab_menu);
        fab_share = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.share);


        fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareContent();
            }
        });

        

    //   fab_setWall=(com.github.clans.fab.FloatingActionButton)findViewById(R.id.setwallpaper);

/*
        fab_setWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(getBaseContext())
                        .load(Common.Selected_background.getImagelink())
                        .into(target);
            }
        });
*/
    }
    private void shareContent(){

        Bitmap bitmap =getBitmapFromView(imageViewWallpaper);
        try {
            File file = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                file = new File(this.getExternalCacheDir(),"logicchip.png");
            }
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                file.setReadable(true, false);
            }
            final Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //      intent.putExtra(Intent.EXTRA_TEXT, movieName);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent, "Share image via"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            bgDrawable.draw(canvas);
        }   else{
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

}
