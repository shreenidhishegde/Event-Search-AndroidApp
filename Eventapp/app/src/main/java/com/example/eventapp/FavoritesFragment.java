package com.example.eventapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    TextView noSearchResult;

    View rootview;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoritesFragment newInstance(String param1, String param2) {
        FavoritesFragment fragment = new FavoritesFragment();
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            List<SearchOutputClass> ResultList = new ArrayList<>();
            sharedPreferences = getContext().getSharedPreferences("myfav", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            JSONObject jsonobject;
            recyclerView = (RecyclerView) rootview.findViewById(R.id.fav_adapter);
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);

            try{
                Map<String, ?> allEntries = sharedPreferences.getAll();

//                if  (allEntries.size() == 0 ){
//
//                    noSearchResult =  rootview.findViewById(R.id.noFavorites);
//                    noSearchResult.setVisibility(View.VISIBLE);}

                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {

//                    noSearchResult.setVisibility(View.GONE);
//                    recyclerView.setVisibility(View.VISIBLE);

                    jsonobject = new JSONObject(entry.getValue().toString());
                    String Date = jsonobject.getString("Date");
                    String EventName = jsonobject.getString("Name");
                    String category = jsonobject.getString("Category");
                    String Venue = jsonobject.getString("Venue");
                    String Id = jsonobject.getString("Id");
                    String lat = jsonobject.getString("Lat");
                    String lng = jsonobject.getString("Lng");
                    SearchOutputClass item = new SearchOutputClass (EventName, Venue, Date, Id, category,lat,lng);
                    ResultList.add(item);
                    //System.out.println("I am in favorites section");
                    System.out.println(ResultList);
                }
                //System.out.println(ResultList);
                mAdapter = new FavoritesRecyclerAdapter(ResultList, this.getContext());
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
//                    Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            }catch (Exception e){
                e.printStackTrace();
            }
            // Refresh your fragment here

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_favorites, container, false);

       rootview = inflater.inflate(R.layout.fragment_favorites, container, false);

        return rootview;
    }
}