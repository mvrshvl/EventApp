package com.example.eventapp.ui.Profile.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eventapp.MainActivity;
import com.example.eventapp.R;
import com.example.eventapp.User;
import com.example.eventapp.ui.Profile.add.addEventFifth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class Registration extends Fragment {
    private EditText pass,name,mail;
    private Spinner town;
    private FirebaseAuth mAuth;
    private boolean verify_flag=false;
    private ProgressBar circularProgress;

    private Button done;
    private boolean vfIsOpen=false;
    AppCompatActivity activity;
    FirebaseUser c_user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_registration, container, false);
        activity = (AppCompatActivity) root.getContext();
        mail=(EditText)root.findViewById(R.id.mail);
        pass=(EditText)root.findViewById(R.id.pass);
        done=(Button)root.findViewById(R.id.success);
        town=(Spinner)root.findViewById(R.id.town);
        name=(EditText)root.findViewById(R.id.name);
        circularProgress = root.findViewById(R.id.progressBar);


        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.success :
                        circularProgress.setVisibility(View.VISIBLE);
                        registration(mail.getText().toString(), pass.getText().toString());

                        break;
                }
            }
        };
        done.setOnClickListener(onClickListener2);
        mAuth = FirebaseAuth.getInstance();
        return root;
    }
    private boolean setData(){
    User.setCity(town.getSelectedItem().toString());
    User.setMail(mail.getText().toString());
    User.setName(name.getText().toString());
    User.setPhoto("https://firebasestorage.googleapis.com/v0/b/eventapp-13058.appspot.com/o/images%2F8331f049-b0f8-457b-b9f2-20c39d3df74a?alt=media&token=0a627934-c3b1-40df-9aed-d9dbef881710");

    if(town.getSelectedItemId()!=0||!User.getName().equals(""))
    return true;
    else
        return false;
    }
    public void registration(String mail,String pass){
        //Создаем аккаунт
        if(setData()) {

            mAuth.createUserWithEmailAndPassword(mail, pass)
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        //формируем запрос к серверу
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //если всё удачно
                                Toast.makeText(getContext(), "all good", Toast.LENGTH_SHORT).show();
                                //создаем обьект пользователя FireBaseUser
                                c_user = mAuth.getCurrentUser();
                                c_user.sendEmailVerification();
                                User.setId(c_user.getUid());
                                //Формирум статический класс User
                                User user = new User(User.getMail(),User.getCity(),User.getName(),User.getId(),User.getPhoto());
                                MainActivity.mDatabaseReference.child("user").child(user.getId()).setValue(user);
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(User.getName()).build();
                                MainActivity.getCurrentUser().updateProfile(profileUpdates);
                                c_user.updateProfile(profileUpdates);
                                circularProgress.setVisibility(View.INVISIBLE);

                            } else {
                                Toast.makeText(getContext(), "Пользователь с таким почтовым адресом уже зарегистрирован", Toast.LENGTH_SHORT).show();
                                circularProgress.setVisibility(View.INVISIBLE);

                            }
                        }
                    });
        }
        else Toast.makeText(getContext(), "Заполните все пустые поля", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}
