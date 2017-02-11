package com.hxqc.mall.core.model;

/**
 * Author: HuJunJie
 * Date: 2015-04-17
 * FIXME
 * Todo 保险公司
 */
@Deprecated
public class InsuranceCompany {

    public String firmID;//保险公司对应ID
    public String firmName;//保险公司名称
    InsuranceKind insuranceKind;
    public InsuranceCompany(String firmID, String firmName) {
        this.firmID = firmID;
        this.firmName = firmName;
    }

    public InsuranceKind getInsuranceKind() {
        return insuranceKind;
    }

    public void setInsuranceKind(InsuranceKind insuranceKind) {
        this.insuranceKind = insuranceKind;
    }
}
