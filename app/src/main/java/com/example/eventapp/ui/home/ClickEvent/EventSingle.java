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
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.eventapp.Event;
import com.example.eventapp.MainActivity;
import com.example.eventapp.R;
import com.example.eventapp.User;
import com.example.eventapp.Utils;
import com.example.eventapp.ui.Profile.DataAdapter;
import com.google.firebase.database.ChildEventListener;
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
import static com.example.eventapp.MainActivity.moderator_mode;
import static com.example.eventapp.Utils.setTime;


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
    private static List<String> IMAGES;
    private ArrayList<String> ImagesArray = new ArrayList<String>();
    //VIEWPAGER IN
    private static ViewPager mPager_in;
    private CirclePageIndicator indicator_in;
    private int public_position =0;
    private Button ok_bt;
    private Button ban_bt;
    private Button diss_bt;
    private Event event;
    private User user;
    private LinearLayout block_price,block_about;
    private ImageView like_bt;
    private boolean like_flag;
    private TextView date_end_tv, type_tv,address_tv,price_tv,price_out_tv,price_kids_tv,price_kids_out_tv,kids_age_et,about_tv,like_tv,profile_tv;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.event_fragment, container, false);

        final TextView date_tv =(TextView) root.findViewById(R.id.date_event);
        final TextView name_tv = (TextView) root.findViewById(R.id.name_event);
        final ImageView full = (ImageView) root.findViewById(R.id.fullscreen);
        date_end_tv= (TextView)root.findViewById(R.id.date_event_end);
        type_tv = (TextView) root.findViewById(R.id.type_tv);
        address_tv = (TextView)root.findViewById(R.id.address_tv);
        price_tv = (TextView)root.findViewById(R.id.price_tv);
        price_out_tv = (TextView) root.findViewById(R.id.price_out);
        price_kids_tv = (TextView) root.findViewById(R.id.price_kids_tv);
        price_kids_out_tv = (TextView) root.findViewById(R.id.price_kids_out);
        price_kids_out_tv = (TextView) root.findViewById(R.id.price_kids_out);
        price_kids_out_tv = (TextView) root.findViewById(R.id.price_kids_out);
        ok_bt = (Button) root.findViewById(R.id.ok_bt);
        ban_bt = (Button) root.findViewById(R.id.bun_bt);
        diss_bt = (Button)root.findViewById(R.id.diss_but);
        kids_age_et = (TextView)root.findViewById(R.id.kids_age_et);
        block_price = (LinearLayout) root.findViewById(R.id.block_price_kids);
        about_tv = (TextView) root.findViewById(R.id.about_tv);
        block_about = (LinearLayout)root.findViewById(R.id.block_about);
        like_bt = (ImageView) root.findViewById(R.id.like_bt);
        like_tv = (TextView)root.findViewById(R.id.like_tv);
        profile_tv = (TextView)root.findViewById(R.id.profile_id_tv);
        //PAGER
        mPager_in = (ViewPager) root.findViewById(R.id.pager);
        indicator_in = (CirclePageIndicator) root.findViewById(R.id.indicator);


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

                IMAGES = new ArrayList<>();
                if(!e.getImages_path1().isEmpty())
                    IMAGES.add(e.getImages_path1());
                if(!e.getImages_path2().isEmpty())
                    IMAGES.add(e.getImages_path2());
                if(!e.getImages_path3().isEmpty())
                    IMAGES.add(e.getImages_path3());
                initIn();
                date_tv.setText(setTime(e.getDate()));
                name_tv.setText(e.getName());
                like_tv.setText(e.getLike()+"");

                ///// установим лайк
                mDatabaseReference.child("favourites").orderByChild("event").equalTo(e.getId()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String userDB = "" + dataSnapshot.child("user").getValue();
                        String userCU = MainActivity.getCurrentUser().getUid();


                        if (userDB.equals(userCU)) {
                            like_bt.setImageResource(R.drawable.ic_favorite_black_24dp);
                            like_flag = true;
                        }

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                if(setTime(e.getDate_end()).indexOf("23 : 59")==-1)
                    date_end_tv.setText(setTime(e.getDate_end()));
                else{
                    ViewGroup.LayoutParams params = date_end_tv.getLayoutParams();
                    params.height = 0;
                }
                type_tv.setText(e.getType());
                address_tv.setText(e.getAddress());

                if(e.getAbout().length()==0){
                    ViewGroup.LayoutParams params = block_about.getLayoutParams();
                    params.height = 0;
                }
                else{
                    about_tv.setText(e.getAbout());
                }
                if(e.getType_age()==0) {
                    if(e.getPrice()==0)
                        price_out_tv.setText("Бесплатно");
                    else
                        price_out_tv.setText(String.valueOf(e.getPrice()));
                    if(e.getPrice()!=0)
                    {
                        if(e.getPrice_kids()==0)
                            price_kids_out_tv.setText("Бесплатно");
                        else
                            price_kids_out_tv.setText(String.valueOf(e.getPrice_kids()));
                    }
                    else{
                        kids_age_et.setVisibility(View.INVISIBLE);
                        price_kids_tv.setVisibility(View.INVISIBLE);
                        ViewGroup.LayoutParams params = block_price.getLayoutParams();
                        params.height = 0;

                    }

                    if(e.getKids_age()!=0) {
                        kids_age_et.setText(String.valueOf(e.getKids_age()));
                    }
                    else
                        kids_age_et.setText("18");
                }
                else if(e.getType_age()==1){
                    if (e.getPrice()==0)
                        price_out_tv.setText("Бесплатно");
                    else
                        price_out_tv.setText(String.valueOf(e.getPrice()));
                    price_kids_tv.setText("Вход только от 18 лет");
                }



                if(moderator_mode){
                    if(e.getState()==1) {
                        setOk();
                    }
                    else if(e.getState()==2) {
                        setBan();
                    }
                    else{
                        ban_bt.setText(getResources().getString(R.string.ban_s));
                        ok_bt.setText(getResources().getString(R.string.ok_s));
                    }
                }


                event = e;
                mDatabaseReference.child("user").child(event.getUser()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        user = dataSnapshot.getValue(User.class);
                        profile_tv.setText(user.name);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //moderator
        if(moderator_mode){
            ok_bt.setVisibility(View.VISIBLE);
            ban_bt.setVisibility(View.VISIBLE);
            diss_bt.setVisibility(View.INVISIBLE);
            diss_bt.setHeight(0);
        }
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.ok_bt:
                        allow();
                        break;
                    case R.id.bun_bt:
                        deny();
                        break;
                    case R.id.like_bt:
                        set_like();
                        break;
                    case R.id.profile_id_tv:
                        Bundle data = new Bundle();
                        data.putString("id",user.id);
                        Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.other_profile,data);
                        break;
                }
            }
        };
        ok_bt.setOnClickListener(onClickListener);
        ban_bt.setOnClickListener(onClickListener);
        like_bt.setOnClickListener(onClickListener);
        profile_tv.setOnClickListener(onClickListener);
        return root;
    }
    private void set_like(){
        if(like_flag){
            Utils.changeImgFalse(like_bt);
            Utils.deleteLike(event);
            event.setLike(event.getLike()-1);
            like_flag = false;
        }
        else{
            Utils.changeImgTrue(like_bt);
            Utils.sendLike(event);
            event.setLike(event.getLike()+1);
            like_flag = true;
        }
        like_tv.setText(event.getLike()+"");
    }
    //Полноэкранный пэйджер
    private void init(Context c) {
        ImagesArray.clear();
        for(int i=0;i<IMAGES.size();i++)
            ImagesArray.add(IMAGES.get(i));

        mPager.setAdapter(new SlidingImage_Adapter(c,ImagesArray));

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =IMAGES.size();

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
        for(int i=0;i<IMAGES.size();i++)
            ImagesArray.add(IMAGES.get(i));

        SlidingImage_Adapter.setFlag(true);
        mPager_in.setAdapter(new SlidingImage_Adapter(getContext(),ImagesArray));

        indicator_in.setViewPager(mPager_in);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator_in.setRadius(5 * density);
        NUM_PAGES =IMAGES.size();

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

    public void allow(){
        if(event.getState()!=1){
            setOk();
            //send
            Utils.setNewState(event,1);
        }
    }
    public void deny(){
        if(event.getState()!=2){
            setBan();
            Utils.setNewState(event,2);
            //send
        }
    }
    private void setBan(){
        ban_bt.setText(getResources().getString(R.string.ban_true_s));
        ok_bt.setText(getResources().getString(R.string.ok_s));
    }
    private void setOk(){
        ban_bt.setText(getResources().getString(R.string.ban_s));
        ok_bt.setText(getResources().getString(R.string.ok_true_s));
    }
}
