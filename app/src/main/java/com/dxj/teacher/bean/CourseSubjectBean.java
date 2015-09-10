package com.dxj.teacher.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kings on 9/3/2015.
 * 科目
 */
public class CourseSubjectBean implements  Serializable{
    private String id;
    private String teacherId;
    private int subjectId;
    private String subjectName;
    private String remark;
    private List<ClassWayBean> classWay = new ArrayList<>();

    public void setClassWay(List<ClassWayBean> classWay) {
        this.classWay = classWay;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public List<ClassWayBean> getClassWay() {
        return classWay;
    }

    public String getId() {
        return id;
    }

    public String getRemark() {
        return remark;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getTeacherId() {
        return teacherId;
    }
}
