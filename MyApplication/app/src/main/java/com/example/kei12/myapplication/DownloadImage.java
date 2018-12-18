package com.example.kei12.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import java.net.URL;
import java.util.HashMap;

public class DownloadImage extends AsyncTask<Void, Void, Bitmap> {

    private String urlStr;
    private ImageView bmImage;
    private HashMap<String, Bitmap> bitmapHash = new HashMap<>();

    public DownloadImage(String urlStr, ImageView bmImage) {
        this.urlStr = urlStr;
        this.bmImage = bmImage;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap bitmap = null;

        try {
            URL url = new URL(urlStr);
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            bitmapHash.put(urlStr, bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if (bitmap == null) // api에서 사진자료를 받아오지 못했을때
        {
            bmImage.setImageResource(R.drawable.noimage);
        }
        else
        {
            bmImage.setImageBitmap(bitmap);
        }
        bmImage.invalidate();
    }


}
