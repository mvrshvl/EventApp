package com.example.eventapp.ui.Profile.settings;

import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.eventapp.MainActivity;
import com.example.eventapp.Moderator;
import com.example.eventapp.R;
import com.example.eventapp.User;
import com.example.eventapp.Utils;

import java.util.UUID;

import static com.example.eventapp.MainActivity.getCurrentUser;
import static com.example.eventapp.MainActivity.mDatabaseReference;
import static com.example.eventapp.Utils.verify;

public class settings extends Fragment {

    private SettingsViewModel mViewModel;
    private Button button_name, button_password, button_verify,button_apply,button_add_moder;
    private String password, name, new_password1, new_password2;
    private Spinner city;
    private Switch switch_mode;
    private LinearLayout block_moderator;
    public static settings newInstance() {
        return new settings();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.settings_fragment, container, false);
        button_name = (Button) root.findViewById(R.id.button_name);
        button_password = (Button) root.findViewById(R.id.button_password);
        button_verify= (Button) root.findViewById(R.id.button_verify);
        city = (Spinner) root.findViewById(R.id.city_spinner);
        button_apply = root.findViewById(R.id.apply);
        switch_mode = (Switch)root.findViewById(R.id.moderator);
        switch_mode.setChecked(MainActivity.moderator_mode);
        button_add_moder = (Button)root.findViewById(R.id.add_moder);
        block_moderator = (LinearLayout) root.findViewById(R.id.block_moderator);
        //выключаем пункты по ролям
        if(!getCurrentUser().getUid().equals("ysQKb26rYiSFKq0I35Od8EUk10i2")){
            button_add_moder.setVisibility(View.INVISIBLE);
        }
        Utils.isModerator(block_moderator);
        city.setSelection(getIndexCity(SettingsViewModel.getCity()));
        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.button_name :
                        changeName();
                        break;
                    case R.id.button_password :
                        changePassword();
                        break;
                    case R.id.button_verify:
                        if (!MainActivity.getCurrentUser().isEmailVerified()) {
                            verify();
                            Toast.makeText(getContext(), "Ссылка для подтверждения отправлена на почтовый ящик указанный при регистрации", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getContext(),"Аккаунт уже подтвержден",Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.apply:
                        Navigation.findNavController(getActivity(),R.id.nav_host_fragment).popBackStack();
                        Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.nav_profile);
                        break;
                    case R.id.add_moder:
                        addModerator();
                        break;
                }
            }

        };
        button_add_moder.setOnClickListener(onClickListener1);
        button_name.setOnClickListener(onClickListener1);
        button_password.setOnClickListener(onClickListener1);
        button_verify.setOnClickListener(onClickListener1);
        button_apply.setOnClickListener(onClickListener1);
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String new_city = (String) city.getSelectedItem();
                if(!new_city.toLowerCase().equals(User.getCity().toLowerCase())){
                    mViewModel.setCity(new_city);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        switch_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.moderator_mode = isChecked;
            }
        });

        return root;
    }

    private void addModerator(){
        mViewModel.setDialogState(1);
        //Получаем вид с файла prompt.xml, который применим для диалогового окна:
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.dialog_add_moderator, null);

        //Создаем AlertDialog
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getContext(),R.style.dialogStyle);

        //Настраиваем prompt.xml для нашего AlertDialog:
        mDialogBuilder.setView(promptsView);

        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        final EditText et_name = (EditText) promptsView.findViewById(R.id.name_moder);
        final EditText et_city = (EditText) promptsView.findViewById(R.id.city_moder);
        //Настраиваем сообщение в диалоговом окне:
        mDialogBuilder

                .setCancelable(false)
                .setTitle(R.string.change_name)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //Вводим текст и отображаем в строке ввода на основном экране:
                                Moderator moderator = new Moderator(et_name.getText().toString(),et_city.getText().toString());
                                mDatabaseReference.child("moderator").child(UUID.randomUUID().toString()).setValue(moderator);
                            }
                        })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        //Создаем AlertDialog:
        AlertDialog alertDialog = mDialogBuilder.create();

        //и отображаем его:
        alertDialog.show();

        alertDialog.getWindow().setLayout(1000,600);
    }

    private void changeName(){

        mViewModel.setDialogState(1);
        //Получаем вид с файла prompt.xml, который применим для диалогового окна:
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.settings_dialog_name, null);

        //Создаем AlertDialog
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getContext(),R.style.dialogStyle);

        //Настраиваем prompt.xml для нашего AlertDialog:
        mDialogBuilder.setView(promptsView);

        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        final EditText et_name = (EditText) promptsView.findViewById(R.id.new_name);

        //Настраиваем сообщение в диалоговом окне:
        mDialogBuilder

                .setCancelable(false)
                .setTitle(R.string.change_name)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //Вводим текст и отображаем в строке ввода на основном экране:

                                mViewModel.setDialogState(0);
                                    mViewModel.sendChanges(
                                            et_name.getText().toString(),
                                            "",
                                            1);

                            }
                        })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                                mViewModel.setDialogState(0);
                            }
                        });

        //Создаем AlertDialog:
        AlertDialog alertDialog = mDialogBuilder.create();

        //и отображаем его:
        alertDialog.show();

        alertDialog.getWindow().setLayout(1000,500);
    }

    private void changePassword(){
        mViewModel.setDialogState(2);
        //Получаем вид с файла prompt.xml, который применим для диалогового окна:
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.settings_dialog_password, null);

        //Создаем AlertDialog
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getContext(),R.style.dialogStyle);

        //Настраиваем prompt.xml для нашего AlertDialog:
        mDialogBuilder.setView(promptsView);



        //Настраиваем сообщение в диалоговом окне:
        mDialogBuilder

                .setCancelable(false)
                .setTitle(R.string.change_password)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //Вводим текст и отображаем в строке ввода на основном экране:
                                    mViewModel.setDialogState(0);
                                    mViewModel.sendChanges(
                                            "",
                                            "",
                                            2);



                            }
                        })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                                mViewModel.setDialogState(0);
                            }
                        });
        //Создаем AlertDialog:
        AlertDialog alertDialog = mDialogBuilder.create();

        //и отображаем его:
        alertDialog.show();
        alertDialog.getWindow().setLayout(1000,540);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        if(mViewModel.getDialogState() == 1){
            changeName();
        }
        else if(mViewModel.getDialogState() == 2){
            changePassword();
        }


    }
    private int getIndexCity(String obj){
        String[] all = getResources().getStringArray(R.array.cities);
        for(int i = 0;i<all.length;i++){
            if(all[i].toLowerCase().equals(obj.toLowerCase())){
                return i;
            }

        }
        return 0;
    }

}
