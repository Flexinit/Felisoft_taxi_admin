package com.example.drupp_driver.ui.base;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class BaseRecyclerAdapter<V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    @NonNull
    @Override
    public V onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull V v, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
