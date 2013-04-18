package ss.member.penalty;

import ss.ComModel.component;
import ss.ComModel.adapter.CPenaltyFragmentAdapter;
import ss.main.wulib.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Window;

public class CPenaltyFragmentActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_detail_fragment);

		try {

			// component.showMsg(this, "test");

			ViewPager pager = (ViewPager) findViewById(R.id.pager);

			FragmentManager fm = getSupportFragmentManager();

			CPenaltyFragmentAdapter pagerAdapter = new CPenaltyFragmentAdapter(
					this, fm);

			pager.setAdapter(pagerAdapter);

			// CSearchDetailInfoFragment.bookDetailCurrentPage =
			// this.getIntent()
			// .getIntExtra("position", 0);

			pager.setCurrentItem(getIntent().getIntExtra("position", 0), true);
		} catch (Exception e) {
			component.showMsg(this, "CPenaltyFragmentActivity " + e.toString());
		}

	}
}
