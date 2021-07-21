package com.example.eventapp;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArtistsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RequestQueue mArtistQueue;

    JSONObject ArtistResultJSon;
    String FirstArtist;
    String SpotifyUrl;
    JSONArray attractions;
    List<String> artistOrTeams;
    String url1;
    String artistName;
    String followers;
    String popularity;
    String spotify_url;
    TableLayout ArtistsTable;
    View rootview;
    TextView NameValue;
    TextView artist2;
    TextView artist1;
    TextView Name2;
    TextView Name1;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ArtistsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArtistsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArtistsFragment newInstance(String param1, String param2) {
        ArtistsFragment fragment = new ArtistsFragment();
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

       rootview = inflater.inflate(R.layout.fragment_artists, container, false);
        EventDetailsActivity activityevents = (EventDetailsActivity) getActivity();

        mArtistQueue = Volley.newRequestQueue(getContext());

        try {
            ArtistResultJSon = new JSONObject(activityevents.getMyResult());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            getSpotify(ArtistResultJSon);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArtistsTable = (TableLayout) rootview.findViewById(R.id.Artist_table);
        ArtistsTable.setStretchAllColumns(true);
        ArtistsTable.bringToFront();


        return rootview;
    }

    public void getSpotify(JSONObject artistResultJSon) throws JSONException {
        //SpotifyUrl = https://rare-inquiry-315516.wl.r.appspot.com/spotify?Keyword=' + eventID;

        artistOrTeams = new ArrayList<>();
        attractions = artistResultJSon.getJSONObject("_embedded").getJSONArray("attractions");
        for (int i = 0; i < attractions.length(); i++) {
            artistOrTeams.add(attractions.getJSONObject(i).getString("name"));
            System.out.println("I am in artist");
            System.out.println(artistOrTeams);
        }

        url1 = "https://rare-inquiry-315516.wl.r.appspot.com/spotify?Keyword=" + artistOrTeams.get(0);
        setSpotify_First(url1, artistOrTeams.get(0));

        if (artistOrTeams.size() >= 2) {
            String url2 = "https://rare-inquiry-315516.wl.r.appspot.com/spotify?Keyword=" + artistOrTeams.get(1);
            setSpotify_Second(url2, artistOrTeams.get(1));
        }
    }


    private void setSpotify_First(String url1, String artistName) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override

                public void onResponse (JSONObject response) {
                try {
                    // followers
                    followers = response.getJSONObject("artists").getJSONArray("items").getJSONObject(0).getJSONObject("followers").getString("total");
                    // popularity
                    popularity = response.getJSONObject("artists").getJSONArray("items").getJSONObject(0).getString("popularity");
                    System.out.println(popularity);
                    // spotify url
                    spotify_url = response.getJSONObject("artists").getJSONArray("items").getJSONObject(0).getJSONObject("external_urls").getString("spotify");
                    System.out.println(spotify_url);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (artistName != null) {
                    View tr = getLayoutInflater ().inflate (R.layout.one_line_event, null, false);
                    Name1 = tr.findViewById(R.id.table_keys);
                    Name1.setText("Name");
                    artist1 = tr.findViewById(R.id.table_values);
                    artist1.setText(artistName);
                    ArtistsTable.addView(tr);
                }

                if (followers != null) {
                    View tr = getLayoutInflater ().inflate (R.layout.one_line_event, null, false);
                    TextView a1 = tr.findViewById(R.id.table_keys);
                    a1.setText("Followers");
                    TextView a2 = tr.findViewById(R.id.table_values);
                    a2.setText(followers);
                    ArtistsTable.addView(tr);
                }

                if (popularity != null) {
                    View tr = getLayoutInflater ().inflate (R.layout.one_line_event, null, false);
                    TextView a1 = tr.findViewById(R.id.table_keys);
                    a1.setText("popularity");
                    TextView a2 = tr.findViewById(R.id.table_values);
                    a2.setText(popularity);
                    ArtistsTable.addView(tr);
                }

                if (spotify_url != null) {
                    View tr = getLayoutInflater ().inflate (R.layout.one_line_event, null, false);
                    TextView a1 = tr.findViewById(R.id.table_keys);
                    a1.setText("Check At");
                    TextView a2 = tr.findViewById(R.id.table_values);
                    a2.setText("Spotify");
                    a2.setClickable(true);
                    a2.setMovementMethod(LinkMovementMethod.getInstance());
                    String UrlText = "<a href=\"" + spotify_url + "\" > Spotify</a>";
                    a2.setText(Html.fromHtml(UrlText));
                    ArtistsTable.addView(tr);
                }

                if (artistName != null && Name1!=null) {
                    if (popularity == null & followers == null & spotify_url == null) {
                        System.out.println("No one is here");
                        TextView noArtists;
                        noArtists = rootview.findViewById(R.id.Noartists);
                        noArtists.setVisibility(View.VISIBLE);
                        noArtists.setText(artistName + ": No details");
                        Name1.setVisibility(View.GONE);
                        artist1.setVisibility(View.GONE);
                    }
                }
                }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mArtistQueue.add(request);
    }

    private void setSpotify_Second(String url2, String artistName) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    followers = response.getJSONObject("artists").getJSONArray("items").getJSONObject(0).getJSONObject("followers").getString("total");
                    System.out.println(followers);
                    // popularity
                    popularity = response.getJSONObject("artists").getJSONArray("items").getJSONObject(0).getString("popularity");
                    System.out.println(popularity);
                    // spotify url
                    spotify_url = response.getJSONObject("artists").getJSONArray("items").getJSONObject(0).getJSONObject("external_urls").getString("spotify");
                    System.out.println(spotify_url);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (artistName!=null)
                {
                    View tr = getLayoutInflater ().inflate (R.layout.one_line_event, null, false);
                    TextView Name2 = tr.findViewById(R.id.table_keys);
                    Name2.setText("Name");
                    artist2 = tr.findViewById(R.id.table_values);
                    artist2.setText(artistName);
                    ArtistsTable.addView(tr);
                }

                if (followers!=null)
                {
                    View tr = getLayoutInflater ().inflate (R.layout.one_line_event, null, false);
                    TextView a1 = tr.findViewById(R.id.table_keys);
                    a1.setText("Followers");
                    TextView a2 = tr.findViewById(R.id.table_values);
                    a2.setText(followers);
                    ArtistsTable.addView(tr);
                }

                if (popularity!=null)
                {
                    View tr = getLayoutInflater ().inflate (R.layout.one_line_event, null, false);
                    TextView a1 = tr.findViewById(R.id.table_keys);
                    a1.setText("popularity");
                    TextView a2 = tr.findViewById(R.id.table_values);
                    a2.setText(popularity);
                    ArtistsTable.addView(tr);
                }

                if (spotify_url!=null)
                {
                    TableRow SpotRow =  new TableRow(getContext());
                    TextView Spotify = new TextView(getContext());
                    Spotify.setText("Check At");
                    TextView SpotValue = new TextView(getContext());
                    SpotValue.setClickable(true);
                    SpotValue.setMovementMethod(LinkMovementMethod.getInstance());
                    String UrlText = "<a href=\""+spotify_url+"\" > Spotify</a>";
                    SpotValue.setText(Html.fromHtml(UrlText));
                    ArtistsTable.addView(SpotRow);
                    SpotRow.addView(Spotify);
                    SpotRow.addView(SpotValue);
                }

                if (artistName != null && Name2!=null) {
                if (popularity==null & followers == null & spotify_url== null) {
                    TextView noArtists2;
                        noArtists2 = rootview.findViewById(R.id.Noartists2);
                        noArtists2.setVisibility(View.VISIBLE);
                        noArtists2.setText(artistName + ": No details");
                        artist2.setVisibility(View.GONE);
                        Name2.setVisibility(View.GONE);
                    }
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mArtistQueue.add(request);
    }
    }
