package com.example.elasz.myfirstaidkit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Alarms extends AppCompatActivity {

    @BindView(R.id.et_hour_alarm)
    TextView et_hour;

    @BindView(R.id.tv_startDate_alarm)
    TextView startdate;

    @BindView(R.id.tv_endDate_alarm)
    TextView enddate;

    @BindView(R.id.btn_addAlarm)
    Button addAlarm;

    @BindView(R.id.et_title_alarm)
    EditText titleAlarm;

    @BindView(R.id.et_description_alarm)
    EditText descriptionAlarm;

    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;

    private static final String TAG = "Alarms";
   /* private TextView et_hour;
    private TextView startdate;
    private TextView enddate;*/
    String amPm;
    private DatePickerDialog.OnDateSetListener mDateSetListenerForEnd;
    private DatePickerDialog.OnDateSetListener mDateSetListenerForStart;

    private String title;
    private String description;
    private String start_date_text;
    private String end_date_text;
    private String hour;
    Date datetostart;
    Date datetoend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms);
        ButterKnife.bind(this);
        et_hour = (TextView) findViewById(R.id.et_hour_alarm);
        et_hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoseTime();
            }
        });
        startdate=(TextView) findViewById(R.id.tv_startDate_alarm);
        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartDate();
            }
        });
        enddate=(TextView) findViewById(R.id.tv_endDate_alarm);
        enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEndDate();
            }
        });
        addAlarm=(Button) findViewById(R.id.btn_addAlarm);
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAlarmToCalendar();
            }
        });



        mDateSetListenerForEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                enddate.setText(date);

            }
        };

        mDateSetListenerForStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                startdate.setText(date);

            }
        };

    }

    private void AddAlarmToCalendar() {
        GetFieldsValue();
        addReminderInCalendar();
    }

    private void GetFieldsValue() {
        title = titleAlarm.getText().toString();
        description = descriptionAlarm.getText().toString();
        end_date_text= enddate.getText().toString();
        start_date_text = startdate.getText().toString();
        hour=et_hour.getText().toString();
        String startconctedate=start_date_text + "," + hour;
        String endconctedate=end_date_text + "," + hour;
        try {
            datetostart= new SimpleDateFormat("dd/MM/yyyy,HH:mm").parse(startconctedate);
            datetoend= new SimpleDateFormat("dd/MM/yyyy,HH:mm").parse(endconctedate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //String concat=start_date_text+hour;
        //String eventDate = new SimpleDateFormat("dd/MM/yyyy").parse(start_date_text);
    }

    public void ChoseTime(){
        calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(Alarms.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        /*if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }*/
                et_hour.setText(String.format("%02d:%02d", hourOfDay, minutes) );
            }
        }, currentHour, currentMinute, true);

        timePickerDialog.show();
    }

    @OnClick(R.id.tv_startDate_alarm)
    public void setStartDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(
                Alarms.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListenerForStart,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @OnClick(R.id.tv_endDate_alarm)
    public void setEndDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(
                Alarms.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListenerForEnd,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void addReminderInCalendar() {
        Calendar cal = Calendar.getInstance();
        Uri EVENTS_URI = Uri.parse(getCalendarUriBase(true) + "events");
        ContentResolver cr = getContentResolver();
        TimeZone timeZone = TimeZone.getDefault();

        //** Inserting an event in calendar. *//*
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, description);
        values.put(CalendarContract.Events.ALL_DAY, 0);

        // event starts at 11 minutes from now
        values.put(CalendarContract.Events.DTSTART, datetostart.getTime() + 1 * 60 * 1000);
        // ends 60 minutes from now
        values.put(CalendarContract.Events.DTEND, datetoend.getTime() + 2 * 60 * 1000);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        values.put(CalendarContract.Events.RRULE,"FREQ=DAILY, COUNT=");
        values.put(CalendarContract.Events.HAS_ALARM, 1);
        Uri event = cr.insert(EVENTS_URI, values);

        // Display event id.
        Toast.makeText(getApplicationContext(), "Event added :: ID :: " + event.getLastPathSegment(), Toast.LENGTH_SHORT).show();

        //** Adding reminder for event added. *//*
        Uri REMINDERS_URI = Uri.parse(getCalendarUriBase(true) + "reminders");
        values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, Long.parseLong(event.getLastPathSegment()));
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        values.put(CalendarContract.Reminders.MINUTES, 10);
        cr.insert(REMINDERS_URI, values);
    }

    //** Returns Calendar Base URI, supports both new and old OS. *//*
    private String getCalendarUriBase(boolean eventUri) {
        Uri calendarURI = null;
        try {
            if (android.os.Build.VERSION.SDK_INT <= 7) {
                calendarURI = (eventUri) ? Uri.parse("content://calendar/") : Uri.parse("content://calendar/calendars");
            } else {
                calendarURI = (eventUri) ? Uri.parse("content://com.android.calendar/") : Uri
                        .parse("content://com.android.calendar/calendars");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendarURI.toString();
    }
}
