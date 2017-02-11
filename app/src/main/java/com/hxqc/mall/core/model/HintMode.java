package com.hxqc.mall.core.model;

import java.util.List;

/**
 * 说明:搜索联想
 *
 * author: liaoguilong
 * since: 2015-11-26 
 * Copyright:恒信汽车电子商务有限公司
 */
public class HintMode {
        
    public List<Suggestion> auto; //小汽车
    
    public List<Suggestion>  electricVehicle; //电动车


    public static class Suggestion{
            public String suggestion;//推荐搜索
    }
}
