package com.quoccuong.model;

/**
 * Created by sev_user on 7/24/2015.
 */

import java.io.Serializable;

/**
 * Created by sev_user on 7/22/2015.
 */
public class PointOfTime implements Serializable{
    private int acc_x;
    private int acc_y;
    private int acc_z;

    private int gyr_x;
    private int gyr_y;
    private int gyr_z;

    public PointOfTime(int acc_x, int acc_y, int gyr_y, int gyr_z, int acc_z, int gyr_x) {
        this.acc_x = acc_x;
        this.acc_y = acc_y;
        this.gyr_y = gyr_y;
        this.gyr_z = gyr_z;
        this.acc_z = acc_z;
        this.gyr_x = gyr_x;
    }

    public PointOfTime() {

    }

    public int getGyrX() {
        return gyr_x;
    }

    public void setGyrX(int gyr_x) {
        this.gyr_x = gyr_x;
    }

    public int getGyrY() {
        return gyr_y;
    }

    public void setGyrY(int gyr_y) {
        this.gyr_y = gyr_y;
    }

    public int getGyrZ() {
        return gyr_z;
    }

    public void setGyrZ(int gyr_z) {
        this.gyr_z = gyr_z;
    }

    public int getAccX() {
        return acc_x;
    }

    public void setAccX(int acc_x) {
        this.acc_x = acc_x;
    }

    public int getAccY() {
        return acc_y;
    }

    public void setAccY(int acc_y) {
        this.acc_y = acc_y;
    }

    public int getAccZ() {
        return acc_z;
    }

    public void setAccZ(int acc_z) {
        this.acc_z = acc_z;
    }
}

