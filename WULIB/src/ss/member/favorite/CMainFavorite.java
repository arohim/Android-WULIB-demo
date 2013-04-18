package ss.member.favorite;

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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
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
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CMainFavorite extends Activity {

	private int positionSelected = 0;
	private String useDB = "net";
	private View loading;
	private View fv_lv;
	private View loadMoreView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.favorite_main);
		try {
			TextView headTitle = (TextView) findViewById(R.id.fm_head);
			headTitle.setText("Book Favorite");
			component.androidOS();
			getFavoriteFromServer();
			bindLVItem();

			loadMoreView = ((LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.loadmore, null, false);
			ListView lv = (ListView) findViewById(R.id.fv_lv);
			lv.addFooterView(loadMoreView);

			fv_lv = findViewById(R.id.fv_lv);
			loading = findViewById(R.id.progress_loading);

		} catch (Exception e) {
			component.showMsg(this,
					"in CMainFavorite.java on onCreate " + e.toString());
		}

	}

	public void btnClick(View v) {

		switch (v.getId()) {
		case R.id.fv_lo_refresh:
			// component.showMsg(this, "Refresh!");
			// getFavoriteFromServer();
			new LoadLVItem().execute((Void) null);
			break;
		}

	}

	private void getFavoriteFromServer() throws Exception {
		// try {
		if (component.isOnline(this)) {
			Log.d("test", "on line");
			String url = getResources().getString(R.string.SERVERTEST)
					+ "getFavorite.php";
			List<NameValuePair> ldata = new ArrayList<NameValuePair>();
			ldata.add(new BasicNameValuePair("username", component.username));

			// String data = component.convertStreamToString(this,
			// component.connectDBPost(this, url, ldata));
			// component.showYesNoDialogBox(this, "", data);

			// set books variable for public
			component.books = component.getArrayListBook(component
					.convertStreamToString(this,
							component.connectDBPost(this, url, ldata)));

			//
		} else {
			// Log.d("test", "not on line");
			component.showYesNoDialogBox(this, "Warning!",
					"No connect internet");
			// useDB = "mobile";
			// FavoriteDB fdb = new FavoriteDB(this);
			// component.books = fdb.getArrayListBooks();
		}

		// } catch (Exception e) {
		// component.showYesNoDialogBox(this, "", e.toString());
		// }
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			getFavoriteFromServer();
			bindLVItem();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void bindLVItem() {
		// try {
		ListView lv = (ListView) findViewById(R.id.fv_lv);
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

		// } catch (Exception e) {
		// component.showYesNoDialogBox(this, "", e.toString());
		// }

	}

	public void goToDetail() {
		Intent in = new Intent(CMainFavorite.this,
				CSearchDetailInfoFragmentActivity.class);
		in.putExtra("useDB", useDB);
		in.putExtra("position", positionSelected);
		startActivity(in);
	}

	public void deleteItemFavorite(final int positionSelected, final String type) {

		try {
			AlertDialog.Builder dia = new AlertDialog.Builder(this).setTitle(
					"Warning!").setMessage("Are you sure?");
			dia.setNegativeButton("Cancel", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});

			dia.setPositiveButton("Yes", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					if (component.deleteItemFavoriteDo(CMainFavorite.this,
							positionSelected, type)) {
						// component.showMsg(CMainFavorite.this, "delete");
						try {
							getFavoriteFromServer();
							bindLVItem();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
			dia.create().show();

		} catch (Exception e) {
			component.showYesNoDialogBox(this, "Sorry!", e.toString());
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		try {
			super.onCreateContextMenu(menu, v, menuInfo);
			menu.setHeaderTitle(component.books.get(positionSelected)
					.getTitle());
			MenuInflater mif = this.getMenuInflater();
			mif.inflate(R.menu.menu_favorite, menu);
			mif = null;
		} catch (Exception e) {
			component
					.showMsg(
							this,
							e.toString()
									+ "  in MainFavorite.java in onCreateContextMenu function ");
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		try {
			switch (item.getItemId()) {
			case R.id.menu_fv_opendetail:
				goToDetail();
				break;
			case R.id.menu_fv_delete:
				deleteItemFavorite(positionSelected, "C");
				break;
			}
		} catch (Exception e) {
			component
					.showMsg(
							this,
							e.toString()
									+ "  in MainFavorite.java in onContextItemSelected function ");
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.favorite_main, menu);
		return true;
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
				component.androidOS();
				getFavoriteFromServer();
			} catch (Exception e) {
				Log.d("doInbackground error", e.toString());
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// component.showMsg(NewsMain.this, "Loading complete. ");
			component.showProgress(false, loading, fv_lv);
			try {
				bindLVItem();
			} catch (Exception e) {
				component.showMsg(
						CMainFavorite.this,
						"error in NewsMain.java on onPostExecute function "
								+ e.toString());
				e.printStackTrace();
			}
		}
	}

}
