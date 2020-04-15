package com.vladimir.rpp_lab_6.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vladimir.rpp_lab_6.Constants;
import com.vladimir.rpp_lab_6.R;
import com.vladimir.rpp_lab_6.database.ShopEntity;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class BackRecyclerAdapter
        extends RecyclerView.Adapter<BackRecyclerAdapter.ViewHolder> {

    private final String TAG = "RecyclerAdapter";
    private List<ShopEntity> list = null;

    @Nullable
    private BackRecyclerAdapter.OnEditButtonClickListener onEditButtonClickListener;

    public BackRecyclerAdapter(List<ShopEntity> list) {
        this.list = new LinkedList<>();
        if (list != null) {
            this.list = list;
        }
    }

    @NonNull
    @Override
    public BackRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.view_back_item, parent, false);

        return new BackRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BackRecyclerAdapter.ViewHolder holder, int position) {
        ShopEntity product = list.get(position);

        holder.textName.setText(product.name);
        holder.textPrice.setText("$" + product.price);
        holder.textQuantity.setText("(" + product.quantity + " in stock)");

        holder.productImage.setImageResource(Constants.productImages[product.image]);

        holder.editButton.setTag(product);
        holder.productImage.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public int updatePosition(ShopEntity updatedProduct) {
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) == updatedProduct) {
                list.set(i, updatedProduct);
                return i;
            }
        }
        return 0;
    }

    public int insertNewProduct(ShopEntity product) {
        list.add(product);
        return list.size()-1;
    }

    public int deleteProduct(ShopEntity product) {
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) == product) {
                list.remove(i);
                return i;
            }
        }
        return 0;
    }

    public void setOnEditButtonClickListener(
            @Nullable BackRecyclerAdapter.OnEditButtonClickListener onEditButtonClickListener
    ) {
        this.onEditButtonClickListener = onEditButtonClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textName;
        private final TextView textPrice;
        private final TextView textQuantity;
        private final ImageView productImage;
        private final Button editButton;

        public ShopEntity getProduct() {
            return (ShopEntity) editButton.getTag();
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.back_text_name);
            textPrice = itemView.findViewById(R.id.back_text_price);
            textQuantity = itemView.findViewById(R.id.back_text_quantity);
            productImage = itemView.findViewById(R.id.back_image);
            editButton = itemView.findViewById(R.id.back_edit_button);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShopEntity product = (ShopEntity) v.getTag();
                    int position = (int) productImage.getTag();

                    if (onEditButtonClickListener != null) {
                        onEditButtonClickListener.onClick(product, position);
                    }
                }
            });
        }
    }

    public interface OnEditButtonClickListener {
        void onClick(ShopEntity product, int position);
    }

}
