package com.hxqc.mall.core.model;

import com.google.gson.annotations.Expose;

/**
 * 说明:首页上方子模块
 *
 * author: 吕飞
 * since: 2015-06-17
 * Copyright:恒信汽车电子商务有限公司
 */
public class ModuleForHome {
    @Expose
    public String moduleKey;//模块键值 
    @Expose
    public String moduleThumb;//对应图URL
    @Expose
    public String moduleTitle;//模块名称
    @Expose
    public String moduleType;//模块类型
    @Expose
    public String moduleURL;//对应URL
}
