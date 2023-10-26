package com.example.donutchat.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donutchat.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyGroupListAdapter extends RecyclerView.Adapter<MyGroupListAdapter.ViewHolder>
{
    GroupListActivity context;
    public MyGroupListAdapter(GroupListActivity context) {
        this.context = context;
    }

    ArrayList<String> groups = new ArrayList<>();


    @NonNull
    @NotNull
    @Override
    public MyGroupListAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grouplist_recyclerview_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.getTextView().setText(groups.get(position));
        holder.itemView.setOnClickListener(view -> {


          context.onItemclick(holder.getTextView().getText().toString());

        });


    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public void setArray(ArrayList<String> aBoolean) {
        groups= aBoolean;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.textView2);
        }

        public TextView getTextView() {
            return textView;
        }
    }
}
