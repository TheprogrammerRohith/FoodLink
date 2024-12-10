package com.example.food_application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter5 extends RecyclerView.Adapter<MyAdapter5.MyViewHolder5> {

    ArrayList<CollectorPL> list;
    Context context;
    @NonNull
    @Override
    public MyViewHolder5 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.cpl_recycler_view,parent,false);
        return new MyAdapter5.MyViewHolder5(v);
    }

    public MyAdapter5(Context context,ArrayList<CollectorPL>list){
        this.context=context;
        this.list=list;
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder5 holder, int position) {
        CollectorPL obj=list.get(position);
        holder.d_name.setText(obj.getName());
        holder.d_phno.setText(obj.getContactNo());
        holder.d_addr.setText(obj.getAddress());
        holder.date.setText(obj.getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder5 extends RecyclerView.ViewHolder{

        TextView d_name,d_phno,d_addr,date;

        public MyViewHolder5(@NonNull View itemView) {
            super(itemView);
            d_name=itemView.findViewById(R.id.d_name);
            d_phno=itemView.findViewById(R.id.d_phno);
            d_addr=itemView.findViewById(R.id.d_addr);
            date=itemView.findViewById(R.id.date);
        }
    }
}
