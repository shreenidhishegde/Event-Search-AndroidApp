package com.example.eventapp;
import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {
    String EventName;
    String Venue;
    String eventArtistOrTeams;
    String EventVenue;
    String localDate;
    String localTime;
    String eventTime;
    String eventCategory;
    String SubGenre;
    String segment;
    String genre;
    String SubType;
    String eventPriceRange;
    String min;
    String max;
    String eventTicketStatus;
    String eventBuyTicketAt;
    String eventSeatMap;
    JSONObject eventResultJson;
    StringBuilder artistOrTeams;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventFragment newInstance(String param1, String param2) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("Is eventFragment being calle?");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview =inflater.inflate(R.layout.fragment_event, container, false);
        EventDetailsActivity activityevents = (EventDetailsActivity) getActivity();
        //System.out.println("I am in eventfragment");
        //System.out.println(activityevents.getMyResult());

        try {
            eventResultJson = new JSONObject(activityevents.getMyResult());
            //System.out.println(eventResultJson);

            EventName = eventResultJson.getString("name");
            System.out.println(EventName);

           //artist or team
            JSONArray attractions = eventResultJson.getJSONObject("_embedded").getJSONArray("attractions");
            artistOrTeams = new StringBuilder();
            for (int i = 0; i < attractions.length(); i++) {
                String singleName = attractions.getJSONObject(i).getString("name");
                if (i == 0) {
                    artistOrTeams.append(singleName);
                } else {
                    artistOrTeams.append(" | ");
                    artistOrTeams.append(singleName);
                }
            }
            eventArtistOrTeams = artistOrTeams.toString();
            //System.out.println(eventArtistOrTeams);

            //venue
            try {
                EventVenue = eventResultJson.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
            } catch (JSONException e) {
                //LinearLayout eventVenueLayout = view.findViewById(R.id.eventVenueLayout);
                //eventVenueLayout.setVisibility(view.GONE);
            }

            //System.out.println(EventVenue);

            //Date
            try {
                localDate = eventResultJson.getJSONObject("dates").getJSONObject("start").getString("localDate");
                localTime = eventResultJson.getJSONObject("dates").getJSONObject("start").getString("localTime");
                //eventTime = formatDate(localDate) + " " + localTime;
            } catch (JSONException e) {
                //LinearLayout eventTimeLayout = view.findViewById(R.id.eventTimeLayout);
                //eventTimeLayout.setVisibility(view.GONE);
            }
            //System.out.println(localDate);

            //Category
                 try {
                     SubGenre = eventResultJson.getJSONArray("classifications").getJSONObject(0).getJSONObject("subGenre").getString("name");
                 }
                 catch (JSONException e) {}

                try {
                 segment = eventResultJson.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");}
                catch (JSONException e) {}

                try{
                 genre = eventResultJson.getJSONArray("classifications").getJSONObject(0).getJSONObject("genre").getString("name");}
                catch (JSONException e) {}

                try{
                 SubType = eventResultJson.getJSONArray("classifications").getJSONObject(0).getJSONObject("SubType").getString("name");}
                catch (JSONException e) {}

                eventCategory = "";

                if (segment!= null)
                {
                    eventCategory = eventCategory + segment;
                }
                if (genre!= null) {
                    eventCategory = eventCategory + " | " + genre;
                }
                if (SubGenre!=null)
                {
                    eventCategory = eventCategory + " | " + SubGenre;
                }
                if (SubType!=null)
                {
                    eventCategory = eventCategory + " | " + SubType;
                }


               // LinearLayout eventCategoryLayout = view.findViewById(R.id.eventCategoryLayout);
               // eventCategoryLayout.setVisibility(view.GONE);


            //System.out.println(eventCategory);

            //Price range
            try {
                min =  eventResultJson.getJSONArray("priceRanges").getJSONObject(0).getString("min");
                max =  eventResultJson.getJSONArray("priceRanges").getJSONObject(0).getString("max");
                if (min.length() > 1 && max.length() > 1) {
                    eventPriceRange = min +" " + "-" + " " + max + " " + "USD";
                } else if (min.length() > 1) {
                    eventPriceRange = min;
                } else {
                    eventPriceRange = max;
                }
            } catch (JSONException e) {
               // LinearLayout eventPriceRangeLayout = view.findViewById(R.id.eventPriceRangeLayout);
               // eventPriceRangeLayout.setVisibility(view.GONE);
            }

            //System.out.println(eventPriceRange);

            //TicketStatus
            try {
                eventTicketStatus = eventResultJson.getJSONObject("dates").getJSONObject("status").getString("code");
            } catch (JSONException e) {
                //LinearLayout eventTicketStatusLayout = view.findViewById(R.id.eventTicketStatusLayout);
                //eventTicketStatusLayout.setVisibility(view.GONE);
            }
           // System.out.println(eventTicketStatus);

            //Buy Ticket at
            try {
                eventBuyTicketAt = eventResultJson.getString("url");
            } catch (JSONException e) {
                //LinearLayout eventBuyTicketAtLayout = view.findViewById(R.id.eventBuyTicketAtLayout);
                //eventBuyTicketAtLayout.setVisibility(view.GONE);
            }
            //System.out.println(eventBuyTicketAt);
            // seat map
            try {
                eventSeatMap = eventResultJson.getJSONObject("seatmap").getString("staticUrl");
            } catch (JSONException e) {
                //LinearLayout eventSeatMapLayout = view.findViewById(R.id.eventSeatMapLayout);
                //eventSeatMapLayout.setVisibility(view.GONE);
            }
           // System.out.println(eventSeatMap);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        TableLayout EventTable = (TableLayout) rootview.findViewById(R.id.Event_Table);
        EventTable.setStretchAllColumns(true);
        EventTable.bringToFront();


            if (artistOrTeams!=null)
            {
                View tr = getLayoutInflater ().inflate (R.layout.one_line_event, null, false);
                TextView a1 = tr.findViewById(R.id.table_keys);
                a1.setText("Artist(s)/Teams");
                TextView a2 = tr.findViewById(R.id.table_values);
                a2.setText(artistOrTeams);
                EventTable.addView(tr);
            }


        if (EventVenue!=null)
        {
            View tr = getLayoutInflater ().inflate (R.layout.one_line_event, null, false);
            TextView a1 = tr.findViewById(R.id.table_keys);
            a1.setText("Venue");
            TextView a2 = tr.findViewById(R.id.table_values);
            a2.setText(EventVenue);
            EventTable.addView(tr);
        }

        if (localDate!=null)
        {
            View tr = getLayoutInflater ().inflate (R.layout.one_line_event, null, false);
            TextView a1 = tr.findViewById(R.id.table_keys);
            a1.setText("Date");
            TextView a2 = tr.findViewById(R.id.table_values);
            a2.setText(localDate);
            EventTable.addView(tr);

        }

        if (eventCategory!=null)
        {
            View tr = getLayoutInflater ().inflate (R.layout.one_line_event, null, false);
            TextView a1 = tr.findViewById(R.id.table_keys);
            a1.setText("Category");
            TextView a2 = tr.findViewById(R.id.table_values);
            a2.setText(eventCategory);
            EventTable.addView(tr);

        }

        if (eventPriceRange!=null)
        {
            View tr = getLayoutInflater ().inflate (R.layout.one_line_event, null, false);
            TextView a1 = tr.findViewById(R.id.table_keys);
            a1.setText("Price Range");
            TextView a2 = tr.findViewById(R.id.table_values);
            a2.setText(eventPriceRange);
            EventTable.addView(tr);


        }


        if (eventTicketStatus!=null)
        {
            View tr = getLayoutInflater ().inflate (R.layout.one_line_event, null, false);
            TextView a1 = tr.findViewById(R.id.table_keys);
            a1.setText("Ticket Status");
            TextView a2 = tr.findViewById(R.id.table_values);
            a2.setText(eventTicketStatus);
            EventTable.addView(tr);
        }

//
        if (eventBuyTicketAt!=null)
        {
            View tr = getLayoutInflater ().inflate (R.layout.one_line_event, null, false);
            TextView a1 = tr.findViewById(R.id.table_keys);
            a1.setText("Buy Ticket At");
            TextView a2 = tr.findViewById(R.id.table_values);
            a2.setClickable(true);
            a2.setMovementMethod(LinkMovementMethod.getInstance());
            String UrlText = "<a href=\""+eventBuyTicketAt+"\" > TicketMaster </a>";
            a2.setText(Html.fromHtml(UrlText));
            EventTable.addView(tr);
        }

        if (eventSeatMap!=null)
        {
//            TableRow SeatRow =  new TableRow(getContext());
            View tr = getLayoutInflater ().inflate (R.layout.one_line_event, null, false);
            TextView a1 = tr.findViewById(R.id.table_keys);
            TextView Seat = new TextView(getContext());
            a1.setText("Seat Map");
//            TextView SeatMap = new TextView(getContext());
            TextView a2 = tr.findViewById(R.id.table_values);
            a2.setClickable(true);
            a2.setMovementMethod(LinkMovementMethod.getInstance());
            String UrlText = "<a href=\""+eventSeatMap+"\" > View Seat Map here </a>";
            //System.out.println(eventSeatMap);
            a2.setText(Html.fromHtml(UrlText));
            EventTable.addView(tr);
        }

        // Inflate the layout for this fragment
        return rootview;
    }
}