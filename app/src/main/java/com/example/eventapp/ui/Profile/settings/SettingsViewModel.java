package com.example.eventapp.ui.Profile.settings;

import androidx.lifecycle.ViewModel;

import com.example.eventapp.MainActivity;
import com.example.eventapp.User;
import com.example.eventapp.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

import static com.example.eventapp.MainActivity.mDatabaseReference;
import static com.example.eventapp.MainActivity.mFirebaseDatabase;

public class SettingsViewModel extends ViewModel {

    private HashMap<String, String> data;
    private int dialogState = 0;
    private static String password;
    private static boolean  flag = false;
    private static boolean  password_success = false;
    private static String city = User.getCity();

    protected static void sendChanges(final String arg1,final String arg2, final int type){
        flag = false;
        
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        password_success = mAuth.signInWithEmailAndPassword(MainActivity.getCurrentUser().getEmail(),arg1).isSuccessful();
            if(type == 1 & !arg1.isEmpty()){
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(User.getName()).build();
                User.setName(arg1);
                MainActivity.getCurrentUser().updateProfile(profileUpdates);
                DatabaseReference ref = mFirebaseDatabase.getInstance().getReference("user").child(MainActivity.getCurrentUser().getUid());
                ref.child("name").setValue(arg1);
                //firebase.auth().sendPasswordResetEmail(firebase.auth().currentUser.email)
            }
            else if(type == 2){
                    mAuth.sendPasswordResetEmail(MainActivity.getCurrentUser().getEmail());
            }
        }


    public int getDialogState(){
        return dialogState;
    }
    public void setDialogState(int i){
        dialogState = i;
    }
    public static void setCity(String new_city){
        city = new_city;
        Utils.setNewCity(new_city);
    }
    public  static String getCity(){
        return city;
    }


}
