package com.example.studytimer.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.studytimer.R;
import com.example.studytimer.dboperation.dbobjects.Times;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class ArrayAdapter extends RecyclerView.Adapter<ArrayAdapter.ViewHolder> {

    //All methods in this adapter are required for a bare minimum recyclerview adapter
    private int listItemLayout;
    private ArrayList<Times> itemList;
    // Constructor of the class
    public ArrayAdapter(int layoutId, ArrayList<Times> itemList) {
        listItemLayout = layoutId;
        this.itemList = itemList;
    }

    // get the size of the list
    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }


    // specify the row layout file and click for each row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(listItemLayout, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        TextView item = holder.item;
        StringBuilder sb = new StringBuilder();
        sb.append(itemList.get(listPosition).getStart().getDate()).append(".");
        sb.append(itemList.get(listPosition).getStart().getMonth()).append(".");
        sb.append(itemList.get(listPosition).getStart().getYear()+1900).append("       ");
        sb.append((itemList.get(listPosition).getStop().getTime()-itemList.get(listPosition).getStart().getTime())/60000).append(" min ");
        sb.append((itemList.get(listPosition).getStop().getTime()-itemList.get(listPosition).getStart().getTime())/1000 % 60).append(" s");
        item.setText(sb.toString());
    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView item;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            item = (TextView) itemView.findViewById(R.id.row_item);
        }
        @Override
        public void onClick(View view) {
            Log.d("onclick", "onClick " + getLayoutPosition() + " " + item.getText());
        }
    }
}