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

public class FrontViewPagerAdapter extends RecyclerView.Adapter<FrontViewPagerAdapter.ViewHolder> {

    private final String TAG = "PagerAdapter";
    private List<ShopEntity> list;

    @Nullable
    private OnBuyButtonClickListener onBuyButtonClickListener;

    public FrontViewPagerAdapter(List<ShopEntity> list) {
        this.list = new LinkedList<>();
        if (list != null) {
            this.list = list;
        }
    }

    @NonNull
    @Override
    public FrontViewPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.view_front_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FrontViewPagerAdapter.ViewHolder holder, int position) {
        ShopEntity product = list.get(position);

        holder.textName.setText(product.name);
        holder.textPrice.setText("$" + product.price);
        holder.textQuantity.setText(product.quantity + " in stock");

        holder.productImage.setImageResource(Constants.productImages[product.image]);
        holder.buyButton.setTag(product);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public int updatePosition(ShopEntity product) {
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) == product) {
                if (product.quantity > 0) {
                    list.set(i, product);
                    return -1;
                } else {
                    list.remove(i);
                    return Math.min(list.size()-1, i);
                }
            }
        }
        return -1;
    }

    public void setOnBuyButtonClickListener(
            @Nullable OnBuyButtonClickListener onBuyButtonClickListener
    ) {
        this.onBuyButtonClickListener = onBuyButtonClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textName;
        private final TextView textPrice;
        private final TextView textQuantity;
        private final ImageView productImage;
        private final Button buyButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.front_text_name);
            textPrice = itemView.findViewById(R.id.front_text_price);
            textQuantity = itemView.findViewById(R.id.front_text_quantity);
            productImage = itemView.findViewById(R.id.front_image);
            buyButton = itemView.findViewById(R.id.front_buy_button);

            buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShopEntity product = (ShopEntity) v.getTag();
                    if (onBuyButtonClickListener != null) {
                        onBuyButtonClickListener.onClick(product);
                    }
                }
            });
        }
    }

    public interface OnBuyButtonClickListener {
        void onClick(ShopEntity product);
    }
}