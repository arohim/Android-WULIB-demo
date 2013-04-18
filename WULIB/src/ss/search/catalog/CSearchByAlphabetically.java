package ss.search.catalog;

import ss.ComModel.component;
import ss.main.wulib.R;
import ss.search.result.CSearchResult;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Spinner;

@SuppressLint("NewApi")
public class CSearchByAlphabetically extends Activity {

	Spinner in = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_by_alphabetically);

		bindWidget();
	}

	private void bindWidget() {
		in = (Spinner) findViewById(R.id.sac_spn_in);

		component.spinner(this, in,
				getResources().getStringArray(R.array.spn_in));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search_by_alphabetically, menu);
		return true;
	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.sac_btn_search:
//			if (component.isOnline(this))
				searchByAlphabetically();
//			else {
//				component.showYesNoDialogBox(this, "Warning!",
//						"No connect internet");
//			}
			break;
		}
	}

	public void searchByAlphabetically() {
		EditText sac_et_searchfor = (EditText) findViewById(R.id.sac_et_searchfor);

		// component.showYesNoDialogBox(this, "", "Search for : "
		// + sac_et_searchfor.getText().toString() + " In : "
		// + in.getSelectedItem().toString());
		if (!((sac_et_searchfor.getText().toString()).isEmpty())) {
			Intent in = new Intent(this, CSearchResult.class);
			in.putExtra("className", CSearchByAlphabetically.class + "");
			in.putExtra("searchfor1", sac_et_searchfor.getText().toString());
			in.putExtra("in1",
					component.getSearchIn(this.in.getSelectedItem().toString()));
//			startActivity(in);
			goToActivity(in);
		} else {
			component.showYesNoDialogBox(this, "Warning!",
					"Must be enter search for");
		}
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
				CSearchByAlphabetically.this);

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
