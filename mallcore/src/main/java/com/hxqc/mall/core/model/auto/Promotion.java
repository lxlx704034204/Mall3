package com.hxqc.mall.core.model.auto;


import android.os.Parcel;

import com.hxqc.mall.core.util.OtherUtil;

/**
 * 说明:促销
 * <p/>
 * author: 吕飞
 * since: 2015-03-19
 * Copyright:恒信汽车电子商务有限公司
 */
public class Promotion extends BasePromotion {
    public String participant;//参与人数
    public String store;//剩余
    public String fall;//降幅
    public String introduce;//图文
    public String price;//价格
    public String title;//活动标题

    public String getParticipant() {
        return participant;
    }

    public String getStore() {
        int inventory = 0;
        try {
            inventory = Integer.valueOf(this.store);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "0";
        }
        if (inventory > 0 && inventory > 9999) {
            inventory = 9999;
            this.store = String.valueOf(inventory);
        }
        return store;
    }

    public String getFallU() {
        return OtherUtil.amountFormat(fall);
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getTitle() {
        return title;
    }


    /**
     * 交易状态
     *
     * @return
     */
    public AutoDetail.TransactionStatus transactionStatus() {

        if (Integer.valueOf(store) > 0) {
            if (OtherUtil.int2Boolean(Integer.valueOf(isEnded))) {
                return AutoDetail.TransactionStatus.END;
            }

            if (OtherUtil.int2Boolean(Integer.valueOf(isStarted))) {
                return AutoDetail.TransactionStatus.NORMAL;
            } else {
                return AutoDetail.TransactionStatus.PREPARE;
            }

        } else {
            return AutoDetail.TransactionStatus.SELLOUT;
        }


    }

    /**
     * 价格
     */
    public float getPrice() {
        try {
            return Float.valueOf(price);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0f;
        }
    }

    /**
     * 价格  单位  万
     */
    public String getPriceU() {
        return OtherUtil.amountFormat(price);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.participant);
        dest.writeString(this.store);
        dest.writeString(this.fall);
        dest.writeString(this.introduce);
        dest.writeString(this.price);
        dest.writeString(this.title);
    }

    public Promotion() {
    }

    protected Promotion(Parcel in) {
        super(in);
        this.participant = in.readString();
        this.store = in.readString();
        this.fall = in.readString();
        this.introduce = in.readString();
        this.price = in.readString();
        this.title = in.readString();
    }

    public static final Creator< Promotion > CREATOR = new Creator< Promotion >() {
        public Promotion createFromParcel(Parcel source) {
            return new Promotion(source);
        }

        public Promotion[] newArray(int size) {
            return new Promotion[size];
        }
    };
}
