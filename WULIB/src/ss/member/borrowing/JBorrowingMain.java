package ss.member.borrowing;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ss.ComModel.JournalsHistory;
import ss.ComModel.component;
import ss.ComModel.adapter.JHistoryAdapter;
import ss.main.wulib.R;
import ss.member.history.JHistoryFragmentActivity;
import ss.member.history.JMainHistory;
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

public class JBorrowingMain extends Activity {
	private static int positionSelected = 0;
	private View loading;
	private View fv_lv;
	private View loadMoreView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_renew);

		try {
			getFromServer();

			// new LoadLVItem().execute((Void) null);
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
			component.showYesNoDialogBox(
					this,
					"test",
					"in JBorrowingMain.java in oncreate function "
							+ e.toString());
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
		getMenuInflater().inflate(R.menu.jmain_renews, menu);
		return true;
	}

	@Override
	protected void onResume() {

		super.onResume();
		try {
			// new LoadLVItem().execute((Void) null);
			bindLVItem();
		} catch (Exception e) {
			component.showMsg(
					this,
					"in JBorrowingMain.java in onResume function "
							+ e.toString());
		}
	}

	private boolean getFromServer() throws Exception {
		String url = getResources().getString(R.string.SERVERTEST)
				+ "getBorrowingJournal.php";
		List<NameValuePair> ldata = new ArrayList<NameValuePair>();
		ldata.add(new BasicNameValuePair("username", component.username));
		String data = component.getArrayList(this, url, ldata);
		// component.showYesNoDialogBox(this, "test", data);
		if (data != null) {
			JMainHistory.journalHistory = getArrayListJournalsHistory(data);
			return true;
		}
		return false;
		// bindLVItem();
	}

	public ArrayList<JournalsHistory> getArrayListJournalsHistory(String data)
			throws JSONException {
		ArrayList<JournalsHistory> booksData = new ArrayList<JournalsHistory>();
		JSONArray ja = new JSONArray(data);
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			JournalsHistory jHis = new JournalsHistory();
			jHis.setId(jo.getString("id"));
			jHis.setTitle(jo.getString("title"));
			jHis.setAuthor(jo.getString("author"));
			jHis.setStatus(component.getStatusString(jo.getString("status")));
			jHis.setSubject(jo.getString("subject"));
			jHis.setSource(jo.getString("source"));
			jHis.setLanguage(jo.getString("language"));
			jHis.setIssn(jo.getString("issn"));
			jHis.setStatus(jo.getString("status"));
			jHis.setStartDate(jo.getString("startDate"));
			jHis.setEndDate(jo.getString("endDate"));
			jHis.setFavorite(jo.getString("favorite"));
			jHis.setStatusbor(jo.getString("statusbor"));
			booksData.add(jHis);
		}
		return booksData;
	}

	private void bindLVItem() throws Exception {
		ListView lv = (ListView) findViewById(R.id.renews_lv);
		lv.setAdapter(new JHistoryAdapter(this, JMainHistory.journalHistory));
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
		Intent in = new Intent(JBorrowingMain.this,
				JHistoryFragmentActivity.class);
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
				return getFromServer();
			} catch (Exception e) {
				Log.d("doInbackground error", e.toString());
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			component.showProgress(false, loading, fv_lv);
			try {
				if (result)
					bindLVItem();
				else {
					component.showMsg(JBorrowingMain.this, " Loading fail. ");
				}
			} catch (Exception e) {
				component.showMsg(JBorrowingMain.this,
						"error in JBorrowingMain.java on onPostExecute function "
								+ e.toString());
				e.printStackTrace();
			}
		}
	}

}
