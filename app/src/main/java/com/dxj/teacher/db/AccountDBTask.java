package com.dxj.teacher.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dxj.teacher.bean.CardBean;
import com.dxj.teacher.bean.HeadUrl;
import com.dxj.teacher.bean.UserBean;
import com.dxj.teacher.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * User
 */
public class AccountDBTask {

    private AccountDBTask() {

    }

    private static SQLiteDatabase getWsd() {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        return databaseHelper.getWritableDatabase();
    }

    private static SQLiteDatabase getRsd() {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        return databaseHelper.getReadableDatabase();
    }

    /**
     * 保存用户信息
     */
    public static void saveUserBean(UserBean userBean) {
        if (getWsd().isOpen()) {
            ContentValues values = new ContentValues();
            values.put(AccountTable.ID, userBean.getUserInfo().getId());
            values.put(AccountTable.ABILITY, userBean.getUserInfo().getAbility());
            values.put(AccountTable.BIRTHDAY, userBean.getUserInfo().getBirthday());
            values.put(AccountTable.CHAMPIONTYPE, String.valueOf(userBean.getUserInfo().getChampionType()));
            values.put(AccountTable.DEGREES, userBean.getUserInfo().getDegrees());
            values.put(AccountTable.DIALECT, userBean.getUserInfo().getDialect());
            values.put(AccountTable.ENTRANCETIME, userBean.getUserInfo().getEntranceTime());
            values.put(AccountTable.EXPERIENCE, userBean.getUserInfo().getExperience());
            values.put(AccountTable.GRADES, String.valueOf(userBean.getUserInfo().getGrades()));
            values.put(AccountTable.HEADURL, userBean.getUserInfo().getHeadUrl());
            values.put(AccountTable.HOROSCOPE, userBean.getUserInfo().getHoroscope());
            values.put(AccountTable.JSZ, userBean.getUserInfo().getJsz());
            values.put(AccountTable.LIVINGCITY, userBean.getUserInfo().getLivingCity());
            values.put(AccountTable.MAJOR, userBean.getUserInfo().getMajor());
            values.put(AccountTable.MOBILE, userBean.getUserInfo().getMobile());
            values.put(AccountTable.NATIONALITY, userBean.getUserInfo().getNationality());
            values.put(AccountTable.NICKNAME, userBean.getUserInfo().getNickName());
            values.put(AccountTable.REMARK, userBean.getUserInfo().getRemark());
            values.put(AccountTable.RESULT, userBean.getUserInfo().getResult());
            values.put(AccountTable.ROLENAME, userBean.getUserInfo().getRoleName());
            values.put(AccountTable.UNIVERSITY, userBean.getUserInfo().getUniversity());
            values.put(AccountTable.TYPE, String.valueOf(userBean.getUserInfo().getType()));
            values.put(AccountTable.SET, userBean.getUserInfo().getSex());
            values.put(AccountTable.SCHOOL, userBean.getUserInfo().getSchool());
            values.put(AccountTable.SCHOOLAGE, String.valueOf(userBean.getUserInfo().getSchoolAge()));
            values.put(AccountTable.SCHOOLCITY, userBean.getUserInfo().getSchoolCity());
            values.put(AccountTable.SCHOOLPROVINCE, userBean.getUserInfo().getSchoolProvince());
            values.put(AccountTable.PASS_JSZ, userBean.getUserInfo().getPassJsz());
            values.put(AccountTable.DEGREES, userBean.getUserInfo().getDegrees());
            values.put(AccountTable.PASS_DEGREES, userBean.getUserInfo().getPassDegrees());
            values.put(AccountTable.APTITUDE, userBean.getUserInfo().getPassAptitude());
            values.put(AccountTable.PASS_APTITUDE, userBean.getUserInfo().getPassAptitude());
            values.put(AccountTable.PASSCHAMPION, userBean.getUserInfo().getPassChampion());
            values.put(AccountTable.CHAMPION, userBean.getUserInfo().getChampion());
            String json = new Gson().toJson(userBean.getUserInfo().getCard());

            values.put(AccountTable.CARD, json);
            HeadUrl headUrl = new HeadUrl();
            headUrl.setImages(userBean.getUserInfo().getImages());
            String jsonPhoto = new Gson().toJson(headUrl);
            values.put(AccountTable.PHOTO, jsonPhoto);

            if (userBean.getUserInfo().getLabel() != null && userBean.getUserInfo().getLabel().size() > 0) {
                StringBuffer strBuffer = new StringBuffer();
                for (int i = 0; i < userBean.getUserInfo().getLabel().size(); i++) {
                    if (i != userBean.getUserInfo().getLabel().size() - 1) {
                        strBuffer.append(userBean.getUserInfo().getLabel().get(i)).append(",");
                    } else {
                        strBuffer.append(userBean.getUserInfo().getLabel().get(i));
                    }
                }
                Log.i("TAG", "strBuffer=" + strBuffer.toString());
                values.put(AccountTable.LABEL, strBuffer.toString());

            }
            if (userBean.getUserInfo().getSubject()!=null&&userBean.getUserInfo().getSubject().size()>0){
                StringBuffer strBuffer = new StringBuffer();
                for (int i = 0; i < userBean.getUserInfo().getSubject().size(); i++) {
                    if (i != userBean.getUserInfo().getSubject().size() - 1) {
                        strBuffer.append(userBean.getUserInfo().getSubject().get(i)).append(",");
                    } else {
                        strBuffer.append(userBean.getUserInfo().getSubject().get(i));
                    }
                }
                values.put(AccountTable.SUBJECT, strBuffer.toString());

            }
            if (userBean.getUserInfo().getSolveLabel() != null && userBean.getUserInfo().getSolveLabel().size() > 0) {
                StringBuffer strBuffer = new StringBuffer();
                for (int i = 0; i < userBean.getUserInfo().getSolveLabel().size(); i++) {
                    if (i != userBean.getUserInfo().getSolveLabel().size() - 1) {
                        strBuffer.append(userBean.getUserInfo().getSolveLabel().get(i)).append(",");
                    } else {
                        strBuffer.append(userBean.getUserInfo().getSolveLabel().get(i));
                    }
                }
                Log.i("TAG", "strBuffer=" + strBuffer.toString());
                values.put(AccountTable.SOLVELABEL, strBuffer.toString());

            }
            getWsd().insert(AccountTable.TABLE_NAME, null, values);
        }

    }


    /**
     * 查询用户信息
     *
     * @return
     */
    public static UserBean getAccount() {

        String sql = "select * from " + AccountTable.TABLE_NAME;
        /** 获取游标 */
        Cursor c = getRsd().rawQuery(sql, null);
        if (c.moveToNext()) {
            UserBean account = new UserBean();
            UserBean.UserInfo userinfo = account.new UserInfo();
            int colid = c.getColumnIndex(AccountTable.ID);
            userinfo.setId(c.getString(colid));
            colid = c.getColumnIndex(AccountTable.ABILITY);
            userinfo.setAbility(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.BIRTHDAY);
            userinfo.setBirthday(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.CHAMPIONTYPE);
            userinfo.setChampionType(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.DEGREES);
            userinfo.setDegrees(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.DIALECT);
            userinfo.setDialect(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.ENTRANCETIME);
            userinfo.setEntranceTime(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.EXPERIENCE);
            userinfo.setExperience(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.GRADES);
            userinfo.setGrades(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.HEADURL);
            userinfo.setHeadUrl(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.BIRTHDAY);
            userinfo.setBirthday(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.HOROSCOPE);
            userinfo.setHoroscope(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.JSZ);
            userinfo.setJsz(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.LIVINGCITY);
            userinfo.setLivingCity(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.MAJOR);
            userinfo.setMajor(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.MOBILE);
            userinfo.setMobile(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.NATIONALITY);
            userinfo.setNationality(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.NICKNAME);
            userinfo.setNickName(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.REMARK);
            userinfo.setRemark(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.RESULT);
            userinfo.setResult(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.ROLENAME);
            userinfo.setRoleName(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.SCHOOL);
            userinfo.setSchool(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.SCHOOLAGE);
            userinfo.setSchoolAge(Integer.valueOf(c.getString(colid)));

            colid = c.getColumnIndex(AccountTable.SCHOOLCITY);
            userinfo.setSchoolCity(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.SCHOOLPROVINCE);
            userinfo.setSchoolProvince(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.SET);
            userinfo.setSex(c.getString(colid));
            colid = c.getColumnIndex(AccountTable.PASS_DEGREES);
            userinfo.setPassDegrees(c.getInt(colid));
            colid = c.getColumnIndex(AccountTable.APTITUDE);
            userinfo.setAptitude(c.getString(colid));
            colid = c.getColumnIndex(AccountTable.PASS_APTITUDE);
            userinfo.setPassAptitude(c.getInt(colid));
            colid = c.getColumnIndex(AccountTable.PASS_JSZ);
            userinfo.setPassJsz(c.getInt(colid));
            colid = c.getColumnIndex(AccountTable.PASSCHAMPION);
            userinfo.setPassChampion(c.getInt(colid));
            colid = c.getColumnIndex(AccountTable.CHAMPION);
            userinfo.setChampion(c.getString(colid));
            colid = c.getColumnIndex(AccountTable.PHOTO);
            String photoJson = c.getString(colid);
//            List<String> photoList = new ArrayList<>();
            Gson gsonPhoto = new Gson();
            HeadUrl headUrl = gsonPhoto.fromJson(photoJson, HeadUrl.class);
            for (int i=0;i<headUrl.getImages().size();i++)
            {
                Log.i("TAG","HEADuRL="+headUrl.getImages().get(i));
            }
            userinfo.setImages(headUrl.getImages());
            Gson gson = new Gson();
            String json = c.getString(c.getColumnIndex(AccountTable.CARD));

            CardBean value = gson.fromJson(json, CardBean.class);
            userinfo.setCard(value);

//		colid = c.getColumnIndex(AccountTable.TYPE);
//		if (c.getString(colid)!=null)
//		userinfo.setType(Long.valueOf(c.getString(colid)));

            colid = c.getColumnIndex(AccountTable.UNIVERSITY);
            userinfo.setUniversity(c.getString(colid));
            colid = c.getColumnIndex(AccountTable.LABEL);
            String str = c.getString(colid);
            Log.i("TAG", "account=" + userinfo.getNickName());
            if (!StringUtils.isEmpty(str)) {
                ArrayList<String> strList = new ArrayList<String>();
                String[] array = str.split(Pattern.quote(","));
                for (int i = 0; i < array.length; i++) {
                    Log.i("TAG", "strList");
                    strList.add(array[i]);
                }
                userinfo.setLabel(strList);
            }
            colid = c.getColumnIndex(AccountTable.SOLVELABEL);
            String strSolveLabel = c.getString(colid);
            Log.i("TAG", "account=" + userinfo.getNickName());
            if (!StringUtils.isEmpty(strSolveLabel)) {
                ArrayList<String> strList = new ArrayList<>();
                String[] array = strSolveLabel.split(Pattern.quote(","));
                for (int i = 0; i < array.length; i++) {
                    Log.i("TAG", "strList");
                    strList.add(array[i]);
                }
                userinfo.setSolveLabel(strList);
            }
            colid = c.getColumnIndex(AccountTable.SUBJECT);
            String strSubject = c.getString(colid);
            if (!StringUtils.isEmpty(strSubject)) {
                ArrayList<String> strList = new ArrayList<>();
                String[] array = strSubject.split(Pattern.quote(","));
                for (int i = 0; i < array.length; i++) {
                    Log.i("TAG", "strList");
                    strList.add(array[i]);
                }
                userinfo.setSubject(strList);
            }
            account.setUserInfo(userinfo);
            return account;
        }
        return null;
    }

    public static void updateNickName(String uid, String nickName, String bigLetter) {
        if (getWsd().isOpen()) {
            ContentValues values = new ContentValues();
            values.put(bigLetter, nickName);
            String[] whereArgs = {uid};
            getWsd().update(AccountTable.TABLE_NAME, values, AccountTable.ID + "=?", whereArgs);
        }
    }

    public static void updatePhoto(String uid, HeadUrl photoList, String bigLetter) {
        if (getWsd().isOpen()) {
            ContentValues values = new ContentValues();
            Gson gson = new Gson();
            String photoJson = gson.toJson(photoList);
            values.put(bigLetter, photoJson);
            String[] whereArgs = {uid};
            getWsd().update(AccountTable.TABLE_NAME, values, AccountTable.ID + "=?", whereArgs);
        }
    }

    public static void clear() {
        String sql = "delete from " + AccountTable.TABLE_NAME;

        getWsd().execSQL(sql);
    }
}
