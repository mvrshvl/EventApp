package com.example.eventapp.ui.notifications.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventapp.MainActivity;
import com.example.eventapp.R;
import com.example.eventapp.ui.notifications.add.AddEventViewModel;
import com.example.eventapp.ui.notifications.add.addEvent;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;

public class Unauth extends Fragment {

    private FirebaseAuth mAuth;
    private Button enter;
    private Button register;
    private TextView later;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.unauthorized_fragment_profile, container, false);
        final AppCompatActivity activity = (AppCompatActivity) root.getContext();


        mAuth = FirebaseAuth.getInstance();
        enter=(Button)root.findViewById(R.id.enter);
        register=(Button)root.findViewById(R.id.register);
        later = (TextView) root.findViewById(R.id.later);
        MainActivity.visibleMenu(false);

        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.enter :
                        Navigation.findNavController(activity,R.id.nav_host_fragment).navigate(R.id.enter);
                        break;
                    case R.id.register :
                        Navigation.findNavController(activity,R.id.nav_host_fragment).navigate(R.id.registration);
                        break;
                    case R.id.later :
                        //Navigation.findNavController(activity,R.id.nav_host_fragment).popBackStack();
                        MainActivity.visibleMenu(true);
                        Navigation.findNavController(activity,R.id.nav_host_fragment).navigate(R.id.navigation_home);


                }
            }
        };
        enter.setOnClickListener(onClickListener2);
        register.setOnClickListener(onClickListener2);
        later.setOnClickListener(onClickListener2);
        return root;
    }


}
