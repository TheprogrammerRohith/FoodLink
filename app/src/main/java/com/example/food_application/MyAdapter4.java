package com.example.food_application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter4 extends RecyclerView.Adapter<MyAdapter4.MyViewHolder4> {

    Context context;
    ArrayList<DonorPL> list;
    public MyAdapter4(Context context,ArrayList<DonorPL> list){
        this.context=context;
        this.list=list;
    }
    @NonNull
    @Override
    public MyViewHolder4 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.dpl_recycler_view,parent,false);
        return new MyAdapter4.MyViewHolder4(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder4 holder, int position) {
        DonorPL obj=list.get(position);
        holder.c_name.setText(obj.getName());
        holder.c_phno.setText(obj.getContactNo());
        holder.c_address.setText(obj.getAddress());
        holder.date.setText(obj.getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder4 extends RecyclerView.ViewHolder{
        TextView c_name,c_phno,c_address,date;

        public MyViewHolder4(@NonNull View itemView) {
            super(itemView);
            c_name=itemView.findViewById(R.id.c_name);
            c_phno=itemView.findViewById(R.id.c_phno);
            c_address=itemView.findViewById(R.id.c_address);
            date=itemView.findViewById(R.id.date);
        }
    }
}
