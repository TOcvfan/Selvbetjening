package com.cbrain.cmh.selvbetjening;

import android.app.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

    private static String url;

    private static final String TITLE = "title";
    private static final String LINK = "link";

    ArrayList<HashMap<String, String>> linkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        linkList = new ArrayList<>();
        url = this.getString(R.string.url);
        lv = (ListView) findViewById(R.id.list);
        new GetLinks().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
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
            // Creating service handler class instance
            Document doc = null;

            Elements links;
            // Making a request to url and getting response

            try {
                doc = Jsoup.connect(url).get();


            } catch (IOException e) {
                e.printStackTrace();
            }
            links = doc.getElementsByClass("processlink");


            Log.d(TAG, "Response from url: "  + links + " : ");


            try {
                linkList = ParseHTML(links);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    Controller.this, linkList,
                    R.layout.list_item, new String[]{TITLE, LINK}, new int[]{R.id.title});

            lv.setAdapter(adapter);
        }

    }

    private ArrayList<HashMap<String, String>> ParseHTML(Elements links) throws IOException {

        if (links != null) {
            // Hashmap for ListView
            ArrayList<HashMap<String, String>> studentList = new ArrayList<HashMap<String, String>>();


            // looping through All Students
            for (Element link : links) {


                String linkhref = "http://lev-demo.cbrain.net" + link.select("a").attr("href");
                String linktext = link.text();

                // Phone node is JSON Object

                // tmp hashmap for single student
                HashMap<String, String> student = new HashMap<String, String>();

                // adding each child node to HashMap key => value
                student.put(TITLE, linktext);
                student.put(LINK, linkhref);


                // adding student to students list
                studentList.add(student);
            }
            return studentList;
        }
        else {
            Log.e(TAG, "Couldn't get json from server.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });

        }

        return null;

    }

}