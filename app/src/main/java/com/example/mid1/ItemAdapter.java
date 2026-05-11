package com.example.mid1;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends ListAdapter<items, ItemAdapter.ItemViewHolder> {
    Context context;
    ArrayList<items> items;

    public ItemAdapter(Context context, ArrayList<items> items) {
        super(new ItemDiffCallback());
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.activity_single_item_design, null);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        items item = getItem(position);
        holder.iv_fav.setImageResource(item.getImage());
        holder.tv_name.setText(item.getName());
        holder.tv_price.setText(item.getPrice());
        holder.tv_desc.setText(item.getDescription());

        if(item.isFav()){
            holder.iv_fav.setImageResource(R.drawable.filled_fav);
        }
        else {
            holder.iv_fav.setImageResource(R.drawable.favorite);
        }

        holder.iv_fav.setOnClickListener((v)->{
            item.setFav(!item.isFav());
            notifyItemChanged(position);

            if (MyApplication.favItemAdapter != null && MyApplication.favList != null) {
                MyApplication.favList.clear();
                for (items it : items) {
                    if (it.isFav()) {
                        MyApplication.favList.add(it);
                    }
                }
                MyApplication.favItemAdapter.notifyDataSetChanged();
            }
        });

        holder.iv_img.setOnClickListener((v) -> {
            Intent intent = new Intent(context, detail_card.class);
            intent.putExtra("image", item.getImage());
            intent.putExtra("name", item.getName());
            intent.putExtra("price", item.getPrice());
            intent.putExtra("desc", item.getDescription());
            intent.putExtra("detail", item.getDetails());
            context.startActivity(intent);
        });
    }

    public void updateItems(ArrayList<items> newItems) {
        this.items = newItems;
        submitList(new ArrayList<>(newItems));
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_fav, iv_img;
        TextView tv_name,tv_price,tv_desc;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_fav = itemView.findViewById(R.id.fav);
            iv_img = itemView.findViewById(R.id.img_card);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_desc = itemView.findViewById(R.id.tv_desc);
        }
    }

    private static class ItemDiffCallback extends DiffUtil.ItemCallback<items> {
        @Override
        public boolean areItemsTheSame(@NonNull items oldItem, @NonNull items newItem) {
            return oldItem.getName().equals(newItem.getName()) && 
                   oldItem.getPrice().equals(newItem.getPrice());
        }

        @Override
        public boolean areContentsTheSame(@NonNull items oldItem, @NonNull items newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                   oldItem.getPrice().equals(newItem.getPrice()) &&
                   oldItem.getDescription().equals(newItem.getDescription()) &&
                   oldItem.isFav() == newItem.isFav();
        }
    }
}