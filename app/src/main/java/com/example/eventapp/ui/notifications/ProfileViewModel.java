package com.example.eventapp.ui.notifications;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventapp.Event;
import com.example.eventapp.MainActivity;
import com.example.eventapp.ui.notifications.favourites.myFavourites;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.example.eventapp.MainActivity.mDatabaseReference;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("USERNAME");
    }

    public LiveData<String> getText() {
        return mText;
    }



}