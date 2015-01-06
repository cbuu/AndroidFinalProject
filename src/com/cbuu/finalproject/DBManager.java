package com.cbuu.finalproject;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper{
	
	private static final String DB_NAME = "note.db";
	private static final int DB_VRESION = 1;
	private static final String TABLE_NAME = "note"; 
	
	private static final String SQL_CREATE_TABLE = "create table "+TABLE_NAME+
			" (_id integer primary key autoincrement, content text, time long,longitude double,latitude double);";

	public DBManager(Context context) {
		super(context,DB_NAME, null,DB_VRESION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
	}
	
	public long insert(Note note){
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("content", note.getContent());
		values.put("time", note.getTime());
		values.put("longitude", note.getLongitude());
		values.put("latitude", note.getLatitude());
		
		long rid = db.insert(TABLE_NAME, null, values);
		db.close();
		
		return rid;
	}
	
	public int update(Note note) {
		SQLiteDatabase db = getWritableDatabase();
		String where = "_id=?";
		String[] args = {String.valueOf(note.getId())};
		ContentValues values = new ContentValues();
		values.put("content", note.getContent());
		values.put("time", note.getTime());
		values.put("longitude", note.getLongitude());
		values.put("latitude", note.getLatitude());
		int rows = db.update(TABLE_NAME, values, where, args);
		db.close();
		return rows;
	}
	
	public int delete(int id) {
		SQLiteDatabase db = getWritableDatabase();
		String where = "_id=?";
		String[] args = {String.valueOf(id)};
		int rows = db.delete(TABLE_NAME, where, args);
		db.close();
		return rows;
	}
	
	public List<Note> query(){
		List<Note> list = new ArrayList<Note>();
		SQLiteDatabase db = getWritableDatabase();
		Cursor cs = db.query(TABLE_NAME, null, null, null, null, null, null);
		while (cs.moveToNext()) {
			Note item = new Note();
			item.setId(cs.getInt(0));
			item.setContent(cs.getString(1));
			item.setTime(cs.getLong(2));
			item.setLongitude(cs.getDouble(3));
			item.setLatitude(cs.getDouble(4));
			list.add(item);
		}
		cs.close();
		db.close();
		
		return list;
	}
}
