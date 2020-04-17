package com.vladimir.rpp_lab_7;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.vladimir.rpp_lab_7.database.ShopDatabase;
import com.vladimir.rpp_lab_7.database.ShopEntity;
import com.vladimir.rpp_lab_7.ui.MainActivity;
import com.vladimir.rpp_lab_7.ui.fragments.BackFragment;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

public class Repository extends Application {

    public static Repository instance;
    private final String TAG = "Repository";

    private ShopDatabase database;

    private MutableLiveData<Boolean> loadingLiveData;
    private int loadingState;

    private HashSet<Integer> transactions;

    @Override
    public void onCreate() {
        super.onCreate();
        loadingState = 0;
        transactions = new HashSet<>();
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

    private void addTransaction(int id) {
        transactions.add(id);
    }

    private void removeTransaction(int id) {
        transactions.remove(id);
    }

    public boolean isTransaction(int id) {
        return transactions.contains(id);
    }

    public void buyProduct(
            ShopEntity product,
            final Activity activity
    ) {
        if (product.quantity <= 0) return;
        product.quantity--;

        final ShopEntity updatedProduct = product;

        new Thread(new Runnable() {
            @Override
            public void run() {
                addTransaction(updatedProduct.id);
                randomDelay();

                instance
                        .database
                        .shopDao()
                        .buyProductById(updatedProduct.id);

                removeTransaction(updatedProduct.id);
                Log.d(TAG, "inside of thread : " + updatedProduct.quantity);
                try {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((MainActivity)activity).reloadInterface(updatedProduct);
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
            final Activity activity
    ) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                addTransaction(product.id);
                randomDelay();

                instance
                        .database
                        .shopDao()
                        .update(product);

                removeTransaction(product.id);
                try {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((MainActivity)activity).reloadInterface(product);
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

    public LiveData<Boolean> getLoadingLiveData() {
        initLiveData();
        return loadingLiveData;
    }

    private void initLiveData() {
        if (loadingLiveData == null) {
            loadingLiveData = new MutableLiveData<>();
        }
    }

    private void setLoadingState(boolean isLoading) {
        if (isLoading) {
            loadingState++;
        } else {
            loadingState--;
        }
        initLiveData();
        loadingLiveData.postValue(loadingState > 0);
    }

    private void randomDelay() {
        setLoadingState(true);
        int delay = new Random().nextInt(2001) + 3000;
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setLoadingState(false);
    }

}
