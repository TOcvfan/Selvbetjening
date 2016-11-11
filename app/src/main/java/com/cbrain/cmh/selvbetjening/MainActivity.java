package com.cbrain.cmh.selvbetjening;

import android.app.*;
import android.os.*;
import android.util.*;
import android.widget.*;
import org.json.*;
import java.util.*;


public class MainActivity extends ListActivity {


    String url;


    // JSON Node names
    private static final String PROCESSES = "processes";
    private static final String PROCESSLINK = "processlink";
    private static final String PROCESSTITLE = "processtitle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        url = this.getString(R.string.url2);
        // Calling async task to get json
        new GetLinks().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetLinks extends AsyncTask<Void, Void, Void> {

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> linkList;
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
            String jsonStr = webreq.makeWebServiceCall(url, WebRequest.GET);

            Log.d("Response: ", "> " + jsonStr);
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

            setListAdapter(adapter);
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
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
            return null;
        }
    }

}