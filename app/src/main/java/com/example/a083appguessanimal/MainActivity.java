package com.example.a083appguessanimal;



import java.io.IOException;  //REQUIRED FOR WEBCONTENT DOWNLOAD
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



import android.graphics.Bitmap;  // REQUIRED FOR IMAGE DOWNLOAD
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {



    ArrayList<String> IMG_URLS=new ArrayList<String>(); //IMAGE
    ArrayList<String> IMAGE_NAMES=new ArrayList<String>();

    // Download web content  https://www.hindimeaning.com/2016/10/animals-photos-pictures-of-animals.html
    public class DownloadTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls) {
            String HTML_data = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection)url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    HTML_data += current;
                    data = reader.read();
                }
                return HTML_data;
            }
            catch(Exception e) {
                e.printStackTrace();
                return "Failed Sorry !!";
            }
        }

    }

    public String DownloadContent()
    {
        DownloadTask task = new DownloadTask();
        String HTMLCODE= null;
        try
        {
            HTMLCODE = task.execute("http://www.posh24.se/kandisar").get();

        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return HTMLCODE;

    }
    public int list_urls_and_names()
    {
        String HTMLCODE=DownloadContent();
        Log.i("Contents Of URL", HTMLCODE);

        Pattern p=Pattern.compile("<img src=\"(.*?)\" alt=\"");// Pattern here
        Matcher m=p.matcher(HTMLCODE);
        while(m.find())
        {
            String s=m.group(1);
            IMG_URLS.add(s);
            Log.i("HELLO",s);
            Log.i("SIZE",Integer.toString(IMG_URLS.size()));

                    }

       //NAME FINDER
         Pattern pp=Pattern.compile("<div class=\"name\">(,*?)</div>"); //Pattern here
         Matcher mm=pp.matcher(HTMLCODE);
          while(mm.find())
        {
           String n=mm.group(1);;
           Log.i("NAME",n);
           IMAGE_NAMES.add(n);
        }
        return 0;
    }


    //Search the image using erl
    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {

            try {

                URL url = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream inputStream = connection.getInputStream();

                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);

                return myBitmap;


            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();
            }
            return null;
        }
    }


































































    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list_urls_and_names();
    }
}
