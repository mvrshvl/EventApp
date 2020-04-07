package com.example.eventapp.ui.home;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventapp.Favourites;
import com.example.eventapp.MainActivity;
import com.example.eventapp.R;
import com.example.eventapp.Event;
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
import static com.example.eventapp.MainActivity.moderator_mode;
import static com.example.eventapp.MainActivity.ref_color_ban;
import static com.example.eventapp.MainActivity.ref_color_ok;
import static com.example.eventapp.Utils.deleteLike;
import static com.example.eventapp.Utils.sendLike;
import static com.example.eventapp.Utils.setTime;


class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List <Event> home_fragments;
    private Context context;
    boolean flag_like =false;
    HashMap <Integer,Boolean> flags = new HashMap<>();

    public DataAdapter(Context context, List<Event> home_fragments) {
        this.home_fragments = home_fragments;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.card, parent, false);
        return new ViewHolder(view);
    }

    private void changeImgTrue(ImageView v){
        v.setImageResource(R.drawable.ic_favorite_black_24dp);
    }
    private void changeImgFalse(ImageView v){
        v.setImageResource(R.drawable.ic_favorite_border_black_24dp);
    }

    @Override
    public void onBindViewHolder(final DataAdapter.ViewHolder holder, final int position) {
        //Picasso.get().load(home_fragment.getImage()).into(holder.imageView);
        //holder.nameView.setText(home_fragment.getName());
        //holder.dateView.setText(home_fragment.getDate());

        //ArrayList tmp = (ArrayList)home_fragments.get(position);
        final String name = home_fragments.get(position).getName();//(String)tmp.get(1);
        final String type = home_fragments.get(position).getType();
        final int likes = home_fragments.get(position).getLike();
        Long date_ms = home_fragments.get(position).getDate();//(String)tmp.get(2);
        final String date = setTime(date_ms);
        final String image =home_fragments.get(position).getImages_path1();//(String) tmp.get(0);
        Picasso.get().load(image).fit().centerCrop().into(holder.imageView);//.fit
        flags.put(position,false);
        if(getCurrentUser()!=null) {
            mDatabaseReference.child("favourites").orderByChild("event").equalTo(home_fragments.get(position).getId()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String userDB = "" + dataSnapshot.child("user").getValue();
                    String userCU = MainActivity.getCurrentUser().getUid();


                    if (userDB.equals(userCU)) {
                        holder.b_like.setImageResource(R.drawable.ic_favorite_black_24dp);
                        flags.put(position, true);
                    }

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
        //разметка типов дя модератеров
        if(moderator_mode) {
            if (home_fragments.get(position).getState() == 1)
                holder.parent.setBackgroundColor(ref_color_ok);
            else if (home_fragments.get(position).getState() == 2)
                holder.parent.setBackgroundColor(ref_color_ban);
        }
        holder.nameView.setText(name);
        holder.dateView.setText(date);
        holder.typeView.setText(type);
        holder.likeView.setText(likes+"");
        holder.cv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Bundle data = new Bundle();
                data.putString("id",home_fragments.get(position).getId());
                MainActivity.recyclerPosition = position;
                Navigation.findNavController(activity,R.id.nav_host_fragment).navigate(R.id.event,data);
            }
        });

        holder.b_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event = home_fragments.get(position);
                if (!flags.get(position)) {
                    changeImgTrue(holder.b_like);
                    flags.put(position, true);
                    sendLike(home_fragments.get(position));
                    event.setLike(event.getLike()+1);
                }
                else {
                    changeImgFalse(holder.b_like);
                    flags.put(position,false);
                    deleteLike(home_fragments.get(position));
                    event.setLike(event.getLike()-1);
                }
                holder.likeView.setText(event.getLike()+"");

            }
        });


    }





    @Override
    public int getItemCount() {
        return home_fragments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout parent;
        final ImageView imageView, b_like;
        final TextView nameView, dateView, typeView,likeView;
        CardView cv;
        ViewHolder(View view){
            super(view);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            parent = (LinearLayout) itemView.findViewById(R.id.parent_home);
            imageView = (ImageView)view.findViewById(R.id.image);
            nameView = (TextView) view.findViewById(R.id.name);
            dateView = (TextView) view.findViewById(R.id.date);
            typeView = (TextView)view.findViewById(R.id.type);
            likeView = (TextView) view.findViewById(R.id.likes);
            b_like = (ImageView)view.findViewById(R.id.b_like);

        }
    }
}