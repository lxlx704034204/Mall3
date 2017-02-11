package com.hxqc.carcompare.ui.compare.adapter;


import com.hxqc.carcompare.model.comparebasic.ChildParm;
import com.hxqc.carcompare.model.comparebasic.CompareGroupParm;
import com.hxqc.carcompare.model.comparebasic.CompareParm;
import com.hxqc.carcompare.model.comparegrade.CarGrade;
import com.hxqc.carcompare.model.comparegrade.GradeEntity;
import com.hxqc.carcompare.model.comparenews.AutoNews;
import com.hxqc.carcompare.model.comparenews.NewsEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaofan on 2016/10/8.
 */
@Deprecated
public class TestData {

    public static void getData1(List<CompareParm> compareParmList) {
        List<CompareGroupParm> parametersList = new ArrayList<>();
        List<ChildParm> list1 = new ArrayList<>();
        list1.add(new ChildParm("参考价", "58万"));
        list1.add(new ChildParm("国家补助", "--"));
        list1.add(new ChildParm("test-1", "haha01"));
        list1.add(new ChildParm("厂商指导价", "62万"));
        list1.add(new ChildParm("厂商", "一汽-大众奥迪"));
        list1.add(new ChildParm("test1", "haha1"));
        parametersList.add(new CompareGroupParm("基本参数", list1));

        List<ChildParm> list2 = new ArrayList<>();
        list2.add(new ChildParm("长度", "40001"));
        list2.add(new ChildParm("宽度", "20001"));
        list2.add(new ChildParm("高度", "15001"));
        list2.add(new ChildParm("轴距", "40"));
        parametersList.add(new CompareGroupParm("车身", list2));

        List<ChildParm> list3 = new ArrayList<>();
        list3.add(new ChildParm("发送机位置", "前置1"));
        list3.add(new ChildParm("排量", "1L1"));
        list3.add(new ChildParm("进气形式", "涡轮增压1"));
        list3.add(new ChildParm("汽缸数", "4个"));
        parametersList.add(new CompareGroupParm("发动机", list3));

        compareParmList.add(new CompareParm("奥迪", "奥迪A4 2013新款", "奥迪A4", "3", parametersList));
    }

    public static void getData2(List<CompareParm> compareParmList) {
        List<CompareGroupParm> parametersList = new ArrayList<>();
        List<ChildParm> list1 = new ArrayList<>();
        list1.add(new ChildParm("参考价", "58万"));
        list1.add(new ChildParm("国家补助", "--"));
        list1.add(new ChildParm("厂商指导价", "68万"));
        list1.add(new ChildParm("厂商", "一汽-大众奥迪"));
        list1.add(new ChildParm("test2", "haha2"));
        parametersList.add(new CompareGroupParm("基本参数", list1));

        List<ChildParm> list2 = new ArrayList<>();
        list2.add(new ChildParm("长度", "40002"));
        list2.add(new ChildParm("宽度", "20002"));
        list2.add(new ChildParm("高度", "15002"));
        list2.add(new ChildParm("轴距", "40"));
        parametersList.add(new CompareGroupParm("车身", list2));

        List<ChildParm> list3 = new ArrayList<>();
        list3.add(new ChildParm("发送机位置", "前置1"));
        list3.add(new ChildParm("排量", "1L1"));
        list3.add(new ChildParm("进气形式", "涡轮增压1"));
        list3.add(new ChildParm("汽缸数", "4个"));
        parametersList.add(new CompareGroupParm("发动机", list3));

        compareParmList.add(new CompareParm("奥迪", "奥迪A4 2016新款", "奥迪A4", "3", parametersList));
    }

    public static void getData3(List<CompareParm> compareParmList) {
        List<CompareGroupParm> parametersList = new ArrayList<>();
        List<ChildParm> list1 = new ArrayList<>();
        list1.add(new ChildParm("参考价", "58万"));
        list1.add(new ChildParm("国家补助", "--"));
        list1.add(new ChildParm("厂商指导价", "68万"));
        list1.add(new ChildParm("厂商", "一汽-大众奥迪"));
        list1.add(new ChildParm("test3", "haha3"));
        parametersList.add(new CompareGroupParm("基本参数", list1));

        List<ChildParm> list2 = new ArrayList<>();
        list2.add(new ChildParm("长度", "4000"));
        list2.add(new ChildParm("宽度", "2000"));
        list2.add(new ChildParm("高度", "1500"));
        list2.add(new ChildParm("轴距", "40"));
        parametersList.add(new CompareGroupParm("车身", list2));

        List<ChildParm> list3 = new ArrayList<>();
        list3.add(new ChildParm("发送机位置", "前置"));
        list3.add(new ChildParm("排量", "1L"));
        list3.add(new ChildParm("进气形式", "涡轮增压"));
        list3.add(new ChildParm("汽缸数", "4个"));
        parametersList.add(new CompareGroupParm("发动机", list3));

        compareParmList.add(new CompareParm("奥迪", "奥迪A4 2014新款", "奥迪A4", "3", parametersList));
    }

    public static void getData4(List<CompareParm> compareParmList) {
        List<CompareGroupParm> parametersList = new ArrayList<>();
        List<ChildParm> list1 = new ArrayList<>();
        list1.add(new ChildParm("参考价", "58万"));
        list1.add(new ChildParm("国家补助", "--"));
        list1.add(new ChildParm("厂商指导价", "68万"));
        list1.add(new ChildParm("厂商", "一汽-大众奥迪"));
        parametersList.add(new CompareGroupParm("基本参数", list1));

        List<ChildParm> list2 = new ArrayList<>();
        list2.add(new ChildParm("长度", "4000"));
        list2.add(new ChildParm("宽度", "2000"));
        list2.add(new ChildParm("高度", "1500"));
        list2.add(new ChildParm("轴距", "40"));
        list2.add(new ChildParm("test4", "haha4"));
        parametersList.add(new CompareGroupParm("车身", list2));

        List<ChildParm> list3 = new ArrayList<>();
        list3.add(new ChildParm("发送机位置", "前置"));
        list3.add(new ChildParm("排量", "1L"));
        list3.add(new ChildParm("进气形式", "涡轮增压"));
        list3.add(new ChildParm("汽缸数", "4个"));
        parametersList.add(new CompareGroupParm("发动机", list3));

        List<ChildParm> list4 = new ArrayList<>();
        list4.add(new ChildParm("a", "前置"));
        parametersList.add(new CompareGroupParm("变速箱", list4));

        List<ChildParm> list5 = new ArrayList<>();
        list5.add(new ChildParm("b", "前置"));
        parametersList.add(new CompareGroupParm("底盘转向", list5));

        List<ChildParm> list6 = new ArrayList<>();
        list6.add(new ChildParm("c", "前置"));
        parametersList.add(new CompareGroupParm("车轮制动", list6));


        List<ChildParm> list7 = new ArrayList<>();
        list7.add(new ChildParm("d", "前置"));
        parametersList.add(new CompareGroupParm("安全装备", list7));

        List<ChildParm> list8 = new ArrayList<>();
        list8.add(new ChildParm("e", "前置"));
        parametersList.add(new CompareGroupParm("操控配置", list8));

        List<ChildParm> list9 = new ArrayList<>();
        list9.add(new ChildParm("f", "前置"));
        parametersList.add(new CompareGroupParm("外部配置", list9));

        List<ChildParm> lista = new ArrayList<>();
        lista.add(new ChildParm("g", "前置"));
        parametersList.add(new CompareGroupParm("内部配置", lista));

        List<ChildParm> listb = new ArrayList<>();
        listb.add(new ChildParm("h", "前置"));
        parametersList.add(new CompareGroupParm("座椅配置", listb));

        List<ChildParm> listc = new ArrayList<>();
        listc.add(new ChildParm("i", "前置"));
        parametersList.add(new CompareGroupParm("多媒体配置", listc));


        List<ChildParm> listd = new ArrayList<>();
        listd.add(new ChildParm("j", "前置"));
        parametersList.add(new CompareGroupParm("灯光配置", listd));

        List<ChildParm> liste = new ArrayList<>();
        liste.add(new ChildParm("k", "前置"));
        parametersList.add(new CompareGroupParm("玻璃/后视镜", liste));

        List<ChildParm> listf = new ArrayList<>();
        listf.add(new ChildParm("l", "前置"));
        parametersList.add(new CompareGroupParm("空调/冰箱", listf));

        List<ChildParm> listg = new ArrayList<>();
        listg.add(new ChildParm("m", "前置"));
        parametersList.add(new CompareGroupParm("高科技配置", listg));


        compareParmList.add(new CompareParm("奥迪", "奥迪A4 2014新款", "奥迪A4", "3", parametersList));
    }


    public static List<AutoNews> getAutoNews(int count) {
        List<AutoNews> mList = new ArrayList<>();
        List<NewsEntity> list1 = new ArrayList<>();
        list1.add(new NewsEntity("10001", "奔驰GLS售价100万起售1"));
        list1.add(new NewsEntity("10002", "奔驰GLS售价100万起售2"));
        list1.add(new NewsEntity("10003", "奔驰GLS售价100万起售3"));
        //   list1.add(new NewsEntity("10004", "奔驰GLS售价100万起售4"));
        for (int i = 0; i < count; i++)
            mList.add(new AutoNews("", "", "", "", list1));

        return mList;
    }

    public static List<AutoNews> addAutoNews() {
        List<AutoNews> mList = new ArrayList<>();
        List<NewsEntity> list1 = new ArrayList<>();
        list1.add(new NewsEntity("10001", "奥迪A6最新款售价100万起售1"));
        list1.add(new NewsEntity("10002", "奥迪A6最新款售价100万起售2"));
        list1.add(new NewsEntity("10003", "奥迪A6最新款售价100万起售3"));
        list1.add(new NewsEntity("10004", "奥迪A6最新款售价100万起售4"));
        mList.add(new AutoNews("", "", "", "", list1));

        return mList;
    }


    public static List<CarGrade> getAutoGrade(int count) {
        List<CarGrade> mList = new ArrayList<>();
        List<GradeEntity> list1 = new ArrayList<>();
        list1.add(new GradeEntity("舒适性", "5"));
        list1.add(new GradeEntity("空间", "5"));
        list1.add(new GradeEntity("油耗", "3"));
        list1.add(new GradeEntity("外观", "4"));
        list1.add(new GradeEntity("内饰", "5"));
        list1.add(new GradeEntity("平均分", "5"));

        for (int i = 0; i < count; i++)
            mList.add(new CarGrade("", "", "", "", list1));

        return mList;
    }


    public static List<CarGrade> addAutoGrade() {
        List<CarGrade> mList = new ArrayList<>();
        List<GradeEntity> list1 = new ArrayList<>();
//        list1.add(new GradeEntity("3", "3","3","3","3","3","3"));
//        list1.add(new GradeEntity("4", "4","4","4","4","4","4"));
//        list1.add(new GradeEntity("5", "5","5","5","5","5","5"));
        //   list1.add(new NewsEntity("10004", "奔驰GLS售价100万起售4"));
        list1.add(new GradeEntity("舒适性", "2"));
        list1.add(new GradeEntity("空间", "3"));
        list1.add(new GradeEntity("油耗", "1"));
        list1.add(new GradeEntity("外观", "3"));
        list1.add(new GradeEntity("内饰", "2"));
        list1.add(new GradeEntity("平均分", "3"));

        mList.add(new CarGrade("", "", "", "", list1));

        return mList;
    }



}
