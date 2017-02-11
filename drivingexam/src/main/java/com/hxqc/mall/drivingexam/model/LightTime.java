package com.hxqc.mall.drivingexam.model;

import java.util.List;

/**
 * Created by zhaofan on 2016/8/25.
 */
public class LightTime {
    public List<Integer> time;

    public List<Integer> getTime() {
        return time;
    }

    public void setTime(List<Integer> time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "LightTime{" +
                "time=" + time +
                '}';
    }
}
