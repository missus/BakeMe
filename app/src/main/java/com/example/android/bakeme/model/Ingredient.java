package com.example.android.bakeme.model;

import java.io.Serializable;

public class Ingredient implements Serializable {

    private final int mQuantity;
    private final String mMeasure;
    private final String mName;

    public Ingredient(int quantity, String measure, String name) {
        mQuantity = quantity;
        mMeasure = measure;
        mName = name;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public String getName() {
        return mName;
    }
}