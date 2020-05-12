package com.example.eventapp.ui.Profile;

import android.widget.ImageView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventapp.CircularTransformation;
import com.example.eventapp.User;
import com.squareup.picasso.Picasso;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("USERNAME");
    }


    public LiveData<String> getText() {
        return mText;
    }

    public static void setPhoto(ImageView iv){
        Picasso.get()
                .load(User.getPhoto())
                .transform(new CircularTransformation(0))
                .into(iv);

    }

}