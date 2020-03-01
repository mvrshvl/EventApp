package com.example.eventapp.ui.Profile.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eventapp.R;

public class addEventFirst extends Fragment {

AddEventViewModel AddEventViewModel;

    public static addEventFirst newInstance() {
        return new addEventFirst();
    }

    Button exhibition;
    Button concert;
    Button theatre ;
    Button party;
    Button master_class;
    Button kids;
    Button active;
    Button fair;
    Button excursion;
    Button cinema ;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.add_event_first_step, container, false);
        exhibition = (Button) root.findViewById(R.id.exhibition);

        concert = (Button) root.findViewById(R.id.concert);
         theatre = (Button) root.findViewById(R.id.theatre);
         party = (Button) root.findViewById(R.id.party);
         master_class = (Button) root.findViewById(R.id.master_class);
         kids = (Button) root.findViewById(R.id.kids);
         active = (Button) root.findViewById(R.id.active);
         fair = (Button) root.findViewById(R.id.fair);
         excursion = (Button) root.findViewById(R.id.excursion);
         cinema = (Button) root.findViewById(R.id.cinema);
        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.concert :
                        AddEventViewModel.setType(concert.getText().toString());
                        break;
                    case R.id.theatre :
                        AddEventViewModel.setType(theatre.getText().toString());
                        break;
                    case R.id.party :
                        AddEventViewModel.setType(party.getText().toString());
                        break;
                    case R.id.master_class :
                        AddEventViewModel.setType(master_class.getText().toString());
                        break;
                    case R.id.kids :
                        AddEventViewModel.setType(kids.getText().toString());
                        break;
                    case R.id.active :
                        AddEventViewModel.setType(active.getText().toString());
                        break;
                    case R.id.fair :
                        AddEventViewModel.setType(fair.getText().toString());
                        break;
                    case R.id.excursion :
                        AddEventViewModel.setType(excursion.getText().toString());
                        break;
                    case R.id.exhibition :
                        AddEventViewModel.setType(exhibition.getText().toString());
                        break;
                    case R.id.cinema :
                        AddEventViewModel.setType(cinema.getText().toString());
                        break;

                }

                addEvent.next(1);
            }

        };
         exhibition.setOnClickListener(onClickListener1);
        concert.setOnClickListener(onClickListener1);
         theatre.setOnClickListener(onClickListener1);
        party.setOnClickListener(onClickListener1);
        master_class.setOnClickListener(onClickListener1);
        kids.setOnClickListener(onClickListener1);
        active.setOnClickListener(onClickListener1);
        fair.setOnClickListener(onClickListener1);
        excursion.setOnClickListener(onClickListener1);
        cinema.setOnClickListener(onClickListener1);


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}
