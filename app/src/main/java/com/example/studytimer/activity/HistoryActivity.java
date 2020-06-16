package com.example.studytimer.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.studytimer.R;
import com.example.studytimer.adapter.ArrayAdapter;
import com.example.studytimer.dboperation.DBHelper;
import com.example.studytimer.dboperation.TypeOfAction;
import com.example.studytimer.dboperation.dbobjects.Times;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryActivity  extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DBHelper dbHelper;
    private TextView timeSummedUp;
    private TextView timeToday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        timeSummedUp = (TextView)findViewById(R.id.timeSummedUp);
        timeToday = (TextView)findViewById(R.id.timeToday);

        ArrayList<Times> itemList = new ArrayList<Times>();

        ArrayAdapter itemArrayAdapter = new ArrayAdapter(R.layout.list_item, itemList);
        recyclerView = (RecyclerView) findViewById(R.id.item_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemArrayAdapter);


        long allWorkTime = 0;
        long WorkTimeToday = 0;
        Date today = new Date(System.currentTimeMillis());
        DBHelper dbHelper = new DBHelper(this);
        try {
            for(Times t : dbHelper.getAllTimes()){
                if(t.getTypeOfAction() == TypeOfAction.WORK){
                    itemList.add(t);
                    allWorkTime = allWorkTime + t.getStop().getTime()-t.getStart().getTime();
                    if(today.getYear() == t.getStart().getYear() && today.getMonth() == t.getStart().getMonth() && today.getDate() == t.getStart().getDate() ){
                        WorkTimeToday = WorkTimeToday + t.getStop().getTime()-t.getStart().getTime();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Łączny czas nauki to: ");
        sb.append(allWorkTime/3600000).append(" h ").append(allWorkTime/60000).append(" min ").append(allWorkTime/1000 % 60).append(" s");
        timeSummedUp.setText(sb.toString());

        sb = new StringBuilder();
        sb.append("Dzisiaj: \n");
        sb.append(WorkTimeToday/3600000).append(" h ").append(WorkTimeToday/60000).append(" min ").append(WorkTimeToday/1000 % 60).append(" s");

        timeToday.setText(sb.toString());
    }
}
