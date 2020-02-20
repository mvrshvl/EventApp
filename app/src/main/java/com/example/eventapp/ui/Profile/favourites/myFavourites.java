package com.example.eventapp.ui.Profile.favourites;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventapp.Favourites;
import com.example.eventapp.R;
import com.example.eventapp.ui.Profile.DataAdapter;

import java.util.ArrayList;
import java.util.List;

public class myFavourites extends Fragment {
    private maFavouritesViewModel maFavouritesViewModel;
    protected List<Favourites> list_events= new ArrayList<>();
    protected static ProgressBar circular_progress;
    protected static RecyclerView recyclerView;
    private static TextView tv_empty;
    private static ImageView iv_empty;
    protected static boolean is_loaded;
    protected static Activity activity;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.favourites_fragment, container, false);

        circular_progress = (ProgressBar) root.findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) root.findViewById(R.id.list);
        tv_empty = (TextView) root.findViewById(R.id.tv_empty);
        iv_empty = (ImageView) root.findViewById(R.id.iv_empty);
        activity = getActivity();


        maFavouritesViewModel =
                ViewModelProviders.of(this).get(maFavouritesViewModel.class);

        maFavouritesViewModel.getText().observe(this, new Observer<List>() {
            @Override
            public void onChanged(@Nullable List events) {
                DataAdapter adapter = new DataAdapter(getActivity(), events);
                DataAdapter.type_screen = true;
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
            tv_empty.setVisibility(View.INVISIBLE);
            iv_empty.setVisibility(View.INVISIBLE);
        }
        else{
            circular_progress.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.VISIBLE);
            iv_empty.setVisibility(View.VISIBLE);
        }

    }
}
