package com.smdeveloper.whatsappstatusimages;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.smdeveloper.whatsappstatusimages.Notification.Config.Config;
import com.squareup.picasso.Picasso;

public class Display extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        imageView = (ImageView)findViewById(R.id.imageView1);

        if (!TextUtils.isEmpty(Config.imageLink))
            Picasso.with(this)
            .load(Config.imageLink)
            .into(imageView);

    }
}
