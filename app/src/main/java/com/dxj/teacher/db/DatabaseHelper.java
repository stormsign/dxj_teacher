package com.dxj.teacher.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dxj.teacher.application.MyApplication;
import com.dxj.teacher.base.BaseApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * User: qii Date: 12-7-30
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper singleton = null;

    private static final String DATABASE_NAME = "dxjteacher.db";

    private static final int DATABASE_VERSION = 1;

	/*public static final String NICKNAME="nickName";//用户昵称
	public static final String HEADURL="headUrl";//头像
	public static final String SET="sex";//性别
	public static final String TYPE="type";//老师类型
	public static final String ROLENAME ="roleName";//老师类型名称
	public static final String MOBILE="mobile";//手机号码
	public static final String UNIVERSITY="university"; //大学名称
	public static final String MAJOR="major";//大学专业
	public static final String ABILITY="ability";//教学能力
	public static final String GRADES="grades";//高考分数
	public static final String ENTRANCETIME="entranceTime";//入学时间
	public static final String CHAMPIONTYPE="championType";//状元等级
	//    public static final List<String> images = new ArrayList<String>();//相册
	public static final String SCHOOL="school";//高中学校
	public static final String SCHOOLPROVINCE="schoolProvince";//省份
	public static final String SCHOOLCITY="schoolCity";//城市
	public static final String LIVINGCITY="livingCity";//现居城市
	public static final String SCHOOLAGE="schoolAge";//教龄
	public static final  String DIALECT="dialect";//方言
	public static final String BIRTHDAY="birthday";//生日
	public static final String NATIONALITY="nationality";//国籍
	public static final String REMARK="remark";//自我描述
	public  static final String EXPERIENCE="experience";//过往经历
	public static final String RESULT="result";//教学成果
	public static final String DEGREES="degrees";//学历
	public static final String HOROSCOPE="horoscope";//星座
	public static final List<String> label = new ArrayList<String>();//个人标签
	public static final List<String> solveLabel = new ArrayList<String>();//擅长
	//        private String grade;//年级
	private static final String JSZ="jsz";//教师证*/
    static final String CREATE_ACCOUNT_TABLE_SQL = "create table " + AccountTable.TABLE_NAME + "(" + AccountTable.ID + " text primary key,"
	    + AccountTable.NICKNAME + " text," + AccountTable.SET + " text," + AccountTable.TYPE + " text," + AccountTable.ROLENAME + " text," + AccountTable.MOBILE
	    + " text," + AccountTable.UNIVERSITY + " text," + AccountTable.MAJOR + " text," + AccountTable.ABILITY + " text," +AccountTable.GRADES+" text,"+AccountTable.HEADURL+" text,"
			+ AccountTable.ENTRANCETIME + " text," + AccountTable.CHAMPIONTYPE + " text," + AccountTable.SCHOOL + " text," + AccountTable.SCHOOLPROVINCE + " text," + AccountTable.SCHOOLCITY
			+ " text," + AccountTable.LIVINGCITY + " text," + AccountTable.SCHOOLAGE + " text," + AccountTable.DIALECT + " text," +AccountTable.BIRTHDAY + " text," + AccountTable.NATIONALITY + " text," +AccountTable.REMARK+" text,"
			+ AccountTable.EXPERIENCE + " text," + AccountTable.RESULT + " text," + AccountTable.DEGREES + " text," + AccountTable.HOROSCOPE + " text," + AccountTable.JSZ
			+ " text"  +");";

    DatabaseHelper(Context context) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

	db.execSQL(CREATE_ACCOUNT_TABLE_SQL);

	// createOtherTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	Log.i("TAG", "oldVersion=" + oldVersion);
	if (oldVersion > 1) {
	    deleteAllTable(db);
	    onCreate(db);
	}
	if (oldVersion <= 36) {
	    // Upgrade36to37.upgrade(db);
	}

	if (oldVersion <= 35) {
	    // Upgrade35to36.upgrade(db);
	}

	if (oldVersion <= 34) {
	    // upgrade34To35(db);
	}

	if (oldVersion <= 33) {
	    deleteAllTable(db);
	    onCreate(db);
	}

    }

    public static synchronized DatabaseHelper getInstance() {
	if (singleton == null) {
	    singleton = new DatabaseHelper(MyApplication.getInstance());
	}
	return singleton;
    }

    private void deleteAllTable(SQLiteDatabase db) {
	db.execSQL("DROP TABLE IF EXISTS " + AccountTable.TABLE_NAME);

	// deleteAllTableExceptAccount(db);

    }

    // private void upgrade34To35(SQLiteDatabase db) {
    // db.execSQL(CREATE_NOTIFICATION_TABLE_SQL);
    // }
}
