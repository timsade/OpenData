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

    private static final String TAG_PLACES = "places";
    private static final String TAG_ID = "id_place";
    private static final String TAG_NAME = "nom_place";
    private static final String TAG_ADDRESS = "address_place";
    private static final String TAG_DESC = "desc_place";
    private static final String TAG_TAG = "label_tag";

    JSONArray places = null;
    ArrayList<HashMap<String, String>> placeList;

    private ArrayList<String> lstPlaces = new ArrayList<String>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places);

        placeList = new ArrayList<HashMap<String, String>>();

        ListView lv = getListView();

        // Listview on item click listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String name = ((TextView) view.findViewById(R.id.namePlace))
                        .getText().toString();
                String tag = ((TextView) view.findViewById(R.id.tagPlace))
                        .getText().toString();
                String address = ((TextView) view.findViewById(R.id.addressPlace))
                        .getText().toString();
                String desc = ((TextView) view.findViewById(R.id.descPlace))
                        .getText().toString();

                // Starting single contact activity
                Intent in = new Intent(Places.this, PlaceDetails.class);
                in.putExtra(TAG_NAME, name);
                in.putExtra(TAG_TAG, tag);
                in.putExtra(TAG_ADDRESS, address);
                in.putExtra(TAG_DESC, desc);
                startActivity(in);

            }
        });

        new GetContacts().execute();
/*
        JSONArray jArray = null;
        String result = null;
        StringBuilder sb = null;
        InputStream is = null;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        DefaultHttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);

        HttpResponse response;
        HttpEntity entity;

        lstPlaces.add("Place 1");
        lstPlaces.add("Place 2");
        lstPlaces.add("Place 3");
        lstPlaces.add("Place 4");
        lstPlaces.add("Place 5");
        lstPlaces.add("Place 6");
        lvRefresh();

        try
        {
            // On établit un lien avec le script PHP
            HttpPost post = new HttpPost(UPDATE_URL);
            post.setHeader("Content-type", "application/json");
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = client.execute(post);
            entity = response.getEntity();
            is = entity.getContent();
        }catch(Exception e){
            Log.e("log_tag", "Error in http connection" + e.toString());
        }

//convert response to string
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");

            String line="0";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result=sb.toString();
        }catch(Exception e){
            Log.e("log_tag", "Error converting result "+e.toString());
        }

        String name;
        try{
            jArray = new JSONArray(result);
            JSONObject json_data=null;

            for(int i=0;i<jArray.length();i++){
                json_data = jArray.getJSONObject(i);
                lstPlaces.add(json_data.getString("nom_place"));//here "Name" is the column name in database
            }
            lvRefresh();
        }
        catch(JSONException e1){
            Toast.makeText(getBaseContext(), "No Data Found", Toast.LENGTH_LONG).show();
        }

*/

    }

/**
 * Async task class to get json by making HTTP call
 * */
private class GetContacts extends AsyncTask<Void, Void, Void> {

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
                    String address = c.getString(TAG_ADDRESS);
                    String desc = c.getString(TAG_DESC);
                    String tag = c.getString(TAG_TAG);

                    // Phone node is JSON Object
                    /*JSONObject phone = c.getJSONObject(TAG_PHONE);
                    String mobile = phone.getString(TAG_PHONE_MOBILE);
                    String home = phone.getString(TAG_PHONE_HOME);
                    String office = phone.getString(TAG_PHONE_OFFICE);*/

                    // tmp hashmap for single contact
                    HashMap<String, String> place = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    place.put(TAG_ID, id);
                    place.put(TAG_NAME, name);
                    place.put(TAG_ADDRESS, address);
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
                R.layout.list_item, new String[] { TAG_NAME, TAG_ADDRESS,
                TAG_DESC, TAG_TAG }, new int[] { R.id.namePlace,
                R.id.addressPlace, R.id.descPlace, R.id.tagPlace});

        setListAdapter(adapter);
    }

}
/*
    private void lvRefresh() {
        LogAdapter logAdapter = new LogAdapter(lstPlaces);
        listView.setAdapter(logAdapter);

    }
*/
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
/*
    private class LogAdapter extends BaseAdapter {

        private ArrayList stringList;

        public LogAdapter(ArrayList arraylistLog) {
            // TODO Auto-generated constructor stub
            this.stringList = arraylistLog;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return stringList.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return stringList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            TextView v = (TextView) new TextView(getBaseContext());
            v.setText((CharSequence) this.stringList.get(arg0));
            return v;
        }
    }
    */
}
