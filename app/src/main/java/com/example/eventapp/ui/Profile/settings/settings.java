package com.example.eventapp.ui.Profile.settings;

import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eventapp.MainActivity;
import com.example.eventapp.R;

import static com.example.eventapp.Utils.verify;

public class settings extends Fragment {

    private SettingsViewModel mViewModel;
    private Button button_name, button_password, button_verify;
    private String password, name, new_password1, new_password2;

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

                }
            }

        };

        button_name.setOnClickListener(onClickListener1);
        button_password.setOnClickListener(onClickListener1);
        button_verify.setOnClickListener(onClickListener1);

        return root;
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

        alertDialog.getWindow().setLayout(1000,600);
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
        alertDialog.getWindow().setLayout(1000,630);
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

}
