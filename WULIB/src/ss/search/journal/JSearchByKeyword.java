package ss.search.journal;

import ss.ComModel.component;
import ss.main.wulib.R;
import ss.search.result.JSearchResult;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressLint({ "NewApi", "DefaultLocale" })
public class JSearchByKeyword extends Activity {

	private LinearLayout filterLayout = null;
	private TextView tvFilter = null;
	private Spinner sk_spn_beginyear = null;
	private Spinner sk_spn_endyear = null;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_by_keyword);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		bindWidget();
	}

	public void bindWidget() {
		try {
			filterLayout = (LinearLayout) findViewById(R.id.SK_FILTER_LAYOUT);
			filterLayout.setVisibility(LinearLayout.GONE);
			tvFilter = (TextView) findViewById(R.id.TV_SK_FILTER);
			tvFilter.setText(" --vvv--        Filter        --vvv-- ");

			sk_spn_beginyear = (Spinner) findViewById(R.id.sk_spn_beginyear);
			sk_spn_endyear = (Spinner) findViewById(R.id.sk_spn_endyear);

			component.spinner(this, (Spinner) findViewById(R.id.sk_spn_in),
					getResources().getStringArray(R.array.spn_in));
			component.spinner(this,
					(Spinner) findViewById(R.id.sk_spn_language),
					getResources().getStringArray(R.array.spn_language));
			String[] year = component.getYear(10);
			component.spinner(this, sk_spn_beginyear, year);
			component.spinner(this, sk_spn_endyear, year);
			year = null;

			sk_spn_endyear
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View v, int position, long id) {
							try {
								if (!(parent.getItemAtPosition(position)
										.toString().equals("ALL"))
										&& !(sk_spn_beginyear.getSelectedItem()
												.toString().equals("ALL"))) {
									// component.showMsg(SearchByKeyword.this,
									// "not equal");
									if (!(Integer.parseInt(parent
											.getItemAtPosition(position)
											.toString()) >= Integer
											.parseInt(sk_spn_beginyear
													.getSelectedItem()
													.toString()))) {

										sk_spn_beginyear.setSelection(position);
									}
								} else {
									// component.showMsg(SearchByKeyword.this,
									// "equal");
								}
							} catch (Exception e) {
								component.showMsg(JSearchByKeyword.this,
										e.toString());
							}

							// component.showMsg(SearchByKeyword.this,
							// parent.getItemAtPosition(position).toString() +
							// "  " +
							// sk_spn_beginyear.getSelectedItem().toString());
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
						}
					});

			sk_spn_beginyear
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View v, int position, long id) {
							try {
								// component.showMsg(SearchByKeyword.this,
								// (parent.getItemAtPosition(position).toString().equals("ALL"))
								// + " ");
								if (!(parent.getItemAtPosition(position)
										.toString().equals("ALL"))
										&& !(sk_spn_endyear.getSelectedItem()
												.toString().equals("ALL"))) {
									// component.showMsg(SearchByKeyword.this,
									// "not equal");
									if (!(Integer.parseInt(parent
											.getItemAtPosition(position)
											.toString()) <= Integer
											.parseInt(sk_spn_endyear
													.getSelectedItem()
													.toString()))) {

										sk_spn_endyear.setSelection(position);
									}
								} else {
									// component.showMsg(SearchByKeyword.this,
									// "equal");
								}
							} catch (Exception e) {
								component.showMsg(JSearchByKeyword.this,
										e.toString());
							}

						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {

						}
					});
		} catch (Exception e) {
			component.showMsg(this, e.toString());
		}
	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.FILTER_BTN:
			showHideFilterLayout();
			break;
		case R.id.sk_btn_search:
			// if (component.isOnline(this))
			searchByKeyword();
			// else {
			// component.showYesNoDialogBox(this, "Warning!",
			// "No connect internet");
			// }
			break;
		}
	}

	private EditText sk_et_searchfor = null;

	@SuppressLint("DefaultLocale")
	public void searchByKeyword() {
		try {

			sk_et_searchfor = (EditText) findViewById(R.id.sk_et_searchfor);

			if (!((sk_et_searchfor.getText().toString()).isEmpty())) {
				Spinner sk_spn_in = (Spinner) findViewById(R.id.sk_spn_in);
				Spinner sk_spn_language = (Spinner) findViewById(R.id.sk_spn_language);

				String searchIn = sk_spn_in.getSelectedItem().toString();

				searchIn = component.getSearchIn(searchIn);

				// String txt = "Search for = " +
				// sk_et_searchfor.getText().toString()
				// + "  In : " + searchIn;

				// if (filterLayout.getVisibility() == LinearLayout.VISIBLE) {
				// txt += "  lang. : "
				// + sk_spn_language.getSelectedItem().toString()
				// + "  b year : "
				// + sk_spn_beginyear.getSelectedItem().toString()
				// + " e year : "
				// + sk_spn_endyear.getSelectedItem().toString();
				// }
				// component.showYesNoDialogBox(this, "", txt);

				Intent in = new Intent(this, JSearchResult.class);
				in.putExtra("className", JSearchByKeyword.class + "");
				in.putExtra("searchfor1", sk_et_searchfor.getText().toString());
				in.putExtra("in1", searchIn);

				if (filterLayout.getVisibility() == LinearLayout.VISIBLE) {
					in.putExtra("language", sk_spn_language.getSelectedItem()
							.toString());
					in.putExtra("beginYear", sk_spn_beginyear.getSelectedItem()
							.toString());
					in.putExtra("endYear", sk_spn_endyear.getSelectedItem()
							.toString());
					in.putExtra("filter", true);
				} else {
					in.putExtra("filter", false);
				}

				// startActivity(in);
				goToActivity(in);
			} else {
				component.showYesNoDialogBox(this, "Warning!",
						"Must be enter search for");
			}
		} catch (Exception e) {
			component.showMsg(this, e.toString());
		}
	}

	public void showHideFilterLayout() {
		if (filterLayout.getVisibility() == LinearLayout.VISIBLE) {
			filterLayout.setVisibility(LinearLayout.GONE);
			tvFilter.setText(" --vvv--        Filter        --vvv-- ");
		} else {
			filterLayout.setVisibility(LinearLayout.VISIBLE);
			tvFilter.setText(" --^^^--        Filter        --^^^-- ");

		}
		// component.showMsg(this,filterLayout.getVisibility()+"");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	public void goToActivity(Intent in) {
		if (component.isOnline(this)) {
			new LoadActivityThread().execute(in);
		} else
			component.showYesNoDialogBox(this,
					getResources().getString(R.string.warning), getResources()
							.getString(R.string.nointernet));
	}

	private class LoadActivityThread extends AsyncTask<Intent, Void, Void> {

		private ProgressDialog dialog = new ProgressDialog(
				JSearchByKeyword.this);

		protected void onPreExecute() {
			dialog.setMessage("Loading");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Intent... params) {
			startActivity(params[0]);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			dialog.dismiss();
		}

	}

}
