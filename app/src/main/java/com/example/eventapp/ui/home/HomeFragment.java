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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.eventapp.MainActivity.getDisplay;
import static com.example.eventapp.MainActivity.mDatabaseReference;

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
        bt_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Получаем вид с файла prompt.xml, который применим для диалогового окна:
                LayoutInflater li = LayoutInflater.from(getContext());
                View promptsView = li.inflate(R.layout.filter_dialog, null);

                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getContext(),R.style.dialogStyle);

                //Настраиваем prompt.xml для нашего AlertDialog:
                mDialogBuilder.setView(promptsView);

                //Настраиваем отображение поля для ввода текста в открытом диалоге:
                //final EditText et_name = (EditText) promptsView.findViewById(R.id.new_name);

                //Настраиваем сообщение в диалоговом окне:
                mDialogBuilder

                        .setCancelable(false)
                        .setTitle(R.string.parameters)
                        .setPositiveButton(R.string.continue_but,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                    }
                                })
                        .setNegativeButton(R.string.drop,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                //Создаем AlertDialog:
                AlertDialog alertDialog = mDialogBuilder.create();

                //и отображаем его:
                alertDialog.show();
                Point size = getDisplay();
                int x = size.x,y = size.y;
                alertDialog.getWindow().setLayout(size.x,size.y*3/5);
                alertDialog.getWindow().setGravity(Gravity.BOTTOM);

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

}