package com.vladimir.rpp_lab_6.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.vladimir.rpp_lab_6.R;
import com.vladimir.rpp_lab_6.Repository;
import com.vladimir.rpp_lab_6.adapters.FrontViewPagerAdapter;
import com.vladimir.rpp_lab_6.database.ShopEntity;

public class FrontFragment extends Fragment {

    private FrontViewPagerAdapter viewPagerAdapter;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private TextView emptyText;

    public FrontFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_front, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = getView().findViewById(R.id.front_view_pager);
        tabLayout = getView().findViewById(R.id.front_tab_layout);
        emptyText = getView().findViewById(R.id.front_empty_text);

        new Thread(new Runnable() {
            @Override
            public void run() {
                viewPagerAdapter = new FrontViewPagerAdapter(
                        Repository.getInstance().getAllProductsInStock()
                );
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setAdapter(viewPagerAdapter);
                            viewPagerAdapter.notifyDataSetChanged();
                            viewPagerAdapter.setOnBuyButtonClickListener(onBuyButtonClickListener);
                            new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
                                @Override
                                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                                    tab.setText(Integer.toString(position+1));
                                }
                            }).attach();
                            setEmptyTextVisibility();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Fragment getThisFragment() {
        return this;
    }

    private void setEmptyTextVisibility() {
        if (viewPagerAdapter.getItemCount() > 0) {
            emptyText.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.VISIBLE);
        }
    }

    public void updateAdapter(ShopEntity product) {
        int deletedPosition = viewPagerAdapter.updatePosition(product);
        viewPagerAdapter.notifyDataSetChanged();
        if (deletedPosition >= 0) {
            viewPager.setCurrentItem(deletedPosition, false);
        }
        setEmptyTextVisibility();
    }

    private final FrontViewPagerAdapter.OnBuyButtonClickListener onBuyButtonClickListener =
            new FrontViewPagerAdapter.OnBuyButtonClickListener() {
                @Override
                public void onClick(ShopEntity product) {
                    Repository.getInstance().buyProduct(product, getThisFragment());
                }
            };
}
