package com.hxqc.mall.thirdshop.activity.auto.activity;

import com.hxqc.mall.thirdshop.activity.auto.control.BaseFilterController;

/**
 * Function: controller生成器
 *
 * @author 袁秉勇
 * @since 2016年05月25日
 */
public interface ControllerConstruct {

    void initController();

    BaseFilterController getController();

    void destroyController();
}
