package com.example.food_application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyAdapter6 extends RecyclerView.Adapter<MyAdapter6.MyViewHolder6> {

    Context context;
    ArrayList<Events> list;
    public MyAdapter6(Context context, ArrayList<Events> list){
        this.context=context;
        this.list=list;
    }
    @NonNull
    @Override
    public MyViewHolder6 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.view_event,parent,false);
        return new MyAdapter6.MyViewHolder6(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter6.MyViewHolder6 holder, int position) {
        Events event=list.get(position);
        holder.event_name.setText(event.getName());
        holder.event_location.setText(event.getLocation());
        holder.event_date.setText(event.getDate());
        holder.event_time.setText(event.getTime());
        holder.contact_no.setText(event.getContact_no());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder6 extends RecyclerView.ViewHolder{
        TextView event_name,event_location,event_date,event_time,contact_no;
        public MyViewHolder6(@NonNull View itemView) {
            super(itemView);
            event_name=itemView.findViewById(R.id.event_name);
            event_location=itemView.findViewById(R.id.event_location);
            event_date=itemView.findViewById(R.id.event_date);
            event_time=itemView.findViewById(R.id.event_time);
            contact_no=itemView.findViewById(R.id.event_contact);
        }
    }
}
