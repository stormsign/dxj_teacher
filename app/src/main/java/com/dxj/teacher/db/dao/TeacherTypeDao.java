package com.dxj.teacher.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dxj.teacher.bean.SchoolBean;
import com.dxj.teacher.bean.TeacherBean;
import com.dxj.teacher.bean.TeacherRoleBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khb on 2015/8/31.
 */
public class TeacherTypeDao {

    /**
     * 获取科目一级列表
     * @param db
     * @return
     */
    public static List<SchoolBean> getFirstTeacher(SQLiteDatabase db){
        List<SchoolBean> subjects = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from t_teacher_role", null);
        while(cursor.moveToNext()){
            SchoolBean subject = new SchoolBean();
            subject.setId(cursor.getInt(0));
            subject.setParentId(cursor.getInt(2));
            subject.setName(cursor.getString(1));
            subjects.add(subject);
        }
        cursor.close();
        return subjects;
    }

    /**
     * 根据上级科目查找下级科目列表
     * @param db
     * @param parentId
     * @return
     */
    public static List<SchoolBean> getChildTeacherFromParent(SQLiteDatabase db, int parentId){
        List<SchoolBean> subjects = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from t_teacher_role_level where type_id = ?", new String[]{String.valueOf(parentId)});
        while(cursor.moveToNext()){
            SchoolBean subject = new SchoolBean();
            subject.setId(cursor.getInt(0));
            subject.setParentId(cursor.getInt(1));
            subject.setName(cursor.getString(2));
            subjects.add(subject);
        }
        cursor.close();
        return subjects;
    }



}
