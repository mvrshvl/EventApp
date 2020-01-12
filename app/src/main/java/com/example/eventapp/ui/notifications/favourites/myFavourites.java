package com.example.eventapp.ui.notifications.favourites;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventapp.Event;
import com.example.eventapp.Favourites;
import com.example.eventapp.R;

import java.util.ArrayList;
import java.util.List;

public class myFavourites extends Fragment {
    private maFavouritesViewModel maFavouritesViewModel;
    protected List<Favourites> list_events= new ArrayList<>();
    protected static ProgressBar circular_progress;
    protected static RecyclerView recyclerView;
    protected static boolean is_loaded;
    protected static Activity activity;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.favourites_fragment, container, false);

        circular_progress = (ProgressBar) root.findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) root.findViewById(R.id.list);
        activity = getActivity();

        maFavouritesViewModel =
                ViewModelProviders.of(this).get(maFavouritesViewModel.class);

        maFavouritesViewModel.getText().observe(this, new Observer<List>() {
            @Override
            public void onChanged(@Nullable List events) {
                DataAdapter adapter = new DataAdapter(getActivity(), events);
                recyclerView.setAdapter(adapter);
            }
        });
        is_loaded = false;
        return root;
    }

    protected static void loading(boolean b){
        if(b){
            circular_progress.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
        else{
            circular_progress.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }

    }
}
