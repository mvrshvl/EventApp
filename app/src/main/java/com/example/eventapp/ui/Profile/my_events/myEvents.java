package com.example.eventapp.ui.Profile.my_events;

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
import com.example.eventapp.Utils;
import com.example.eventapp.ui.Profile.DataAdapter;

import java.util.ArrayList;
import java.util.List;

public class myEvents extends Fragment {
    private myEventsViewModel myEventsViewModel;
    protected List<Favourites> list_events= new ArrayList<>();
    protected static ProgressBar circular_progress;
    protected static RecyclerView recyclerView;
    protected static boolean is_loaded;
    protected static Activity activity;
    private static TextView tv_empty;
    private static ImageView iv_empty;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.my_events_fragment, container, false);

        circular_progress = (ProgressBar) root.findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) root.findViewById(R.id.list);
        tv_empty = (TextView) root.findViewById(R.id.tv_empty);
        iv_empty = (ImageView) root.findViewById(R.id.iv_empty);
        activity = getActivity();
        Utils.loading(recyclerView,circular_progress,tv_empty
                ,iv_empty,true,true);
        myEventsViewModel =
                ViewModelProviders.of(this).get(myEventsViewModel.class);

        myEventsViewModel.getText().observe(this, new Observer<List>() {
            @Override
            public void onChanged(@Nullable List events) {
                if(events.size()!=0)
                    Utils.loading(recyclerView,circular_progress,tv_empty
                            ,iv_empty,false,true);
                else
                    Utils.loading(recyclerView,circular_progress,tv_empty
                            ,iv_empty,false,false);
                DataAdapter adapter = new DataAdapter(getActivity(), events);
                DataAdapter.type_screen = false;
                recyclerView.setAdapter(adapter);
            }
        });
        is_loaded = false;
        return root;
    }

}
