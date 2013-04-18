package ss.search;

import ss.ComModel.component;
import ss.callcenter.CallCenter;
import ss.main.wulib.R;
import ss.search.catalog.CMainSearch;
import ss.search.journal.JMainSearch;
import ss.search.speech.SearchBySpeech;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Spinner;

public class SearchSelectType extends Activity {

	private Spinner spnFor = null;
	private Spinner spnType = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_select_type);
		bindWidget();
	}

	public void bindWidget() {
		spnFor = (Spinner) findViewById(R.id.sst_spn_for);
		spnType = (Spinner) findViewById(R.id.sst_spn_type);
		component.spinner(this, spnFor,
				getResources().getStringArray(R.array.spn_sst_for));
		component.spinner(this, spnType,
				getResources().getStringArray(R.array.spn_sst_type));
	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sst_select:
			select();
			break;
		case R.id.callcenter:
			startActivity(new Intent(this, CallCenter.class));
			break;

		}
	}

	private void select() {
		try {

			String searchFor = spnFor.getSelectedItem().toString();
			String searchType = spnType.getSelectedItem().toString();
			// component.showMsg(this, "for : " +
			// spnFor.getSelectedItem().toString()+
			// " type : " + spnType.getSelectedItem().toString());

			// String txt = "";
			Intent in = null;
			if (searchType.equals("Standard")) {
				if (searchFor.equals("Catalog")) {
					in = new Intent(this, CMainSearch.class);
					in.putExtra("for", "Catalog");
					// txt += "Catalog and standard";
				} else {
					in = new Intent(this, JMainSearch.class);
					in.putExtra("for", "Journal");
					// txt += "Journal and standard";
				}
			} else {
				in = new Intent(this, SearchBySpeech.class);
				if (searchFor.equals("Catalog")) {
					in.putExtra("for", "Catalog");
					// txt += "Catlog and speech";
				} else {
					in.putExtra("for", "Journal");
					// txt += "Journal and speech";
				}
			}

			startActivity(in);
			// component.showYesNoDialogBox(this, "", txt);
		} catch (Exception e) {
			component.showYesNoDialogBox(this, "", e.toString());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_select_type, menu);
		return true;
	}

}
