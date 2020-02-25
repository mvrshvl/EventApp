package com.example.eventapp.ui.home.ClickEvent;

import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.eventapp.Event;
import com.example.eventapp.R;
import com.example.eventapp.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.eventapp.MainActivity.getDisplay;
import static com.example.eventapp.MainActivity.mDatabaseReference;
import static com.example.eventapp.MainActivity.setTime;


public class EventSingle extends Fragment {


    private String name;
    private String date;
    private String image1;
//    public static EventSingle newInstance() {
//        return new EventSingle();
//    }
    //VIEWPAGER OUT
    private static ViewPager mPager;
    private static int currentPage = 0;
    CirclePageIndicator indicator;
    private static int NUM_PAGES = 0;
    private static String[] IMAGES;
    private ArrayList<String> ImagesArray = new ArrayList<String>();
    //VIEWPAGER IN
    private static ViewPager mPager_in;
    private CirclePageIndicator indicator_in;
    private int public_position =0;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.event_fragment, container, false);

        final TextView date_tv =(TextView) root.findViewById(R.id.date_event);
        final TextView name_tv = (TextView) root.findViewById(R.id.name_event);
        final ImageView full = (ImageView) root.findViewById(R.id.fullscreen);
        //PAGER
        mPager_in = (ViewPager) root.findViewById(R.id.pager);
        indicator_in = (CirclePageIndicator) root.findViewById(R.id.indicator);
        IMAGES = new String[3];
        //////


        //Вызываем диалоговое окно с полноэкранной картинкой
        full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    SlidingImage_Adapter.setFlag(false);
                    LayoutInflater li = LayoutInflater.from(getContext());
                    final View promptsView = li.inflate(R.layout.dialog_full_screen, null);

                    AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getContext(),R.style.dialogStyleImage);
                    mPager = (ViewPager) promptsView.findViewById(R.id.pager);
                    indicator = (CirclePageIndicator)
                            promptsView.findViewById(R.id.indicator);
                    //Настраиваем prompt.xml для нашего AlertDialog:
                    mDialogBuilder.setView(promptsView);
                    mDialogBuilder
                            .setCancelable(true)
                            .setNegativeButton("Закрыть",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                            SlidingImage_Adapter.setFlag(true);
                                            run_in();
                                        }
                                    });
                    //Вызываем пэйджер
                    init(promptsView.getContext());
                    //устанавиваем позицию картинки
                    run();
                    //Создаем AlertDialog:
                    AlertDialog alertDialog = mDialogBuilder.create();
                    //и отображаем его:
                    alertDialog.show();
                    //определяем размеры по экрануу
                    Point size = getDisplay();
                    alertDialog.getWindow().setLayout(size.x,size.y);
                    alertDialog.getWindow().setGravity(Gravity.BOTTOM);
                }

        });

        //Получаем id из бандла и подгружаем его
        final String event_id = getArguments().getString("id");
        mDatabaseReference.child("events").child(event_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Event e = dataSnapshot.getValue(Event.class);

                name = e.getName();
                long ms = e.getDate();
                date = setTime(ms);
                IMAGES[0] = e.getImages_path1();
                IMAGES[1] = e.getImages_path1();
                IMAGES[2] = e.getImages_path1();
                initIn();
                date_tv.setText(date);
                name_tv.setText(name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return root;
    }
    //Полноэкранный пэйджер
    private void init(Context c) {
        ImagesArray.clear();
        for(int i=0;i<IMAGES.length;i++)
            ImagesArray.add(IMAGES[i]);

        mPager.setAdapter(new SlidingImage_Adapter(c,ImagesArray));

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =IMAGES.length;

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                public_position = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }
    //Внутренний пэйджер
    private void initIn() {
        ImagesArray.clear();
        for(int i=0;i<IMAGES.length;i++)
            ImagesArray.add(IMAGES[i]);

        SlidingImage_Adapter.setFlag(true);
        mPager_in.setAdapter(new SlidingImage_Adapter(getContext(),ImagesArray));

        indicator_in.setViewPager(mPager_in);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator_in.setRadius(5 * density);
        NUM_PAGES =IMAGES.length;

        // Pager listener over indicator
        indicator_in.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                public_position = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }
    public  void run() {
        mPager.setCurrentItem(public_position, false);
    }
    public  void run_in() {
        mPager_in.setCurrentItem(public_position, true);
    }

}
