package ss.member.books.mt;

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
public class BooksMtMain extends TabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.main_favorite);
			((ImageView) findViewById(R.id.mf_headericon))
					.setImageResource(R.drawable.book);
			((TextView) findViewById(R.id.mf_headertext)).setText("Books");

			bindTabs();
		} catch (Exception e) {
			component.showMsg(this, e.toString());
		}
	}

	public void bindTabs() {
		try {
			TabHost tabHost = getTabHost();

			TabSpec catalog = tabHost.newTabSpec("Catalog")
					.setIndicator("Catalog", null)
					// icon
					.setContent(new Intent().setClass(this, CBooksMt.class));

			TabSpec journal = tabHost.newTabSpec("Journal")
					.setIndicator("Journal", null)
					// icon
					.setContent(new Intent().setClass(this, JBooksMt.class));
			tabHost.addTab(catalog);
			tabHost.addTab(journal);

		} catch (Exception e) {
			component.showMsg(this, e.toString());
		}
	}

}
