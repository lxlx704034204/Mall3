package com.hxqc.carcompare.model.comparebasic;

import java.util.List;

/**
 * 基本参数对比
 * Created by zhaofan on 2016/10/8.
 */
public class CompareGroupParm {
    public String groupLabel;

    public List<ChildParm> chileParameter;


    public CompareGroupParm(String groupLabel, List<ChildParm> chileParameter) {
        this.groupLabel = groupLabel;
        this.chileParameter = chileParameter;
    }


    public String getGroupLabel() {
        return groupLabel;
    }

    public void setGroupLabel(String groupLabel) {
        this.groupLabel = groupLabel;
    }

    public List<ChildParm> getChileParameter() {
        return chileParameter;
    }

    public void setChileParameter(List<ChildParm> chileParameter) {
        this.chileParameter = chileParameter;
    }
}
