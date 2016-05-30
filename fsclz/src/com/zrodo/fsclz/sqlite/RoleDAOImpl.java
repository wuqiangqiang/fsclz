package com.zrodo.fsclz.sqlite;

import java.util.ArrayList;

import com.zrodo.fsclz.model.GenreModel;
import com.zrodo.fsclz.model.RoleModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RoleDAOImpl {

	private RoleSqlHelper sqlHeleper;
	
	private static RoleDAOImpl mRoleDaoImpl;
	
	public static RoleDAOImpl getmRoleDaoImpl(Context context){
		 if(mRoleDaoImpl==null){
			 mRoleDaoImpl=new RoleDAOImpl(context);
	        }
	        return mRoleDaoImpl;
	}

	public RoleDAOImpl() {}

	public RoleDAOImpl(Context context) {
		sqlHeleper = new RoleSqlHelper(context);
	}

	public int roleInsert(ArrayList<RoleModel> roleList) {

		SQLiteDatabase db = sqlHeleper.getWritableDatabase();
		db.beginTransaction();
		try {
			int count = roleList.size();
			for (int i = 0; i < count; i++) {
				ContentValues value = new ContentValues();
				value.put("fkSubId", roleList.get(i).getFkSubId());
				value.put("subId", roleList.get(i).getSubId());
				if (db.insert("role_table", null, value) < 0) {
					return 0;
				}
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			if (db != null && db.isOpen()) {
				db.close();
			}
		}
		return roleList.size();
	}

	public int genreInsert(ArrayList<GenreModel> genreList) {

		SQLiteDatabase db = sqlHeleper.getWritableDatabase();
		db.beginTransaction();
		try {
			int count = genreList.size();
			for (int i = 0; i < count; i++) {
				ContentValues value = new ContentValues();
				value.put("objectname", genreList.get(i).getObjectname());
				value.put("typeid", genreList.get(i).getTypeid());
				value.put("objectid", genreList.get(i).getObjectid());
				if (db.insert("genre_table", null, value) < 0) {
					return 0;
				}
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			if (db != null && db.isOpen()) {
				db.close();
			}
		}
		return genreList.size();
	}

	public ArrayList<RoleModel> getRoleList(String fkSubId) {

		Cursor cursor = null;
		SQLiteDatabase db = null;

		try {
			db = sqlHeleper.getReadableDatabase();
			cursor = db.query("role_table", null, "fkSubId=?",
					new String[] { fkSubId }, null, null, null);
			RoleModel roleModel = null;
			ArrayList<RoleModel> roleList = new ArrayList<RoleModel>();
			while (cursor.moveToNext()) {
				String fksubid = cursor.getString(1);
				String subid = cursor.getString(2);
				roleModel = new RoleModel(fksubid, subid);
				roleList.add(roleModel);
			}

			return roleList;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null && db.isOpen()) {
				db.close();
			}
		}
		return null;
	}

	public ArrayList<GenreModel> getGenreList(String typeid) {

		Cursor cursor = null;
		SQLiteDatabase db = null;

		try {
			db = sqlHeleper.getReadableDatabase();
			cursor = db.query("genre_table", null, "typeid=?",
					new String[] { typeid }, null, null, null);
			GenreModel genreModel = null;
			ArrayList<GenreModel> genreList = new ArrayList<GenreModel>();
			while (cursor.moveToNext()) {
				String objectName = cursor.getString(1);
				String typeId = cursor.getString(2);
				String objectId = cursor.getString(3);
				genreModel = new GenreModel(objectName, typeId, objectId);
				genreList.add(genreModel);
			}

			return genreList;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null && db.isOpen()) {
				db.close();
			}
		}
		return null;
	}

}
