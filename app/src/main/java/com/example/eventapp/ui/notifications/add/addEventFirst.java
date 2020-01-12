package com.example.eventapp.ui.notifications.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.eventapp.R;

public class addEventFirst extends Fragment {

AddEventViewModel AddEventViewModel;

    public static addEventFirst newInstance() {
        return new addEventFirst();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.add_event_first_step, container, false);
        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.concert :
                        AddEventViewModel.setType("Консерт");
                        break;
                    case R.id.theatre :
                        AddEventViewModel.setType("Театр");
                        break;
                    case R.id.party :
                        AddEventViewModel.setType("Вечеринка");
                        break;
                    case R.id.master_class :
                        AddEventViewModel.setType("Мастер класс");
                        break;
                    case R.id.kids :
                        AddEventViewModel.setType("Детям");
                        break;
                    case R.id.active :
                        AddEventViewModel.setType("Активный отдых");
                        break;
                    case R.id.fair :
                        AddEventViewModel.setType("Ярмарка");
                        break;
                    case R.id.excursion :
                        AddEventViewModel.setType("Экскурсия");
                        break;
                    case R.id.exhibition :
                        AddEventViewModel.setType("Выставка");
                        break;
                    case R.id.cinema :
                        AddEventViewModel.setType("Кино");
                        break;

                }

                addEvent.next(1);
            }

        };
        Button exhibition = (Button) root.findViewById(R.id.exhibition); exhibition.setOnClickListener(onClickListener1);
        Button concert = (Button) root.findViewById(R.id.concert); concert.setOnClickListener(onClickListener1);
        Button theatre = (Button) root.findViewById(R.id.theatre); theatre.setOnClickListener(onClickListener1);
        Button party = (Button) root.findViewById(R.id.party); party.setOnClickListener(onClickListener1);
        Button master_class = (Button) root.findViewById(R.id.master_class); master_class.setOnClickListener(onClickListener1);
        Button kids = (Button) root.findViewById(R.id.kids); kids.setOnClickListener(onClickListener1);
        Button active = (Button) root.findViewById(R.id.active); active.setOnClickListener(onClickListener1);
        Button fair = (Button) root.findViewById(R.id.fair); fair.setOnClickListener(onClickListener1);
        Button excursion = (Button) root.findViewById(R.id.excursion); excursion.setOnClickListener(onClickListener1);
        Button cinema = (Button) root.findViewById(R.id.cinema); cinema.setOnClickListener(onClickListener1);


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}
