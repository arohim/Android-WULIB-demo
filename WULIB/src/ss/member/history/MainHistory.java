package ss.member.history;

import ss.ComModel.component;
import ss.main.wulib.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class MainHistory extends TabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.main_favorite);
			ImageView icon = (ImageView) findViewById(R.id.mf_headericon);
			TextView text = (TextView) findViewById(R.id.mf_headertext);

			icon.setImageResource(R.drawable.history);
			text.setText("History");

			bindTabs();
		} catch (Exception e) {
			component.showMsg(
					this,
					"error in MainHistory.java in oncreate function "
							+ e.toString());
		}
	}

	public void bindTabs() {
		try {
			TabHost tabHost = getTabHost();

			TabSpec catalog = tabHost
					.newTabSpec("Catalog")
					.setIndicator("Catalog", null)
					// icon
					.setContent(new Intent().setClass(this, CMainHistory.class));

			TabSpec journal = tabHost
					.newTabSpec("Journal")
					.setIndicator("Journal", null)
					// icon
					.setContent(new Intent().setClass(this, JMainHistory.class));

			tabHost.addTab(catalog);
			tabHost.addTab(journal);

		} catch (Exception e) {
			component.showMsg(this, e.toString());
		}
	}
}
