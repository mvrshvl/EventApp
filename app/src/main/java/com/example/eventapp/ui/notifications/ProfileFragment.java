package com.example.eventapp.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.eventapp.MainActivity;
import com.example.eventapp.R;
import com.example.eventapp.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.eventapp.MainActivity.getCurrentUser;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private boolean authorizization = false;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    AppCompatActivity activity;
    private TextView id,name,town,mail;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root;

        if(getCurrentUser()==null){
            Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.auth);
        }
        mAuth = FirebaseAuth.getInstance();
        root = inflater.inflate(R.layout.fragment_profile, container, false);
        MainActivity.recyclerPosition=0;

        //id = (TextView)root.findViewById(R.id.id) ;
        name = (TextView)root.findViewById(R.id.name_profile) ;
        town = (TextView)root.findViewById(R.id.town) ;
        mail = (TextView)root.findViewById(R.id.mail) ;

        //id.setText(currentUser.getUid());
        name.setText(User.getName());
        town.setText(User.getCity());
        mail.setText(User.getMail());
/*
        final TextView textView = root.findViewById(R.id.name_profile);
        profileViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
 */
        currentUser=MainActivity.getCurrentUser();
        activity = (AppCompatActivity) root.getContext();
        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.button_city :
                        if(currentUser.isEmailVerified())
                        Navigation.findNavController(activity,R.id.nav_host_fragment).navigate(R.id.city);
                        else
                            Toast.makeText(getContext(), "Подтвердите почтовый адрес указанный при регистрации", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.add_event :
                        if(currentUser.isEmailVerified())
                            Navigation.findNavController(activity,R.id.nav_host_fragment).navigate(R.id.add_fragment);
                        else
                            Toast.makeText(getContext(), "Подтвердите почтовый адрес указанный при регистрации", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.exit_profile:
                        mAuth.signOut();
                        break;
                    case R.id.fav:
                        Navigation.findNavController(activity,R.id.nav_host_fragment).navigate(R.id.fav);
                        break;

                }
            }
        };
        Button button_c = (Button) root.findViewById(R.id.button_city);
        Button button_a = (Button) root.findViewById(R.id.add_event);
        Button button_e=(Button)root.findViewById(R.id.exit_profile);
        Button button_f=(Button)root.findViewById(R.id.fav);

        if(currentUser!=null) {
            button_a.setOnClickListener(onClickListener2);
            button_c.setOnClickListener(onClickListener2);
            button_e.setOnClickListener(onClickListener2);
            button_f.setOnClickListener(onClickListener2);
        }

        return root;
    }
}