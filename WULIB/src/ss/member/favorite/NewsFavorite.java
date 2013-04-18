package ss.member.favorite;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import ss.ComModel.component;
import ss.ComModel.adapter.NewsAdapter;
import ss.main.wulib.R;
import ss.news.NewsFragmentActivity;
import ss.news.NewsMain;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class NewsFavorite extends Activity {

	private int positionSelected = 0;
	private View loading;
	private View news_lv;
	private View loadMoreView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.news_main);
			ImageView icon = (ImageView) findViewById(R.id.news_iconheader);
			icon.setVisibility(View.GONE);

			component.androidOS();
			getNewsFromServer();
			bindLVItem();

			loadMoreView = ((LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.loadmore, null, false);
			ListView lv = (ListView) findViewById(R.id.news_lv);
			lv.addFooterView(loadMoreView);
			
			
			news_lv = findViewById(R.id.news_lv);
			loading = findViewById(R.id.progress_loading);
		} catch (Exception e) {
			component.showMsg(this,
					"in NewsFavorite.java on onCreate " + e.toString());
		}

	}

	private void getNewsFromServer() {
		try {
			String url = getResources().getString(R.string.SERVERTEST)
					+ "getFavoriteNews.php";

			List<NameValuePair> ldata = new ArrayList<NameValuePair>();
			ldata.add(new BasicNameValuePair("username", component.username));

			NewsMain.news = component
					.getNewsFavoriteArrayList(this, url, ldata);

		} catch (Exception e) {
			component
					.showMsg(this, "in NewsFavorite.java on getNewsFromServer "
							+ e.toString());
		}
	}

	@Override
	protected void onResume() {

		super.onResume();
		bindLVItem();
	}

	private void bindLVItem() {
		try {
			ListView lv = (ListView) findViewById(R.id.news_lv);
			lv.setAdapter(new NewsAdapter(this, NewsMain.news));
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long posiLast) {
					positionSelected = position;
					if (posiLast != -1)
						goToDetail();

				}
			});
		} catch (Exception e) {
			component
					.showMsg(this, "in NewsFavorite.java on getNewsFromServer "
							+ e.toString());
		}

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
						NewsFavorite.this,
						"error in NewsMain.java on onPostExecute function "
								+ e.toString());
				e.printStackTrace();
			}
		}
	}

	public void btnClick(View v) {

		switch (v.getId()) {
		case R.id.news_refresh:
//			component.showMsg(this, "Refresh!");
			// getNewsFromServer();
			new LoadLVItem().execute((Void) null);
			break;
		}

	}

	protected void goToDetail() {
		Intent in = new Intent(this, NewsFragmentActivity.class);
		in.putExtra("position", positionSelected + "");
		startActivity(in);
	}
}
