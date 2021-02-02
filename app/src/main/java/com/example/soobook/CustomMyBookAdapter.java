package com.example.soobook;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomMyBookAdapter extends RecyclerView.Adapter<CustomMyBookAdapter.CustomViewHolder> {
    FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
    private ArrayList<Book> arrayList;
    private Context context;

    public CustomMyBookAdapter(ArrayList<Book> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_book, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;


    }



    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getRecImage())
                .into(holder.iv_rec);
        holder.tv_owner.setText(arrayList.get(position).getOwner());
        holder.tv_auth.setText(arrayList.get(position).getAuth());
        holder.tv_title.setText(arrayList.get(position).gettitle());

        holder.itemView.setOnClickListener(v ->  {
            String uid = arrayList.get(position).getUid();
            String title = arrayList.get(position).gettitle();
            String Isbn = arrayList.get(position).getIsbn();
            String Auth = arrayList.get(position).getAuth();
            String Rec = arrayList.get(position).getRec();
            String Pub = arrayList.get(position).getPub();
            String Star = arrayList.get(position).getStar();
            String Owner = arrayList.get(position).getOwner();
            String Time = arrayList.get(position).getTime();

            Intent intent = new Intent(context,MyBookDetailView.class);
            intent.putExtra("uid",uid);
            intent.putExtra("title", title);
            intent.putExtra("isbn",Isbn);
            intent.putExtra("auth", Auth);
            intent.putExtra("rec",Rec);
            intent.putExtra("pub", Pub);
            intent.putExtra("star",Star);
            intent.putExtra("owner",Owner.toString());
            intent.putExtra("time",Time);
            context.startActivity(intent);

        });


    }
    @Override
    public int getItemCount() {
        // 삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_rec;
        TextView tv_owner;
        TextView tv_auth;
        TextView tv_title;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_rec = itemView.findViewById(R.id.iv_rec);
            this.tv_owner = itemView.findViewById(R.id.tv_owner);
            this.tv_auth = itemView.findViewById(R.id.tv_auth);
            this.tv_title = itemView.findViewById(R.id.tv_title);

        }

    }
}
