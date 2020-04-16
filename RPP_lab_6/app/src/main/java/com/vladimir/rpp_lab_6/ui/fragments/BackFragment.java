package com.vladimir.rpp_lab_6.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vladimir.rpp_lab_6.Constants;
import com.vladimir.rpp_lab_6.R;
import com.vladimir.rpp_lab_6.adapters.RecyclerItemTouchHelperCallback;
import com.vladimir.rpp_lab_6.Repository;
import com.vladimir.rpp_lab_6.adapters.BackRecyclerAdapter;
import com.vladimir.rpp_lab_6.database.ShopEntity;

public class BackFragment extends Fragment {

    private final String TAG = "BackFragment";

    private SetProductDialogFragment setProductDialog;
    private BackRecyclerAdapter backRecyclerAdapter;
    private RecyclerView recyclerView = null;

    private TextView emptyText;

    public BackFragment() {
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_back, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setProductDialog = new SetProductDialogFragment();

        emptyText = getView().findViewById(R.id.back_empty_text);
        recyclerView = getView().findViewById(R.id.back_recycler_view);
        initRecyclerView();

        FloatingActionButton addFAB = view.findViewById(R.id.back_add_fab);
        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!setProductDialog.isAdded()) {
                    showDialog(null, Constants.DIALOG_TITLE_ADD);
                }
            }
        });
    }

    private void showDialog(ShopEntity product, String title) {
        setProductDialog.setProduct(product);
        setProductDialog.setParent(getThisFragment());
        setProductDialog.show(getFragmentManager(), title);
    }

    private void setEmptyTextVisibility() {
        if (backRecyclerAdapter.getItemCount() > 0) {
            emptyText.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.VISIBLE);
        }
    }

    private void initRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        final RecyclerItemTouchHelperCallback touchHelperCallback =
                new RecyclerItemTouchHelperCallback(0, ItemTouchHelper.RIGHT);
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelperCallback);

        new Thread(new Runnable() {
            @Override
            public void run() {
                backRecyclerAdapter = new BackRecyclerAdapter(
                        Repository.getInstance().getAllProducts()
                );
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setLayoutManager(layoutManager);
                            backRecyclerAdapter.setOnEditButtonClickListener(
                                    onEditButtonClickListener
                            );
                            recyclerView.setAdapter(backRecyclerAdapter);
                            setEmptyTextVisibility();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                getContext(),
                layoutManager.getOrientation()
        );
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.addItemDecoration(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        touchHelperCallback.setOnSwipeListener(onSwipeListener);
    }

    public void updateAdapter(ShopEntity product, int state) {
        switch (state) {
            case Constants.STATE_UPDATED:
                backRecyclerAdapter.notifyItemChanged(
                        backRecyclerAdapter.updatePosition(product)
                );
                break;
            case Constants.STATE_INSERTED:
                backRecyclerAdapter.notifyItemInserted(
                        backRecyclerAdapter.insertNewProduct(product)
                );
                break;
            case Constants.STATE_DELETED:
                backRecyclerAdapter.notifyItemRemoved(
                        backRecyclerAdapter.deleteProduct(product)
                );
                break;
            default: break;
        }
        setEmptyTextVisibility();
    }

    private Fragment getThisFragment() {
        return this;
    }

    private final BackRecyclerAdapter.OnEditButtonClickListener onEditButtonClickListener =
            new BackRecyclerAdapter.OnEditButtonClickListener() {
                @Override
                public void onClick(ShopEntity product, int position) {
                    if (!setProductDialog.isAdded()) {
                        showDialog(product, Constants.DIALOG_TITLE_EDIT);
                    }
                }
            };

    private final RecyclerItemTouchHelperCallback.OnSwipeListener onSwipeListener =
            new RecyclerItemTouchHelperCallback.OnSwipeListener() {
                @Override
                public void onSwipe(ShopEntity product) {
                    Repository.getInstance().deleteProduct(product, getThisFragment());
                }
            };

    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}
