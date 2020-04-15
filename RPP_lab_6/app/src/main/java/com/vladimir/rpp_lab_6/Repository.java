package com.vladimir.rpp_lab_6;

import android.app.Application;
import android.util.Log;

import com.vladimir.rpp_lab_6.database.ShopDatabase;
import com.vladimir.rpp_lab_6.database.ShopEntity;
import com.vladimir.rpp_lab_6.ui.fragments.BackFragment;
import com.vladimir.rpp_lab_6.ui.fragments.FrontFragment;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

public class Repository extends Application {

    public static Repository instance;
    private final String TAG = "Repository";

    private ShopDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("hay", "hello");
        instance = this;
        database = Room.databaseBuilder(this, ShopDatabase.class, "shop_db")
                .build();
    }

    public static Repository getInstance() {
        return instance;
    }

    public ShopDatabase getDatabase() {
        return database;
    }

    public List<ShopEntity> getAllProducts() {
        Log.d("Repository", "#getAllProducts");
        return instance
                .database
                .shopDao()
                .getAll();
    }

    public List<ShopEntity> getAllProductsInStock() {
        Log.d("Repository", "#getAllProducts");
        return instance
                .database
                .shopDao()
                .getAllInStock();
    }

    public void insertProduct(final ShopEntity product, final Fragment fragment) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                instance
                        .database
                        .shopDao()
                        .insert(product);
                try {
                    fragment.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "#runOnUI");
                            BackFragment backFragment = (BackFragment) fragment;
                            backFragment.updateAdapter(product, Constants.STATE_INSERTED);
                            backFragment.showToast("product was created");
                        }
                    });
                } catch (Exception e) {
                    Log.wtf(TAG, "#runOnUI unknown error");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void buyProduct(final ShopEntity product, final Fragment fragment) {
        product.quantity--;
        if (product.quantity < 0) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                instance
                        .database
                        .shopDao()
                        .update(product);
                try {
                    fragment.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            FrontFragment ff = (FrontFragment) fragment;
                            ff.updateAdapter(product);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void updateProduct(
            final ShopEntity product,
            final Fragment fragment
    ) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                instance
                        .database
                        .shopDao()
                        .update(product);
                try {
                    fragment.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BackFragment backFragment = (BackFragment)fragment;
                            backFragment.updateAdapter(product, Constants.STATE_UPDATED);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void deleteProduct(
            final ShopEntity product,
            final Fragment fragment
    ) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                instance
                        .database
                        .shopDao()
                        .delete(product);
                try {
                    fragment.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BackFragment backFragment = (BackFragment)fragment;
                            backFragment.updateAdapter(product, Constants.STATE_DELETED);
                            backFragment.showToast("product was deleted");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
