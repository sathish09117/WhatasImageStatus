package com.smdeveloper.whatsappstatusimages.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.smdeveloper.whatsappstatusimages.Inteface.ItemClickListener;
import com.smdeveloper.whatsappstatusimages.R;

public class BackgroundViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView imageView;
    private ItemClickListener itemClickListener;

    public BackgroundViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView)itemView.findViewById(R.id.bg_image);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
