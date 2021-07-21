package com.example.eventapp;

import android.app.MediaRouteButton;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FavoritesRecyclerAdapter extends RecyclerView.Adapter<FavoritesRecyclerAdapter.MyViewHolder> {

    List<SearchOutputClass> ResultList;
    Context context;
    private String EventId;
    private String Name;
    private String Venue;
    private String Date;
    private RequestQueue mEventqueue;
    String eventUrl;
    String eventResultString;
    String category_list;
    View view;
    String latitude;
    String longitude;


    public FavoritesRecyclerAdapter(List<SearchOutputClass> resultList, Context context) {
        ResultList = resultList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public FavoritesRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_line_search,parent,false);
        FavoritesRecyclerAdapter.MyViewHolder holder = new FavoritesRecyclerAdapter.MyViewHolder(view);

        mEventqueue = Volley.newRequestQueue(context);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {

        if (ResultList == null ){
            TextView noSearchResult;
            System.out.println("There are no results for this and I am in Recycler");
            noSearchResult =  view.findViewById(R.id.noSearchResult);
            noSearchResult.setVisibility(View.VISIBLE);}

        SharedPreferences sharedPreferences = context.getSharedPreferences("myfav", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String sharedPreferencesName;


        EventId = ResultList.get(position).getId();
        sharedPreferencesName = EventId;

        Name = ResultList.get(position).getEventName();
        Venue = ResultList.get(position).getVenue();
        Date = ResultList.get(position).getDate();
        latitude = ResultList.get(position).getLat();
        longitude = ResultList.get(position).getLng();
        holder.EventName.setText(ResultList.get(position).getEventName());
        //holder.EventName.setText(Name);
        holder.EventVenue.setText(ResultList.get(position).getVenue());
        //holder.EventDate.setText(Venue);
        holder.EventDate.setText(ResultList.get(position).getDate());
        //holder.EventName.setText(Date);
        //

        category_list = ResultList.get(position).getCategory();
        //System.out.println("categories list"+category_list.toString());
        if (category_list!= null && category_list.contains("Music")) {
            //System.out.println("I am music");
            Glide.with(this.context).load("").error(R.drawable.music_icon).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.CatThumbnail);
        } else if (category_list!= null && category_list.contains("Miscellaneous")) {
            //System.out.println("I am Miscle");
            Glide.with(this.context).load("").error(R.drawable.miscellaneous_icon).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.CatThumbnail);
        } else if (category_list!= null && category_list.contains("Arts & Theatre")) {
            //System.out.println("I am Art");
            Glide.with(this.context).load("").error(R.drawable.art_icon).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.CatThumbnail);
        } else if (category_list!= null && category_list.contains("Sports")) {
            //System.out.println("I am Sports");
            Glide.with(this.context).load("").error(R.drawable.ic_sport_icon).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.CatThumbnail);
        } else if (category_list!= null && category_list.contains("Film")) {
            //System.out.println("I am Film");
            Glide.with(this.context).load("").error(R.drawable.film_icon).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.CatThumbnail);
        } else {
            Glide.with(this.context).load("").error(R.drawable.miscellaneous_icon).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.CatThumbnail);
        }
//         if (sharedPreferences.contains(sharedPreferencesName)) {
            Glide.with(this.context).load("").error(R.drawable.heart_fill_red).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.heart);
//        }
//        else
//        {
//            Glide.with(this.context).load("").error(R.drawable.heart_outline_black).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.heart);
//        }


        holder.heart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ResultList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,ResultList.size());
                editor.remove(sharedPreferencesName);
                editor.commit();
            }
        });

        //Favorites Ends here

        holder.EventName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("I am Event" +EventId);
                eventUrl = "https://rare-inquiry-315516.wl.r.appspot.com/api/eventdetails/"+EventId;
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, eventUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        eventResultString = response.toString();
                        JSONObject eventResult = null;
                        try {
                            eventResult = new JSONObject(eventResultString);
                            //System.out.println(eventResult);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(context, EventDetailsActivity.class);
                        intent.putExtra("eventResult",  eventResult.toString());
                        context.startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                mEventqueue.add(request);
                //getVenueDetails(ResultList.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        //System.out.println(ResultList);
        return ResultList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView CatThumbnail;
        TextView EventName;
        TextView EventVenue;
        TextView EventDate;
        ImageView heart;
        TextView NoResults;
        //TextView Id;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            CatThumbnail = itemView.findViewById(R.id.imageViewCat);
            EventName = itemView.findViewById(R.id.EventName);
            EventVenue = itemView.findViewById(R.id.Venue);
            EventDate = itemView.findViewById(R.id.DateTime);
            heart = itemView.findViewById(R.id.Heart);
            NoResults = itemView.findViewById(R.id.noSearchResult);
        }
    }
}
