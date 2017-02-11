package com.hxqc.autonews.model;

import com.hxqc.autonews.model.pojos.AutoInformation;

/**
 * Author:李烽
 * Date:2016-10-21
 * FIXME
 * Todo 汽车资讯类型管理
 */

public class AutoInformationTyper {
    /**
     * todo 获取汽车资讯的类型
     * @param formSource
     * @param pType
     * @param nType
     * @return
     */
    public static AutoInformation.Type getType(int formSource, String pType, String nType) {
        switch (formSource) {
            case 10:
                switch (pType) {
                    case "10":
                        return AutoInformation.Type.textAndImage;
                    case "20":
                        return AutoInformation.Type.Images;
                    default:
                        return null;
                }
            case 20:
                switch (nType) {
                    case "10":
                        return AutoInformation.Type.promotion;
                    case "20":
                        return AutoInformation.Type.autoInformation;
                    case "30":
                        return AutoInformation.Type.news;
                    case "40":
                        return AutoInformation.Type.maintenance;
                    case "50":
                        return AutoInformation.Type.shopActivity;
                    case "60":
                        return AutoInformation.Type.maintenancePromotion;
                    default:
                        return null;
                }
            default:
                return null;
        }
    }
}
