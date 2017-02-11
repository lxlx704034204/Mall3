package com.hxqc.aroundservice.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 09
 * FIXME
 * Todo 违章信息
 */
public class IllegalQueryResultInfo implements Parcelable {

    /**
     * wzID : 违章IDstring
     * date : 违章时间string
     * area : 违章地点string
     * act : 违章行为string
     * code : 违章代码string
     * fen : 违章扣分number
     * money : 违章罚款string
     * handled : 是否处理string
     * PayNo : 交款编号string
     * longitude : 经度number
     * latitude : 纬度number
     * statusCode:	状态number
     */
    public String wzID;//违章ID
    public String date;
    public String area;
    public String act;
    public String xh;//电子警察序号
//    public String wfsj;//违章时间
//    public String wfdd;//违章地点
//    public String wfxw;//违章行为
    public String code;//违章代码
    //    public String fen;
    public String money;
    public String handled;
    public String wfjfs;//违章记分数
//    public String fkje;//罚款金额
//    public String clbj;//处理标记
    public String PayNo;//交款编号
    public String longitude;//经度
    public String latitude;//纬度
    public String statusCode;//状态

    public String cjjg;//采集机关
    public String cjjgmc;//采集机关名称
    public String wfdz;//违章地址
    public String clsj;//处理时间
    public String dsr;//当事人
    public String gxsj;//更新时间
    public String wfnr;//违章内容
    public String hpzl;//号牌种类
    public String hphm;//号牌号码



    public IllegalQueryResultInfo() {
    }

    protected IllegalQueryResultInfo(Parcel in) {
        wzID = in.readString();
        xh = in.readString();
        date = in.readString();
        area = in.readString();
        act = in.readString();
        code = in.readString();
        wfjfs = in.readString();
        money = in.readString();
        handled = in.readString();
        PayNo = in.readString();
        longitude = in.readString();
        latitude = in.readString();
        statusCode = in.readString();
        cjjg = in.readString();
        cjjgmc = in.readString();
        wfdz = in.readString();
        clsj = in.readString();
        dsr = in.readString();
        gxsj = in.readString();
        wfnr = in.readString();
        hpzl = in.readString();
        hphm = in.readString();
    }

    public static final Creator<IllegalQueryResultInfo> CREATOR = new Creator<IllegalQueryResultInfo>() {
        @Override
        public IllegalQueryResultInfo createFromParcel(Parcel in) {
            return new IllegalQueryResultInfo(in);
        }

        @Override
        public IllegalQueryResultInfo[] newArray(int size) {
            return new IllegalQueryResultInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(wzID);
        dest.writeString(xh);
        dest.writeString(date);
        dest.writeString(area);
        dest.writeString(act);
        dest.writeString(code);
        dest.writeString(wfjfs);
        dest.writeString(money);
        dest.writeString(handled);
        dest.writeString(PayNo);
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(statusCode);
        dest.writeString(cjjg);
        dest.writeString(cjjgmc);
        dest.writeString(wfdz);
        dest.writeString(clsj);
        dest.writeString(dsr);
        dest.writeString(gxsj);
        dest.writeString(wfnr);
        dest.writeString(hpzl);
        dest.writeString(hphm);
    }

    @Override
    public String toString() {
        return "IllegalQueryResultInfo{" +
                "wzID='" + wzID + '\'' +
                ", date='" + date + '\'' +
                ", area='" + area + '\'' +
                ", act='" + act + '\'' +
                ", xh='" + xh + '\'' +
                ", code='" + code + '\'' +
                ", money='" + money + '\'' +
                ", handled='" + handled + '\'' +
                ", wfjfs='" + wfjfs + '\'' +
                ", PayNo='" + PayNo + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", cjjg='" + cjjg + '\'' +
                ", cjjgmc='" + cjjgmc + '\'' +
                ", wfdz='" + wfdz + '\'' +
                ", clsj='" + clsj + '\'' +
                ", dsr='" + dsr + '\'' +
                ", gxsj='" + gxsj + '\'' +
                ", wfnr='" + wfnr + '\'' +
                ", hpzl='" + hpzl + '\'' +
                ", hphm='" + hphm + '\'' +
                '}';
    }
}
