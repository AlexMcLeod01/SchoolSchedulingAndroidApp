package com.example.c196;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * A simple adapter for the Recycler View
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private List<String> data;
    private LayoutInflater inflater;
    private ItemClickListener listener;
    private int layoutID;
    private int textID;
    private int lastPosition = -1;

    //Constructor
    RecyclerAdapter(Context context, List<String> data, int layoutID, int textID) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.layoutID = layoutID;
        this.textID = textID;
    }

    //Inflates rows as needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int type) {

        View view = inflater.inflate(layoutID, group, false);
        return new ViewHolder(view, textID);
    }

    //Puts text in rows
    @Override
    public void onBindViewHolder(ViewHolder holder, int pos) {
        int p = pos;
        String data = this.data.get(p);
        holder.text.setText(data);
        if (lastPosition == p) {
            holder.text.setBackgroundColor(ContextCompat.getColor(holder.text.getContext(), R.color.teal_200));
        } else {
            holder.text.setBackgroundColor(ContextCompat.getColor(holder.text.getContext(), R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    /**
     * Just a getter function
     * @param id of row
     * @return String item
     */
    String getItem(int id) {
        return this.data.get(id);
    }

    void setClickListener(ItemClickListener listener) {
        this.listener = listener;
    }


    /**
     * Implementation for abstract RecyclerView.ViewHolder
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text;

        ViewHolder(View item, int textID) {
            super(item);
            text = item.findViewById(textID);
            item.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                int prevLastPosition = lastPosition;
                lastPosition = getAbsoluteAdapterPosition();
                listener.onItemClick(view, lastPosition);
                notifyItemChanged(lastPosition);
                notifyItemChanged(prevLastPosition);
            }
        }


    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
