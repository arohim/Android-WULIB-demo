package ss.search.journal;

import ss.ComModel.component;
import ss.main.wulib.R;
import ss.search.result.JSearchResult;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

@SuppressLint("NewApi")
public class JSearchAdvance extends Activity {

	Spinner in1 = null;
	Spinner in2 = null;
	Spinner in3 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_advance);

		bindWidget();
	}

	public void bindWidget() {
		component.spinner(this, (Spinner) findViewById(R.id.sa_spn_in1),
				getResources().getStringArray(R.array.spn_in));
		component.spinner(this, (Spinner) findViewById(R.id.sa_spn_in2),
				getResources().getStringArray(R.array.spn_in));
		component.spinner(this, (Spinner) findViewById(R.id.sa_spn_in3),
				getResources().getStringArray(R.array.spn_in));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_advance, menu);
		return true;
	}

	@SuppressLint("NewApi")
	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.sa_btn_search:
			// if (component.isOnline(this))
			searchAdvance();
			// else {
			// component.showYesNoDialogBox(this, "Warning!",
			// "No connect internet");
			// }
			break;
		}
	}

	public void searchAdvance() {
		EditText sf1 = (EditText) findViewById(R.id.sa_et_searchfor1);
		if (!((sf1.getText().toString()).isEmpty())) {
			EditText sf2 = (EditText) findViewById(R.id.sa_et_searchfor2);
			EditText sf3 = (EditText) findViewById(R.id.sa_et_searchfor3);
			Spinner in1 = (Spinner) findViewById(R.id.sa_spn_in1);
			Spinner in2 = (Spinner) findViewById(R.id.sa_spn_in2);
			Spinner in3 = (Spinner) findViewById(R.id.sa_spn_in3);
			// RadioGroup rg2 = (RadioGroup) findViewById(R.id.sa_radiogroup2);
			// int selected = rg2.getCheckedRadioButtonId();
			RadioButton rdG1 = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.sa_radiogroup1))
					.getCheckedRadioButtonId());
			RadioButton rdG2 = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.sa_radiogroup2))
					.getCheckedRadioButtonId());

			// String txt = "Sf1 = " + sf1.getText().toString() + "  In1 : "
			// + in1.getSelectedItem().toString();

			Intent in = new Intent(this, JSearchResult.class);
			in.putExtra("className", JSearchAdvance.class + "");
			in.putExtra("searchfor1", sf1.getText().toString());
			in.putExtra("in1",
					component.getSearchIn(in1.getSelectedItem().toString()));

			// if (!sf2.getText().toString().equals("")) {
			// // txt += " g2 = " + rdG1.getText().toString() + "  sf2 "
			// // + sf2.getText().toString() + "  In2 : "
			// // + in2.getSelectedItem().toString();

			in.putExtra("condition2", rdG1.getText().toString());
			in.putExtra("searchfor2", sf2.getText().toString());
			in.putExtra("in2",
					component.getSearchIn(in2.getSelectedItem().toString()));
			// }
			// if (!sf3.getText().toString().equals("")) {
			// txt += " g3 = " + rdG2.getText().toString() + " Sf3 = "
			// + sf3.getText().toString() + "  In3 : "
			// + in3.getSelectedItem().toString() ;

			in.putExtra("condition3", rdG2.getText().toString());
			in.putExtra("searchfor3", sf3.getText().toString());
			in.putExtra("in3",
					component.getSearchIn(in3.getSelectedItem().toString()));
			// }
			// component.showYesNoDialogBox(this, "", txt);
			// startActivity(in);
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

		private ProgressDialog dialog = new ProgressDialog(JSearchAdvance.this);

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
