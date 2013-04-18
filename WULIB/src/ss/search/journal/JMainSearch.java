package ss.search.journal;

import ss.ComModel.component;
import ss.main.wulib.R;
import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class JMainSearch extends TabActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			 this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.main_search);
			TextView head = (TextView) findViewById(R.id.ms_mainTabText);
			head.setText("Journal");
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
							new Intent().setClass(this, JSearchByKeyword.class));

			TabSpec searchAlpha = tabHost.newTabSpec("Alpha")
					.setIndicator("Alpha", null)
					// icon
					.setContent(
							new Intent().setClass(this,
									JSearchByAlphabetically.class));

			TabSpec searchAdvance = tabHost
					.newTabSpec("Advance")
					.setIndicator("Advance", null)
					// icon
					.setContent(
							new Intent().setClass(this, JSearchAdvance.class));

			tabHost.addTab(searchByKeyword);
			tabHost.addTab(searchAlpha);
			tabHost.addTab(searchAdvance);

		} catch (Exception e) {
			component.showMsg(this, e.toString());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_search2, menu);
		return true;
	}

}
