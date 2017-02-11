package com.hxqc.mall.core.views.vedit;


import com.hxqc.mall.core.views.vedit.tech.VMallContactMenEmpty;
import com.hxqc.mall.core.views.vedit.tech.VMallEmail;
import com.hxqc.mall.core.views.vedit.tech.VMallID;
import com.hxqc.mall.core.views.vedit.tech.VMallPhoneNumber;
import com.hxqc.mall.core.views.vedit.tech.VMallPlateNumber;
import com.hxqc.mall.core.views.vedit.tech.VMallRealName;

/**
 * Author:  wh
 * Date:  2016/11/2
 * FIXME
 * Todo  枚举已封好的校验规则
 */

public enum ValidatorTech {
//    Password(VMallPassword.class),
    ContactMen(VMallContactMenEmpty.class),//非空联系人
    Email(VMallEmail.class),//非空邮箱
    RealName(VMallRealName.class),//判断真实姓名2-12个汉字
    PhoneNumber(VMallPhoneNumber.class),//非空电话
    ID(VMallID.class),//身份证判断
    PlateNumber(VMallPlateNumber.class);//非空车牌号

    private Class validatorClazz;

    ValidatorTech(Class clazz){
        validatorClazz = clazz;
    }

    public VMETValidator getValidator(){
        try{
            return (VMETValidator) validatorClazz.newInstance();
        }catch (Exception e){
            throw new Error("Can not init validatorClazz instance");
        }
    }
}
