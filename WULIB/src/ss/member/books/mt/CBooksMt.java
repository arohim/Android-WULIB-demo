package ss.member.books.mt;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ss.ComModel.Books;
import ss.ComModel.component;
import ss.ComModel.adapter.CBooksAdapter;
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

public class CBooksMt extends Activity {
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
					.setText("Catalog");
			// getFromServer();

			// new LoadLVItem().execute((Void) null);

			loadMoreView = ((LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.loadmore, null, false);
			ListView lv = (ListView) findViewById(R.id.penalty_lo_lv);
			lv.addFooterView(loadMoreView);

			listview = findViewById(R.id.penalty_lo_lv);
			loading = findViewById(R.id.progress_loading);

			// new LoadLVItem().execute((Void) null);

		} catch (Exception e) {
			component.showMsg(this, e.toString());
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		new LoadLVItem().execute((Void) null);
	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.cpenalty_lo_refresh:
			new LoadLVItem().execute();
			break;
		}

	}

	private boolean getFromServer() throws Exception {
		String url = getString(R.string.SERVERTEST) + "getBooksMtCatalog.php";
		List<NameValuePair> ldata = new ArrayList<NameValuePair>();
		ldata.add(new BasicNameValuePair("username", component.username));
		// component.books = component.getSearchKey(this, url, ldata);
		String data = component.getArrayList(this, url, ldata);
		if (data != null) {
			component.books = getArrayListBook(data);
			// bindLVItem();
			return true;
		}
		return false;

	}

	public ArrayList<Books> getArrayListBook(String data) throws JSONException {
		ArrayList<Books> booksData = new ArrayList<Books>();
		JSONArray ja = new JSONArray(data);
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			Books b = new Books();
			b.setId(jo.getString("id"));
			b.setTitle(jo.getString("title"));
			b.setAuthor(jo.getString("author"));
			b.setPublisher(jo.getString("publisher"));
			b.setCollection(jo.getString("collection"));
			b.setCallnumber(jo.getString("callnumber"));
			b.setImgUrl(jo.getString("imgUrl"));
			b.setMtType(jo.getString("mtType"));
			b.setStatus(component.getStatusString(jo.getString("status")));
			b.setSubject(jo.getString("subject"));
			b.setDescription(jo.getString("description"));
			b.setFavorite(jo.getString("favorite"));
			b.setBooksDate(jo.getString("date"));
			booksData.add(b);
		}
		return booksData;
	}

	private void bindLVItem() {
		try {
			ListView lv = (ListView) findViewById(R.id.penalty_lo_lv);
			lv.setAdapter(new CBooksAdapter(this, component.books));

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
			component.showMsg(this, "in CBooksMt.java on bindLVItem function "
					+ e.toString());
		}

	}

	protected void goToDetail() {
		Intent in = new Intent(CBooksMt.this, CBooksMtFragmentActivity.class);
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
				// component.androidOS();
				return getFromServer();
			} catch (Exception e) {
				Log.d("doInbackground error", e.toString());

			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// component.showMsg(NewsMain.this, "Loading complete. ");
			component.showProgress(false, loading, listview);
			try {
				if (result)
					bindLVItem();
				else
					component.showMsg(CBooksMt.this, "server error");
			} catch (Exception e) {
				component.showMsg(
						CBooksMt.this,
						"error in CBooksMt.java on onPostExecute function "
								+ e.toString());
				e.printStackTrace();
			}
		}
	}
}
