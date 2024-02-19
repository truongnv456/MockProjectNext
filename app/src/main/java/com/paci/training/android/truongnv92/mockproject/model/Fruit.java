package com.paci.training.android.truongnv92.mockproject.model;

import java.io.Serializable;

public class Fruit implements Serializable {
    private final int id;
    private final String name;
    private final String detail;
    private final int src;
    private boolean isChecked;

    public Fruit(int id, String name, String detail, int src, boolean isChecked) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.src = src;
        this.isChecked = isChecked;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }

    public int getSrc() {
        return src;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
