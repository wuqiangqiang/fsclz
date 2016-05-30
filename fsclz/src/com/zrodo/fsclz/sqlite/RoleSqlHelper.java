package com.zrodo.fsclz.sqlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class RoleSqlHelper extends SQLiteOpenHelper {

	private static String name= "RoleDb.db";
	
	private static Integer version = 1;
	
	
	public RoleSqlHelper(Context context) {
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String Role_table ="create table role_table"
				+ "(id INTEGER not null primary key AUTOINCREMENT,"
				+ "fkSubId varchar(20),"
				+ "subId varchar(20)"
				+ ")";
		
		db.execSQL(Role_table);
		
		String genre_table = "create table genre_table"
				+ "(id INTEGER not null primary key AUTOINCREMENT,"
				+ "objectname varchar(20),"
				+ "typeid varchar(20),"
				+ "objectid varchar(20)"
				+ ")";
		
		db.execSQL(genre_table);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		String Role_table = "alter table if not exists role_table add subId varchar(20)";
		
		String genre_table = "alter table if not exists genre_table add typeid varchar(20)";
		try {
			db.execSQL(Role_table);
			db.execSQL(genre_table);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
