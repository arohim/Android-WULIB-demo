package ss.member.history;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import ss.ComModel.BookHistory;
import ss.ComModel.component;
import ss.ComModel.adapter.CHistoryAdapter;
import ss.main.wulib.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;

public class CMainHistory extends Activity {
	public static ArrayList<BookHistory> bookHistory = null;
	private static int positionSelected = 0;
	private View loading;
	private View listview;
	private View loadMoreView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.history_main);
			component.androidOS();

			TextView head = (TextView) findViewById(R.id.hm_head);
			head.setText("Catalog History");

			getHistoryFromServer();

			loadMoreView = ((LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.loadmore, null, false);
			ListView lv = (ListView) findViewById(R.id.history_lv);
			lv.addFooterView(loadMoreView);

			listview = findViewById(R.id.history_lv);
			loading = findViewById(R.id.progress_loading);
		} catch (Exception e) {
			e.printStackTrace();
			component.showMsg(this, e.toString());
		}
	}

	public void btnClick(View v) {

		switch (v.getId()) {
		case R.id.history_lo_refresh:
			new LoadLVItem().execute((Void) null);
			break;
		}

	}

	public void getHistoryFromServer() throws Exception {
		String url = getResources().getString(R.string.SERVERTEST)
				+ "getBorrows.php";
		List<NameValuePair> ldata = new ArrayList<NameValuePair>();
		ldata.add(new BasicNameValuePair("username", component.username));

		// try {
		bookHistory = component.getArrayListBookHistory((component
				.convertStreamToString(this,
						component.connectDBPost(this, url, ldata))));

	}

	@Override
	protected void onResume() {

		super.onResume();
		try {
			bindLVItem();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void bindLVItem() throws Exception {
		// try {
		ListView lv = (ListView) findViewById(R.id.history_lv);
		lv.setAdapter(new CHistoryAdapter(this, bookHistory));
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long posiLast) {
				if (posiLast != -1) {
					positionSelected = position;
					goToDetail();
				}
			}
		});

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long posiLast) {
				if (posiLast != -1) {
					positionSelected = position;
				}
				return false;
			}
		});

		registerForContextMenu(lv);
		// } catch (Exception e) {
		// e.printStackTrace();
		// component.showMsg(this, e.toString());
		// }

	}

	protected void goToDetail() {
		Intent in = new Intent(CMainHistory.this,
				CHistoryFragmentActivity.class);
		in.putExtra("position", positionSelected);
		startActivity(in);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history_main, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		try {
			super.onCreateContextMenu(menu, v, menuInfo);
			menu.setHeaderTitle(bookHistory.get(positionSelected).getTitle());
			MenuInflater mif = this.getMenuInflater();
			mif.inflate(R.menu.menu_history, menu);
			mif = null;
		} catch (Exception e) {
			component.showMsg(this, e.toString()
					+ "  in MainHistory.java in onCreateContextMenu function ");
		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		try {
			switch (item.getItemId()) {
			case R.id.menu_his_opendetail:
				goToDetail();
				break;
			case R.id.menu_his_addfavorite:
				// component.showMsg(this, "test");
				component.addFavorite(this, positionSelected, true, "C");
				break;
			}
		} catch (Exception e) {
			component
					.showMsg(
							this,
							e.toString()
									+ "  in MainHistory.java in onContextItemSelected function ");
		}
		return super.onContextItemSelected(item);
	}

	public class LoadLVItem extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			component.showProgress(true, loading, listview);
			// showProgress(true);
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			try {
				component.androidOS();
				getHistoryFromServer();
			} catch (Exception e) {
				Log.d("doInbackground error", e.toString());
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// component.showMsg(NewsMain.this, "Loading complete. ");
			component.showProgress(false, loading, listview);
			try {
				bindLVItem();
			} catch (Exception e) {
				component.showMsg(
						CMainHistory.this,
						"error in CMainHistory.java on onPostExecute function "
								+ e.toString());
				e.printStackTrace();
			}
		}
	}
}
