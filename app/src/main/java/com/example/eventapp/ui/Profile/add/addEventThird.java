package com.example.eventapp.ui.Profile.add;

import android.app.usage.NetworkStatsManager;
import android.location.Address;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eventapp.R;
import com.example.eventapp.User;
import com.example.eventapp.Utils;
import com.example.eventapp.ui.dateTimePicker;

import java.util.List;


public class addEventThird extends Fragment {
    public static addEventThird newInstance() {
        return new addEventThird();
    }


private static Button next;
private static Button date;
private static Button time;
private static Button findAdd;
private static AutoCompleteTextView address;
private static EditText price;
private static CheckBox free;
private static CheckBox kids;
List<String> addresses;
    private ArrayAdapter<String> mAutoCompleteAdapter;
 View view_root;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.add_event_third_step, container, false);
        final View next_root = inflater.inflate(R.layout.add_event_four_step, container, false);
        view_root=root;
        ImageView back = (ImageView) root.findViewById(R.id.back_to_1);

        date = (Button) root.findViewById(R.id.date_picker_st);
        time = (Button) root.findViewById(R.id.time_picker_st);
        address = (AutoCompleteTextView) root.findViewById(R.id.address);
        price = (EditText) root.findViewById(R.id.price);
        free=(CheckBox) root.findViewById(R.id.price_free);
        next=(Button) root.findViewById(R.id.next_to_3);
        kids=(CheckBox) next_root.findViewById(R.id.price_free_kids);
        findAdd = (Button) root.findViewById(R.id.findAddress);
        final dateTimePicker date_time = new dateTimePicker();


        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.findAddress:
                        addresses = Utils.getAddresses(address.getText().toString(), User.getCity(),getContext());
                        address.setAdapter(new ArrayAdapter<>(getContext(),
                                android.R.layout.simple_spinner_dropdown_item, addresses));
                        address.showDropDown();
                        break;
                    case R.id.date_picker_st :
                        date_time.callDatePicker(date,root);
                        break;
                    case R.id.time_picker_st :
                        date_time.callTimePicker(time,root);
                        break;
                    case R.id.next_to_3 :
                        addEvent.next(3);
                        break;
                    case R.id.back_to_1 :
                        addEvent.next(1);
                        break;
                    case R.id.price_free :
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
        date.setOnClickListener(onClickListener1);
        time.setOnClickListener(onClickListener1);
        free.setOnClickListener(onClickListener1);
        back.setOnClickListener(onClickListener1);
        next.setOnClickListener(onClickListener1);
        findAdd.setOnClickListener(onClickListener1);
        return root;
    }

    public static void setData(){


        AddEventViewModel.setTime(time.getText().toString());
        String t = time.getText().toString();

        AddEventViewModel.setDate(date.getText().toString());

        AddEventViewModel.setAddress(address.getText().toString());

        if(free.isChecked()){
            AddEventViewModel.setPrice(0);
        }
        else{
            if(!price.getText().toString().equals(""))
                AddEventViewModel.setPrice(Integer.parseInt(price.getText().toString()));
            else  AddEventViewModel.setPrice(-1);
        }
    }
    private void getData(){
        if(AddEventViewModel.getDate()!=null){
            date.setText(AddEventViewModel.getDate());
        }
        if(AddEventViewModel.getTime()!=null){            String d = AddEventViewModel.getTime();

            time.setText(AddEventViewModel.getTime());

        }
        if(AddEventViewModel.getAddress()!=null){
            address.setText(AddEventViewModel.getAddress());
        }
        if(AddEventViewModel.getPrice()!=-1){
            date.setText(AddEventViewModel.getDate());
            free.setChecked(false);
        }
        else if (AddEventViewModel.getPrice()==0){
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
