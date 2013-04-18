package ss.member.favorite;

import ss.ComModel.component;
import ss.main.wulib.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class MainFavorite extends TabActivity {

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.main_favorite);
			bindTabs();
		} catch (Exception e) {
			component.showMsg(
					this,
					"error in MainFavorite.java in oncreate function "
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
					.setContent(
							new Intent().setClass(this, CMainFavorite.class));

			TabSpec journal = tabHost
					.newTabSpec("Journal")
					.setIndicator("Journal", null)
					// icon
					.setContent(
							new Intent().setClass(this, JMainFavorite.class));
			TabSpec news = tabHost
					.newTabSpec("News")
					.setIndicator("News", null)
					// icon
					.setContent(new Intent().setClass(this, NewsFavorite.class));
			tabHost.addTab(news);
			tabHost.addTab(catalog);
			tabHost.addTab(journal);

		} catch (Exception e) {
			component.showMsg(this, e.toString());
		}
	}
}
