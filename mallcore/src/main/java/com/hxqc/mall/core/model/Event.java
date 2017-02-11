package com.hxqc.mall.core.model;

/**
 * Created by zf
 * eventbus
 */
public class Event {
    public Object obj;
    public String what;

    public Event(Object obj, String what) {
        this.obj = obj;
        this.what = what;
    }
}
