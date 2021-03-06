package myapplication.app;

        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.Comparator;
        import java.util.HashMap;
        import java.util.List;
        import java.util.StringTokenizer;

        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.location.Location;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.util.Log;
        import android.util.LruCache;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AbsListView;
        import android.widget.AdapterView;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.TextView;

        import com.nhaarman.listviewanimations.ArrayAdapter;
        import com.nhaarman.listviewanimations.itemmanipulation.OnDismissCallback;
        import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;
        import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

        import org.apache.http.NameValuePair;
        import org.apache.http.message.BasicNameValuePair;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

public class Data extends Activity implements OnDismissCallback {

    private GoogleCardsAdapter mGoogleCardsAdapter;
    private static final String	GET_PLACES_URL	= "http://tristanguillevin.fr/getPlacesByTag.php";

    private static final String TAG_ID = "id_place";
    private static final String TAG_NAME = "nom_place";
    private static final String TAG_DESC = "desc_place";
    private static final String TAG_TAG = "label_tag";
    private static final String TAG_GPSX = "gpsx_place";
    private static final String TAG_GPSY = "gpsy_place";
    private static final String TAG_ADDR = "address_place";
    private static final String TAG_OPENINGS = "openings_place";
    private static final String TAG_TAG_ID = "parent_tag_place";

    private static final int IMG_WIDTH = 120;
    private static final int IMG_HEIGHT = 120;

    ArrayList<Place> pList;
    LstPlaces lstPlaces;
    ArrayList<Place> pListFar;
    private ProgressDialog pDialog;

    SharedPreferences preferences;
    SharedPreferences preferencesData;
    ImageButton btn_map;

    HashMap<String, Integer> tagIdMap;

    private static GPSTracker gps;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlecards);

        preferences = getSharedPreferences("SXBN", MODE_PRIVATE);
        preferencesData = getSharedPreferences("SXBN_data", MODE_PRIVATE);

        if(preferences.getString("SXBN_exist", "").equals(""))
        {
            Intent intent = new Intent(Data.this, Questions.class);
            startActivity(intent);
        }

        fillTagIdMap();
        ListView listView = (ListView) findViewById(R.id.activity_googlecards_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String idP = ((TextView) view.findViewById(R.id.activity_googlecard_tv_idPlace)).getText().toString();
                // Starting single contact activity
                Intent in = new Intent(Data.this, PlaceDetails.class);
                in.putExtra(TAG_ID, idP);
                startActivity(in);
            }
        });

        btn_map = (ImageButton) findViewById(R.id.btn_map);
        btn_map.setOnClickListener(openmap);

        mGoogleCardsAdapter = new GoogleCardsAdapter(this);
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(new SwipeDismissAdapter(mGoogleCardsAdapter, this));
        swingBottomInAnimationAdapter.setInitialDelayMillis(300);
        swingBottomInAnimationAdapter.setAbsListView(listView);

        listView.setAdapter(swingBottomInAnimationAdapter);

        gps = new GPSTracker(this);
        new GetPlaces().execute();
    }

    private void fillTagIdMap(){
        tagIdMap = new HashMap<String, Integer>();
        tagIdMap.put("spectacle",1);
        tagIdMap.put("equipsportif",2);
        tagIdMap.put("offtour",3);
        tagIdMap.put("culte", 4);
        tagIdMap.put("parking",5);
        tagIdMap.put("autotrement",6);
        tagIdMap.put("velhop",7);
        tagIdMap.put("eau",8);
        tagIdMap.put("toilette",9);
        tagIdMap.put("pratique",10);
        tagIdMap.put("promenade",11);
        tagIdMap.put("vert",12);
        tagIdMap.put("airejeux",13);
        tagIdMap.put("fontaine",14);
    }

    private ArrayList<Integer> getItems() {
        ArrayList<Integer> items = new ArrayList<Integer>();
        for (int i = 0; i < 100; i++) {
            items.add(i);
        }
        return items;
    }

    @Override
    public void onDismiss(final AbsListView listView, final int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            mGoogleCardsAdapter.remove(position);
        }
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetPlaces extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Data.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            String data = preferencesData.getString("tag","");
            Log.d("Tokens > ",data);
            StringTokenizer tokensTag = new StringTokenizer(data, "_"); // Parse les tags contenus dans les préférences

            int tagTmp;
            ServiceHandler sh;
            pList = new ArrayList<Place>();
            pListFar = new ArrayList<Place>();
            lstPlaces = new LstPlaces();
            // On parcours tous les tags et par chacun on intègre les lieux à la vue.

            while(tokensTag.hasMoreTokens()){
                String tmpTok = tokensTag.nextToken();
                if(!tmpTok.equals("data")){
                    tagTmp = tagIdMap.get(tmpTok);

                    Log.d("Token: ", "> " + tagTmp);

                    // Creating service handler class instance
                    sh = new ServiceHandler();

                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("tagId", String.valueOf(tagTmp)));

                    // Making a request to url and getting response
                    String jsonStr = sh.makeServiceCall(GET_PLACES_URL, ServiceHandler.POST, nvps);

                    Log.d("Response: ", "> " + jsonStr);

                    if (jsonStr != null) {
                        try {
                            Place pTemp;

                            JSONArray jsonArray = new JSONArray(jsonStr);

                            // looping through All Contacts
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.getJSONObject(i);

                                int id = Integer.valueOf(c.getString(TAG_ID));
                                String name = c.getString(TAG_NAME);
                                String desc = c.getString(TAG_DESC);
                                String tag = c.getString(TAG_TAG);
                                int tagId = Integer.valueOf(c.getString(TAG_TAG_ID));
                                double gpsX = Double.valueOf(c.getString(TAG_GPSX));
                                double gpsY = Double.valueOf(c.getString(TAG_GPSY));
                                String address = c.getString(TAG_ADDR);
                                String openings = c.getString(TAG_OPENINGS);

                                double distance = getDistance(gpsX, gpsY);

                                pTemp = new Place(id, name, tag, tagId,  gpsX, gpsY, address, desc, openings, distance);
                                if(distance < 3000)
                                    pList.add(pTemp);
                                else
                                    pListFar.add(pTemp);

                                Collections.sort(pList, new Comparator<Place>()
                                {
                                    public int compare(Place o1, Place o2)
                                    {
                                        return o1.compareTo(o2);
                                    }
                                });

                                lstPlaces.addPlace(pTemp);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            mGoogleCardsAdapter.addAll(pList);
        }
    }

    /* Retourne la distance entre l'utilisateur et les coordonnées passées en parametres*/
    public static float getDistance(double x, double y){
        float[] distances = new float[3];
        Location.distanceBetween(gps.getLatitude(), gps.getLongitude(), x, y, distances);
        return distances[0];
    }

    public View.OnClickListener openmap = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(Data.this, MyMapActivity.class);
            startActivity(intent);
        }
    };

    private static class GoogleCardsAdapter extends ArrayAdapter<Place> {

        private final Context mContext;
        private final LruCache<Integer, Bitmap> mMemoryCache;

        public GoogleCardsAdapter(final Context context) {
            mContext = context;

            final int cacheSize = (int) (Runtime.getRuntime().maxMemory() / 1024);
            mMemoryCache = new LruCache<Integer, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(final Integer key, final Bitmap bitmap) {
                    // The cache size will be measured in kilobytes rather than
                    // number of items.
                    return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
                }
            };
        }

        public View getView(final int position, final View convertView, final ViewGroup parent) {
            Place p = getItem(position);
            View view = convertView;

            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.activity_googlecards_card, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.tv_namePlace = (TextView) view.findViewById(R.id.activity_googlecard_namePlace);
                viewHolder.tv_tagPlace = (TextView) view.findViewById(R.id.activity_googlecard_tagPlace);
                viewHolder.tv_descPlace = (TextView) view.findViewById(R.id.activity_googlecard_descPlace);
                viewHolder.tv_idPlace = (TextView) view.findViewById(R.id.activity_googlecard_tv_idPlace);
                view.setTag(viewHolder);

                viewHolder.imageTag = (ImageView) view.findViewById(R.id.activity_googlecards_imageTag);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }


            viewHolder.tv_namePlace.setText(p.getNamePlace());
            //viewHolder.tv_tagPlace.setText(p.getTagPlace());
            viewHolder.tv_tagPlace.setText(String.format("%.1f",p.getDistanceToUser()/1000)+" km");
            viewHolder.tv_descPlace.setText(p.getDescriptionPlace());
            viewHolder.tv_idPlace.setText(String.valueOf(p.getIdPlace()));

            setImageView(viewHolder, p.getTagId());
/*
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Data.this, PlaceDetails.class);

                }
            });
*/
            return view;
        }

        private void setImageView(final ViewHolder viewHolder, final int tag) {
            int imageResId;
            switch (tag) {
                case 1:
                    imageResId = R.drawable.img_spectacle;
                    break;
                case 2:
                    imageResId = R.drawable.img_equsport;
                    break;
                case 3:
                    imageResId = R.drawable.img_offtour;
                    break;
                case 4:
                    imageResId = R.drawable.img_culte;
                    break;
                case 5:
                    imageResId = R.drawable.img_parking;
                    break;
                case 6:
                    imageResId = R.drawable.img_autotrement;
                    break;
                case 7:
                    imageResId = R.drawable.img_velhop;
                    break;
                case 8:
                    imageResId = R.drawable.img_pteau;
                    break;
                case 9:
                    imageResId = R.drawable.img_toilette;
                    break;
                case 10:
                    imageResId = R.drawable.img_pratique;
                    break;
                case 11:
                    imageResId = R.drawable.img_promenade;
                    break;
                case 12:
                    imageResId = R.drawable.img_verts;
                    break;
                case 13:
                    imageResId = R.drawable.img_jeux;
                    break;
                case 14:
                    imageResId = R.drawable.img_fontaine;
                    break;
                default:
                    imageResId = R.drawable.img_default;
            }

            Bitmap bitmap = getBitmapFromMemCache(imageResId);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageResId);
                bitmap = Bitmap.createScaledBitmap(bitmap, IMG_WIDTH, IMG_HEIGHT, true);
                addBitmapToMemoryCache(imageResId, bitmap);
            }
            viewHolder.imageTag.setImageBitmap(bitmap);
        }

        private void addBitmapToMemoryCache(final int key, final Bitmap bitmap) {
            if (getBitmapFromMemCache(key) == null) {
                mMemoryCache.put(key, bitmap);
            }
        }

        private Bitmap getBitmapFromMemCache(final int key) {
            return mMemoryCache.get(key);
        }

        private static class ViewHolder {
            ImageView imageTag;
            TextView tv_namePlace;
            TextView tv_tagPlace;
            TextView tv_descPlace;
            TextView tv_idPlace;
        }
    }
}