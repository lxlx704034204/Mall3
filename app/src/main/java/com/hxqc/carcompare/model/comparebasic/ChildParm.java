package com.hxqc.carcompare.model.comparebasic;

/**
 * 基本参数对比
 * Created by zhaofan on 2016/10/8.
 */
public class ChildParm {
    public String label;

    public String value;

    public int def;

    public ChildParm(String label, String value) {
        this.label = label;
        this.value = value;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
