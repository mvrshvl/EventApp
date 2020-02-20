package com.example.eventapp.ui.Profile.city;

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
import android.widget.Button;

import com.example.eventapp.R;

public class changeCity extends Fragment {

    private ChangeCityViewModel mViewModel;

    public static changeCity newInstance() {
        return new changeCity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.change_city_fragment, container, false);
        Button button_c = (Button) root.findViewById(R.id.button_city_continue);
        button_c.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AppCompatActivity activity = (AppCompatActivity) getContext();
                //activity.getSupportFragmentManager().beginTransaction().remove(changeCity.this);
                Navigation.findNavController(activity,R.id.nav_host_fragment).popBackStack();
            }
        });


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ChangeCityViewModel.class);
        // TODO: Use the ViewModel
    }

}
