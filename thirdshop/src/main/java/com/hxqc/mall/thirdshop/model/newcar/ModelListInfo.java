package com.hxqc.mall.thirdshop.model.newcar;

import com.hxqc.mall.thirdshop.model.PriceInfo;

import java.util.List;

/**
 * 车型列表 （新车）
 * Created by 赵帆
 */
public class ModelListInfo {

    public String modelGroupTag;   //车型分类 例如 1.5升 涡轮增压 177马力 | 1.8升 141马力

    public List<model> model;  //车型

    public ModelListInfo(String modelGroupTag) {
        this.modelGroupTag = modelGroupTag;
    }

    public String getModelGroupTag() {
        return modelGroupTag;
    }

    public void setModelGroupTag(String modelGroupTag) {
        this.modelGroupTag = modelGroupTag;
    }

    public List<ModelListInfo.model> getModel() {
        return model;
    }

    public void setModel(List<ModelListInfo.model> model) {
        this.model = model;
    }

    public class model {
        public String modelName;  //车型名称
        public String extID;  //车型ID
        public String itemOrigPrice; // 厂家指导价
        public String priceRange;// 经销商价格区间
        public int taxReduction;// 是否减税
        public PriceInfo priceInfo;// 参考总价明细
    }


}
