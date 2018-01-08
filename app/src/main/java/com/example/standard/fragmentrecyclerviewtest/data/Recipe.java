package com.example.standard.fragmentrecyclerviewtest.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vince on 26.12.2017.
 */

public class Recipe implements Parcelable {

    // Recipe Name
    private String mName;
    // Ingredients
    private String mIngredient, mQuantity, mMeasure;
    // Preparation Steps
    private String mShortDescription, mDescription, mVideoURL;

    public Recipe(String mName, String mIngredient, String mQuantity, String mMeasure,
                  String mShortDescription, String mDescription, String mVideoURL) {
        this.mName = mName;
        this.mIngredient = mIngredient;
        this.mQuantity = mQuantity;
        this.mMeasure = mMeasure;
        this.mShortDescription = mShortDescription;
        this.mDescription = mDescription;
        this.mVideoURL = mVideoURL;
    }

    public Recipe (Parcel parcel){
        this.mName = parcel.readString();
        this.mIngredient = parcel.readString();
        this.mQuantity = parcel.readString();
        this.mMeasure = parcel.readString();
        this.mShortDescription = parcel.readString();
        this.mDescription = parcel.readString();
        this.mVideoURL = parcel.readString();
    }

    public String getmName() {
        return mName;
    }

    public String getmIngredient() {
        return mIngredient;
    }

    public String getmQuantity() {
        return mQuantity;
    }

    public String getmMeasure() {
        return mMeasure;
    }

    public String getmShortDescription() {
        return mShortDescription;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmVideoURL() {
        return mVideoURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeString(mIngredient);
        parcel.writeString(mQuantity);
        parcel.writeString(mMeasure);
        parcel.writeString(mShortDescription);
        parcel.writeString(mDescription);
        parcel.writeString(mVideoURL);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
