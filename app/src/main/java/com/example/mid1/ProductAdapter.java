package com.example.mid1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView adapter for displaying seller's products
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> products;
    private OnProductClickListener onProductClickListener;
    private OnProductDeleteListener onProductDeleteListener;

    public ProductAdapter(Context context) {
        this.context = context;
        this.products = new ArrayList<>();
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public void setOnProductClickListener(OnProductClickListener listener) {
        this.onProductClickListener = listener;
    }

    public void setOnProductDeleteListener(OnProductDeleteListener listener) {
        this.onProductDeleteListener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        if (product != null) {
            holder.bind(product);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImage;
        private TextView tvProductName;
        private TextView tvProductPrice;
        private TextView tvProductCategory;
        private TextView tvProductStock;
        private View btnDelete;
        private View btnEdit;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_product_image);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvProductCategory = itemView.findViewById(R.id.tv_product_category);
            tvProductStock = itemView.findViewById(R.id.tv_product_stock);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnEdit = itemView.findViewById(R.id.btn_edit);
        }

        public void bind(Product product) {
            tvProductName.setText(product.getName());
            tvProductPrice.setText(String.format("$%.2f", product.getPrice()));
            tvProductCategory.setText(product.getCategory());
            tvProductStock.setText(String.format("Stock: %d", product.getStock()));

            // Load image using Glide
            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                Glide.with(context)
                        .load(product.getImageUrl())
                        .placeholder(R.drawable.ic_menu)
                        .error(R.drawable.ic_menu)
                        .into(ivProductImage);
            } else {
                ivProductImage.setImageResource(R.drawable.ic_menu);
            }

            // Item click listener
            itemView.setOnClickListener(v -> {
                if (onProductClickListener != null) {
                    onProductClickListener.onProductClick(product);
                }
            });

            // Delete button click listener
            btnDelete.setOnClickListener(v -> {
                if (onProductDeleteListener != null) {
                    onProductDeleteListener.onProductDelete(product);
                }
            });

            // Edit button click listener
            btnEdit.setOnClickListener(v -> {
                if (onProductClickListener != null) {
                    onProductClickListener.onProductEdit(product);
                }
            });
        }
    }

    public interface OnProductClickListener {
        void onProductClick(Product product);
        void onProductEdit(Product product);
    }

    public interface OnProductDeleteListener {
        void onProductDelete(Product product);
    }
}

