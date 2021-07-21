package com.example.eventapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.Call;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class EventDetailsActivity extends AppCompatActivity {

    private SectionsPagerAdapter1 mSectionsPagerAdapter;
    private ViewPager mViewPager;
    TabLayout tabLayout;
    String eventResult;
    String eventId;
    JSONObject resultTableJSON;
    String sharedPreferencesName;

    private int[] tabIcons = {
            R.drawable.info_outline,
            R.drawable.artist,
            R.drawable.venue

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        eventResult = getIntent().getExtras().getString("eventResult");
        //System.out.println("Hey there result");
        //System.out.println(eventResult);
        try {
            resultTableJSON = new JSONObject(getIntent().getExtras().getString("eventResult"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter1(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.view_pager1);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs1);
        tabLayout.setupWithViewPager(mViewPager);

        String eventname = null;
        try {
            eventname = resultTableJSON.getString("name");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //actionBar.setTitle(eventName);
        actionBar.setTitle((Html.fromHtml("<font color=\"#0000\">" + eventname + "</font>")));


        setupTabIcons();
    }

    public String getMyResult()
    {
        //System.out.println ("I am in getMyResult");
        //System.out.println(eventResult);

        System.out.println("I am in EventDetails");
        System.out.println(eventResult);
        return eventResult;
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(2).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

    }

    public class SectionsPagerAdapter1 extends FragmentPagerAdapter {

        public SectionsPagerAdapter1(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new EventFragment();
                case 1:
                    return new ArtistsFragment();
                case 2:
                    return new VenueFragment();

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "EVENTS";
                case 1:
                    return "ARTIST(S)";
                case 2:
                    return "VENUE";
            }
            return null;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbuttons, menu);

        SharedPreferences sharedPreferences = this.getSharedPreferences("myfav", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            eventId = resultTableJSON.getString("Id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sharedPreferencesName = eventId;


        try{

            if (sharedPreferences.contains(sharedPreferencesName)) {
                menu.findItem(R.id.fav_button).setIcon(R.drawable.heart_fill_white);

            } else {
                menu.findItem(R.id.fav_button).setIcon(R.drawable.heart_outline_white);

            }}catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.twit_button:
                call_twitter();
                return true;
            case R.id.fav_button:
                call_setFav(item);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void call_twitter() {
        try {
            System.out.println("hello twitter");
            String eventname = resultTableJSON.getString("name");
            String venue = resultTableJSON.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
            String twitterEventBuyTicketAt = "eventBuyTicketAt";
            String twitterURL = "https://twitter.com/intent/tweet?text=Checkout "
                    + eventname
                    + " located at " + venue
                    + " %23CSCI571EventSearch";

            System.out.println(twitterURL);
            Uri uriUrl = Uri.parse(twitterURL);
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
        }catch (JSONException e){
            e.printStackTrace();

        }

    }

    public void call_setFav(MenuItem menu)
    {

        SharedPreferences sharedPreferences = this.getSharedPreferences("myfav", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String sharedPreferencesName = eventId;

        try{

            if (sharedPreferences.contains(sharedPreferencesName)) {
                menu.setIcon(R.drawable.heart_outline_white);
                editor.remove(sharedPreferencesName);
                editor.commit();

            } else {
                menu.setIcon(R.drawable.heart_fill_white);
                String event_obj = getIntent().getStringExtra("eventObj");
                editor.putString(sharedPreferencesName, event_obj);
                editor.apply();

            }}catch (Exception e) {
            e.printStackTrace();
        }


    }
}