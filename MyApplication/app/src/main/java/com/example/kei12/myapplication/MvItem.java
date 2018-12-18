package com.example.kei12.myapplication;

public class MvItem {

    public String image;
    public String title;
    public String pubDate;
    public String director;
    public String actor;
    public String userRating;
    public String link;

    public MvItem(String link,
                  String image,
                  String title,
                  String pubDate,
                  String director,
                  String actor,
                  String userRating) {
        this.link = link;
        this.image = image;
        this.title = title;
        this.pubDate = pubDate;
        this.director = director;
        this.actor = actor;
        this.userRating = userRating;
    }
}