package com.zzia.rxjavademo.model;

/**
 * Created by fyc on 2017/10/17.
 */

public class UserModel {
    String name;
    String sex;

    public UserModel(String name, String sex) {
        this.name = name;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
