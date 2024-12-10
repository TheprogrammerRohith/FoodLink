package com.example.food_application;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyViewHolder2> {

    Context context;
    ArrayList<Engaged> list;

    String UserID;
    @NonNull
    @Override
    public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.recycler_view2,parent,false);
        return new MyAdapter2.MyViewHolder2(v);
    }

    public MyAdapter2(Context context, ArrayList<Engaged> list,String userID) {
        this.context = context;
        this.list = list;
        UserID=userID;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder2 holder, int position) {
        Engaged obj=list.get(position);
        holder.c_name.setText(obj.getName());
        holder.c_phno.setText(obj.getContactNo());
        holder.c_address.setText(obj.getAddress());
        holder.donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> foodnames = obj.getFoodname();
                ArrayList<String> foodquantities = obj.getFoodquantity();

                // Create and display the dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View dialogView = LayoutInflater.from(context).inflate(R.layout.food_items_list, null);
                builder.setView(dialogView);

                LinearLayout foodListContainer = dialogView.findViewById(R.id.food_list_container);
                Button engageButton = dialogView.findViewById(R.id.engage_button);
                TextView dialog_title=dialogView.findViewById(R.id.dialog_title);
                dialog_title.setText("Donated food items");
                engageButton.setText("Donate");
                Button closeButton = dialogView.findViewById(R.id.close_button);

                // Populate food items in the dialog
                for (int i = 0; i < foodnames.size(); i++) {
                    String foodName = foodnames.get(i);
                    String quantity = foodquantities.get(i);

                    LinearLayout itemLayout = new LinearLayout(context);
                    itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                    itemLayout.setPadding(0, 10, 0, 10);

                    TextView foodNameTextView = new TextView(context);
                    foodNameTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                    foodNameTextView.setText(foodName);
                    foodNameTextView.setTextSize(16);

                    TextView quantityTextView = new TextView(context);
                    quantityTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                    quantityTextView.setText(quantity);
                    quantityTextView.setTextSize(16);

                    itemLayout.addView(foodNameTextView);
                    itemLayout.addView(quantityTextView);
                    foodListContainer.addView(itemLayout);
                }

                AlertDialog dialog = builder.create();
                dialog.show();
                // Set up the engage button in the dialog
                engageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        storeDetails(obj,holder);
                        dialog.dismiss();
                    }
                });

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void storeDetails(Engaged obj,MyViewHolder2 holder){
        DatabaseReference dRef=FirebaseDatabase.getInstance().getReference().child("d_pastlistings").child(UserID);
        String id2=dRef.push().getKey();
        String date= DateFormat.getDateInstance().format(new Date());
        DonorPL dpl=new DonorPL(obj.getName(),obj.getContactNo(),obj.getAddress(),date);
        dRef.child(id2).setValue(dpl);
        DatabaseReference DRef= FirebaseDatabase.getInstance().getReference().child("Engaged").child(UserID);
        String id=obj.getId();
        DRef.child(id).removeValue();
        holder.donate.setText("Engaged");
        holder.donate.setEnabled(false);
        MyAdapter3.removeItems();
        MyAdapter3.addItems();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder2 extends RecyclerView.ViewHolder{

        TextView c_name,c_phno,c_address;
        Button donate;

        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);
            c_name=itemView.findViewById(R.id.c_name);
            c_phno=itemView.findViewById(R.id.c_phno);
            c_address=itemView.findViewById(R.id.c_address);
            donate=itemView.findViewById(R.id.donate_btn);
        }
    }
}
