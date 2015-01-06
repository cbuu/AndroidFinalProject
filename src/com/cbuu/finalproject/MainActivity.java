package com.cbuu.finalproject;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private MusicService service = null;

	private ListView notelist = null;
	private List<Note> dataList = null;
	private MyAdapter adapter = null;

	private static final int format = DateUtils.FORMAT_SHOW_DATE
			| DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_YEAR;

	private Context context = null;
	private ShakeUtil shakeUtil = null;

	private DBManager dbManager = null;

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.SHAKE:
				service.PlayOrPause();
				break;
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();
		Handler handler = new MyHandler();

		// Log.d("debug","hehe");
		startMusicService();
		autoPlayMusic();

		shakeUtil = new ShakeUtil(this, handler);
		shakeUtil.register();
	}

	private void init() {
		context = this;
		notelist = (ListView) findViewById(R.id.notelist);
		adapter = new MyAdapter(this);
		service = new MusicService(this);

		dbManager = new DBManager(this);
		dataList = dbManager.query();
		
		if (dataList.size()==0) {
			Note note = new Note("戳右上角添加记事~戳我修改我,用力戳我杀死我~用力摇晃手机可关闭音乐~",System.currentTimeMillis(),0,0);
			dataList.add(note);
			dbManager.insert(note);
		}

		notelist.setAdapter(adapter);
		notelist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Note note = dataList.get(arg2);

				String content = note.getContent();

				int id = note.getId();

				Intent intent = new Intent(MainActivity.this,
						EditActivity.class);
				intent.putExtra("content", content);
				intent.putExtra("isEdit", true);
				intent.putExtra("id", id);

				startActivityForResult(intent, 1);
			}
		});

		notelist.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int id = dataList.get(arg2).getId();
				dbManager.delete(id);
				dataList = dbManager.query();
				adapter.notifyDataSetChanged();
				notelist.setAdapter(adapter);
				return true;
			}
		});
	}

	private void startMusicService() {
		Intent intent = new Intent(MainActivity.this, MusicService.class);
		startService(intent);
	}

	private void autoPlayMusic() {
		service.PlayOrPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_addNote) {

			Intent intent = new Intent(MainActivity.this, EditActivity.class);
			intent.putExtra("isEdit", false);
			startActivityForResult(intent, 1);

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		service.Stop();
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String content = bundle.getString("content");
			long time = bundle.getLong("time");
			double longitude = bundle.getDouble("longitude");
			double latitude = bundle.getDouble("latitude");

			Note note = new Note(content, time, longitude, latitude);

			if (bundle.getBoolean("isEdit")) {
				// DebugLog.log(String.valueOf(bundle.getInt("id")));
				// dao.update(bundle.getInt("id"), name, number);
			} else {
				dbManager.insert(note);
			}

			dataList = dbManager.query();

			adapter.notifyDataSetChanged();

			notelist.setAdapter(adapter);

		}
	}

	public final class ViewHolder {
		public TextView contentView;
		public TextView timeView;
		public TextView locationView;
	}

	public class MyAdapter extends BaseAdapter {

		private LayoutInflater inflater = null;

		public MyAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return dataList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;

			if (convertView == null) {

				holder = new ViewHolder();

				convertView = inflater.inflate(R.layout.item, null);
				holder.contentView = (TextView) convertView
						.findViewById(R.id.text);
				holder.timeView = (TextView) convertView
						.findViewById(R.id.time);
				holder.locationView = (TextView) convertView
						.findViewById(R.id.location);

				holder.contentView.setText(dataList.get(position).getContent());
				long time = dataList.get(position).getTime();

				String date = (String) DateUtils.formatDateTime(context, time,
						format);
				holder.timeView.setText(date);
				StringBuilder sb = new StringBuilder();
				sb.append("经度 : " + dataList.get(position).getLongitude());
				sb.append("   ");
				sb.append("纬度 : " + dataList.get(position).getLatitude());
				holder.locationView.setText(sb.toString());

				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();
				holder.contentView.setText(dataList.get(position).getContent());
				long time = dataList.get(position).getTime();

				String date = (String) DateUtils.formatDateTime(context, time,
						format);
				holder.timeView.setText(date);
				StringBuilder sb = new StringBuilder();
				sb.append("经度 : " + dataList.get(position).getLongitude());
				sb.append("   ");
				sb.append("纬度 : " + dataList.get(position).getLatitude());
				holder.locationView.setText(sb.toString());
			}

			return convertView;
		}

	}
}
