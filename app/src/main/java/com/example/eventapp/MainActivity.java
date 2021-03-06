package com.example.eventapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;

    private static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    static private FirebaseUser currentUser;
    static BottomNavigationView navView;
    private List<String> task;
    public static int recyclerPosition=0;
    private static Display display;
    public static boolean moderator_mode;
    public static int ref_color_ok;
    public static int ref_color_ban;
    private int in_register = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ref_color_ok = getResources().getColor(R.color.ok);
        ref_color_ban = getResources().getColor(R.color.banned);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.nav_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        initFirebase();
        //настраиваем экран
        display = getWindowManager().getDefaultDisplay();

        //инициализируем авторизацию приложения
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                 currentUser = mAuth.getCurrentUser();
                if(currentUser != null){
                        visibleMenu(true);
                        setUserInfo();
                    //Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).popBackStack();
                    //Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.navigation_home);

                }
                else if(in_register == 0){
                    in_register++;
                    visibleMenu(false);
                    //еси пльзоватеь не авторизован в приложении переводим на экран регистрации/авторизации
                    Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).popBackStack();
                    Navigation.findNavController(MainActivity.this,R.id.nav_host_fragment).navigate(R.id.auth);
                }
            }
        };
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() ==  MotionEvent.ACTION_DOWN) hideKeyboard();
        return super.dispatchTouchEvent(ev);
    }

    private void initFirebase(){
        //Соединяемся с сервером
        FirebaseApp.initializeApp(this);
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        //Создаем ссылку на базу данных для дальнейшего общения
        mDatabaseReference=mFirebaseDatabase.getReference();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    public static void visibleMenu(boolean type){
        if(type){
            navView.setVisibility(View.VISIBLE);
        }
        else{
            navView.setVisibility(View.INVISIBLE);
        }
    }

    private void setUserInfo(){
        mDatabaseReference.child("user").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                User.setName(user.name);
                User.setId(user.id);
                User.setMail(user.mail);
                User.setCity(user.city);
                User.setPhoto(user.photo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public static FirebaseUser getCurrentUser(){
        return currentUser;
    }


    public static Point getDisplay(){
        Point size = new Point();
        display.getSize(size);
        return size;
    }

/*    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(Navigation.findNavController(this,R.id.nav_host_fragment).getCurrentDestination().getId());
        fragment.onActivityResult(requestCode,resultCode,data);
    }

 */
}
