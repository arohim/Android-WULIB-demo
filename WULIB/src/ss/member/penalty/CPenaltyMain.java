package ss.member.penalty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import ss.ComModel.BookPenalty;
import ss.ComModel.component;
import ss.ComModel.adapter.CPenaltyAdapter;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class CPenaltyMain extends Activity {

	private static int positionSelected = 0;
	public static ArrayList<BookPenalty> bookPenalty = null;
	private View loading;
	private View listview;
	private View loadMoreView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cpenalty_main);

		try {
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

	protected void goToDetail() {
		Intent in = new Intent(this, CPenaltyFragmentActivity.class);
		in.putExtra("position", positionSelected);
		startActivity(in);

	}

	private void getFromServer() throws Exception {
		String url = getResources().getString(R.string.SERVERTEST)
				+ "getPenaltyCatalog.php";
		List<NameValuePair> ldata = new ArrayList<NameValuePair>();
		ldata.add(new BasicNameValuePair("username", component.username));

		// String data = component.getStringFromServerPost(url, ldata, this);
		bookPenalty = getBookPenaltyArrayList(url, ldata);

		bindLVItem();
		// Log.d("bookPenalty", bookPenalty.size()+"");
	}

	private void bindLVItem() throws Exception {
		try {
			ListView lv = (ListView) findViewById(R.id.penalty_lo_lv);

			lv.setAdapter(new CPenaltyAdapter(this, bookPenalty));
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

	public ArrayList<BookPenalty> getBookPenaltyArrayList(String url,
			List<NameValuePair> ldata) {

		try {
			HttpResponse res = component.connectDBPost(this, url, ldata);
			int resCode = res.getStatusLine().getStatusCode();
			if (resCode == 200) {
				String data = component.convertStreamToString(this, res);
				// showYesNoDialogBox(actClass,"", data);

				return getArrayListBookPenalty(data);

			} else if (resCode == 404) {
				component.showMsg(this, "File not found");
			} else if (resCode == 500) {
				component.showMsg(this, "Compile error");
			}

		} catch (Exception e) {
			component.showYesNoDialogBox(this, "", e.toString());
		}
		return null;
	}

	public static ArrayList<BookPenalty> getArrayListBookPenalty(String data)
			throws Exception {
		ArrayList<BookPenalty> booksData = new ArrayList<BookPenalty>();
		JSONArray ja = new JSONArray(data);
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			BookPenalty b = new BookPenalty();

			b.setId(jo.getString("id"));
			b.setTitle(jo.getString("title"));
			b.setAuthor(jo.getString("author"));
			b.setPublisher(jo.getString("publisher"));
			b.setCollection(jo.getString("collection"));
			b.setCallnumber(jo.getString("callnumber"));
			b.setMtType(jo.getString("mtType"));
			b.setStatus(component.getStatusString(jo.getString("status")));
			b.setSubject(jo.getString("subject"));
			b.setImgUrl(jo.getString("imgUrl"));
			b.setDescription(jo.getString("description"));
			b.setStartDate(jo.getString("startDate"));
			b.setEndDate(jo.getString("endDate"));
			b.setFavorite(jo.getString("favorite"));
			b.setPenalty(jo.getString("penalty"));

			booksData.add(b);
		}
		return booksData;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cpenalty_main, menu);
		return true;
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
				component.showMsg(CPenaltyMain.this,
						"error in CPenaltyMain.java on onPostExecute function "
								+ e.toString());
				e.printStackTrace();
			}
		}
	}

}
