package com.example.kei12.myapplication;
import android.content.Context;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyleAdapter extends RecyclerView.Adapter<ViewHolder>{


    public String image;

    private ArrayList<MvItem> movies;

    RecyleAdapter(ArrayList<MvItem> items1) {
        this.movies = items1;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View itemView = layoutInflater.inflate(R.layout.mv_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final MvItem movieItem = movies.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) { // chrome custom tab
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                builder.setShowTitle(true);
                customTabsIntent.launchUrl(v.getContext(), Uri.parse(movieItem.link));
            }
        });

        TextView mtitle = holder.titleView; // movie title
        Spanned title = Html.fromHtml(movieItem.title);
        String strData = title.toString();      // 해당 과정을 거치지 않으면 edittext에 입력한 검색 부분에 bold 태그가 같이 달려서 나오게 됩니다.
        mtitle.setText(strData);                // ex ("<b>검색어</b>")

        RatingBar ratingBar = holder.rateView; // movie rating
        ratingBar.setIsIndicator(true);
        ratingBar.setRating((float) (Float.parseFloat(movieItem.userRating)*0.5));

        TextView myear = holder.yearView; // movie year
        myear.setText(movieItem.pubDate);

        TextView direct = holder.directorView; // movie dierctor
        Spanned director = Html.fromHtml(movieItem.director);
        String directorName = director.toString();
        if (directorName.length() > 0)
        {
            directorName = directorName.replace("|",", ");
            directorName = directorName.substring(0, directorName.length()-2);
        }
        direct.setText(directorName);

        TextView act = holder.actorView; // movie actors
        Spanned actor = Html.fromHtml(movieItem.actor);
        String actorName = actor.toString();
        if (actorName.length() > 0)
        {
            actorName = actorName.replace("|",", ");
            actorName = actorName.substring(0, actorName.length()-2);
        }
        act.setText(actorName);

//
        ImageView imageView = holder.imageView; // movie image
        imgAsync(movieItem.image, imageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void imgAsync(String str, ImageView imageView) {
        DownloadImage task = new DownloadImage(str, imageView);
        task.execute();
    }


}