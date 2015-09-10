package com.dxj.teacher.bean;

import java.io.Serializable;

/**
 * Created by kings on 9/7/2015.
 * 评论
 */
public class CommentBean extends BaseBean implements Serializable {
    public int getCountComment() {
        return countComment;
    }

    public void setCountComment(int countComment) {
        this.countComment = countComment;
    }

    private int countComment;//	评论数

    public double getOnTime() {
        return onTime;
    }

    public void setOnTime(double onTime) {
        this.onTime = onTime;
    }

    public double getAttitude() {
        return attitude;
    }

    public void setAttitude(double attitude) {
        this.attitude = attitude;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public double getMethod() {
        return method;
    }

    public void setMethod(double method) {
        this.method = method;
    }

    public double getAbility() {
        return ability;
    }

    public void setAbility(double ability) {
        this.ability = ability;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    private double onTime;//准时足时评价
    private double attitude;//态度评价
    private double totalScore;//总评价
    private double method;//方法评价
    private double ability;//能力评价
    private String comments;

}
