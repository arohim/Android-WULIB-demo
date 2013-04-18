package ss.member.penalty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ss.ComModel.JournalPenalty;
import ss.ComModel.component;
import ss.ComModel.adapter.JPenaltyAdapter;
import ss.main.wulib.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class JPenaltyMain extends Activity {
	private static int positionSelected = 0;
	public static ArrayList<JournalPenalty> journalPenalty = null;
	private View loading;
	private View listview;
	private View loadMoreView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cpenalty_main);
		try {

			((TextView) findViewById(R.id.cpenalty_lo_title))
					.setText("Journal");

			getFromServer();

			loadMoreView = ((LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.loadmore, null, false);
			ListView lv = (ListView) findViewById(R.id.penalty_lo_lv);
			lv.addFooterView(loadMoreView);

			listview = findViewById(R.id.penalty_lo_lv);
			loading = findViewById(R.id.progress_loading);
		} catch (Exception e) {
			Log.d("JPenaltyMain", e.toString());
		}

	}

	private void getFromServer() throws Exception {
		String url = getResources().getString(R.string.SERVERTEST)
				+ "getPenaltyJournal.php";
		List<NameValuePair> ldata = new ArrayList<NameValuePair>();
		ldata.add(new BasicNameValuePair("username", component.username));

		String data = component.getArrayList(this, url, ldata);
		// component.showYesNoDialogBox(this, "test", data);
		if (data != null) {
			journalPenalty = getArrayListJournalsHistory(data);
			bindLVItem();
		}
	}

	private void bindLVItem() throws Exception {
		try {
			ListView lv = (ListView) findViewById(R.id.penalty_lo_lv);

			lv.setAdapter(new JPenaltyAdapter(this, journalPenalty));
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long posiLast) {
					if (posiLast != -1) {
						positionSelected = position;
						// goToDetail();
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
		} catch (Exception e) {
			e.printStackTrace();
			component.showMsg(this, e.toString());
		}

	}

	public void btnClick(View v) {

		switch (v.getId()) {
		case R.id.cpenalty_lo_refresh:
			new LoadLVItem().execute((Void) null);
			break;
		}

	}

	protected void goToDetail() {
		Intent in = new Intent(this, CPenaltyFragmentActivity.class);
		in.putExtra("position", positionSelected);
		startActivity(in);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.cpenalty_main, menu);
		return true;
	}

	public static ArrayList<JournalPenalty> getArrayListJournalsHistory(
			String data) throws JSONException {
		ArrayList<JournalPenalty> journalData = new ArrayList<JournalPenalty>();
		JSONArray ja = new JSONArray(data);
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			JournalPenalty jPen = new JournalPenalty();
			jPen.setId(jo.getString("id"));
			jPen.setTitle(jo.getString("title"));
			jPen.setAuthor(jo.getString("author"));
			jPen.setStatus(component.getStatusString(jo.getString("status")));
			jPen.setSubject(jo.getString("subject"));
			jPen.setSource(jo.getString("source"));
			jPen.setLanguage(jo.getString("language"));
			jPen.setIssn(jo.getString("issn"));
			jPen.setStatus(jo.getString("status"));
			jPen.setStartDate(jo.getString("startDate"));
			jPen.setEndDate(jo.getString("endDate"));
			jPen.setFavorite(jo.getString("favorite"));
			jPen.setFavorite(jo.getString("penalty"));
			journalData.add(jPen);
		}
		return journalData;
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
				getFromServer();
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
				component.showMsg(JPenaltyMain.this,
						"error in JPenaltyMain.java on onPostExecute function "
								+ e.toString());
				e.printStackTrace();
			}
		}
	}

}
