package com.example.eventapp.ui.Profile.social;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Parcelable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.PagerAdapter;

import com.example.eventapp.Event;
import com.example.eventapp.MainActivity;
import com.example.eventapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



import androidx.viewpager.widget.PagerAdapter;

import com.example.eventapp.R;
import com.example.eventapp.ui.home.ClickEvent.SlidingImage_Adapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.eventapp.MainActivity.getCurrentUser;
import static com.example.eventapp.MainActivity.mDatabaseReference;
import static com.example.eventapp.Utils.setTime;

public class EventSliding extends PagerAdapter{


        private List<Event> events;
        private LayoutInflater inflater;
        private Context context;
        private static  boolean flag;

        public static boolean isFlag() {
            return flag;
        }

        public static void setFlag(boolean _flag) {
            flag = _flag;
        }


        public EventSliding(Context context, List<Event> events) {
            this.context = context;
            this.events=events;
            inflater = LayoutInflater.from(context);

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return events.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            final View imageLayout;
            final TextView name, type, date, like;
            final ImageView like_bt;
            //в зависимости от типа вызываемого пэйджера определяем свойства
                imageLayout = inflater.inflate(R.layout.card, view, false);

                assert imageLayout != null;
                final ImageView imageView = (ImageView) imageLayout
                        .findViewById(R.id.image);
                name = (TextView) imageLayout.findViewById(R.id.name);
            type = (TextView) imageLayout.findViewById(R.id.type);
            like = (TextView) imageLayout.findViewById(R.id.likes);
            date = (TextView) imageLayout.findViewById(R.id.date);
            like_bt = (ImageView)view.findViewById(R.id.b_like);

            Long date_ms = events.get(position).getDate();//(String)tmp.get(2);
            final String dt = setTime(date_ms);

            name.setText(events.get(position).getName());
            type.setText(events.get(position).getType());
            String lk = "" + events.get(position).getLike();
            like.setText(lk);
            date.setText(dt);
            final int pos = position;
            imageLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Bundle data = new Bundle();
                    data.putString("id",events.get(pos).getId());
                    Navigation.findNavController(activity,R.id.nav_host_fragment).navigate(R.id.event,data);
                }
            });
                //Загрузка события
            Picasso.get().load(events.get(position).getImages_path1()).fit().centerCrop().into(imageView);

            view.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        public void goEvent(View v){

        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }



}
