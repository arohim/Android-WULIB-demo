package ss.newsbook;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import ss.ComModel.component;
import ss.ComModel.adapter.CResultSearchAdapter;
import ss.main.wulib.R;
import ss.search.detail.CSearchDetailInfoFragmentActivity;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class NewsBookMain extends Activity {

	private int positionSelected = 0;
	private View loading;
	private View listview;
	private View loadMoreView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_book_main);
		try {
			component.androidOS();
			getNewsBookFromServer();

			loadMoreView = ((LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.loadmore, null, false);
			ListView lv = (ListView) findViewById(R.id.news_books_lv);
			lv.addFooterView(loadMoreView);

			listview = findViewById(R.id.news_books_lv);

			loading = findViewById(R.id.progress_loading);
		} catch (Exception e) {
			component.showMsg(this, e.toString());
		}

	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.news_books_refresh:
			try {

				new LoadLVItem().execute((Void) null);
			} catch (Exception e) {
				component.showMsg(this, e.toString());
			}
			break;
		}
	}

	public void getNewsBookFromServer() throws Exception {
		String url = getResources().getString(R.string.SERVERTEST)
				+ "newsbook.php";
		// try {
		// String data = component.convertStreamToString(this,
		// component.connectDBGet(this, url));
		List<NameValuePair> ldata = new ArrayList<NameValuePair>();
		ldata.add(new BasicNameValuePair("username", component.username));
		component.books = component.getArrayListBook(component
				.convertStreamToString(this,
						component.connectDBPost(this, url, ldata)));

		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1,
		// component.getTitleBooks(component.books));
		// bindLVItem();
		// } catch (Exception e) {
		// e.printStackTrace();
		// component.showMsg(this, e.toString());
		// }
		// component.books = component.getArrayListBook(component)
	}

	@Override
	protected void onResume() {

		super.onResume();
		bindLVItem();
	}

	private void bindLVItem() {
		try {
			ListView lv = (ListView) findViewById(R.id.news_books_lv);
			lv.setAdapter(new CResultSearchAdapter(this, component.books));

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

	public void goToDetail() {
		Intent in = new Intent(this, CSearchDetailInfoFragmentActivity.class);
		in.putExtra("position", positionSelected);
		startActivity(in);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		try {
			super.onCreateContextMenu(menu, v, menuInfo);
			menu.setHeaderTitle(component.books.get(positionSelected)
					.getTitle());
			MenuInflater mif = this.getMenuInflater();
			mif.inflate(R.menu.menu_newsbook, menu);
			mif = null;
		} catch (Exception e) {
			component.showMsg(this, e.toString()
					+ "  in MainHistory.java in onCreateContextMenu function ");
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_nb_opendetail:
			goToDetail();
			break;
		case R.id.menu_nb_addfavorite:

			break;
		}

		return super.onContextItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main_member, menu);
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
				getNewsBookFromServer();
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
						NewsBookMain.this,
						"error in NewsMain.java on onPostExecute function "
								+ e.toString());
				e.printStackTrace();
			}
		}
	}

}
