package myapplication.app;

        import java.util.ArrayList;
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
        import android.os.AsyncTask;
        import android.os.Bundle;
        //import android.support.v4.util.LruCache;
        import android.util.Log;
        import android.util.LruCache;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AbsListView;
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

    ArrayList<Place> pList;
    LstPlaces lstPlaces;
    ArrayList<Integer> pListTag;
    private ProgressDialog pDialog;

    SharedPreferences preferences;
    ImageButton btn_map;


    HashMap<String, Integer> tagIdMap;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlecards);

        preferences = getSharedPreferences("SXBN", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("tags", "spectacle_pratique_parking");
        editor.commit();

        if(!preferences.getString("SXBN_exist", "").equals(""))
        {
            Intent intent = new Intent(Data.this, Questions.class);
            startActivity(intent);
        }

        fillTagIdMap();
        ListView listView = (ListView) findViewById(R.id.activity_googlecards_listview);

        btn_map = (ImageButton) findViewById(R.id.btn_map);
        btn_map.setOnClickListener(openmap);

        mGoogleCardsAdapter = new GoogleCardsAdapter(this);
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(new SwipeDismissAdapter(mGoogleCardsAdapter, this));
        swingBottomInAnimationAdapter.setInitialDelayMillis(300);
        swingBottomInAnimationAdapter.setAbsListView(listView);

        listView.setAdapter(swingBottomInAnimationAdapter);

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

            String data = preferences.getString("tags","");
            StringTokenizer tokensTag = new StringTokenizer(data, "_"); // Parse les tags contenus dans les préférences

            int tagTmp;
            ServiceHandler sh;
            pList = new ArrayList<Place>();
            lstPlaces = new LstPlaces();
            // On parcours tous les tags et par chacun on intègre les lieux à la vue.

            while(tokensTag.hasMoreTokens()){
                tagTmp = tagIdMap.get(tokensTag.nextToken());
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
                        pListTag = new ArrayList<Integer>();


                        JSONArray jsonArray = new JSONArray(jsonStr);

                        // looping through All Contacts
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);

                            int id = Integer.valueOf(c.getString(TAG_ID));
                            String name = c.getString(TAG_NAME);
                            String desc = c.getString(TAG_DESC);
                            String tag = c.getString(TAG_TAG);
                            float gpsX = Float.valueOf(c.getString(TAG_GPSX));
                            float gpsY = Float.valueOf(c.getString(TAG_GPSY));
                            String address = c.getString(TAG_ADDR);
                            String openings = c.getString(TAG_OPENINGS);

                            pTemp = new Place(id, name, tag, gpsX, gpsY, address, desc, openings);
                            pList.add(pTemp);
                            lstPlaces.addPlace(pTemp);
                            pListTag.add(id);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
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
            ViewHolder viewHolder;
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.activity_googlecards_card, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.tv_namePlace = (TextView) view.findViewById(R.id.activity_googlecard_namePlace);
                viewHolder.tv_tagPlace = (TextView) view.findViewById(R.id.activity_googlecard_tagPlace);
                viewHolder.tv_descPlace = (TextView) view.findViewById(R.id.activity_googlecard_descPlace);
//                viewHolder.textView = (TextView) view.findViewById(R.id.activity_googlecards_card_textview);
  //              viewHolder.textView = (TextView) view.findViewById(R.id.activity_googlecards_card_textview);
    //            viewHolder.textView = (TextView) view.findViewById(R.id.activity_googlecards_card_textview);
                /*
                viewHolder.tv_namePlace = (TextView) view.findViewById(R.id.activity_googlecard_namePlace);
                viewHolder.tv_tagPlace = (TextView) view.findViewById(R.id.activity_googlecard_tagPlace);
                viewHolder.tv_descPlace = (TextView) view.findViewById(R.id.activity_googlecard_descPlace);
                */
                view.setTag(viewHolder);

                //viewHolder.imageView = (ImageView) view.findViewById(R.id.activity_googlecards_card_imageview);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            Place p = getItem(position);

            viewHolder.tv_namePlace.setText(p.getNamePlace());
            viewHolder.tv_tagPlace.setText(p.getTagPlace());
            viewHolder.tv_descPlace.setText(p.getDescriptionPlace());

            //viewHolder.textView.setText("Id du lieux : " + (getItem(position) + 1));
            //setImageView(viewHolder, position);

            return view;
        }
/*
        private void setImageView(final ViewHolder viewHolder, final int position) {
            int imageResId;
            switch (getItem(position) % 5) {
                case 0:
                    imageResId = R.drawable.img_nature1;
                    break;
                case 1:
                    imageResId = R.drawable.img_nature2;
                    break;
                case 2:
                    imageResId = R.drawable.img_nature3;
                    break;
                case 3:
                    imageResId = R.drawable.img_nature4;
                    break;
                default:
                    imageResId = R.drawable.img_nature5;
            }

            Bitmap bitmap = getBitmapFromMemCache(imageResId);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageResId);
                addBitmapToMemoryCache(imageResId, bitmap);
            }
            viewHolder.imageView.setImageBitmap(bitmap);
        }
*/
        private void addBitmapToMemoryCache(final int key, final Bitmap bitmap) {
            if (getBitmapFromMemCache(key) == null) {
                mMemoryCache.put(key, bitmap);
            }
        }

        private Bitmap getBitmapFromMemCache(final int key) {
            return mMemoryCache.get(key);
        }

        private static class ViewHolder {
            TextView textView;
            ImageView imageView;

            TextView tv_namePlace;
            TextView tv_tagPlace;
            TextView tv_descPlace;
        }
    }
}