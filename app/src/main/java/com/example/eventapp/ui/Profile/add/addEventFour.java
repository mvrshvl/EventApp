package com.example.eventapp.ui.Profile.add;

import android.location.Address;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eventapp.R;
import com.example.eventapp.ui.dateTimePicker;


public class addEventFour extends Fragment {
    public static addEventFour newInstance() {
        return new addEventFour();
    }

    private Button next;
    private static Button date_end;
    private static Button time_end;
    private static EditText price, kids_age;
    private static CheckBox free;
    private RadioButton rb_null,rb_18,rb_kids;
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
        rb_18 = (RadioButton) root.findViewById(R.id.rb_18);
        rb_kids = (RadioButton)root.findViewById(R.id.rb_kids);
        rb_null = (RadioButton) root.findViewById(R.id.rb_null);
        kids_age = (EditText) root.findViewById(R.id.kids_age_et);


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
                    case R.id.rb_18:
                        price.setEnabled(false);
                        free.setEnabled(false);
                        kids_age.setEnabled(false);
                        AddEventViewModel.setType_age(1);
                        AddEventViewModel.setKids_age(0);
                        break;
                    case R.id.rb_kids:
                        free.setEnabled(true);
                        kids_age.setEnabled(true);
                        if(!free.isChecked())
                            price.setEnabled(true);
                        AddEventViewModel.setType_age(2);
                        break;
                    case R.id.rb_null:
                        free.setEnabled(true);
                        kids_age.setEnabled(true);
                        if(!free.isChecked())
                            price.setEnabled(true);
                        AddEventViewModel.setType_age(0);
                        break;

                }
            }

        };
        date_end.setOnClickListener(onClickListener2);
        time_end.setOnClickListener(onClickListener2);
        free.setOnClickListener(onClickListener2);
        back.setOnClickListener(onClickListener2);
        next.setOnClickListener(onClickListener2);
        rb_null.setOnClickListener(onClickListener2);
        rb_18.setOnClickListener(onClickListener2);
        rb_kids.setOnClickListener(onClickListener2);
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
            else  AddEventViewModel.setPrice_kids(0);
        }
        if (kids_age.getText().toString().length()>0){
            AddEventViewModel.setKids_age(Integer.parseInt(kids_age.getText().toString()));
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
        if(kids_age.getText().toString().length()>0){
            AddEventViewModel.setKids_age(Integer.parseInt(kids_age.getText().toString()));
        }


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
        getData();
    }

}
