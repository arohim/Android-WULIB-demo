package ss.search.catalog;

import ss.ComModel.component;
import ss.main.wulib.R;
import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class CMainSearch extends TabActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.main_search);

			TextView head = (TextView) findViewById(R.id.ms_mainTabText);
			head.setText("Catalog");
			bindTabs();
		} catch (Exception e) {
			// TODO: handle exception
			component.showMsg(this, e.toString());
		}

	}

	public void bindTabs() {
		try {
			TabHost tabHost = getTabHost();

			TabSpec searchByKeyword = tabHost
					.newTabSpec("Keyword")
					.setIndicator("Keyword", null)
					// icon
					.setContent(
							new Intent().setClass(this, CSearchByKeyword.class));

			TabSpec searchAlpha = tabHost.newTabSpec("Alpha")
					.setIndicator("Alpha", null)
					// icon
					.setContent(
							new Intent().setClass(this,
									CSearchByAlphabetically.class));

			TabSpec searchAdvance = tabHost
					.newTabSpec("Advance")
					.setIndicator("Advance", null)
					// icon
					.setContent(
							new Intent().setClass(this, CSearchAdvance.class));

			tabHost.addTab(searchByKeyword);
			tabHost.addTab(searchAlpha);
			tabHost.addTab(searchAdvance);


		} catch (Exception e) {
			component.showMsg(this, e.toString());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main_search2, menu);
		return true;
	}

}
