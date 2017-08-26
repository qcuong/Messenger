package com.quoccuong.model;

import android.util.Pair;

import com.quoccuong.model.PointOfTime;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sev_user on 7/29/2015.
 */
public class MyPair<K, V> implements Serializable {

    public K first;
    public V second;

    /**
     * Constructor for a Pair.
     *
     * @param first  the first object in the Pair
     * @param second the second object in the pair
     */
    public MyPair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    public K getFirst() {
        return first;
    }

    public void setFirst(K first) {
        this.first = first;
    }

    public V getSecond() {
        return second;
    }

    public void setSecond(V second) {
        this.second = second;
    }
}
