package myapplication.app;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public class Places extends ListActivity {

    private static final String	UPDATE_URL	= "http://tristanguillevin.fr/getPlaces.php";
    private ProgressDialog pDialog;

    private static final String TAG_ID = "id_place";
    private static final String TAG_NAME = "nom_place";
    private static final String TAG_DESC = "desc_place";
    private static final String TAG_TAG = "label_tag";

    JSONArray places = null;
    ArrayList<HashMap<String, String>> placeList;

    private ArrayList<String> lstPlaces = new ArrayList<String>();
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places);

        placeList = new ArrayList<HashMap<String, String>>();

        lv = getListView();

        // Listview on item click listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String idP = ((TextView) view.findViewById(R.id.tv_idPlace)).getText().toString();
                // Starting single contact activity
                Intent in = new Intent(Places.this, PlaceDetails.class);
                in.putExtra(TAG_ID, idP);
                startActivity(in);
            }
        });

        new GetPlaces().execute();

    }

/**
 * Async task class to get json by making HTTP call
 * */
private class GetPlaces extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
        pDialog = new ProgressDialog(Places.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

    }

    @Override
    protected Void doInBackground(Void... arg0) {
        // Creating service handler class instance
        ServiceHandler sh = new ServiceHandler();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(UPDATE_URL, ServiceHandler.GET);

        Log.d("Response: ", "> " + jsonStr);

        if (jsonStr != null) {
            try {
//                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONArray jsonArray = new JSONArray(jsonStr);
                // Getting JSON Array node
  //              places = jsonObj.getJSONArray("");

                // looping through All Contacts
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);

                    String id = c.getString(TAG_ID);
                    String name = c.getString(TAG_NAME);
                    String desc = c.getString(TAG_DESC);
                    String tag = c.getString(TAG_TAG);

                    // tmp hashmap for single contact
                    HashMap<String, String> place = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    place.put(TAG_ID, id);
                    place.put(TAG_NAME, name);
                    place.put(TAG_DESC, desc);
                    place.put(TAG_TAG, tag);

                    // adding contact to contact list
                    placeList.add(place);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
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
                Places.this, placeList,
                R.layout.list_item, new String[] { TAG_NAME, TAG_DESC,
                TAG_TAG, TAG_ID }, new int[] { R.id.namePlace,
                R.id.descPlace, R.id.tagPlace, R.id.tv_idPlace});

        setListAdapter(adapter);
    }

}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.places, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
