package com.example.drupp_driver.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drupp_driver.Models.NavigationItemModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.IAdapterItemClickListener;

import java.util.ArrayList;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.Holder> {

    private ArrayList<NavigationItemModel> mList;
    private Context context;
    private IAdapterItemClickListener iAdapterItemClickListener;

    public NavigationDrawerAdapter(ArrayList<NavigationItemModel> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.navigation_item, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NavigationDrawerAdapter.Holder holder, int i) {
        final NavigationItemModel list = mList.get(i);
        /*if (i == 0) {
            holder.parentView.setGravity(Gravity.CENTER);
            holder.text.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.ic_down_arrow_black), null);
            holder.text.setCompoundDrawablePadding(15);
            holder.image.setVisibility(View.GONE);

        }*/
        holder.text.setText(list.getTitle());
        if (list.getImgRefrence() != 0) {
            if (list.getId() == 4) {

            }
            holder.image.setImageResource(list.getImgRefrence());
        }


    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setiAdapterItemClickListener(IAdapterItemClickListener iAdapterItemClickListener) {
        this.iAdapterItemClickListener = iAdapterItemClickListener;
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView text;
        LinearLayout parentView;

        Holder(@NonNull final View itemView) {
            super(itemView);
            parentView = itemView.findViewById(R.id.parent_view);
            image = itemView.findViewById(R.id.menu_image);
            text = itemView.findViewById(R.id.menu_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (iAdapterItemClickListener == null) return;
            iAdapterItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

}
