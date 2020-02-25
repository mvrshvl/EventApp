package com.example.eventapp.ui.home.ClickEvent;


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

import androidx.viewpager.widget.PagerAdapter;

import com.example.eventapp.Event;
import com.example.eventapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SlidingImage_Adapter extends PagerAdapter {


    private ArrayList<String> IMAGES;
    private LayoutInflater inflater;
    private Context context;
    private static  boolean flag;

    public static boolean isFlag() {
        return flag;
    }

    public static void setFlag(boolean flag) {
        SlidingImage_Adapter.flag = flag;
    }


    public SlidingImage_Adapter(Context context,ArrayList<String> IMAGES) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout;
        //в зависимости от типа вызываемого пэйджера определяем свойства
        if (!isFlag()){
            imageLayout = inflater.inflate(R.layout.dialog_full_screen_iv, view, false);
            assert imageLayout != null;
            final ImageView imageView = (ImageView) imageLayout
                    .findViewById(R.id.image);

            Picasso.get().load(IMAGES.get(position)).into(imageView);
        }
         else{
            imageLayout = inflater.inflate(R.layout.event_fragment_image, view, false);
            assert imageLayout != null;
            final ImageView imageView = (ImageView) imageLayout
                    .findViewById(R.id.image);

            Picasso.get().load(IMAGES.get(position)).fit().centerCrop().into(imageView);
        }

        view.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}