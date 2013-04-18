package ss.member.renews;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import ss.ComModel.component;
import ss.ComModel.adapter.CHistoryAdapter;
import ss.main.wulib.R;
import ss.member.history.CMainHistory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class CMainRenews extends Activity {
	private static int positionSelected = 0;
	private View loading;
	private View fv_lv;
	private View loadMoreView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_renew);

		try {
			// getFromServer();
			//
			// bindLVItem();

			TextView head = (TextView) findViewById(R.id.mr_head);
			head.setText("Catalog");

			loadMoreView = ((LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.loadmore, null, false);
			ListView lv = (ListView) findViewById(R.id.renews_lv);
			lv.addFooterView(loadMoreView);

			fv_lv = findViewById(R.id.renews_lv);
			loading = findViewById(R.id.progress_loading);
		} catch (Exception e) {
			component.showYesNoDialogBox(this, "test",
					"in CMainRenews.java in oncreate function " + e.toString());
		}

	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.sresult_lo_refresh:
			new LoadLVItem().execute((Void) null);
			break;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.cmain_renews, menu);
		return true;
	}

	@Override
	protected void onResume() {

		super.onResume();
		try {
			// getFromServer();
			// bindLVItem();
			new LoadLVItem().execute((Void) null);
		} catch (Exception e) {
			component.showMsg(this, "in CMainRenews.java in onResume function "
					+ e.toString());
		}
	}

	private void getFromServer() throws Exception {
		String url = getResources().getString(R.string.SERVERTEST)
				+ "getForRenews.php";
		List<NameValuePair> ldata = new ArrayList<NameValuePair>();
		ldata.add(new BasicNameValuePair("username", component.username));
		String data = component.getStringFromServerPost(url, ldata, this);
		// component.showYesNoDialogBox(this, "test", data);

		if (data != null) {
			CMainHistory.bookHistory = component.getArrayListBookHistory(data);
			// bindLVItem();
		}

	}

	private void bindLVItem() throws Exception {
		ListView lv = (ListView) findViewById(R.id.renews_lv);
		lv.setAdapter(new CHistoryAdapter(this, CMainHistory.bookHistory));
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

	}

	private void goToDetail() {
		Intent in = new Intent(CMainRenews.this, CRenewsItemDetail.class);
		in.putExtra("position", positionSelected);
		startActivity(in);
	}

	public class LoadLVItem extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			component.showProgress(true, loading, fv_lv);
			// showProgress(true);
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			try {
				// component.androidOS();
				getFromServer();
			} catch (Exception e) {
				Log.d("doInbackground error", e.toString());
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			component.showProgress(false, loading, fv_lv);
			try {
				if (result)
					bindLVItem();
				else {
					component.showMsg(CMainRenews.this, " Loading fail. ");
				}
			} catch (Exception e) {
				component.showMsg(CMainRenews.this,
						"error in CMainRenews.java on onPostExecute function "
								+ e.toString());
				e.printStackTrace();
			}
		}
	}

}
