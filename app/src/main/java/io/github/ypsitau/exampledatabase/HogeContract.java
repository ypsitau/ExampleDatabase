package io.github.ypsitau.exampledatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class HogeContract {
	private static final String SQL_CREATE_ENTRIES =
			"CREATE TABLE " + Columns.TABLENAME + " (" +
					Columns._ID + " INTEGER PRIMARY KEY," +
					Columns.COLNAME_TITLE + " TEXT," +
					Columns.COLNAME_SUBTITLE + " TEXT)";

	private static final String SQL_DELETE_ENTRIES =
			"DROP TABLE IF EXISTS " + Columns.TABLENAME;

	private HogeContract() {
	}

	// Inner class that defines the table contents
	public static class Columns implements BaseColumns {
		public static final String TABLENAME = "hoge";
		public static final String COLNAME_TITLE = "title";
		public static final String COLNAME_SUBTITLE = "subtitle";
	}

	public static class OpenHelper extends SQLiteOpenHelper {
		// If you change the database schema, you must increment the database version.
		public static final String DB_NAME = "exampledatabase.sqlite3";
		public static final int DB_VERSION = 1;

		public OpenHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQL_CREATE_ENTRIES);
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// This database is only a cache for online data, so its upgrade policy is
			// to simply to discard the data and start over
			db.execSQL(SQL_DELETE_ENTRIES);
			onCreate(db);
		}

		public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			onUpgrade(db, oldVersion, newVersion);
		}
	}

	static long insertRow(SQLiteDatabase db, String title, String subtitle) {
		ContentValues values = new ContentValues();
		values.put(Columns.COLNAME_TITLE, title);
		values.put(Columns.COLNAME_SUBTITLE, subtitle);
		return db.insert(Columns.TABLENAME, null, values);
	}

	static Cursor queryAll(SQLiteDatabase db) {
		String[] columns = new String[] {
				Columns._ID,
				Columns.COLNAME_TITLE,
				Columns.COLNAME_SUBTITLE,
		};
		String selection = null;
		String[] selectionArgs = null;
		String groupBy = null;
		String having = null;
		String orderBy = Columns._ID + " ASC";
		return db.query(Columns.TABLENAME, columns, selection, selectionArgs, groupBy, having, orderBy);
	}

	static void deleteAll(SQLiteDatabase db) {
		db.delete(Columns.TABLENAME, null, null);
	}

	static int getId(Cursor cursor) { return cursor.getInt(0); }
	static String getTitle(Cursor cursor) { return cursor.getString(1); }
	static String getSubtitle(Cursor cursor) { return cursor.getString(2); }
}
