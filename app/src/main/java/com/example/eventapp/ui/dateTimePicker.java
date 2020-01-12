package com.example.eventapp.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.eventapp.R;

import java.util.Calendar;
public class dateTimePicker {
    int mHour,mMinute,mDay,mMonth,mYear;

    public void callTimePicker(final Button button, View v) {
        // получаем текущее время
        final Calendar cal = Calendar.getInstance();
         mHour = cal.get(Calendar.HOUR_OF_DAY);
         mMinute = cal.get(Calendar.MINUTE);

        // инициализируем диалог выбора времени текущими значениями
        TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), R.style.MyDatePickerDialogTheme,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String editTextTimeParam = hourOfDay + " : " + minute;
                        button.setText(editTextTimeParam);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void callDatePicker(final Button button, View v) {
        // получаем текущую дату
        final Calendar cal = Calendar.getInstance();
         mYear = cal.get(Calendar.YEAR);
         mMonth = cal.get(Calendar.MONTH);
         mDay = cal.get(Calendar.DAY_OF_MONTH);

        // инициализируем диалог выбора даты текущими значениями
        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),R.style.MyDatePickerDialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String editTextDateParam = dayOfMonth + "." + (monthOfYear + 1) + "." + year;
                        button.setText(editTextDateParam);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

}
