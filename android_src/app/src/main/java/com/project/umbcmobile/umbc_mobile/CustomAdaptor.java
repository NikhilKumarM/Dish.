package com.project.umbcmobile.umbc_mobile;

/**
 * Created by Nikhil Kumar Mengani on 5/10/2017.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

public class CustomAdaptor extends ArrayAdapter<String> {

    ArrayList<String> resIdArray ;
    ArrayList<String> imageurlArray;
    ArrayList<String> foodnameArray;
    ArrayList<String> reviewArray;
    ArrayList<String> foodqualityArray;
    ArrayList<String> foodPriceArray;
    ArrayList<String> foodserviceArray;
    ArrayList<String> foodratingArray;
    private final Activity context;
   // private final String[] itemname;
    //private final Integer[] imgid;

    public CustomAdaptor(Activity context,  ArrayList<String> resIdArray,ArrayList<String> imageurlArray,  ArrayList<String> foodnameArray,
                         ArrayList<String> reviewArray, ArrayList<String> foodqualityArray,  ArrayList<String> foodPriceArray,
                         ArrayList<String> foodserviceArray, ArrayList<String> foodratingArray) {
        super(context, R.layout.listview_images_layout, resIdArray);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.resIdArray=resIdArray;
        this.imageurlArray= imageurlArray;
        this.foodnameArray= foodnameArray;
        this.reviewArray = reviewArray;
        this.foodqualityArray=foodqualityArray;
        this.foodPriceArray= foodPriceArray;
        this.foodserviceArray=foodserviceArray;
        this.foodratingArray=foodratingArray;
    }
    String prettyFormatting(String instruction)
    {
        char res[] = new char[14];
        String str =instruction;
        char strArray[] = str.toCharArray();
        for(int i=0;i<res.length;i++){
            res[i]= ' ';
        }
        for(int i=0;i<strArray.length;i++){
            res[i]=strArray[i];
        }
        res[13]=' ';
        String result = new String(res);
        return result;
    }
    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_images_layout, null,true);

        TextView txtTitle1 = (TextView) rowView.findViewById(R.id.Itemname1);
        TextView txtTitle2= (TextView) rowView.findViewById(R.id.Itemname2);
        TextView txtTitle3 = (TextView) rowView.findViewById(R.id.Itemname3);
        TextView txtTitle4 = (TextView) rowView.findViewById(R.id.Itemname4);
        TextView txtTitle5 = (TextView) rowView.findViewById(R.id.Itemname5);
        TextView txtTitle6 = (TextView) rowView.findViewById(R.id.Itemname6);
       // TextView txtTitle7 = (TextView) rowView.findViewById(R.id.Itemname7);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

//        txtTitle1.setText(prettyFormatting("Food Name")  +foodnameArray.get(position));
//        txtTitle2.setText(prettyFormatting("Review")     +reviewArray.get(position));
//        txtTitle3.setText(prettyFormatting("Quality")    +foodqualityArray.get(position));
//        txtTitle4.setText(prettyFormatting("Price")      +foodPriceArray.get(position));
//        txtTitle5.setText(prettyFormatting("Service(10)")+foodserviceArray.get(position));
//        txtTitle6.setText(prettyFormatting("Rating(10)") +foodratingArray.get(position));
        //txtTitle1.setText(resIdArray.get(position));
        txtTitle1.setText(foodnameArray.get(position));
        txtTitle2.setText(reviewArray.get(position));
        txtTitle3.setText(foodqualityArray.get(position));
        txtTitle4.setText(foodPriceArray.get(position));
        txtTitle5.setText(foodserviceArray.get(position));
        txtTitle6.setText(foodratingArray.get(position));
        String url="";
        try {
            url= URLDecoder.decode(imageurlArray.get(position), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        new DownloadImageTask(imageView).execute(url);
        //imageView.setImageResource(imgid[position]);

        return rowView;

    };


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}