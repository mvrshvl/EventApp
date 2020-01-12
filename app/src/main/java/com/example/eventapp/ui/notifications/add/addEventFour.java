package com.example.eventapp.ui.notifications.add;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.eventapp.MainActivity;
import com.example.eventapp.R;
import com.example.eventapp.ui.dateTimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class addEventFour extends Fragment {
    public static addEventFour newInstance() {
        return new addEventFour();
    }

    private Button next;
    private static Button date_end;
    private static Button time_end;
    private static EditText price;
    private static CheckBox free;
    View view_root;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.add_event_four_step, container, false);
        view_root=root;
        ImageView back = (ImageView) root.findViewById(R.id.back_to_2);
        date_end = (Button) root.findViewById(R.id.date_picker_end);
        time_end = (Button) root.findViewById(R.id.time_picker_end);
        price = (EditText) root.findViewById(R.id.price_kids);
        free=(CheckBox) root.findViewById(R.id.price_free_kids);
        next=(Button)root.findViewById(R.id.next_to_4);

        final dateTimePicker date_time_2 = new dateTimePicker();
        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.date_picker_end :
                        date_time_2.callDatePicker(date_end,root);
                        break;
                    case R.id.time_picker_end :
                        date_time_2.callTimePicker(time_end,root);
                        break;
                    case R.id.next_to_4 :
                        setData();
                        addEvent.next(4);
                    case R.id.back_to_2 :
                        addEvent.next(2);
                        break;
                    case R.id.price_free_kids :
                        if(free.isChecked())
                        {
                            price.setEnabled(false);
                            price.setText("0");
                        }
                        else{
                            price.setEnabled(true);
                            price.setText("");
                        }
                        break;
                }
            }

        };
        date_end.setOnClickListener(onClickListener2);
        time_end.setOnClickListener(onClickListener2);
        free.setOnClickListener(onClickListener2);
        back.setOnClickListener(onClickListener2);
        next.setOnClickListener(onClickListener2);
        return root;
    }

    public static void setData(){
        AddEventViewModel.setDate_end(date_end.getText().toString());
        AddEventViewModel.setTime_end(time_end.getText().toString());

        if(free.isChecked()){
            AddEventViewModel.setPrice_kids(0);
        }
        else{
            if(!price.getText().toString().equals(""))
                AddEventViewModel.setPrice_kids(Integer.parseInt(price.getText().toString()));
            else  AddEventViewModel.setPrice_kids(-1);
        }
    }
    private void getData(){
        if(AddEventViewModel.getDate_end()!=null){
            date_end.setText(AddEventViewModel.getDate_end());
        }
        if(AddEventViewModel.getTime_end()!=null){
            time_end.setText(AddEventViewModel.getTime_end());
        }

        if(AddEventViewModel.getPrice_kids()!=-1){
            price.setText(AddEventViewModel.getPrice_kids()+"");
            free.setChecked(false);
        }
        else if (AddEventViewModel.getPrice_kids()==0){
            free.setChecked(true);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
        getData();
    }

}
