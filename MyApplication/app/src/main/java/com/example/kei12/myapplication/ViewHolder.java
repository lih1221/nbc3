package com.example.kei12.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public  class ViewHolder extends RecyclerView.ViewHolder{

    public ImageView imageView;
    public TextView titleView;
    public RatingBar rateView;
    public TextView yearView;
    public TextView directorView;
    public TextView actorView;

    public ViewHolder(View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.img);
        titleView = itemView.findViewById(R.id.mvName);
        rateView = itemView.findViewById(R.id.star_rate);
        yearView = itemView.findViewById(R.id.year);
        directorView = itemView.findViewById(R.id.director);
        actorView = itemView.findViewById(R.id.actor);
    }
}