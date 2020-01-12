package com.example.eventapp.ui.home.ClickEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventapp.R;
import com.example.eventapp.backPressedListener;
import com.squareup.picasso.Picasso;

public class Event extends Fragment implements backPressedListener {

    private EventViewModel mViewModel;
    private String name;
    private String date;
    private String image;
    public static Event newInstance() {
        return new Event();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.event_fragment, container, false);

        name=getArguments().getString("name");
        date=getArguments().getString("date");
        image=getArguments().getString("image");
        final TextView date_tv = root.findViewById(R.id.date_event);
        date_tv.setText(date);
        final TextView name_tv = root.findViewById(R.id.name_event);
        name_tv.setText(name);
        final ImageView image_iv = root.findViewById(R.id.image_event);
        Picasso.get().load(image).into(image_iv);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        // TODO: Use the ViewModel
    }
    @Override
    public void onBackPressed(){
        AppCompatActivity activity = (AppCompatActivity) getContext();
        Navigation.findNavController(activity,R.id.nav_host_fragment).popBackStack();
    }

}
