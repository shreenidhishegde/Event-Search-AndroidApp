package com.example.eventapp.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.eventapp.EventDetailsActivity;
import com.example.eventapp.FavoritesRecyclerAdapter;
import com.example.eventapp.R;
import com.example.eventapp.RecyclerViewAdapter;
import com.example.eventapp.SearchOutputClass;
import com.example.eventapp.Search_Results_Activity;
import com.example.eventapp.databinding.FragmentMainBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment<recyclerView> extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentMainBinding binding;
    FusedLocationProviderClient fusedLocationProviderClient;
    private RequestQueue mQueue;
    private RequestQueue mgeoLocationQueue;
    private RequestQueue mgeohashQueue;
    private String resultTableString;
    private String hashresultString;
    String locationresultString;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<SearchOutputClass> ResultList = new ArrayList<>();


    String latitude;
    String loc_detailsUrl;
    String longitude;
    String loc_type = "here";
    String keyempty;
    EditText isInvalidkey;
    Spinner mycategory;
    Spinner mydistanceType;
    EditText mydistanceValue;
    RadioGroup radioGroup;
    RadioButton currentLocationRadioButton;
    RadioButton otherLocationRadioButton;
    EditText isInvalidloc;
    String locempty;
    Button onSearch;
    Button onClear;
    String category;
    String distanceType;
    String distanceValue;
    String geoUrl;
    String SegmentID;
    String url;
    String hash;
    String locationValue;
    double otherlatitude;
    double otherlongitude;
    String segmentId;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
//    List<String> FavoritesList = new ArrayList<String>();

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }

        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
            //System.out.println("in fragment 1");
            View rootview = inflater.inflate(R.layout.fragment_search, container, false);
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
            //fOR THE KEYWORD VALIDATION
            isInvalidkey = rootview.findViewById(R.id.KeywordInput);
            //category
            mycategory = rootview.findViewById(R.id.categoryvalue);
            //distance
            mydistanceType = rootview.findViewById(R.id.distance_type);
            mydistanceValue = rootview.findViewById(R.id.distanceValue);
            //For location validation
            radioGroup = rootview.findViewById(R.id.location_radio_group);
            currentLocationRadioButton = rootview.findViewById(R.id.current);
            otherLocationRadioButton = rootview.findViewById(R.id.other);
            isInvalidloc = rootview.findViewById(R.id.location_input);
            isInvalidloc.setEnabled(false);
            onSearch = rootview.findViewById(R.id.search);
            onClear = rootview.findViewById(R.id.clear);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {


                    if (currentLocationRadioButton.isChecked()) {
                        //System.out.println("current loc");
                        isInvalidloc.setEnabled(false);
                        loc_type = "here";

                    }
                    if (otherLocationRadioButton.isChecked()) {
                        //System.out.println("other loc");
                        isInvalidloc.setEnabled(true);
                        loc_type = "location";
                    }
                }
            });
            mQueue = Volley.newRequestQueue(getContext());
            //geoloc
            mgeoLocationQueue = Volley.newRequestQueue(getContext());
            //geohash
            mgeohashQueue = Volley.newRequestQueue(getContext());

            //code for value of segmentIds
            mycategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    Object item = parent.getItemAtPosition(pos);
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }

            });

            //OnSearch
            onSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    keyempty = isInvalidkey.getText().toString().trim();
                    //System.out.println(keyempty);
                    if (keyempty.isEmpty()) {
                        isInvalidkey.setError("Please enter mandatory field");
                    } else {
                        isInvalidkey.setError(null);
                    }
                    locempty = isInvalidloc.getText().toString().trim();
                    if (otherLocationRadioButton.isChecked() && locempty.isEmpty()) {
                        isInvalidloc.setError("Please enter mandatory field");
                    } else {
                        isInvalidloc.setError(null);
                    }

                    category = mycategory.getSelectedItem().toString();
                    int spinner_pos = mycategory.getSelectedItemPosition();
                    String[] SegmentIDs = getResources().getStringArray(R.array.SegmentIDs);
                    segmentId = String.valueOf(SegmentIDs[spinner_pos]);
                    //System.out.println(segmentId);

                    //distance
                    distanceType = mydistanceType.getSelectedItem().toString();
                    //System.out.println(distanceType);

                    distanceValue = mydistanceValue.getText().toString().trim();
                    //System.out.println(distanceValue);

                    locationValue = isInvalidloc.getText().toString().trim();
                    //System.out.println(locationValue);


                    //getLocation
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED && getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {

                            getLocation();

                        } else {
//                            getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, 123);
//                            System.out.println("location not found");
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                        }
                    }
                }
            });

            onClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isInvalidkey.getText().clear();
                    isInvalidkey.setError(null);
                    mydistanceValue.getText().clear();
                    isInvalidloc.getText().clear();
                    isInvalidloc.setError(null);
                    mycategory.setSelection(0);
                    mydistanceType.setSelection(0);
                    mydistanceValue.setText("10");
                    currentLocationRadioButton.setChecked(true);
                    isInvalidloc.setEnabled(false);
                }
            });
            return rootview;
//        } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
//            View rootview = inflater.inflate(R.layout.fragment_favorites, container, false);
//            sharedPreferences = getContext().getSharedPreferences("myfav", Context.MODE_PRIVATE);
//            editor = sharedPreferences.edit();
//            JSONObject jsonobject;
//            recyclerView = (RecyclerView) rootview.findViewById(R.id.fav_adapter);
//            layoutManager = new LinearLayoutManager(getContext());
//            recyclerView.setLayoutManager(layoutManager);
//
//            try{
//                Map<String, ?> allEntries = sharedPreferences.getAll();
//                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
//
//                    jsonobject = new JSONObject(entry.getValue().toString());
//                    String Date = jsonobject.getString("Date");
//                    String EventName = jsonobject.getString("Name");
//                    String category = jsonobject.getString("Category");
//                    String Venue = jsonobject.getString("Venue");
//                    String Id = jsonobject.getString("Id");
//                    String lat = jsonobject.getString("Lat");
//                    String lng = jsonobject.getString("Lng");
//                    SearchOutputClass item = new SearchOutputClass (EventName, Venue, Date, Id, category,lat,lng);
//                    ResultList.add(item);
//                    //System.out.println("I am in favorites section");
//                    System.out.println(ResultList);
//                }
//                //System.out.println(ResultList);
//                mAdapter = new FavoritesRecyclerAdapter(ResultList, this.getContext());
//                recyclerView.setAdapter(mAdapter);
//                mAdapter.notifyDataSetChanged();
////                    Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            return rootview;
        } else {
            final TextView textView = binding.sectionLabel;
            pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {
                    textView.setText(s);
                }
            });
            return root;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && (grantResults.length > 0) && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            getLocation();
        } else {
//            Toast.makeText(getContext(), "no loc", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("MissingPermission")
    public void getLocation() {
        //System.out.println(loc_type);
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @SuppressLint("MissingPermission")
                @Override
                public void onComplete(@NonNull @NotNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        if (loc_type == "here") {
                            // System.out.println(location.getLatitude());
                            // System.out.println(location.getLongitude());
                            //System.out.println("I am here dude");
                            latitude = Double.toString(location.getLatitude());
                            longitude = Double.toString(location.getLongitude());
                            geoUrl = "https://rare-inquiry-315516.wl.r.appspot.com/api/geohash/" + latitude + "/" + longitude;
                            //System.out.println(geoUrl);
                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, geoUrl, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    hashresultString = response.toString();
                                    JSONObject hashresult = null;
                                    try {
                                        hashresult = new JSONObject(hashresultString);
                                        //System.out.println(hashresult);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        hash = hashresult.getString("url");
                                        //System.out.println(hash);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    jsonParse(keyempty, category, distanceValue, distanceType, hash);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                }
                            });
                            mgeohashQueue.add(request);
                        } else {
                            otherLocation();
                        }
                    } else {
                        LocationRequest locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(1000)
                                .setFastestInterval(1000).setNumUpdates(1);
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull @NotNull LocationResult locationResult) {
                                Location location2 = locationResult.getLastLocation();
                                latitude = Double.toString(location2.getLatitude());
                                longitude = Double.toString(location2.getLongitude());
                            }
                        };
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }
            });
        }
    }

    private void otherLocation() {
        System.out.println("I am in other");

        //loc_detailsUrl = "https://rare-inquiry-315516.wl.r.appspot.com/api/googleapi/University of So uthern California";
        loc_detailsUrl = "https://rare-inquiry-315516.wl.r.appspot.com/api/googleapi/" + locationValue;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, loc_detailsUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject locationresult = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                    otherlatitude = locationresult.getDouble("lat");
                    otherlongitude = locationresult.getDouble("lng");
                    //System.out.println(otherlatitude);
                    //System.out.println(otherlongitude);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getOtherlocationHash();
                jsonParse(keyempty, category, distanceValue, distanceType, hash);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mgeoLocationQueue.add(request);
    }

    private void getOtherlocationHash() {
        geoUrl = "https://rare-inquiry-315516.wl.r.appspot.com/api/geohash/" + otherlatitude + "/" + otherlongitude;
        System.out.println(geoUrl);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, geoUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hashresultString = response.toString();
                JSONObject hashresult = null;
                try {
                    hashresult = new JSONObject(hashresultString);
                    //System.out.println(hashresult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    hash = hashresult.getString("url");
                    //System.out.println(hash);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonParse(keyempty, category, distanceValue, distanceType, hash);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mgeohashQueue.add(request);
    }

    private void jsonParse(String key_empty, String category, String distanceValue, String distanceType, String hash) {
        //System.out.println("category val" + category);
        //if (category == "All")
        if (category.contains("All")) {
            segmentId = "default";
            //System.out.println(segmentId);
            //System.out.println(category);
        }

        //String url = "https://rare-inquiry-315516.wl.r.appspot.com/api/searchresult/angeles/KZFzniwnSyZfZ7v7nJ/10/miles/9q5cs";
        url = "https://rare-inquiry-315516.wl.r.appspot.com/api/searchresult/" + key_empty + "/" + segmentId + "/" + distanceValue + "/" + distanceType + "/" + hash;
        //System.out.println(url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                resultTableString = response.toString();
                //openSearchResult();
                //System.out.println("Length of Results" +resultTableString.length());
                    Intent intent = new Intent(getActivity(), Search_Results_Activity.class);
                    intent.putExtra("resultTableString", resultTableString.toString());
                    startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Intent intent = new Intent(getActivity(), Search_Results_Activity.class);
//                intent.putExtra("resultTableString", false);
//                startActivity(intent);
            }
        });
        mQueue.add(request);
    }

//    public void openSearchResult(){
//        Intent intent = new Intent(this.getActivity(), Search_Results_Activity.class);
//        intent.putExtra("resultTableString", resultTableString.toString());
//        startActivity(intent);
        @Override
        public void onDestroyView(){
            super.onDestroyView();
            binding = null;
        }
    }
