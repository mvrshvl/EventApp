package com.example.eventapp.ui.home.ClickEvent;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventapp.Event;
import com.example.eventapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static com.example.eventapp.MainActivity.mDatabaseReference;
import static com.example.eventapp.MainActivity.setTime;


public class EventSingle extends Fragment {

    private EventViewModel mViewModel;
    private String name;
    private String date;
    private String image;
    public static EventSingle newInstance() {
        return new EventSingle();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.event_fragment, container, false);

//        name=getArguments().getString("name");
//        date=getArguments().getString("date");
//        image=getArguments().getString("image");
        final TextView date_tv = root.findViewById(R.id.date_event);
        final TextView name_tv = root.findViewById(R.id.name_event);
        final ImageView image_iv = root.findViewById(R.id.image_event);


        final String event_id = getArguments().getString("id");



        mDatabaseReference.child("events").child(event_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Event e = dataSnapshot.getValue(Event.class);
                name = e.getName();
                long ms = e.getDate();
                date = setTime(ms);
                image = e.getImages_path1();
                date_tv.setText(date);
                name_tv.setText(name);
                Picasso.get().load(image).into(image_iv);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        // TODO: Use the ViewModel
    }


}
