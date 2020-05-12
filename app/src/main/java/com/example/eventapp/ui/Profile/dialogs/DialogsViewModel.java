package com.example.eventapp.ui.Profile.dialogs;

import android.graphics.Color;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventapp.CircularTransformation;
import com.example.eventapp.Event;
import com.example.eventapp.MainActivity;
import com.example.eventapp.R;
import com.example.eventapp.User;
import com.example.eventapp.messenger.Dialog;
import com.example.eventapp.messenger.Message;
import com.example.eventapp.ui.Profile.DataAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.eventapp.MainActivity.getCurrentUser;
import static com.example.eventapp.MainActivity.mDatabaseReference;

public class DialogsViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private static MutableLiveData<List> listData;
    private List<User> list;

    public DialogsViewModel(){
        list = new ArrayList<>();
        listData = new MutableLiveData<>();
        addEventFirebaseListener();
    }
    public static void setData(List<User> list){

        listData.setValue(list);
    }

    public LiveData<List> getData() {
        return listData;
    }

    private void addEventFirebaseListener() {

        mDatabaseReference.child("dialog")
                .addValueEventListener(new ValueEventListener() {
                    //если данные в БД меняются
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        list.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Dialog dialog = postSnapshot.getValue(Dialog.class);
                            String id = postSnapshot.getKey();
                            if(id.indexOf(MainActivity.getCurrentUser().getUid()) == 0){
                                id = id.replace(MainActivity.getCurrentUser().getUid(),"");
                                mDatabaseReference.child("user").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        User user = dataSnapshot.getValue(User.class);
                                        boolean exist = false;
                                        for (User dup : list) {
                                            if(dup.id.equals(user.id)){
                                                exist = true;
                                            }
                                        }
                                        if(!exist)
                                            list.add(user);
                                        setData(list);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
    }
    public static void getDialog(String id, final TextView message_tv) {
        mDatabaseReference.child("dialog").child(getCurrentUser().getUid()+id).orderByChild("timeMessage")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Message message = new Message();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                             message = postSnapshot.getValue(Message.class);
                        }
                        message_tv.setText(message.getTextMessage());

                        if(message.getType()==1){
                            message_tv.setTextColor(Color.argb(255,176,0,32));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}
