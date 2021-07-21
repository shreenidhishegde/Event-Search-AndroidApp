package com.example.eventapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VenueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VenueFragment extends Fragment implements OnMapReadyCallback {

    JSONObject VenueResultJson;
    String venueName;
    String venueAddress;
    String venuePhoneNumber;
    String venueCity;
    String venueGeneralRule;
    String venueChildRule;
    String lat;
    String lng;
    Double latitude;
    Double longitude;
    String city;
    String state;
    GoogleMap map;
    MapView mapView;
   // List<String> Venuelist;
    Map<String,String> Venue_dict;
    TextView VenueText;
    TextView VenueValue;
    TextView noSearchResult;
    View rootview;
    String VenueHours;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VenueFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VenueFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VenueFragment newInstance(String param1, String param2) {
        VenueFragment fragment = new VenueFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootview =inflater.inflate(R.layout.fragment_venue, container, false);
        EventDetailsActivity activityevents = (EventDetailsActivity) getActivity();

        Venue_dict = new HashMap<String, String>();
        try {
            VenueResultJson = new JSONObject(activityevents.getMyResult());
            System.out.println(VenueResultJson);

            try {
                venueName = VenueResultJson.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
                //System.out.println(venueName);
                Venue_dict.put("Name", venueName);
            }
         catch (JSONException e) {
        }

            // venue address
            try {
                venueAddress = VenueResultJson.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("address").getString("line1");
                //System.out.println(venueAddress);
                Venue_dict.put("Address", venueAddress );
            } catch (JSONException e) {
            }

            // venue city
            try {
                city = VenueResultJson.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("city").getString("name");
                state = VenueResultJson.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("state").getString("name");
                venueCity = city + ", " + state;
                Venue_dict.put("City", venueCity );
                //System.out.println(venueCity);
            } catch (JSONException e) {
            }

            // venue phone number
            try {
                venuePhoneNumber = VenueResultJson.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("boxOfficeInfo").getString("phoneNumberDetail");
                //System.out.println(venuePhoneNumber);
                Venue_dict.put("Ph No.", venuePhoneNumber );
            } catch (JSONException e) {

            }

            //open hours
            try {
                VenueHours = VenueResultJson.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("boxOfficeInfo").getString("openHoursDetail");
                //System.out.println(venuePhoneNumber);
                Venue_dict.put("Open Hours", VenueHours );
            } catch (JSONException e) {

            }

            // venue general rule
            try {
                venueGeneralRule = VenueResultJson.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("generalInfo").getString("generalRule");
                //System.out.println(venueGeneralRule);
                Venue_dict.put("General rule", venueGeneralRule );
            } catch (JSONException e) {

            }

            // venue child rule
            try {
                venueChildRule = VenueResultJson.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("generalInfo").getString("childRule");
                //System.out.println(venueChildRule);
                Venue_dict.put("Child rule", venueChildRule );

            } catch (JSONException e) {

            }
             //latitude and longitude
             lat = VenueResultJson.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("location").getString("latitude");
             lng = VenueResultJson.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("location").getString("longitude");
             latitude = Double.parseDouble(lat);
             longitude = Double.parseDouble(lng);

            mapView = rootview.findViewById(R.id.map);
            if(mapView != null){
                mapView.onCreate(null);
                mapView.onResume();
                mapView.getMapAsync(VenueFragment.this);
            }
             //System.out.println(latitude);
             //System.out.println(longitude);


            TableLayout VenuTable = (TableLayout) rootview.findViewById(R.id.Venue_Table);
            VenuTable.setStretchAllColumns(true);
            VenuTable.bringToFront();



            if (venueName!=null)
            {
                //TableRow NameRow =  new TableRow(getContext());
                View tr = getLayoutInflater ().inflate (R.layout.one_line_event, null, false);
                VenueText = tr.findViewById(R.id.table_keys);
                VenueText.setText("Name");
                VenueValue = tr.findViewById(R.id.table_values);
                VenueValue.setText(venueName);
                VenuTable.addView(tr);
            }

            if (venueAddress!=null)
            {
                View tr = getLayoutInflater ().inflate (R.layout.one_line_event, null, false);
                TextView a1 = tr.findViewById(R.id.table_keys);
                a1.setText("Address");
                TextView a2 = tr.findViewById(R.id.table_values);
                a2.setText(venueAddress);
                VenuTable.addView(tr);
            }


            if (venueCity!=null)
            {
                View tr = getLayoutInflater ().inflate (R.layout.one_line_event, null, false);
                TextView a1 = tr.findViewById(R.id.table_keys);
                a1.setText("City");
                TextView a2 = tr.findViewById(R.id.table_values);
                a2.setText(venueCity);
                VenuTable.addView(tr);
            }

            if (venuePhoneNumber!=null)
            {
                View tr = getLayoutInflater ().inflate (R.layout.one_line_event, null, false);
                TextView a1 = tr.findViewById(R.id.table_keys);
                a1.setText("Phone Number");
                TextView a2 = tr.findViewById(R.id.table_values);
                a2.setText(venuePhoneNumber);
                VenuTable.addView(tr);
            }

            if (VenueHours!=null)
            {
                View tr = getLayoutInflater ().inflate (R.layout.one_line_event, null, false);
                TextView a1 = tr.findViewById(R.id.table_keys);
                a1.setText("Open Hours");
                TextView a2 = tr.findViewById(R.id.table_values);
                a2.setText(VenueHours);
                VenuTable.addView(tr);
            }

            if (venueGeneralRule!=null)
            {
                View tr = getLayoutInflater ().inflate (R.layout.one_line_event, null, false);
                TextView a1 = tr.findViewById(R.id.table_keys);
                a1.setText("General rule");
                TextView a2 = tr.findViewById(R.id.table_values);
                a2.setText(venueGeneralRule);
                VenuTable.addView(tr);
            }


            if (venueChildRule!=null)
            {
                View tr = getLayoutInflater ().inflate (R.layout.one_line_event, null, false);
                TextView a1 = tr.findViewById(R.id.table_keys);
                a1.setText("Child Rule");
                TextView a2 = tr.findViewById(R.id.table_values);
                a2.setText(venueGeneralRule);
                VenuTable.addView(tr);
            }

            if (venueName!=null) {
                if (venueChildRule == null & venueGeneralRule == null & venuePhoneNumber == null & venuePhoneNumber == null & venueCity != null & venueAddress == null) {
                    System.out.println("No one is here");
                    noSearchResult =  (TextView) rootview.findViewById(R.id.noSearchResult);
                    noSearchResult.setVisibility(View.VISIBLE);
                    //noArtists.setText(artistName + ": No details");
                    VenueText.setVisibility(View.GONE);
                    VenueValue.setVisibility(View.GONE);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootview;
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        map = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
        CameraPosition position = CameraPosition.builder().target(new LatLng(latitude, longitude)).zoom(14).bearing(0).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
    }
    }