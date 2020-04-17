package com.vladimir.rpp_lab_7;

public class Constants {

    public static final int STATE_INSERTED = 0;
    public static final int STATE_DELETED = 1;
    public static final int STATE_UPDATED = 2;

    public static final String DIALOG_TITLE_EDIT = "Edit product";
    public static final String DIALOG_TITLE_ADD = "Add product";

    public static final String TOAST_IS_TRANSACTION = "Transaction has not yet been completed";

    public static final String[] productTypes = new String[] {
            "Monitor",
            "Laptop",
            "Smartphone",
            "Headset",
            "Keyboard",
            "Mouse",
            "Other"
    };

    public static final int[] productImages = new int[] {
            R.drawable.ic_image_monitor,
            R.drawable.ic_image_laptop,
            R.drawable.ic_image_smartphone,
            R.drawable.ic_image_headset,
            R.drawable.ic_image_keyboard,
            R.drawable.ic_image_mouse,
            R.drawable.ic_image_other
    };

}
