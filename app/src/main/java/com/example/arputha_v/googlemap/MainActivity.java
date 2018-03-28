package com.example.arputha_v.googlemap;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, LocationListener, GoogleMap.InfoWindowAdapter {

    private GoogleMap mMap;
    private TextView tvLatLong;
    private LinearLayout llCategory;
    private ViewGroup infoWindow;
    private ListView markerList;
    private MapWrapperLayout mapWrapperLayout;
    private OnInfoWindowElemTouchListener infoButtonListener;

    private double latitude;
    private double longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60; // 1 minute
    protected LocationManager locationManager;

    private Categories categoriesModel;
    private List<Categories> mCategoryItems;
    private List<Locations> mLocationItems;
    private List<EventsModel> mDataset;

    private List<String> categories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_relative_layout);
        mapWrapperLayout.init(mMap, getPixelsFromDp(this, 39 + 20));

        tvLatLong = (TextView) findViewById(R.id.tvLatLong);
        llCategory = findViewById(R.id.category);

        infoWindow = (ViewGroup) getLayoutInflater().inflate(R.layout.custom_marker_layout, null);
        markerList = infoWindow.findViewById(R.id.lv_marker);
        markerList.setOnTouchListener(infoButtonListener);

        this.infoButtonListener = new OnInfoWindowElemTouchListener(markerList,
                getResources().getDrawable(R.color.colorAccent), //btn_default_normal_holo_light
                getResources().getDrawable(R.color.colorPrimary)) //btn_default_pressed_holo_light
        {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                // Here we can perform some action triggered after clicking the button
                Toast.makeText(MainActivity.this, marker.getTitle() + "'s button clicked!", Toast.LENGTH_SHORT).show();
            }
        };


        mDataset = new ArrayList<>();
        categories = new ArrayList<>();
        getMyLocation();
        Button btnNormal = (Button) findViewById(R.id.btn_normal);
        Button btnHybrid = (Button) findViewById(R.id.btn_hybrid);
        Button btnSatellite = (Button) findViewById(R.id.btn_satellite);
        Button btnTerrain = (Button) findViewById(R.id.btn_terrain);
        btnNormal.setOnClickListener(this);
        btnHybrid.setOnClickListener(this);
        btnSatellite.setOnClickListener(this);
        btnTerrain.setOnClickListener(this);
        ApiCall();


//        categoryItems();
        setCategories();
    }

    private void ApiCall() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        EventsModel[] data = gson.fromJson(loadJSONFromAsset(), EventsModel[].class);
        mDataset = Arrays.asList(data);
        mCategoryItems = new ArrayList<>();
        categoriesModel = new Categories();
        for (int i = 0; i < data.length; i++) {

            categories.add(data[i].getCategory());
//            mCategoryItems.add(new Categories(data[i].getCategory(), Color.MAGENTA, data[i].getLocation(), locationItemsMovies()));
            System.out.println("~~~" + data[i].getCategory() + "-->" + data[i].getLocation());

        }

        Set<String> hs = new HashSet<>();
        hs.addAll(categories);
        categories.clear();
        categories.addAll(hs);
        System.out.println("~~~" + categories);


        for (int cat = 0; cat < categories.size(); cat++) {
            Locations location = new Locations();
            List<Locations> locationArray = new ArrayList<>();
            mLocationItems = new ArrayList<>();

            for (int i = 0; i < data.length; i++) {
                EventsModel dataset = data[i];
                if (dataset.getCategory().equalsIgnoreCase(categories.get(cat))) {
//                    categoriesModel.setCategoryName(dataset.getCategory());
//                    location.setTitle(dataset.getLocation());
//                    locationArray.add(location);

                    mLocationItems.add(new Locations(dataset.getTitle(), dataset.getDescription(), dataset.getDpuri(), 13.010, 80.21));
                }
            }

//            categoriesModel.setLocations(locationArray);
            System.out.println("@" + locationArray.size());
//            mCategoryItems.add(categoriesModel);
            mCategoryItems.add(new Categories(categories.get(cat), Color.MAGENTA, mLocationItems));
        }

        System.out.println("@" + "mCategoryItems" + mCategoryItems.size());


    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("events.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int MY_LOCATION_REQUEST_CODE = 1;
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    getMyLocation();
                } else {
                    checkPermission();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                checkPermission();
            }
        }
        mMap.isMyLocationEnabled();
        getMyLocation();

        LatLng myPlace = new LatLng(latitude, longitude);
        MarkerOptions myPlaceMarker = new MarkerOptions().position(myPlace).title("Dlf");
        myPlaceMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mMap.addMarker(myPlaceMarker);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myPlace));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
//        mMap.addPolyline(new PolylineOptions().add(guindy, myPlace).width(10).color(Color.RED));
//        mMap.addPolyline(new PolylineOptions().add(porur, myPlace).width(5).color(Color.DKGRAY));

        mMap.setInfoWindowAdapter(this);
    }

    private void getMyLocation() {
        try {
            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!isGPSEnabled) {
                checkPermission();
            } else {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.e("GPS Enabled", "GPS Enabled");

                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.e("VINCY-LOCATION", "" + latitude + longitude);
                        tvLatLong.setText("Latitude:" + latitude + ", Longitude:" + longitude);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void categoryItems() {
        mCategoryItems = new ArrayList<>();
        mCategoryItems.add(new Categories("Movies", Color.BLUE, locationItemsMovies()));
        mCategoryItems.add(new Categories("Restaurants", Color.CYAN, locationItemsRestaurants()));
        mCategoryItems.add(new Categories("Petrol", Color.GREEN, locationItemsPetrol()));
        mCategoryItems.add(new Categories("Temple", Color.MAGENTA, locationItemsRestaurants()));
        mCategoryItems.add(new Categories("Railway Stations", Color.GRAY, locationItems()));
    }*/

    /*private List<Locations> locationItems() {
        mLocationItems = new ArrayList<>();
        mLocationItems.add(new Locations("Guindy", "This is a demo description", R.mipmap.ic_launcher, 13.010, 80.21));
        mLocationItems.add(new Locations("Vadapalani", "This is a demo description", R.mipmap.ic_launcher, 13.054501, 80.211422));
        mLocationItems.add(new Locations("Porur", "This is a demo description", R.mipmap.ic_launcher, 13.038190, 80.156546));
        mLocationItems.add(new Locations("Chennai Airport", "This is a demo description", R.mipmap.ic_launcher, 12.994112, 80.170867));
        mLocationItems.add(new Locations("Adyar", "This is a demo description", R.mipmap.ic_launcher, 13.001177, 80.256496));
        return mLocationItems;
    }

    private List<Locations> locationItemsRestaurants() {
        mLocationItems = new ArrayList<>();
        mLocationItems.add(new Locations("Guindy", "This is a demo description", R.mipmap.ic_launcher, 13.026244, 80.174626));
        mLocationItems.add(new Locations("Guindy", "This is a demo description", R.mipmap.ic_launcher, 13.053228, 80.248476));
        mLocationItems.add(new Locations("Guindy", "This is a demo description", R.mipmap.ic_launcher, 13.022232, 80.174012));
        mLocationItems.add(new Locations("Guindy", "This is a demo description", R.mipmap.ic_launcher, 13.022160, 80.173735));
        mLocationItems.add(new Locations("Guindy", "This is a demo description", R.mipmap.ic_launcher, 13.031959, 80.163703));
        return mLocationItems;
    }

    private List<Locations> locationItemsMovies() {
        mLocationItems = new ArrayList<>();
        mLocationItems.add(new Locations("Guindy", "This is a demo description", R.mipmap.ic_launcher, 13.028040, 80.170455));
        mLocationItems.add(new Locations("Guindy", "This is a demo description", R.mipmap.ic_launcher, 13.033741, 80.161078));
        mLocationItems.add(new Locations("Guindy", "This is a demo description", R.mipmap.ic_launcher, 13.047030, 80.190328));
        mLocationItems.add(new Locations("Guindy", "This is a demo description", R.mipmap.ic_launcher, 13.029464, 80.208435));
        mLocationItems.add(new Locations("Guindy", "This is a demo description", R.mipmap.ic_launcher, 13.034689, 80.210397));
        return mLocationItems;
    }

    private List<Locations> locationItemsPetrol() {
        mLocationItems = new ArrayList<>();
        mLocationItems.add(new Locations("Guindy", "This is a demo description", R.mipmap.ic_launcher, 13.025954, 80.175396));
        mLocationItems.add(new Locations("Guindy", "This is a demo description", R.mipmap.ic_launcher, 13.007337, 80.199906));
        mLocationItems.add(new Locations("Guindy", "This is a demo description", R.mipmap.ic_launcher, 13.031300, 80.164560));
        mLocationItems.add(new Locations("Guindy", "This is a demo description", R.mipmap.ic_launcher, 13.020240, 80.185293));
        mLocationItems.add(new Locations("Guindy", "This is a demo description", R.mipmap.ic_launcher, 13.037118, 80.201618));
        return mLocationItems;
    }*/


    private void setCategories() {
        for (int i = 0; i < mCategoryItems.size(); i++) {

            LinearLayout childLayout = new LinearLayout(this);
            childLayout.setPadding(8, 8, 8, 8);
            LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView tvCat = new TextView(this);
            tvCat.setId(i);
            tvCat.setTextSize(25);
            tvCat.setPadding(5, 5, 5, 5);
            tvCat.setOnClickListener(categoryListener);
            tvCat.setLayoutParams(parentParams);

            tvCat.setText(mCategoryItems.get(i).getCategoryName());
            tvCat.setBackgroundColor(mCategoryItems.get(i).getColor());
            childLayout.addView(tvCat);

            llCategory.addView(childLayout);
        }
    }

    private View.OnClickListener categoryListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mMap.clear();
            for (int i = 0; i < mCategoryItems.get(view.getId()).getLocations().size(); i++) {
                double mLat = mCategoryItems.get(view.getId()).getLocations().get(i).getLatitude();
                double mLong = mCategoryItems.get(view.getId()).getLocations().get(i).getLongitude();
                getSelectedLocations(mCategoryItems.get(view.getId()).getLocations(), mLat, mLong);
            }
        }
    };

    private void getSelectedLocations(List<Locations> locations, double mLat, double mLong) {
        LatLng mlatlong = new LatLng(mLat, mLong);
        MarkerOptions marker = new MarkerOptions().position(mlatlong);
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        Marker m = mMap.addMarker(marker);
        m.setTag(locations);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(mlatlong));
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_normal) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        if (v.getId() == R.id.btn_hybrid) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
        if (v.getId() == R.id.btn_satellite) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        if (v.getId() == R.id.btn_terrain) {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
      /*
        Here you will inflate the custom layout that you want to use for marker. I have inflate the custom view according to my requirements.
       */

        List<Locations> locationsList = (List<Locations>) marker.getTag();
        markerList.setAdapter(new CustomMarkerAdapter(this, locationsList));
        /*mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                Toast.makeText(MainActivity.this, "Selected item " + marker.getId() + marker.getTitle(), Toast.LENGTH_SHORT).show();
                Log.e("VINCY", "Selected item " + marker.getId() + marker.getTitle());
            }
        });*/
        infoButtonListener.setMarker(marker);
        mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
        return infoWindow;
    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
