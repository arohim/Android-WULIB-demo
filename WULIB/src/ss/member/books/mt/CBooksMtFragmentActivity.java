package ss.member.books.mt;

import ss.ComModel.component;
import ss.ComModel.adapter.CBooksMtFragmentAdapter;
import ss.main.wulib.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Window;

public class CBooksMtFragmentActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_detail_fragment);

		try {

			ViewPager pager = (ViewPager) findViewById(R.id.pager);

			FragmentManager fm = getSupportFragmentManager();

			CBooksMtFragmentAdapter pagerAdapter = new CBooksMtFragmentAdapter(
					this, fm);

			pager.setAdapter(pagerAdapter);

			// CSearchDetailInfoFragment.bookDetailCurrentPage =
			// this.getIntent()
			// .getIntExtra("position", 0);

			pager.setCurrentItem(this.getIntent().getIntExtra("position", 0),
					true);
		} catch (Exception e) {
			component.showMsg(this,
					"CSearchDetailInfoFragmentActivity " + e.toString());
		}

	}
}
