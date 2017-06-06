package com.project.umbcmobile.umbc_mobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ShowPhotosActivity extends AppCompatActivity {

    ListView list;
    ArrayList<String> resIdArray = new ArrayList<String>();
    ArrayList<String> imageurlArray= new ArrayList<String>();
    ArrayList<String> foodnameArray= new ArrayList<String>();
    ArrayList<String> reviewArray= new ArrayList<String>();
    ArrayList<String> foodqualityArray= new ArrayList<String>();
    ArrayList<String> foodPriceArray= new ArrayList<String>();
    ArrayList<String> foodserviceArray= new ArrayList<String>();
    ArrayList<String> foodratingArray= new ArrayList<String>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photos);
        Intent fromMapsIntent= (Intent)getIntent();
        String ID = fromMapsIntent.getStringExtra("ID");
        String resname_ = fromMapsIntent.getStringExtra("resname");
        TextView resn = (TextView) findViewById(R.id.resname);
        resn.setText(resname_);
        new ShowPhotosActivity.InvokeWeService().execute(ID);


    }

    void callAdaptor()
    {
        CustomAdaptor adapter=new CustomAdaptor(this, resIdArray,imageurlArray,foodnameArray,reviewArray,
                foodqualityArray,foodPriceArray,foodserviceArray,foodratingArray);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                // TODO Auto-generated method stub
//                String Slecteditem= itemname[+position];
//                Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }
    public class InvokeWeService extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            URL url;
            String response = "";
            String requestUrl = "http://ec2-54-213-169-9.us-west-2.compute.amazonaws.com/getimages.php";
            StringBuilder str = new StringBuilder();
            StringBuilder result = new StringBuilder();
            //str.append("test=" + "parameter&");
            str.append("?resID=" + strings[0]);
            String mystring = str.toString();
            requestUrl = requestUrl +mystring;
            try {
                url = new URL(requestUrl);
                HttpURLConnection myconnection = (HttpURLConnection) url.openConnection();
                myconnection.setReadTimeout(15000);
                myconnection.setConnectTimeout(15000);
                myconnection.setRequestMethod("GET");
                myconnection.setDoInput(true);
                myconnection.setDoOutput(true);
                /*
                OutputStream os = myconnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));


                writer.write(mystring);
                writer.flush();
                writer.close();
                */
                //String line;
                //int responseCode = myconnection.getResponseCode();
                if (200 == HttpURLConnection.HTTP_OK) ;
                {


                    InputStream in = url.openStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    //System.out.println(result.toString());

                    //  Toast.makeText(getApplicationContext(), "Uploaded Successfully", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result.toString();


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //check.setText(s);
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(s);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray innerJsonArray = jsonArray.getJSONArray(i);
                    switch (i) {
                        case 0:
                            addingElementsToArray(resIdArray, innerJsonArray);
                            break;
                        case 1:
                            addingElementsToArray(imageurlArray, innerJsonArray);
                            break;
                        case 2:
                            addingElementsToArray(foodnameArray, innerJsonArray);
                            break;
                        case 3:
                            addingElementsToArray(reviewArray, innerJsonArray);
                            break;
                        case 4:
                            addingElementsToArray(foodqualityArray, innerJsonArray);
                            break;
                        case 5:
                            addingElementsToArray(foodPriceArray, innerJsonArray);
                            break;
                        case 6:
                            addingElementsToArray(foodserviceArray, innerJsonArray);
                            break;
                        case 7:
                            addingElementsToArray(foodratingArray, innerJsonArray);
                            break;
                    }
                    // String
                    // urls.add(image_comment);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            callAdaptor();

        }
    }

    void addingElementsToArray(ArrayList<String> al,JSONArray jsonArray){
        for(int i=0;i<jsonArray.length();i++){
            try {
                al.add(jsonArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}