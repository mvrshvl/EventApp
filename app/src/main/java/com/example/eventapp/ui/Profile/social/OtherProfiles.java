package com.example.eventapp.ui.Profile.social;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventapp.Event;
import com.example.eventapp.MainActivity;
import com.example.eventapp.R;
import com.example.eventapp.User;
import com.example.eventapp.Utils;
import com.example.eventapp.ui.Profile.DataAdapter;
import com.example.eventapp.ui.home.ClickEvent.SlidingImage_Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import static com.example.eventapp.MainActivity.mDatabaseReference;
import static com.example.eventapp.MainActivity.visibleMenu;
import static com.example.eventapp.Utils.verify;

public class OtherProfiles extends Fragment {

    private OtherProfilesViewModel mViewModel;
    private View root;
    private User user;
    private TextView name_tv,mail_tv,city_tv;
    private Button events_bt,msg_bt;
    private ImageView back_bt;
    private ImageView photo_iv;

    private static ViewPager mPager_in;
    private CirclePageIndicator indicator_in;
    private int position = 0;
    private Event event;
    private List<Event> list_events;
    private int NUM_PAGES;
    int currentPage;

    Bundle data;

    public static OtherProfiles newInstance() {
        return new OtherProfiles();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        visibleMenu(true);
        final String user_id = getArguments().getString("id");
        root =  inflater.inflate(R.layout.other_profiles_fragment, container, false);
        name_tv = (TextView)root.findViewById(R.id.name_tv);
        city_tv = (TextView) root.findViewById(R.id.city_tv);
        back_bt = (ImageView) root.findViewById(R.id.back_bt);
        msg_bt = (Button) root.findViewById(R.id.msg_bt);
        photo_iv = (ImageView) root.findViewById(R.id.photo_iv);

        mPager_in = (ViewPager) root.findViewById(R.id.pager);
        indicator_in = (CirclePageIndicator) root.findViewById(R.id.indicator);

        list_events = new ArrayList<>();
        OtherProfilesViewModel.loadUser(user_id,name_tv,city_tv,photo_iv,list_events);
        if(!list_events.isEmpty())
            initIn();


        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.msg_bt :
                        data = new Bundle();
                        data.putString("id",getArguments().getString("id"));
                        data.putString("name",name_tv.getText().toString());
                        Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.messenger,data);
                        break;
                    case R.id.back_bt:
                        Navigation.findNavController(getActivity(),R.id.nav_host_fragment).popBackStack();
                        break;
                }
            }

        };
        back_bt.setOnClickListener(onClickListener1);
        msg_bt.setOnClickListener(onClickListener1);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OtherProfilesViewModel.class);
        mViewModel.getData().observe(this, new Observer<List>() {
            @Override
            public void onChanged(@Nullable List events) {
                list_events = events;
                initIn();
            }
        });
        // TODO: Use the ViewModel
    }
    //Внутренний пэйджер
    protected void initIn() {
        EventSliding.setFlag(true);
        mPager_in.setAdapter(new EventSliding(getContext(),list_events));

        indicator_in.setViewPager(mPager_in);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator_in.setRadius(5 * density);
        NUM_PAGES =list_events.size();

        // Pager listener over indicator
        indicator_in.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int pos) {
                currentPage = pos;
                position = pos;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }


}
