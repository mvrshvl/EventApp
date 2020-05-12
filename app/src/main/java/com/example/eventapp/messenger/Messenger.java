package com.example.eventapp.messenger;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eventapp.MainActivity;
import com.example.eventapp.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static com.example.eventapp.MainActivity.mDatabaseReference;
import static com.example.eventapp.MainActivity.visibleMenu;

public class Messenger extends Fragment {

    private MessengerViewModel mViewModel;
    private EditText message_et;
    private ImageButton send_bt;
    private ImageView back_bt;
    private RecyclerView recyclerView;
    private List<Message> messages;
    TextView user_tv;
    DataAdapter adapter;
    public String user_id;
    View root;
    public static Messenger newInstance() {
        return new Messenger();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.messenger_fragment, container, false);

        message_et = (EditText)root.findViewById(R.id.msg_et);
        send_bt = (ImageButton) root.findViewById(R.id.send_bt);
        recyclerView = (RecyclerView)root.findViewById(R.id.list);
        user_tv = (TextView) root.findViewById(R.id.user_tv);
        user_tv.setText(getArguments().getString("name"));
        user_id = getArguments().getString("id");
        back_bt = (ImageView) root.findViewById(R.id.back_bt);
        MessengerViewModel.loadData(user_id);
        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.send_bt :
                        MessengerViewModel.send(message_et.getText().toString());
                        message_et.setText("");
                        break;
                    case R.id.user_tv :
                        Bundle data = new Bundle();
                        data.putString("id",user_id);
                        Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.other_profile,data);
                        break;
                    case R.id.back_bt:
                        Navigation.findNavController(getActivity(),R.id.nav_host_fragment).popBackStack();
                        break;
                }
            }

        };
            send_bt.setOnClickListener(onClickListener1);
            user_tv.setOnClickListener(onClickListener1);
            back_bt.setOnClickListener(onClickListener1);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MessengerViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.getData().observe(this, new Observer<List>() {
            @Override
            public void onChanged(@Nullable List events) {
                messages = events;
                if(!messages.isEmpty()){
                    displayChat();
                }
            }
        });
    }
    private void displayChat() {

        //ListView listMessages = (ListView)findViewById(R.id.listView);
        //adapter
        adapter = new DataAdapter(getActivity(), messages);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(messages.size()-1);
    }

}
