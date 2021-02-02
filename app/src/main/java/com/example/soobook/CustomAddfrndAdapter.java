package com.example.soobook;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomAddfrndAdapter extends RecyclerView.Adapter<CustomAddfrndAdapter.CustomViewHolder> {

    List<User> items;
    ArrayList<User> arrayList;
    Context context;

    public CustomAddfrndAdapter(Context context, List<User> items) {
        this.context = context;
        this.items = items;
        arrayList = new ArrayList<>();
        arrayList.addAll(items);
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user, parent, false);
        return new CustomViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        final User item = items.get(position);
        holder.tv_email.setText(arrayList.get(position).getEmail());
        holder.tv_uid.setText(arrayList.get(position).getUid());
    }
    @Override
    public int getItemCount() {
        return (this.items != null ? this.items.size() : 0);
    }
 public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        items.clear();
        if (charText.length() == 0) {
            items.addAll(arrayList);
        } else {
            for (User user : arrayList) {
                String name = user.getEmail();
                if (name.toLowerCase().contains(charText)) {
                    items.add(user);
                }
            }
        }
        notifyDataSetChanged();
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView tv_email;
        TextView tv_uid;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_email = itemView.findViewById(R.id.tv_email);
            this.tv_uid = itemView.findViewById(R.id.tv_uid);
        }
    }
}
