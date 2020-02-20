package com.example.eventapp.ui.Profile.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eventapp.R;

public class addEventSecond extends Fragment {

    private static EditText edit_name;
    private static EditText edit_about;
    private Button  next;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.add_event_second_step, container, false);

        ImageView back = (ImageView) root.findViewById(R.id.back_to_0);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent.next(0);

            }
        });


        edit_name = (EditText) root.findViewById(R.id.edit_name);
        edit_about = (EditText) root.findViewById(R.id.edit_about);

        next = (Button) root.findViewById(R.id.next_to_2);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent.next(2);
            }
        });
        return root;
    }

    public static void setData(){
        AddEventViewModel.setName(edit_name.getText().toString());
        AddEventViewModel.setAbout(edit_about.getText().toString());
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
        if(AddEventViewModel.getName()!=null){
            edit_name.setText(AddEventViewModel.getName());
        }
        if(AddEventViewModel.getAbout()!=null){
            edit_about.setText((AddEventViewModel.getAbout()));
        }
    }

}
