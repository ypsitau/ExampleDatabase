package io.github.ypsitau.exampledatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
	SQLiteOpenHelper openHelper;
	Button button_add;
	Button button_show;
	Button button_deleteAll;
	EditText editText_log;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Context context = this;
		openHelper = new HogeContract.OpenHelper(context);
		button_add = findViewById(R.id.button_add);
		button_show = findViewById(R.id.button_show);
		button_deleteAll = findViewById(R.id.button_deleteAll);
		editText_log = findViewById(R.id.editText_log);
		button_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SQLiteDatabase db = openHelper.getWritableDatabase();
				try {
					db.beginTransaction();
					for (int i = 0; i < 10; i++) {
						HogeContract.insertRow(db, "title" + i, "subtitle" + i);
					}
					db.setTransactionSuccessful();
				} catch (Exception e) {
					// error occured
				} finally {
					db.endTransaction();
				}
			}
		});
		button_show.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SQLiteDatabase db = openHelper.getReadableDatabase();
				Cursor cursor = HogeContract.queryAll(db);
				printf("[%d items]\n", cursor.getCount());
				while (cursor.moveToNext()) {
					printf("_id=%d title=%s subtitle=%s\n",
						HogeContract.getId(cursor),
						HogeContract.getTitle(cursor),
						HogeContract.getSubtitle(cursor));
				}
			}
		});
		button_deleteAll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SQLiteDatabase db = openHelper.getWritableDatabase();
				HogeContract.deleteAll(db);
			}
		});
		editText_log.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				editText_log.setText("");
				return true;
			}
		});
	}
	void printf(String format, Object... args) {
		editText_log.append(String.format(format, args));
		editText_log.setSelection(editText_log.getText().length());
	}
}
