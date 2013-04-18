package ss.member.penalty;

import ss.ComModel.component;
import ss.main.wulib.R;
import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class PenaltyMain extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_favorite);

		((ImageView) findViewById(R.id.mf_headericon))
				.setImageResource(R.drawable.panalty);
		((TextView) findViewById(R.id.mf_headertext)).setText("Penalty");
		bindTabs();
	}

	public void bindTabs() {
		try {
			TabHost tabHost = getTabHost();

			TabSpec catalog = tabHost
					.newTabSpec("Catalog")
					.setIndicator("Catalog", null)
					// icon
					.setContent(new Intent().setClass(this, CPenaltyMain.class));

			TabSpec journal = tabHost
					.newTabSpec("Journal")
					.setIndicator("Journal", null)
					// icon
					.setContent(new Intent().setClass(this, JPenaltyMain.class));

			tabHost.addTab(catalog);
			tabHost.addTab(journal);

		} catch (Exception e) {
			component.showMsg(this, e.toString());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.penalty_main, menu);
		return true;
	}

}
