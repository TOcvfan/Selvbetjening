package com.cbrain.cmh.selvbetjening;

import android.content.Intent;
import android.content.res.Resources;
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

public class Controller extends AppCompatActivity {

    private String TAG = Controller.class.getSimpleName();

    private ListView lv;
    private static String url;
    public Controller customListView = null;

    private ListAdapter adapter;
    private ArrayList<Selfservice> linkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        url = this.getString(R.string.path1);
        linkList = new ArrayList<>();


        customListView = this;
        new GetLinks().execute();

        Resources res =getResources();
        lv = (ListView)findViewById(R.id.list);

        adapter=new CustomAdapter(customListView, linkList,res);

        setupListViewItemClick();
        lv.setAdapter(adapter);
    }

    private class GetLinks extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Document doc;
            Elements links;

            try {
                doc = Jsoup.connect(url).timeout(15*1000).get();
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
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.setTitle("");
            toolbar.setSubtitle("");
        }

    }

    private void setupListViewItemClick() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {

                // Get the student by the position of the adapter.
                Selfservice self = linkList.get(position);
                String linkhref = self.getLink();

                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(linkhref));
                startActivity(browserIntent);
            }
        });
    }

    public void onItemClick(int mPosition)
    {
        Selfservice tempValues = linkList.get(mPosition);


        // SHOW ALERT

        Toast.makeText(customListView, "" +tempValues.getTitle()+"Link: "+tempValues.getLink(), Toast.LENGTH_LONG).show();
    }

    private ArrayList<Selfservice> ParseHTML(Elements links) throws IOException {

        if (links != null) {

            //final ArrayList<HashMap<String, String>> linkList = new ArrayList<>();

            for (Element link : links) {

                //final String linkhref = http + link.select("a").attr("href");
                String linktext = link.text();

                final Selfservice self = new Selfservice();

                self.setTitle(linktext);
                self.setLink(link);
                //Log.i(TAG, "titel: " + linktext + " link: " + linkhref);
                linkList.add(self);
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