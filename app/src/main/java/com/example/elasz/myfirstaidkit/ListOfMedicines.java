package com.example.elasz.myfirstaidkit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class ListOfMedicines extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_medicines);

        ListView listView=(ListView) findViewById(R.id.listview_listOfMedicines);
        

    }
}
