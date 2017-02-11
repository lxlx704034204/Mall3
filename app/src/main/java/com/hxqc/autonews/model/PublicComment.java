package com.hxqc.autonews.model;

import java.util.ArrayList;

/**
 * 口碑评价
 * Created by huangyi on 16/10/24.
 */
public class PublicComment {

    public Grade grade;
    public int count; //评价人数
    public ArrayList<UserGradeComment> userGradeComment;
}
