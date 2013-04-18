package ss.search.result;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.ksoap2.serialization.SoapObject;

import ss.ComModel.component;
import ss.ComModel.adapter.JResultSearchAdapter;
import ss.main.wulib.R;
import ss.search.detail.JSearchDetailInfoFragmentActivity;
import ss.search.journal.JSearchAdvance;
import ss.search.journal.JSearchByKeyword;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;

@SuppressLint("DefaultLocale")
public class JSearchResult extends Activity {

	private String searchFor1 = "";
	private String searchIn1 = "";
	private String language = "";
	private String beginYear = "";
	private String endYear = "";
	private int positionSelected = 0;
	private View loading;
	private View listview;
	private View loadMoreView;

	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			this.setContentView(R.layout.search_result);

			component.androidOS();
			bindWidget2();

			loadMoreView = ((LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.loadmore, null, false);
			ListView lv = (ListView) findViewById(R.id.sresult_lv);
			lv.addFooterView(loadMoreView);

			listview = findViewById(R.id.sresult_lv);
			loading = findViewById(R.id.progress_loading_sr);

		} catch (Exception e) {
			component.showYesNoDialogBox(this, "", e.toString());
		}
	}

	@Override
	protected void onResume() {

		super.onResume();
		bindLVItem();
	}

	private void bindWidget2() {
		try {
			Intent intent = getIntent();
			searchFor1 = intent.getStringExtra("searchfor1");
			searchIn1 = intent.getStringExtra("in1");
			String className = intent.getStringExtra("className");

			if (className.equals(JSearchByKeyword.class + "")) {
				searchByKeyword();
			} else if (className.equals(JSearchAdvance.class + "")) {
				searchAdvance();
			} else {
				searchByKeyword();
			}
		} catch (Exception e) {
			component.showMsg(this, e.toString());
		}
	}

	public void btnClick(View v) {

		switch (v.getId()) {
		case R.id.sresult_lo_refresh:
			// component.showMsg(this, "Refresh!");
			// bindWidget2();
			new LoadLVItem().execute((Void) null);
			break;
		}

	}

	private void searchAdvance() throws Exception {
		Intent intent = getIntent();
		String url = getResources().getString(R.string.SERVERTEST)
				+ "searchAdvanceJournal.php";

		List<NameValuePair> ldata = new ArrayList<NameValuePair>();
		ldata.add(new BasicNameValuePair("username", component.username));
		ldata.add(new BasicNameValuePair("sf1", searchFor1));
		ldata.add(new BasicNameValuePair("in1", searchIn1));

		if (!intent.getStringExtra("searchfor2").equals("")) {

			ldata.add(new BasicNameValuePair("sf2", intent
					.getStringExtra("searchfor2")));
			ldata.add(new BasicNameValuePair("in2", intent
					.getStringExtra("in2")));
			ldata.add(new BasicNameValuePair("con2", intent
					.getStringExtra("condition2")));
		}
		if (!intent.getStringExtra("searchfor3").equals("")) {

			ldata.add(new BasicNameValuePair("sf3", intent
					.getStringExtra("searchfor3")));
			ldata.add(new BasicNameValuePair("in3", intent
					.getStringExtra("in3")));
			ldata.add(new BasicNameValuePair("con3", intent
					.getStringExtra("condition3")));
		}

		// component.getSearchKey(this, url, ldata);
		bindListViewItem(url, ldata);
		// component.showYesNoDialogBox(this, "", ldata.toString());
	}

	private void searchByKeyword() throws Exception {
		if (component.isOnline(this)) {
			Intent intent = getIntent();
			String url = getResources().getString(R.string.SERVERTEST)
					+ "searchByKeywordJournal.php";

			// component.showYesNoDialogBox(this, "", url);
			List<NameValuePair> ldata = new ArrayList<NameValuePair>();
			ldata.add(new BasicNameValuePair("username", component.username));
			ldata.add(new BasicNameValuePair("sf", searchFor1));
			ldata.add(new BasicNameValuePair("in", searchIn1));
			if (intent.getBooleanExtra("filter", false)) {

				ldata.add(new BasicNameValuePair("language", intent
						.getStringExtra("language")));
				ldata.add(new BasicNameValuePair("bYear", intent
						.getStringExtra("beginYear")));
				ldata.add(new BasicNameValuePair("eYear", intent
						.getStringExtra("endYear")));
			}

			bindListViewItem(url, ldata);

		} else {
			component.showYesNoDialogBox(this, "Warning!",
					"No connect internet.");
		}
	}

	public void bindListViewItem(String url, List<NameValuePair> ldata) {
		component.journals = component.getJournalArrayList(this, url, ldata);
		// String[] data = component.getTitleJournals(component.journals);
		// bindLVItem();
	}

	private void bindLVItem() {
		try {
			ListView lv = (ListView) findViewById(R.id.sresult_lv);
			// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			// android.R.layout.simple_list_item_1, data);
			lv.setAdapter(new JResultSearchAdapter(this, component.journals));

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
			component.showMsg(
					this,
					"in JSearchResult.java on bindLVItem function "
							+ e.toString());
		}

	}

	private void goToDetail() {
		Intent in = new Intent(JSearchResult.this,
				JSearchDetailInfoFragmentActivity.class);
		in.putExtra("position", positionSelected);
		startActivity(in);
	}

	@SuppressWarnings("unused")
	@SuppressLint("DefaultLocale")
	private void bindWidget() {
		String NAMESPACE = getResources().getString(R.string.NAMESPACE);
		String METHOD_NAME = "Keyword_Search";
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		String condition = "";
		try {
			Intent intent = getIntent();
			String searchFor1 = intent.getStringExtra("searchfor1");
			String searchIn1 = intent.getStringExtra("in1");
			String className = intent.getStringExtra("className");

			if (className.equals(JSearchByKeyword.class + "")) {
				if (intent.getBooleanExtra("filter", false)) {
					language = intent.getStringExtra("language").toUpperCase();
					beginYear = intent.getStringExtra("beginYear")
							.toUpperCase();
					endYear = intent.getStringExtra("endYear").toUpperCase();
					if (!language.equals("ALL")) {
						String lanId = "";
						if (language.equals("THAI")) {
							lanId = "403";
						} else if (language.equals("ENGLISH")) {
							lanId = "114";
						} else if (language.equals("CHINESE")) {
							lanId = "75";
						}
						request.addProperty("LANGUAGE", lanId);
					}
					if (!beginYear.equals("ALL")) {
						request.addAttribute("PUBSTART", beginYear);
					} else if (!endYear.equals("ALL")) {
						request.addAttribute("PUBEND", endYear);
					}
				}

				request.addProperty("Ntk", searchIn1);
				request.addProperty("Ntt", searchFor1);
			} else if (className.equals(JSearchAdvance.class + "")) {
				component.showMsg(this, "in class search advance");
				String searchIn = searchIn1;
				String searchIn2 = intent.getStringExtra("in2");
				String searchIn3 = intent.getStringExtra("in3");
				String searchFor = searchFor1;

				if (!intent.getStringExtra("searchfor2").equals("")) {
					if (!searchIn1.equals(searchIn2))
						searchIn += "|" + searchIn2;
					searchFor += "|" + intent.getStringExtra("searchfor2");
					condition += intent.getStringExtra("condition2");
				}
				if (!intent.getStringExtra("searchfor3").equals("")) {
					if (!searchIn1.equals(searchIn3)
							&& !searchIn2.equals(searchIn3))
						searchIn += "|" + searchIn3;
					searchFor += "|" + intent.getStringExtra("searchfor3");
					condition += "|" + intent.getStringExtra("condition3");
				}
				component.showMsg(this, "test");

				request.addProperty("Ntk", searchIn);
				request.addProperty("Ntt", searchFor);

				// component.showYesNoDialogBox(this, "", "search for " +
				// searchFor + "  searchIn " + searchIn+ "  condition " +
				// condition);
			} else {
				request.addProperty("Ntk", searchIn1);
				request.addProperty("Ntt", searchFor1);
			}

			request.addProperty("Nto", condition);
			request.addProperty("CurrentPage", 1);
			request.addProperty("RowPerPage", 10);
			request.addProperty("OrderBy", "TITLE");
			listItem(request);
		} catch (Exception e) {
			// TODO: handle exception
			component.showMsg(this, e.toString());
		}

		// component.showMsg(this, "search for " + searchFor + " in " + searchIn
		// + "  language  " + language + "  byear  " + beginYear
		// + " eyear " + endYear);

	}

	public void listItem(SoapObject request) {
		String NAMESPACE = getResources().getString(R.string.NAMESPACE);
		String SOAP_ACTION = NAMESPACE + "Keyword_Search";

		try {

			SoapObject result = component.soapConnect(this, request,
					SOAP_ACTION);

			if (result != null) {

				SoapObject keywordResult = (SoapObject) result
						.getProperty("Keyword_SearchResult");
				SoapObject diffgram = (SoapObject) keywordResult
						.getProperty("diffgram");
				SoapObject library = (SoapObject) diffgram
						.getProperty("library");

				String[] data = new String[library.getPropertyCount() - 1];
				for (int i = 0; i < library.getPropertyCount() - 1; i++) {
					SoapObject each = (SoapObject) library.getProperty(i);
					data[i] = each.getProperty("TITLE").toString();
					// component.showMsg(this, "i = " + i + "  "
					// + each.getProperty("TITLE").toString());

				}

				ListView lv = (ListView) findViewById(R.id.sresult_lv);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, data);
				lv.setAdapter(adapter);

			} else {
				// showMsg(" Not response");
				component.showYesNoDialogBox(this, "", " Not response");
			}

		} catch (Exception e) {
			// TODO: handle exception
			component.showMsg(this, e.toString());
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_search_result, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);
		try {
			super.onCreateContextMenu(menu, v, menuInfo);
			menu.setHeaderTitle(component.journals.get(positionSelected)
					.getTitle());
			MenuInflater mif = this.getMenuInflater();
			mif.inflate(R.menu.menu_search_result, menu);
			mif = null;
		} catch (Exception e) {
			component.showMsg(this, e.toString()
					+ "  in MainHistory.java in onCreateContextMenu function ");
		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_it_sr_detail:
			goToDetail();
			break;
		case R.id.menu_it_sr_addfavorite:

			component.addFavorite(this, positionSelected, false, "J");
			break;
		}

		return super.onContextItemSelected(item);
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
				bindWidget2();
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
				if (result) {
					Log.d("doInbackground ", "complete");
					bindLVItem();
				} else {
					Log.d("doInbackground ", "Error");
				}

			} catch (Exception e) {
				component.showMsg(
						JSearchResult.this,
						"error in NewsMain.java on onPostExecute function "
								+ e.toString());
				e.printStackTrace();
			}
		}
	}
}
