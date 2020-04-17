package com.vladimir.rpp_lab_7.ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.vladimir.rpp_lab_7.Constants;
import com.vladimir.rpp_lab_7.R;
import com.vladimir.rpp_lab_7.Repository;
import com.vladimir.rpp_lab_7.database.ShopEntity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class SetProductDialogFragment extends DialogFragment {

    final String TAG = "myLogs";

    private ShopEntity product;
    private Fragment parent;

    private TextInputEditText nameEdit;
    private TextInputEditText priceEdit;
    private TextInputEditText quantityEdit;

    private TextView titleText;

    private Spinner imageSpinner;

    private ImageView productImage;

    private Button cancelButton;
    private Button okButton;


    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.dialog_set_product, null);
    }

    @Override
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        initFields(product);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameEdit = view.findViewById(R.id.dialog_name);
        priceEdit = view.findViewById(R.id.dialog_price);
        quantityEdit = view.findViewById(R.id.dialog_quantity);
        productImage = view.findViewById(R.id.dialog_product_image);
        imageSpinner = view.findViewById(R.id.dialog_image_spinner);
        cancelButton = view.findViewById(R.id.dialog_cancel_button);
        okButton = view.findViewById(R.id.dialog_ok_button);
        titleText = view.findViewById(R.id.dialog_title);
    }

    public void setProduct(ShopEntity product) {
        this.product = product;
    }

    void setParent(Fragment parent) {
        this.parent = parent;
    }

    public void initFields(ShopEntity product) {
        initSpinner(product);
        initEdits(product);
        setListeners(product);
        setTitle();
        setOkButtonEnabled();
    }

    private void setOkButtonEnabled() {
        if (nameEdit.getText().toString().equals("") ||
                priceEdit.getText().toString().equals("") ||
                quantityEdit.getText().toString().equals("")) {
            okButton.setEnabled(false);
        } else {
            okButton.setEnabled(true);
        }
    }

    private void setTitle() {
        if (getTag() == null) {
            titleText.setVisibility(View.GONE);
        } else {
            titleText.setText(getTag());
        }
    }

    private void initEdits(ShopEntity product) {
        if (product == null) {
            nameEdit.setText("");
            priceEdit.setText("");
            quantityEdit.setText("");
        } else {
            nameEdit.setText(product.name);
            priceEdit.setText(Integer.toString(product.price));
            quantityEdit.setText(Integer.toString(product.quantity));
        }
    }

    private void initSpinner(ShopEntity product) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                Constants.productTypes
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        imageSpinner.setAdapter(spinnerAdapter);
        imageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                productImage.setImageResource(Constants.productImages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        if (product == null) {
            imageSpinner.setSelection(0);
        } else {
            imageSpinner.setSelection(product.image);
        }
    }

    private void setListeners(final ShopEntity product) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                setOkButtonEnabled();
            }
        };

        nameEdit.addTextChangedListener(textWatcher);
        quantityEdit.addTextChangedListener(textWatcher);
        priceEdit.addTextChangedListener(textWatcher);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (product == null) {
                        Repository.getInstance().insertProduct(new ShopEntity(
                                        nameEdit.getText().toString(),
                                        Integer.parseInt(priceEdit.getText().toString()),
                                        Integer.parseInt(quantityEdit.getText().toString()),
                                        imageSpinner.getSelectedItemPosition()
                                ),
                                parent
                        );
                    } else {
                        product.name = nameEdit.getText().toString();
                        product.price = Integer.parseInt(priceEdit.getText().toString());
                        product.quantity = Integer.parseInt(quantityEdit.getText().toString());
                        product.image = imageSpinner.getSelectedItemPosition();
                        Repository.getInstance().updateProduct(product, getActivity());
                    }
                } catch (Exception e) {
                    ((BackFragment)parent).showToast("Error of adding/editing");
                }
                dismiss();
            }
        });
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void doNothing() {
        return;
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}
