package ss.search.detail;

import ss.ComModel.adapter.JSearchDetailInfoFragmentAdapter;
import ss.main.wulib.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;

public class JSearchDetailInfoFragmentActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_detail_fragment);

		try {

			ViewPager pager = (ViewPager) findViewById(R.id.pager);

			FragmentManager fm = getSupportFragmentManager();

			JSearchDetailInfoFragmentAdapter pagerAdapter = new JSearchDetailInfoFragmentAdapter(
					this, fm);

			pager.setAdapter(pagerAdapter);

			// CSearchDetailInfoFragment.bookDetailCurrentPage =
			// this.getIntent()
			// .getIntExtra("position", 0);

			pager.setCurrentItem(this.getIntent().getIntExtra("position", 0),
					true);
		} catch (Exception e) {
			Log.d("JSearchDetailInfoFragmentActivity", e.toString());
		}

	}

	public void btnClick(View v) {

	}
}
