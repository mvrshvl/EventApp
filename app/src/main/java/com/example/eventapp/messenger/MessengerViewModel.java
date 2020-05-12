package com.example.eventapp.messenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventapp.Event;
import com.example.eventapp.MainActivity;
import com.example.eventapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.eventapp.MainActivity.getCurrentUser;
import static com.example.eventapp.MainActivity.mDatabaseReference;
import static com.example.eventapp.MainActivity.mFirebaseDatabase;

public class MessengerViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private static MutableLiveData<List> listData;
    private static List<Message> messages;
    private static String user_id;
    public static boolean dialog_created = false;

    public MessengerViewModel(){
        messages = new ArrayList<>();
        listData = new MutableLiveData<>();
    }
    public static void loadData(String id){
        user_id = id;
        getDialog();
    }
    public static void getDialog() {
        String userCU = MainActivity.getCurrentUser().getUid();
        mDatabaseReference.child("dialog").child(userCU+user_id).orderByChild("timeMessage")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        messages.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Message message = postSnapshot.getValue(Message.class);
                            messages.add(message);
                            if(message.getType()==1){
                                DatabaseReference reference = mFirebaseDatabase.getInstance().
                                        getReference("dialog")
                                        .child(getCurrentUser()
                                                .getUid()+user_id).child(postSnapshot.getKey())
                                                    .child("type");
                                reference.getRef().setValue(0);
                            }
                        }
                        setData(messages);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    public static void send(String msg){
        Message message = new Message(msg,MainActivity.getCurrentUser().getUid(),0);
        String message_id = UUID.randomUUID().toString();
        mDatabaseReference.child("dialog").child(MainActivity.getCurrentUser().getUid() + user_id).child(message_id).setValue(message);
        message.setType(1);
        mDatabaseReference.child("dialog").child(user_id + MainActivity.getCurrentUser().getUid()).child(message_id).setValue(message);
    }
    public static void setData(List<Message> list){

        listData.setValue(list);
    }
    public LiveData<List> getData() {
        return listData;
    }
}
