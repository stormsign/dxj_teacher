package com.dxj.teacher.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dxj.teacher.application.MyApplication;
import com.dxj.teacher.base.BaseApplication;
import com.dxj.teacher.db.dao.NoticeDao;

import java.util.ArrayList;
import java.util.List;

/**
 * User: qii Date: 12-7-30
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper singleton = null;

    private static final String DATABASE_NAME = "dxjteacher.db";

    private static final int DATABASE_VERSION = 1;


    static final String CREATE_ACCOUNT_TABLE_SQL = "create table " + AccountTable.TABLE_NAME + "(" + AccountTable.ID + " text primary key,"
	    + AccountTable.NICKNAME + " text," + AccountTable.SET + " text," + AccountTable.TYPE + " text," + AccountTable.ROLENAME + " text," + AccountTable.MOBILE
	    + " text," + AccountTable.UNIVERSITY + " text," + AccountTable.MAJOR + " text," + AccountTable.ABILITY + " text," +AccountTable.GRADES+" text,"+AccountTable.HEADURL+" text,"
			+ AccountTable.ENTRANCETIME + " text," + AccountTable.CHAMPIONTYPE + " text," + AccountTable.SCHOOL + " text," + AccountTable.SCHOOLPROVINCE + " text," + AccountTable.SCHOOLCITY
			+ " text," + AccountTable.LIVINGCITY + " text," + AccountTable.SCHOOLAGE + " text," + AccountTable.DIALECT + " text," +AccountTable.BIRTHDAY + " text," + AccountTable.NATIONALITY + " text," +AccountTable.REMARK+" text,"
			+ AccountTable.EXPERIENCE + " text," + AccountTable.CARD + " text,"+ AccountTable.PHOTO + " text,"+AccountTable.SUBJECT+" text,"+ AccountTable.RESULT +" text,"+ AccountTable.CHAMPION+" text,"+AccountTable.PASSCHAMPION+" integer,"+AccountTable.APTITUDE+" text,"+AccountTable.PASS_APTITUDE+" integer,"+AccountTable.DEGREES+" text,"+AccountTable.PASS_DEGREES+" integer,"+AccountTable.PASS_JSZ+" integer," + AccountTable.HOROSCOPE + " text,"+ AccountTable.SOLVELABEL + " text," + AccountTable.LABEL + " text," + AccountTable.JSZ
			+ " text"  +");";

	private final static String CREATE_NOTICE_TABLE = "CREATE TABLE "
			+ NoticeDao.NOTICE_TABLE_NAME + " ("
			+ NoticeDao.NOTICE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ NoticeDao.NOTICE_COLUMN_TITLE + " TEXT, "
			+ NoticeDao.NOTICE_COLUMN_CONTENT + " TEXT, "
			+ NoticeDao.NOTICE_COLUMN_READ + " INTEGER, "
			+ NoticeDao.NOTICE_COLUMN_TIME + " TEXT, "
			+ NoticeDao.NOTICE_COLUMN_ACTIVITY + " TEXT, "
			+ NoticeDao.NOTICE_COLUMN_EXTRA + " TEXT "
			+"); ";

    DatabaseHelper(Context context) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

		db.execSQL(CREATE_ACCOUNT_TABLE_SQL);
		db.execSQL(CREATE_NOTICE_TABLE);
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

	public static synchronized DatabaseHelper getInstance(Context context) {
		if (singleton == null) {
			singleton = new DatabaseHelper(context);
		}
		return singleton;
	}

    private void deleteAllTable(SQLiteDatabase db) {
	db.execSQL("DROP TABLE IF EXISTS " + AccountTable.TABLE_NAME);
	db.execSQL("DROP TABLE IF EXISTS " + NoticeDao.NOTICE_TABLE_NAME);
	// deleteAllTableExceptAccount(db);

    }

	public void closeDB() {
		if (singleton != null) {
			try {
				SQLiteDatabase db = singleton.getWritableDatabase();
				db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			singleton = null;
		}
	}

    // private void upgrade34To35(SQLiteDatabase db) {
    // db.execSQL(CREATE_NOTIFICATION_TABLE_SQL);
    // }
}
