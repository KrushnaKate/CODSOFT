package com.hyper.quoteoftheday;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class myViewHolder extends RecyclerView.ViewHolder {
    public TextView tvQuote;


    public myViewHolder(@NonNull View itemView) {
        super(itemView);
        tvQuote = itemView.findViewById(R.id.tvQuoteList);
    }
}
