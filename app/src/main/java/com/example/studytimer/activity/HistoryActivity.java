package com.example.studytimer.activity;

import android.os.Bundle;

import com.example.studytimer.R;
import com.example.studytimer.adapter.ArrayAdapter;
import com.example.studytimer.dboperation.DBHelper;
import com.example.studytimer.dboperation.dbobjects.Times;

import java.sql.SQLException;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryActivity  extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Initializing list view with the custom adapter
        ArrayList<Times> itemList = new ArrayList<Times>();

        ArrayAdapter itemArrayAdapter = new ArrayAdapter(R.layout.list_item, itemList);
        recyclerView = (RecyclerView) findViewById(R.id.item_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemArrayAdapter);


        DBHelper dbHelper = new DBHelper(this);
        try {
            for(Times t : dbHelper.getAllTimes()){
                itemList.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
