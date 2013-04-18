package ss.member.books.mt;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import ss.ComModel.Journals;
import ss.ComModel.component;
import ss.ComModel.adapter.JResultSearchAdapter;
import ss.main.wulib.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class JBooksMt extends Activity {
	private int positionSelected = 0;
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
					.setText("Journals");
			getFromServer();

			loadMoreView = ((LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.loadmore, null, false);
			ListView lv = (ListView) findViewById(R.id.penalty_lo_lv);
			lv.addFooterView(loadMoreView);

			listview = findViewById(R.id.penalty_lo_lv);
			loading = findViewById(R.id.progress_loading);
		} catch (Exception e) {
			component.showMsg(this, e.toString());
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		bindLVItem();
	}

	private boolean getFromServer() throws Exception {
		String url = getString(R.string.SERVERTEST) + "getBooksMtJournal.php";
		List<NameValuePair> ldata = new ArrayList<NameValuePair>();
		ldata.add(new BasicNameValuePair("username", component.username));
		String data = component.getArrayList(this, url, ldata);
		if (data != null) {
			component.journals = getArrayListJournals(data);
			bindLVItem();
			return true;
		}
		return false;
	}

	public ArrayList<Journals> getArrayListJournals(String data)
			throws Exception {
		ArrayList<Journals> booksData = new ArrayList<Journals>();
		JSONArray ja = new JSONArray(data);
		for (int i = 0; i < ja.length(); i++) {

			JSONObject jo = ja.getJSONObject(i);
			Journals b = new Journals();

			b.setId(jo.getString("id"));
			b.setTitle(jo.getString("title"));
			b.setAuthor(jo.getString("author"));
			b.setStatus(component.getStatusString(jo.getString("status")));
			b.setSubject(jo.getString("subject"));
			b.setSource(jo.getString("source"));
			b.setLanguage(jo.getString("language"));
			b.setIssn(jo.getString("issn"));
			b.setStatus(jo.getString("status"));
			b.setFavorite(jo.getString("favorite"));
			b.setBooksDate(jo.getString("date"));
			booksData.add(b);
		}
		return booksData;
	}

	public void btnClick(View v) {

		switch (v.getId()) {
		case R.id.cpenalty_lo_refresh:
			bindLVItem();
			break;
		}

	}

	private void bindLVItem() {
		try {
			ListView lv = (ListView) findViewById(R.id.penalty_lo_lv);
			lv.setAdapter(new JResultSearchAdapter(this, component.journals));

			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> view, View v,
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
		} catch (Exception e) {
			component.showMsg(this, "in JBooksMt.java on bindLVItem function "
					+ e.toString());
		}

	}

	protected void goToDetail() {
		Intent in = new Intent(JBooksMt.this, JBooksMtFragmentActivity.class);
		in.putExtra("position", positionSelected);
		startActivity(in);

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
				component.showMsg(
						JBooksMt.this,
						"error in JBooksMt.java on onPostExecute function "
								+ e.toString());
				e.printStackTrace();
			}
		}
	}

}
