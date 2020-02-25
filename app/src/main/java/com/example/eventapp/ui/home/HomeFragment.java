package com.example.eventapp.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import static com.example.eventapp.MainActivity.getDisplay;
import static com.example.eventapp.MainActivity.mDatabaseReference;
import static com.example.eventapp.ui.home.HomeViewModel.isPrice;
import static com.example.eventapp.ui.home.HomeViewModel.isPrice_kids;
import static com.example.eventapp.ui.home.HomeViewModel.isToday;
import static com.example.eventapp.ui.home.HomeViewModel.setPrice;
import static com.example.eventapp.ui.home.HomeViewModel.setPrice_kids;
import static com.example.eventapp.ui.home.HomeViewModel.setToday;

public class HomeFragment extends Fragment {
    private List<Event> list_events= new ArrayList<>();
    private HomeViewModel homeViewModel;
    public Fragment old;
    public ProgressBar circular_progress;
    public RecyclerView recyclerView;
    private static TextView tv_empty;
    private static ImageView iv_empty;
    private boolean is_loaded;
    private ImageButton bt_filter;
    private HashMap<String,List> filter_hash;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        circular_progress = (ProgressBar) root.findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) root.findViewById(R.id.list);
        tv_empty = (TextView) root.findViewById(R.id.tv_empty);
        iv_empty = (ImageView) root.findViewById(R.id.iv_empty);
        bt_filter = (ImageButton) root.findViewById(R.id.filter_but);
        addEventFirebaseListener();
        homeViewModel.getData().observe(this, new Observer<List>() {
            @Override
            public void onChanged(@Nullable List events) {
                DataAdapter adapter = new DataAdapter(getActivity(), events);
                recyclerView.setAdapter(adapter);            }
        });
        //Toast.makeText(getContext(), "Обновление", Toast.LENGTH_SHORT).show();
        is_loaded = false;


        //FILTRATION
        filter_hash = new HashMap<>();
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
                final Spinner dialog_type = (Spinner) promptsView.findViewById(R.id.types);

                final EditText price_min = (EditText) promptsView.findViewById(R.id.min_between);
                final EditText price_max = (EditText) promptsView.findViewById(R.id.max_between);
                final CheckBox free   = (CheckBox) promptsView.findViewById(R.id.price_free);

                final EditText price_kids_min = (EditText) promptsView.findViewById(R.id.min_between_k);
                final EditText price_kids_max = (EditText) promptsView.findViewById(R.id.max_between_k);
                final CheckBox free_kids   = (CheckBox) promptsView.findViewById(R.id.price_free_kids);

                final Button date_min = (Button) promptsView.findViewById(R.id.min_date);
                final Button date_max = (Button) promptsView.findViewById(R.id.max_date);
                final CheckBox today   = (CheckBox) promptsView.findViewById(R.id.today);



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
                                if(today.isChecked())
                                {
                                    setToday(true);
                                    date_min.setClickable(false);
                                    date_max.setClickable(false);
                                    date_min.setText(getResources().getText(R.string.min_date));
                                    date_max.setText(getResources().getText(R.string.max_date));

                                }
                                else{
                                    setToday(false);
                                    date_min.setClickable(true);
                                    date_max.setClickable(true);
                                }
                                break;
                            case R.id.price_free:
                                if(free.isChecked()){
                                    setPrice(true);
                                    setPrice_kids(true);
                                    price_min.setEnabled(false);
                                    price_max.setEnabled(false);
                                    price_kids_max.setEnabled(false);
                                    price_kids_min.setEnabled(false);
                                    free_kids.setChecked(true);
                                    free_kids.setEnabled(false);

                                }
                                else{
                                    setPrice(false);
                                    setPrice_kids(false);
                                    price_min.setEnabled(true);
                                    price_max.setEnabled(true);
                                    price_kids_max.setEnabled(true);
                                    price_kids_min.setEnabled(true);
                                    free_kids.setChecked(false);
                                    free_kids.setEnabled(true);
                                }
                                break;
                            case R.id.price_free_kids:
                                if(free_kids.isChecked())
                                {
                                    setPrice_kids(true);
                                    price_kids_max.setEnabled(false);
                                    price_kids_min.setEnabled(false);
                                }
                                else{
                                    setPrice_kids(false);
                                    price_kids_max.setEnabled(true);
                                    price_kids_min.setEnabled(true);
                                }
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

                        .setCancelable(false)
                        .setTitle(R.string.parameters)
                        .setPositiveButton(R.string.continue_but,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        //TYPE
                                        List<String> local_list = new ArrayList<>();
                                        if(!dialog_type.getSelectedItem().toString().equals("Тип")){
                                            local_list.add(dialog_type.getSelectedItem().toString());
                                            filter_hash.put("type",local_list);
                                        }
                                        filter_hash.put("type",local_list);
                                        //PRICE
                                        List<Integer> price_list = checkPrice(free,price_min,price_max);
                                        filter_hash.put("price",price_list);
                                        price_list = checkPrice(free_kids,price_kids_min,price_kids_max);
                                        filter_hash.put("price_kids",price_list);
                                        //DATE
                                        List<Long> date_list = new ArrayList<>();
                                        if(today.isChecked()){
                                            date_list.add((long)0);date_list.add((long)0);
                                        }
                                        else if(!date_min.getText().toString().toLowerCase().equals("начало") &
                                                date_max.getText().toString().toLowerCase().equals("конец")){
                                            long date = Utils.getDate(date_min.getText().toString());
                                            date_list.add(date);
                                            date_list.add(date+86400000-1);
                                        }
                                        else if(date_min.getText().toString().toLowerCase().equals("начало") &
                                                !date_max.getText().toString().toLowerCase().equals("конец")){
                                            long date = Utils.getDate(date_max.getText().toString());
                                            date_list.add(date-86400000-1);
                                            date_list.add(date);
                                        }
                                        else if(!date_min.getText().toString().toLowerCase().equals("начало") &
                                                !date_max.getText().toString().toLowerCase().equals("конец")){
                                            long date = Utils.getDate(date_min.getText().toString());
                                            date_list.add(date);
                                            date = Utils.getDate(date_max.getText().toString());
                                            date_list.add(date);
                                        }
                                        filter_hash.put("date",date_list);
                                        //сохраняем строковый эквивалент чтобы не переводить по сто раз
                                        List<String> dates_string = new ArrayList<>();
                                        dates_string.add(date_min.getText().toString());
                                        dates_string.add(date_max.getText().toString());
                                        filter_hash.put("date_string",dates_string);
                                    }
                                })
                        .setNegativeButton(R.string.drop,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        filter_hash.clear();
                                        dialog.cancel();
                                    }
                                });

                //Создаем AlertDialog:
                AlertDialog alertDialog = mDialogBuilder.create();

                //и отображаем его:
                alertDialog.show();
                Point size = getDisplay();
                alertDialog.getWindow().setLayout(size.x,size.y*3/5);
                alertDialog.getWindow().setGravity(Gravity.BOTTOM);
                //если есть в фильтрах значения то восстанавливаем их в диалоговое окно
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
                        price_max.setText(filter_hash.get("price").get(1).toString());
                    }
                    if(isPrice_kids()){
                        free_kids.setChecked(true);
                        price_kids_max.setEnabled(false);
                        price_kids_min.setEnabled(false);
                    }
                    else if(filter_hash.get("price")!=null & filter_hash.get("price").size()==2){
                        price_kids_min.setText(filter_hash.get("price_kids").get(0).toString());
                        price_kids_max.setText(filter_hash.get("price_kids").get(1).toString());
                    }
                    if(isToday()){
                        today.setChecked(true);
                        date_min.setClickable(false);
                        date_max.setClickable(false);
                    }
                    else if(filter_hash.get("date")!=null & filter_hash.get("date").size()==2){
                        long time = (long)filter_hash.get("date").get(0);
                        date_min.setText(filter_hash.get("date_string").get(0).toString());
                        date_max.setText(filter_hash.get("date_string").get(1).toString());
                    }
                }
            }
        });
        return root;
    }

    private void addEventFirebaseListener() {
        //показываем View загрузки
        circular_progress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        tv_empty.setVisibility(View.INVISIBLE);
        iv_empty.setVisibility(View.INVISIBLE);
        mDatabaseReference.child("events")
                .addValueEventListener(new ValueEventListener() {
                    //если данные в БД меняются
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (list_events.size() > 0) {
                            list_events.clear();
                        }
                        //проходим по всем записям и помещаем их в list_users в виде класса User
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Event event = postSnapshot.getValue(Event.class);

                            list_events.add(event);
                        }
                        if(!is_loaded) {
                            //публикуем данные в ListView
                            DataAdapter adapter = new DataAdapter(getActivity(), list_events);
                            recyclerView.setAdapter(adapter);
                            homeViewModel.setData(list_events);
                            recyclerView.scrollToPosition(MainActivity.recyclerPosition);
                            //убираем View загрузки
                            circular_progress.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            tv_empty.setVisibility(View.VISIBLE);
                            iv_empty.setVisibility(View.VISIBLE);
                            is_loaded=true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
    }
    public List<Integer> checkPrice(CheckBox free,EditText price_min, EditText price_max){
        List<Integer> price_list = new ArrayList<>();
        if(free.isChecked()){
            price_list.add(0);price_list.add(0);
        }
        else if(!price_min.getText().toString().isEmpty()&&price_max.getText().toString().isEmpty()){
            price_list.add(Integer.parseInt(price_min.getText().toString()));
            price_list.add(99999);
        }
        else if(price_min.getText().toString().isEmpty()&&!price_max.getText().toString().isEmpty()){
            price_list.add(0);
            price_list.add(Integer.parseInt(price_max.getText().toString()));
        }
        else if(!price_min.getText().toString().isEmpty()&&!price_max.getText().toString().isEmpty()){
            price_list.add(Integer.parseInt(price_min.getText().toString()));
            price_list.add(Integer.parseInt(price_max.getText().toString()));
        }

        return price_list;
    }

}