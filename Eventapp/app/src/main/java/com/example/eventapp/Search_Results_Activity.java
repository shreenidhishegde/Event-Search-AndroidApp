package com.example.eventapp;

import androidx.appcompat.app.ActionBar;

import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Search_Results_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    String category;
    String EventName;
    String Id;
    String Date;
    String Venue;
    String lat;
    String lng;

    private List<SearchOutputClass> ResultList;
    TextView noSearchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        recyclerView = (RecyclerView) findViewById(R.id.myAndroidRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        noSearchResult =  (TextView) findViewById(R.id.noSearchResult);



        ResultList = new ArrayList<>();

        String resultTableString = getIntent().getExtras().getString("resultTableString");

       // System.out.println("Am I null" + resultTableString);
        JSONObject resultTableJSON;
        try {

            noSearchResult.setVisibility(View.GONE);

            resultTableJSON = new JSONObject(getIntent().getExtras().getString("resultTableString"));
            JSONArray events = resultTableJSON.getJSONObject("_embedded").getJSONArray("events");
            for (int i = 0; i < events.length(); i++) {
                JSONObject singleEvent = events.getJSONObject(i);
                try {
                    EventName = singleEvent.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Id = singleEvent.getString("id");
                try {
                    category = singleEvent.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Date = singleEvent.getJSONObject("dates").getJSONObject("start").getString("localDate");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Venue = singleEvent.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    lat = singleEvent.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("location").getString("latitude");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //System.out.println(lat);

                try {
                    lng = singleEvent.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("location").getString("longitude");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //System.out.println(lng);

//                SearchOutputClass results = new SearchOutputClass(EventName, Venue, Date, Id, category, lat, lng);
                SearchOutputClass results = new SearchOutputClass(EventName, Venue, Date, Id, category, lat, lng);
                ResultList.add(results);
                //System.out.println(ResultList);
                //Log.i("Events", events.getJSONObject(i).toString());

                mAdapter = new RecyclerViewAdapter(ResultList, Search_Results_Activity.this);
                recyclerView.setAdapter(mAdapter);

                //use a linear layout
                layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);

                //specify an adapter
            }




        }
    
        catch (JSONException e) {
                noSearchResult.setVisibility(View.VISIBLE);
        }

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
