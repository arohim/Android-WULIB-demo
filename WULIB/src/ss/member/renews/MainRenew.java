package ss.member.renews;

import ss.ComModel.component;
import ss.main.wulib.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class MainRenew extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.renews_main);
			bindTabHost();
		} catch (Exception e) {
			component.showYesNoDialogBox(this, "", e.toString());
		}
	}

	private void bindTabHost() {
		try {
			TabHost tabHost = getTabHost();

			TabSpec catalog = tabHost.newTabSpec("Catalog")
					.setIndicator("Catalog", null)
					// icon
					.setContent(new Intent().setClass(this, CMainRenews.class));

			TabSpec journal = tabHost.newTabSpec("Journal")
					.setIndicator("Journal", null)
					// icon
					.setContent(new Intent().setClass(this, JMainRenews.class));

			tabHost.addTab(catalog);
			tabHost.addTab(journal);

		} catch (Exception e) {
			component.showYesNoDialogBox(this, "", e.toString());
		}
	}

}
