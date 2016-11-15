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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    String url;

    // JSON Node names
    private static final String PROCESSES = "processes";
    private static final String PROCESSLINK = "processlink";
    private static final String PROCESSTITLE = "processtitle";

    ArrayList<HashMap<String, String>> linkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        linkList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);
        new GetLinks().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetLinks extends AsyncTask<Void, Void, Void> {

        // Hashmap for ListView

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            // Making a request to url and getting response
            String jsonStr = webreq.makeServiceCall(url);

            Log.d(TAG, "Response from url: " + jsonStr);
            // place json parse here
            linkList = ParseJSON(jsonStr);

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
                    MainActivity.this, linkList,
                    R.layout.list_item, new String[]{PROCESSLINK, PROCESSTITLE}, new int[]{R.id.processlink,
                    R.id.processtitle});

            lv.setAdapter(adapter);
        }

    }

    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        if (json != null) {
            try {
                // Hashmap for ListView
                ArrayList<HashMap<String, String>> linkList = new ArrayList<HashMap<String, String>>();

                JSONObject jsonObj = new JSONObject(json);

                // Getting JSON Array node
                JSONArray links = jsonObj.getJSONArray(PROCESSES);

                // looping through All Students
                for (int i = 0; i < links.length(); i++) {
                    JSONObject c = links.getJSONObject(i);

                    String pLink = c.getString(PROCESSLINK);
                    String pTitle = c.getString(PROCESSTITLE);

                    // tmp hashmap for single link
                    HashMap<String, String> link = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    link.put(PROCESSLINK, pLink);
                    link.put(PROCESSTITLE, pTitle);

                    // adding link to links list
                    linkList.add(link);
                }
                return linkList;
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
        } else {
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