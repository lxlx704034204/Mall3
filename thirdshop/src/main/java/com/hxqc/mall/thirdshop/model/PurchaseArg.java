package com.hxqc.mall.thirdshop.model;

import java.util.ArrayList;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年05月06日
 * 购买选项 {
 *              method:
 *                  购车方式[]
 *              insurance:
 *                  购买保险[]
 *              decorate:
 *                  装饰[]
 *          }
 */
public class PurchaseArg {
    public ArrayList< PurchaseArgDetail > method;

    public ArrayList< PurchaseArgDetail > insurance;

    public ArrayList< PurchaseArgDetail > decorate;
}
