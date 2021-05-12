package com.example.prj_2_1602414;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String totalDistance;
    ArrayList<stepObject> arr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Intent intent2 = getIntent();
        String startLocation2 = intent2.getStringExtra("starting");
        String endLocation2 = intent2.getStringExtra("ending");
        PostApi(startLocation2,endLocation2);
    }

    public void PostApi(String Startp,String Endp){

        new AsyncTask<String,String,String>(){
            @Override
            protected String doInBackground(String... strings) {
                RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
                String url ="https://maps.googleapis.com/maps/api/directions/json?origin=" + Startp + "&destination=" + Endp + "&key=AIzaSyBAiCJqZJ3u7GEneEOP5cbbXnfMxMxcQKY";
                // String url ="https://maps.googleapis.com/maps/api/directions/json?origin=istanbul&destination=ankara&key=AIzaSyBAiCJqZJ3u7GEneEOP5cbbXnfMxMxcQKY";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    JSONArray routes = new JSONArray(jsonObject.getString("routes"));
                                    JSONObject r2 = new JSONObject();
                                    r2 = (JSONObject) routes.get(0);
                                    JSONArray legs = new JSONArray(r2.getString("legs"));
                                    JSONObject l2 = new JSONObject();
                                    l2 = (JSONObject) legs.get(0);
                                    JSONArray steps = new JSONArray(l2.getString("steps"));

                                    for(int i = 0; i < steps.length(); i++){
                                        JSONObject s2 = new JSONObject();
                                        s2 = (JSONObject) steps.get(i);
                                        JSONObject startLocation = new JSONObject(s2.getString("start_location"));
                                        arr.add(new stepObject (Double.parseDouble(startLocation.getString("lat")),Double.parseDouble(startLocation.getString("lng"))));
                                        JSONObject endLocation = new JSONObject(s2.getString("end_location"));
                                        arr.add(new stepObject (Double.parseDouble(endLocation.getString("lat")),Double.parseDouble(endLocation.getString("lng"))));
                                        Log.e("deneme1", arr.get(i).getxPoint() + " " + arr.get(i).getyPoint());
                                    }
                                    Log.e("array",String.valueOf(steps.length()));
                                    JSONObject distance = new JSONObject(l2.getString("distance"));
                                    totalDistance = distance.getString("text");

                                    ArrayList<LatLng> pointss = new ArrayList<>();
                                    Log.e("anil",String.valueOf(arr.get(0).getxPoint()));
                                    for(int i = 0; i < arr.size(); i++){
                                        double lat = (arr.get(i).getxPoint());
                                        double lng = (arr.get(i).getyPoint());
                                        LatLng position = new LatLng(lat, lng);
                                        pointss.add(position);
                                    }

                                    PolylineOptions lineOptions = new PolylineOptions();;
                                    lineOptions.addAll(pointss);
                                    lineOptions.width(12);
                                    lineOptions.color(Color.RED);
                                    lineOptions.geodesic(true);
                                    mMap.addPolyline(lineOptions);
                                    LatLng ilk = new LatLng(arr.get(0).getxPoint(),arr.get(0).getyPoint());
                                    mMap.addMarker(new MarkerOptions().position(ilk).title("ilk"));
                                    mMap.addPolyline(lineOptions);
                                    LatLng son = new LatLng(arr.get(arr.size()-1).getxPoint(),arr.get(arr.size()-1).getyPoint());
                                    mMap.addMarker(new MarkerOptions().position(son).title("son"));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(ilk));

                                }catch (JSONException err){
                                    Log.d("Error", err.toString());
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }

                });
                queue.add(stringRequest);
                return null;

            }
        }.execute();
    }
}


