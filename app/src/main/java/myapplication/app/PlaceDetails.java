package myapplication.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceDetails extends Activity {

    private static final String	UPDATE_URL	= "http://tristanguillevin.fr/getPlaceById.php";
    private ProgressDialog pDialog;

    private static final String TAG_ID = "id_place";

    private HashMap<String, String> place;

    List<NameValuePair> params ;
    private static final String TAG_NAME = "nom_place";
    private static final String TAG_ADDRESS = "address_place";
    private static final String TAG_DESC = "desc_place";
    private static final String TAG_TAG = "label_tag";
    private static final String TAG_OPENINGS = "openings_place";
    private static final String TAG_GPSX = "gpsx_place";
    private static final String TAG_GPSY = "gpsy_place";
    private static final String TAG_URL = "url_place";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_details);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String id_place = extras.getString(TAG_ID);
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("idPlace", id_place));
            new GetPlaceById().execute();
        }
        else{
            Intent in = new Intent(PlaceDetails.this, Places.class);
            startActivity(in);
        }

    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetPlaceById extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(PlaceDetails.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(UPDATE_URL, ServiceHandler.POST, params);


            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    place = new HashMap<String, String>();
                    place.put(TAG_ID, jsonObj.getString(TAG_ID));
                    place.put(TAG_NAME, jsonObj.getString(TAG_NAME));
                    place.put(TAG_DESC, jsonObj.getString(TAG_DESC));
                    place.put(TAG_ADDRESS, jsonObj.getString(TAG_ADDRESS));
                    place.put(TAG_OPENINGS, jsonObj.getString(TAG_OPENINGS));
                    place.put(TAG_URL, jsonObj.getString(TAG_URL));
                    place.put(TAG_TAG, jsonObj.getString(TAG_TAG));
                    place.put(TAG_GPSX, jsonObj.getString(TAG_GPSX));
                    place.put(TAG_GPSY, jsonObj.getString(TAG_GPSY));

                    Log.d("Place : ", "> "+ place);

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

            TextView name_place = (TextView)findViewById(R.id.tv_place_name_details);
            TextView desc_place = (TextView)findViewById(R.id.tv_desc_place_details);
            TextView address_place = (TextView)findViewById(R.id.tv_address_place_details);
            TextView tag_place = (TextView)findViewById(R.id.tv_tag_place_details);
            TextView openings_place = (TextView) findViewById(R.id.tv_opening_place_details);

            name_place.setText(place.get(TAG_NAME));
            desc_place.setText(place.get(TAG_DESC));
            address_place.setText(place.get(TAG_ADDRESS));
            tag_place.setText(place.get(TAG_TAG));
            openings_place.setText(place.get(TAG_OPENINGS));
            Log.d("Horaires d'ouveture"," > "+place.get(TAG_OPENINGS));
            /**
             * Updating parsed JSON data into ListView
             *
            ListAdapter adapter = new SimpleAdapter(
                    Places.this, placeList,
                    R.layout.list_item, new String[] { TAG_NAME, TAG_DESC,
                    TAG_TAG, TAG_ID }, new int[] { R.id.namePlace,
                    R.id.descPlace, R.id.tagPlace, R.id.tv_idPlace});

            setListAdapter(adapter);
             */
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.place_details, menu);
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
