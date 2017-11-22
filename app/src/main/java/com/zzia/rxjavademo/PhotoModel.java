package com.zzia.rxjavademo;

import java.io.Serializable;

/**
 * Created by fyc on 2017/11/16.
 */

public class PhotoModel implements Serializable {

    private String photoId;
    private String photoName;
    private String photoPath;


    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
