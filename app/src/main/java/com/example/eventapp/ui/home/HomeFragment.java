package com.example.eventapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventapp.MainActivity;
import com.example.eventapp.R;
import com.example.eventapp.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.eventapp.MainActivity.mDatabaseReference;

public class HomeFragment extends Fragment {
    private List<Event> list_events= new ArrayList<>();
    private HomeViewModel homeViewModel;
    public Fragment old;
    public ProgressBar circular_progress;
    public RecyclerView recyclerView;
    private boolean is_loaded;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        circular_progress = (ProgressBar) root.findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) root.findViewById(R.id.list);
        addEventFirebaseListener();
        homeViewModel.getData().observe(this, new Observer<List>() {
            @Override
            public void onChanged(@Nullable List events) {
                DataAdapter adapter = new DataAdapter(getActivity(), events);
                recyclerView.setAdapter(adapter);            }
        });
        //Toast.makeText(getContext(), "Обновление", Toast.LENGTH_SHORT).show();
        is_loaded = false;
        return root;
    }

    private void addEventFirebaseListener() {
        //показываем View загрузки
        circular_progress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);

        mDatabaseReference.child("events")
                .addValueEventListener(new ValueEventListener() {
                    //если данные в БД меняются
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (list_events.size() > 0) {
                            list_events.clear();
                        }
                        //проходим по всем записям и помещаем их в list_users в виде класса User
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Event event = postSnapshot.getValue(Event.class);

                            list_events.add(event);
                        }
                        if(!is_loaded) {
                            //публикуем данные в ListView
                            DataAdapter adapter = new DataAdapter(getActivity(), list_events);
                            recyclerView.setAdapter(adapter);
                            homeViewModel.setData(list_events);
                            recyclerView.scrollToPosition(MainActivity.recyclerPosition);
                            //убираем View загрузки
                            circular_progress.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            is_loaded=true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
    }

}