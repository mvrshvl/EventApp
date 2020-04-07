package com.example.eventapp.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventapp.MainActivity;
import com.example.eventapp.R;
import com.example.eventapp.Event;
import com.example.eventapp.Utils;
import com.example.eventapp.ui.dateTimePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.EventRaiser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import static com.example.eventapp.MainActivity.getDisplay;
import static com.example.eventapp.ui.home.HomeViewModel.dateEn_b;
import static com.example.eventapp.ui.home.HomeViewModel.dateSt_b;
import static com.example.eventapp.ui.home.HomeViewModel.getFilter_hash;
import static com.example.eventapp.ui.home.HomeViewModel.getOrderIndex;
import static com.example.eventapp.ui.home.HomeViewModel.maxPrice_et;
import static com.example.eventapp.ui.home.HomeViewModel.maxPrice_k_et;
import static com.example.eventapp.ui.home.HomeViewModel.minPrice_et;
import static com.example.eventapp.ui.home.HomeViewModel.minPrice_k_et;
import static com.example.eventapp.ui.home.HomeViewModel.priceMin;
import static com.example.eventapp.ui.home.HomeViewModel.price_cb;
import static com.example.eventapp.ui.home.HomeViewModel.price_kids_cb;
import static com.example.eventapp.ui.home.HomeViewModel.setFilter_hash;
import static com.example.eventapp.ui.home.HomeViewModel.setOrder;
import static com.example.eventapp.ui.home.HomeViewModel.today_cb;
import static com.example.eventapp.ui.home.HomeViewModel.type;
import static com.example.eventapp.ui.home.HomeViewModel.type_index;
import static com.example.eventapp.ui.home.filter.clearFilter;
import static com.example.eventapp.ui.home.filter.setType;

public class HomeFragment extends Fragment {
    Spinner dialog_type;
    Spinner sort_type;

    EditText price_min;
    EditText price_max;
    CheckBox free;

    EditText price_kids_min;
    EditText price_kids_max;
    CheckBox free_kids;

    Button date_min;
    Button date_max;
    CheckBox today;


    public static List<Event> list_events= new ArrayList<>();
    public static List<Event> current_list = new ArrayList<>();
    private HomeViewModel homeViewModel;
    public static ProgressBar circular_progress;
    public static RecyclerView recyclerView;
    private static TextView tv_empty;
    private static ImageView iv_empty;
    protected static boolean is_loaded;
    private ImageButton bt_filter;
    private HashMap<String,List> filter_hash;
    private EditText search;
    DataAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //Инит элементов
        circular_progress = (ProgressBar) root.findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) root.findViewById(R.id.list);
        tv_empty = (TextView) root.findViewById(R.id.tv_empty);
        iv_empty = (ImageView) root.findViewById(R.id.iv_empty);
        Utils.loading(recyclerView,circular_progress,tv_empty
                ,iv_empty,true,true);
        bt_filter = (ImageButton) root.findViewById(R.id.filter_but);
        search = (EditText) root.findViewById(R.id.search);
        //Грузим контент
        if(HomeViewModel.getData().getValue() == null)
        {
            clearFilter();
            HomeViewModel.addEventFirebaseListener();
        }

            HomeViewModel.getData().observe(this, new Observer<List>() {
                @Override
                public void onChanged(@Nullable List events) {
                    adapter = new DataAdapter(getActivity(), events);
                    list_events = events;
                    current_list = events;
                    if(events.size()!=0)
                        Utils.loading(recyclerView,circular_progress,tv_empty
                                ,iv_empty,false,true);
                    else
                        Utils.loading(recyclerView,circular_progress,tv_empty
                                ,iv_empty,false,false);
                    recyclerView.setAdapter(adapter);            }
            });
                //перехватываем загруженный контент

        is_loaded = false;

        bt_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Получаем вид с файла prompt.xml, который применим для диалогового окна:
                LayoutInflater li = LayoutInflater.from(getContext());
                final View promptsView = li.inflate(R.layout.filter_dialog, null);

                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getContext(),R.style.dialogStyle);

                //Настраиваем prompt.xml для нашего AlertDialog:
                mDialogBuilder.setView(promptsView);

                //Настраиваем отображение поля для ввода текста в открытом диалоге:
                dialog_type = (Spinner) promptsView.findViewById(R.id.types);
                sort_type   = (Spinner) promptsView.findViewById(R.id.sort);

                price_min = (EditText) promptsView.findViewById(R.id.min_between);
                price_max = (EditText) promptsView.findViewById(R.id.max_between);
                free   = (CheckBox) promptsView.findViewById(R.id.price_free);

                price_kids_min = (EditText) promptsView.findViewById(R.id.min_between_k);
                price_kids_max = (EditText) promptsView.findViewById(R.id.max_between_k);
                free_kids   = (CheckBox) promptsView.findViewById(R.id.price_free_kids);

                date_min = (Button) promptsView.findViewById(R.id.min_date);
                date_max = (Button) promptsView.findViewById(R.id.max_date);
                today   = (CheckBox) promptsView.findViewById(R.id.today);



                //DATE AND TiME
                final dateTimePicker date_time_2 = new dateTimePicker();
                View.OnClickListener onClickListener2 = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        switch (v.getId()) {
                            case R.id.min_date:
                                date_time_2.callDatePicker(date_min, promptsView);
                                break;
                            case R.id.max_date:
                                date_time_2.callDatePicker(date_max, promptsView);
                                break;
                            case R.id.today:
                                setToday(today.isChecked());
                                break;
                            case R.id.price_free:
                                setFree(free.isChecked());
                                break;
                            case R.id.price_free_kids:
                                setFreeKids(free_kids.isChecked());
                                break;

                        }
                    }
                };
                date_min.setOnClickListener(onClickListener2);
                date_max.setOnClickListener(onClickListener2);
                today.setOnClickListener(onClickListener2);
                free.setOnClickListener(onClickListener2);
                free_kids.setOnClickListener(onClickListener2);



                            //Настраиваем сообщение в диалоговом окне:
                mDialogBuilder

                        .setCancelable(true)
                        .setTitle(R.string.parameters)
                        .setPositiveButton(R.string.continue_but,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        //Грузим
                                        //Выставляю прогресс
                                        Utils.loading(recyclerView,circular_progress,tv_empty
                                                ,iv_empty,true,true);

                                        //запоминаем фильтры
                                        setType(dialog_type.getSelectedItem().toString(),getResources().getStringArray(R.array.types));
                                        filter.setPrice(free.isChecked(),free_kids.isChecked(),
                                                price_min.getText().toString(),
                                                price_max.getText().toString(),
                                                price_kids_min.getText().toString(),
                                                price_kids_max.getText().toString());
                                        filter.setDate(today.isChecked(),
                                                date_min.getText().toString(),
                                                date_max.getText().toString());
                                        setOrder(sort_type.getSelectedItem().toString());


                                    }
                                })
                        .setNegativeButton(R.string.drop,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                        clearFilter();
                                        setOrder("По дате");
                                        reload(list_events);


//                                        Utils.loading(recyclerView,circular_progress,tv_empty
//                                                ,iv_empty,false,true);
                                    }
                                });

                //Создаем AlertDialog:
                AlertDialog alertDialog = mDialogBuilder.create();

                //и отображаем его:
                alertDialog.show();
                Point size = getDisplay();
                alertDialog.getWindow().setLayout(size.x,size.y*4/6);
                alertDialog.getWindow().setGravity(Gravity.BOTTOM);
                sort_type.setSelection(getOrderIndex());
                //если есть в фильтрах значения то восстанавливаем их в диалоговое окно
                //type
                dialog_type.setSelection(type_index);
                setFree(price_cb);
                setFreeKids(price_kids_cb);
                setToday(today_cb);
                price_min.setText(minPrice_et);
                price_max.setText(maxPrice_et);
                price_kids_min.setText(minPrice_k_et);
                price_kids_max.setText(maxPrice_k_et);
                date_min.setText(dateSt_b);
                date_max.setText(dateEn_b);

                /*


                if(filter_hash.size()!=0){
                    //восстанавливаем значение в спинере
                    if(filter_hash.get("type")!=null & filter_hash.get("type").size()!=0){
                        String [] array = getResources().getStringArray(R.array.types);
                        for(int i =0;i<array.length;i++){
                            if(array[i].equals(filter_hash.get("type").get(0))){
                                dialog_type.setSelection(i);
                            }
                        }
                    }

                    if(isPrice()){
                        free.setChecked(true);
                        price_min.setEnabled(false);
                        price_max.setEnabled(false);
                        price_kids_max.setEnabled(false);
                        price_kids_min.setEnabled(false);
                        free_kids.setChecked(true);
                        free_kids.setEnabled(false);
                    }
                    else if(filter_hash.get("price")!=null & filter_hash.get("price").size()==2){
                        price_min.setText(filter_hash.get("price").get(0).toString());
                        if(!filter_hash.get("price").get(1).toString().equals("99999999")){
                            price_max.setText(filter_hash.get("price").get(1).toString());
                    }
                    }
                    if(isPrice_kids()){
                        free_kids.setChecked(true);
                        price_kids_max.setEnabled(false);
                        price_kids_min.setEnabled(false);
                    }
                    else if(filter_hash.get("price_kids")!=null & filter_hash.get("price_kids").size()==2){
                        price_kids_min.setText(filter_hash.get("price_kids").get(0).toString());
                        if(!filter_hash.get("price_kids").get(1).toString().equals("99999999"))
                        price_kids_max.setText(filter_hash.get("price_kids").get(1).toString());
                    }

                    else if(filter_hash.get("date")!=null & filter_hash.get("date").size()==2){
                        long time = (long)filter_hash.get("date").get(0);
                        date_min.setText(filter_hash.get("date_string").get(0).toString());
                        date_max.setText(filter_hash.get("date_string").get(1).toString());
                    }

                }
                 */

            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(search.getText().toString().length()!=0) {
                    reload(filter.goSearch(current_list, search.getText().toString()));
                }
                else{
                    reload(current_list);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return root;
    }

    private void reload(List<Event> events){
        current_list = list_events;
        adapter = new DataAdapter(getActivity(), events);
        recyclerView.setAdapter(adapter);
        if(events.size()==0){
            Utils.loading(recyclerView,circular_progress,tv_empty
                    ,iv_empty,false,false);
        }
    }

    private void setFreeKids(boolean b){
        if(b)
        {
            free_kids.setChecked(true);
            price_kids_max.setEnabled(false);
            price_kids_min.setEnabled(false);
        }
        else{
            price_kids_max.setEnabled(true);
            price_kids_min.setEnabled(true);
        }
    }
    private void setFree(boolean b){
        if(b){
            price_min.setEnabled(false);
            price_max.setEnabled(false);
            price_kids_max.setEnabled(false);
            price_kids_min.setEnabled(false);
            free.setChecked(true);
            free_kids.setChecked(true);
            free_kids.setEnabled(false);

        }
        else{
            price_min.setEnabled(true);
            price_max.setEnabled(true);
            price_kids_max.setEnabled(true);
            price_kids_min.setEnabled(true);
            free_kids.setChecked(false);
            free_kids.setEnabled(true);
        }
    }
    private void setToday(boolean b){
        if(b)
        {
            today.setChecked(true);
            date_min.setClickable(false);
            date_max.setClickable(false);
            date_min.setText(getResources().getText(R.string.min_date));
            date_max.setText(getResources().getText(R.string.max_date));

        }
        else{
            date_min.setClickable(true);
            date_max.setClickable(true);
        }
    }

}