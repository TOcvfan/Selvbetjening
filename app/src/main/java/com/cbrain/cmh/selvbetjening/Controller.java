package com.cbrain.cmh.selvbetjening;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class Controller extends Activity {

    private String TAG = Controller.class.getSimpleName();
    private String http;
    CustomAdapter adapter;
    public Controller con = null;
    private ListView lv;
    private static String url;
    ArrayList<Selfservice> linkList = new ArrayList<Selfservice>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        con = this;
        http = this.getString(R.string.http);
        url = this.getString(R.string.path1);

        new GetLinks().execute();

        lv = (ListView)findViewById(R.id.list);

        //Resources res = getResources();
        //adapter = new CustomAdapter(con, linkList, res);
        //lv.setAdapter(adapter);
    }

    private class GetLinks extends AsyncTask<Void, Void, List<Selfservice>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected List<Selfservice> doInBackground(Void... arg0) {
                    Document doc;
                    Elements links;
                    List<Selfservice> returnList = null;
                    try {
                        doc = Jsoup.connect(url).timeout(10000).get();
                        links = doc.getElementsByClass("processlink");
                        returnList = ParseHTML(links);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            return returnList;
        }

        @Override
        protected void onPostExecute(final List<Selfservice> result) {
            super.onPostExecute(result);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                    //setSupportActionBar(toolbar);
                    //getSupportActionBar().setDisplayShowTitleEnabled(false);
                    toolbar.setTitle("");
                    toolbar.setSubtitle("");
                    Resources res = getResources();
                    //Log.e(TAG, linkList.toString());
                    linkList = (ArrayList<Selfservice>) result;
                    adapter = new CustomAdapter(con, result, res);
                    adapter.notifyDataSetChanged();
                    lv.setAdapter(adapter);
                }
            });

        }

    }



    public void onItemClick(int mPos)
    {
        Selfservice self = linkList.get(mPos);

        Toast.makeText(con, "" +self.getTitle()+"Link: "+self.getLink(), Toast.LENGTH_LONG).show();
    }

    private ArrayList<Selfservice> ParseHTML(Elements links) throws IOException {

        if (links != null) {

            //final ArrayList<HashMap<String, String>> linkList = new ArrayList<>();

            for (Element link : links) {

                final String linkhref = http + link.select("a").attr("href");
                String linktext = link.text();

                final Selfservice self = new Selfservice(linktext, linkhref);

                self.setTitle(linktext);
                self.setLink(linkhref);
                //Log.i(TAG, "titel: " + linktext + "link: " + linkhref);
                linkList.add(self);

            }
            Log.i(TAG, "" + linkList.size());
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