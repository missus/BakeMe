package com.example.android.bakeme.model;

import java.io.Serializable;

public class Step implements Serializable {

    private final int mId;
    private final String mShortDescription;
    private final String mDescription;
    private final String mVideo;
    private final String mImage;

    public Step(int id, String shortDescription, String description, String video, String image) {
        mId = id;
        mShortDescription = shortDescription;
        mDescription = description;
        mVideo = video;
        mImage = image;
    }

    public int getId() {
        return mId;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getVideo() {
        return mVideo;
    }

    public String getImage() {
        return mImage;
    }
}