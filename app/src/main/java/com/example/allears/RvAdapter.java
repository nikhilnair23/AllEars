package com.example.allears;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.RviewHolder> {

    private ArrayList<Score> itemList;
    private ItemClickListener listener;


    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public static class RviewHolder extends RecyclerView.ViewHolder{
        public TextView itemUserName;
        public TextView itemScore;

        public RviewHolder(@NonNull final View itemView, final ItemClickListener listener){
            super(itemView);
            itemUserName = itemView.findViewById(R.id.item_username);
            itemScore = itemView.findViewById(R.id.item_score);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getLayoutPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public RvAdapter(ArrayList<Score> itemList) {
        this.itemList = itemList;
    }


    @NonNull
    @Override
    public RviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new RviewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RviewHolder holder, int position) {
        Score current_item = itemList.get(position);
        holder.itemUserName.setText(current_item.getUsername());
        holder.itemScore.setText(current_item.getScore());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
