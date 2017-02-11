package com.hxqc.mall.usedcar.model;

/**
 * 说明:下架原因
 *
 * @author: 吕飞
 * @since: 2015-09-09
 * Copyright:恒信汽车电子商务有限公司
 */
public class OffsaleReason {
    public String content;
    public String id;
    public boolean isSelect = false;

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
