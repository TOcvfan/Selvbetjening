package com.cbrain.cmh.selvbetjening;

import android.app.*;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Controller extends AppCompatActivity {

    private String TAG = Controller.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    private static String http;
    private static String path1;
    private static String url;

    private static final String TITLE = "title";
    private static final String LINK = "link";

    ArrayList<HashMap<String, String>> linkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        linkList = new ArrayList<>();
        http = this.getString(R.string.http);
        path1 = this.getString(R.string.path1);
        url = http + path1;
        lv = (ListView) findViewById(R.id.list);
        new GetLinks().execute();
    }

    private class GetLinks extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Controller.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Document doc;
            Elements links;

            try {
                doc = Jsoup.connect(url).get();

                links = doc.getElementsByClass("processlink");

                linkList = ParseHTML(links);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing())
                pDialog.dismiss();

            ListAdapter adapter = new SimpleAdapter(
                    Controller.this, linkList,
                    R.layout.list_item, new String[]{TITLE, LINK}, new int[]{R.id.title});

            lv.setAdapter(adapter);
        }

    }

    private ArrayList<HashMap<String, String>> ParseHTML(Elements links) throws IOException {

        if (links != null) {

            ArrayList<HashMap<String, String>> linkList = new ArrayList<>();

            for (Element link : links) {

                final String linkhref = this.getString(R.string.http) + link.select("a").attr("href");
                String linktext = link.text();

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkhref));
                        startActivity(browserIntent);
                    }
                });
                HashMap<String, String> student = new HashMap<>();

                student.put(TITLE, linktext);
                student.put(LINK, linkhref);

                linkList.add(student);

            }

            return linkList;
        }
        else {
            Log.e(TAG, "Couldn't get html from server.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get html from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });

        }
        return null;
    }

}