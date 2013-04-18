package ss.news;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import ss.ComModel.NewsModel;
import ss.ComModel.component;
import ss.ComModel.adapter.NewsAdapter; 
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
import android.widget.ListView;

public class NewsMain extends Activity {

	public static ArrayList<NewsModel> news = null;
	private int positionSelected = 0;

	private View loading;
	private View news_lv;
	private View loadMoreView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_main);

		component.androidOS();
		try {
			getNewsFromServer();
			bindLVItem();

			loadMoreView = ((LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.loadmore, null, false);
			ListView lv = (ListView) findViewById(R.id.news_lv);
			lv.addFooterView(loadMoreView);

			news_lv = findViewById(R.id.news_lv);
			loading = findViewById(R.id.progress_loading);

			// new LoadLVItem().execute((Void) null);
		} catch (Exception e) {
			component.showMsg(this,
					"in NewsMain.java on onCreate " + e.toString());
			e.printStackTrace();
		}
	}

	public void btnClick(View v) {

		switch (v.getId()) {
		case R.id.news_refresh:
			// component.showMsg(this, "Refresh!");
			new LoadLVItem().execute((Void) null);
			break;
		}

	}

	private void getNewsFromServer() throws Exception {
		// try {
		String url = getResources().getString(R.string.SERVERTEST)
				+ "getNews.php";
		List<NameValuePair> ldata = new ArrayList<NameValuePair>();
		ldata.add(new BasicNameValuePair("username", component.username));
		// news = null;
		news = component.getNewsArrayList(this, url, ldata);
		// String[] newsTitle = component.getTitleNews(news);
		//
		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, newsTitle);
		// bindLVItem();
		// component.showYesNoDialogBox(this, "test",
		// "finish getNewsFromServer");
		// } catch (Exception e) {
		// component.showMsg(this, "in NewsMain.java on getNewsFromServer "
		// + e.toString());
		// }
	}

	@Override
	protected void onResume() {
		super.onResume();

		try {

			bindLVItem();
		} catch (Exception e) {
			component.showMsg(this,
					"in NewsMain.java on onResume " + e.toString());
			e.printStackTrace();
		}
	}

	private void bindLVItem() throws Exception {
		// try {
		// component.showYesNoDialogBox(this, "test", "" + news.size());
		ListView lv = (ListView) findViewById(R.id.news_lv);

		lv.setAdapter(new NewsAdapter(this, news));
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long totalItemCount) {

				if (totalItemCount != -1) {
					positionSelected = position;
					goToDetail();
				}
			}
		});
		// } catch (Exception e) {
		// component.showMsg(this, "in NewsMain.java on bindLVItem function "
		// + e.toString());
		// }
		// component.showYesNoDialogBox(this, "test", "finish bindLVItem");
	}

	public class LoadLVItem extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			component.showProgress(true, loading, news_lv);
			// showProgress(true);
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			try {
				component.androidOS();
				getNewsFromServer();
			} catch (Exception e) {
				Log.d("doInbackground error", e.toString());
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// component.showMsg(NewsMain.this, "Loading complete. ");
			component.showProgress(false, loading, news_lv);
			try {
				bindLVItem();
			} catch (Exception e) {
				component.showMsg(
						NewsMain.this,
						"error in NewsMain.java on onPostExecute function "
								+ e.toString());
				e.printStackTrace();
			}
		}
	}

	protected void goToDetail() {
		try {
			Intent in = new Intent(this, NewsFragmentActivity.class);
			in.putExtra("position", positionSelected + "");
			startActivity(in);
		} catch (Exception e) {
			Log.d("NewsMain", e.toString());
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.news_main, menu);
		return true;
	}

}
