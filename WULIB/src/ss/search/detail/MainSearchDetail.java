package ss.search.detail;

import ss.ComModel.component;
import ss.main.wulib.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings({ "deprecation", "unused" })
public class MainSearchDetail extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_detail_main);
		
		bindTabs();
	}

	public void bindTabs() {
		try {
			TabHost tabHost = getTabHost();

			TabSpec searchByKeyword = tabHost
					.newTabSpec("Detail")
					.setIndicator("Detail", null)
					// icon
					.setContent(
							new Intent().setClass(this, CSearchDetailInfo.class));

			TabSpec searchAlpha = tabHost.newTabSpec("Card")
					.setIndicator("Card", null)
					// icon
					.setContent(
							new Intent().setClass(this,
									SearchDetailCard.class));

			TabSpec searchAdvance = tabHost
					.newTabSpec("Marc")
					.setIndicator("Marc", null)
					// icon
					.setContent(
							new Intent().setClass(this, SearchDetailMarc.class));

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
		getMenuInflater().inflate(R.menu.search_detail, menu);
		return true;
	}

}
