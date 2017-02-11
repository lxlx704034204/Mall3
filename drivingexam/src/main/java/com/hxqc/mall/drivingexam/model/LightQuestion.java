package com.hxqc.mall.drivingexam.model;

import java.util.List;

/**
 * 灯光
 * Created by zhaofan on 2016/8/25.
 *
 */
public class LightQuestion {
    public String question;

    public String answer;

    public String detail;

    public List<String> image;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "LightQuetion{" +
                "question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", detail='" + detail + '\'' +
                ", image=" + image +
                '}';
    }
}
