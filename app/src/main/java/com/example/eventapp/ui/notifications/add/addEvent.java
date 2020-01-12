package com.example.eventapp.ui.notifications.add;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import com.example.eventapp.R;

public class addEvent extends Fragment {

    public static ViewPager viewPager;
    static MyAdapter adapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.add_event_fragment, container, false);
        AppCompatActivity activity = (AppCompatActivity) root.getContext();
       AddEventViewModel.initial();
        FragmentManager fm = activity.getSupportFragmentManager();
        adapter = new MyAdapter(fm);
        viewPager = (ViewPager) root.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter); // устанавливаем адаптер
        viewPager.setCurrentItem(0); // выводим второй экран
        viewPager.setSaveFromParentEnabled(false);

/*        Блокирует прокрутку viewpager
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });

 */
        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View v, float pos) {
                final float opacity = Math.abs(Math.abs(pos) - 1);
                v.setAlpha(opacity);
            }
        });
        return root;
    }
    public static void next(int i){
        viewPager.setCurrentItem(i);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    private class MyAdapter extends FragmentStatePagerAdapter {

        MyAdapter( FragmentManager fm) {
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new addEventFirst();
                case 1:
                    return new addEventSecond();
                case 2:
                    return new addEventThird();
                case 3:
                    return new addEventFour();
                case 4:
                    return new addEventFifth();

                default:
                    return new addEventFirst();
            }
        }
    }

}
