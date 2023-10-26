package com.example.donutchat.ui;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donutchat.R;

import java.util.ArrayList;

public class MyGroupAdapter extends RecyclerView.Adapter<MyGroupAdapter.MyViewHolder> {
    private final MyView myView;
    ArrayList<String> messages;

    LinearLayout messageLinearLayout;

    boolean previousMessage;
    boolean currentMessage=false;


    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
        this.notifyDataSetChanged();
    }

    public ArrayList<String> getMessages() {
        return messages;
    }


    public MyGroupAdapter(MyView groupActivity) {
        this.myView = groupActivity;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_recyclerview_item, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String currentUser = myView.getCurrentuser();
        messageLinearLayout = holder.itemView.findViewById(R.id.messageLinearLayout);
        if (changeMessageLinearLayoutGravity(position, holder)) {
            holder.textView.setText(messages.get(position).substring(currentUser.length() + 2));
        } else {
            holder.textView.setText(messages.get(position));
        }


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_message);
        }
    }


    public boolean checkIfItIsCurrentuserMessage(int position) {
        String currentUser = myView.getCurrentuser();
        if (messages.get(position).substring(0, currentUser.length()).equals(currentUser)) {


            return true;
        } else {

            return false;
        }

    }

    public boolean changeMessageLinearLayoutGravity(int position, MyViewHolder holder) {

        boolean isChangedToRight;
        previousMessage = currentMessage;
        ///*** Automatic scroll up when onBinding of a viewHolder
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) (holder.textView.getLayoutParams());


        if (checkIfItIsCurrentuserMessage(position)) {

            messageLinearLayout.setGravity(Gravity.RIGHT);
            holder.textView.setBackgroundResource(R.drawable.message_bg);
            currentMessage = true;
            isChangedToRight = true;

        } else {

            messageLinearLayout.setGravity(Gravity.LEFT);
            holder.textView.setBackgroundResource(R.drawable.not_currentuser_message_bg);

            isChangedToRight = false;
            currentMessage = false;

        }
/*
        if (currentMessage != previousMessage) {
            params.setMargins(12, 100, 12, 0); //substitute parameters for left, top, right, bottom

            holder.textView.setLayoutParams(params);
        } else {
            params.setMargins(12, 0, 12, 0); //substitute parameters for left, top, right, bottom

            holder.textView.setLayoutParams(params);
        }
        Log.i("messy", "previousMessage" + previousMessage);
        Log.i("messy", "currentMessage" + currentMessage);



 */
        return isChangedToRight;
    }


}
