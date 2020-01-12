package com.example.eventapp.ui.notifications.favourites;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventapp.Event;
import com.example.eventapp.Favourites;
import com.example.eventapp.MainActivity;
import com.example.eventapp.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.example.eventapp.MainActivity.getCurrentUser;
import static com.example.eventapp.MainActivity.mDatabaseReference;
import static com.example.eventapp.MainActivity.mFirebaseDatabase;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Favourites> favourite_cards;
    private Context context;
    boolean flag_like =false;
    private String name;
    private String type ;
    private long likes ;
    private String image;

    public DataAdapter(Context context, List<Favourites> home_fragments) {
        this.favourite_cards = home_fragments;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.favourites_card, parent, false);
        return new DataAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final DataAdapter.ViewHolder holder, final int position) {

        String eventId = favourite_cards.get(position).getEvent();
        mDatabaseReference.child("events").orderByChild("id").equalTo(eventId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                name = dataSnapshot.child("name").getValue().toString();
                type = dataSnapshot.child("type").getValue().toString();
                likes = (long)dataSnapshot.child("like").getValue();
                image = dataSnapshot.child("images_path1").getValue().toString();

                Picasso.get().load(image).fit().centerCrop().into(holder.imageView);//.fit


                holder.nameView.setText(name);
                holder.typeView.setText(type);
                holder.likeView.setText(likes+" Нравится");
                holder.cv.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        Bundle data = new Bundle();
                        data.putString("name",name);
                        //data.putString("date",date);
                        data.putString("image",image);
                        MainActivity.recyclerPosition = position;
                        Navigation.findNavController(activity,R.id.nav_host_fragment).navigate(R.id.event,data);
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });










    }

    @Override
    public int getItemCount() {
        return favourite_cards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView nameView, typeView,likeView;
        CardView cv;
        ViewHolder(View view){
            super(view);
            cv = (CardView)itemView.findViewById(R.id.card_view_fav);
            imageView = (ImageView)view.findViewById(R.id.image);
            nameView = (TextView) view.findViewById(R.id.name);
            typeView = (TextView)view.findViewById(R.id.type);
            likeView = (TextView) view.findViewById(R.id.likes);

        }
    }
}
