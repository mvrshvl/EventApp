package com.example.eventapp.ui.Profile.dialogs;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventapp.R;
import com.example.eventapp.User;


import java.util.List;

public class Dialogs extends Fragment {

    private DialogsViewModel mViewModel;
    private List<User> users;
    public static Dialogs newInstance() {
        return new Dialogs();
    }
    DataAdapter adapter;
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialogs_fragment, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.list);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DialogsViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.getData().observe(this, new Observer<List>() {
            @Override
            public void onChanged(@Nullable List us) {
                users = us;
                if(!users.isEmpty()){
                    //adapter
                    adapter = new DataAdapter(getActivity(), users);
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }

}
