package com.example.eventapp.ui.Profile.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.eventapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.eventapp.MainActivity.visibleMenu;

public class Enter extends Fragment {
    private EditText mail;
    private EditText pass,name;
    private FirebaseAuth mAuth;
    private boolean verify_flag=false;
    private ProgressBar circularProgress;

    private Button done;
    private boolean vfIsOpen=false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_enter, container, false);
        mail=(EditText)root.findViewById(R.id.mail);
        pass=(EditText)root.findViewById(R.id.pass);
        done=(Button)root.findViewById(R.id.success);
        circularProgress = root.findViewById(R.id.progressBar);

        name=(EditText)root.findViewById(R.id.name);
        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.success :
                        circularProgress.setVisibility(View.VISIBLE);
                        signing(mail.getText().toString(),pass.getText().toString());
                        break;
                }
            }
        };
        done.setOnClickListener(onClickListener2);
        mAuth = FirebaseAuth.getInstance();
        return root;
    }

    public void signing(String mail,String pass){
        mAuth.signInWithEmailAndPassword(mail,pass)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            visibleMenu(true);
                            //user = mAuth.getCurrentUser();
                            Navigation.findNavController(getActivity(),R.id.nav_host_fragment).popBackStack();
                            Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.navigation_home);
                            circularProgress.setVisibility(View.INVISIBLE);

                        }
                        else{
                            Toast.makeText(getContext(),"Вход не удачен",Toast.LENGTH_SHORT).show();
                            circularProgress.setVisibility(View.INVISIBLE);
                        }

                    }
                });
    }


}
