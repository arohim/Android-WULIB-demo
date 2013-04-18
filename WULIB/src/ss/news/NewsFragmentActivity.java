package ss.news;

import ss.ComModel.adapter.NewsFragmentAdapter;
import ss.main.wulib.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Window;

public class NewsFragmentActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_detail_fragment);

		if (arg0 == null) {
			try {

				// NewsFragment.mCurrentPage = Integer.parseInt(getIntent()
				// .getStringExtra("position"));

				bindFragment();
			} catch (Exception e) {
				Log.d("NewsFragmentActivity", e.toString());
			}
		}

	}

	private void bindFragment() throws Exception {
		ViewPager pager = (ViewPager) findViewById(R.id.pager);

		FragmentManager fm = getSupportFragmentManager();

		NewsFragmentAdapter pagerAdapter = new NewsFragmentAdapter(this, fm);

		pager.setAdapter(pagerAdapter);

		pager.setCurrentItem(
				Integer.parseInt(getIntent().getStringExtra("position")), true);

	}

	// ImageView favorite;
	//
	// public void btnClick(View v) {
	//
	// switch (v.getId()) {
	// case R.id.nd_btn_favorite:
	// favorite = (ImageView) v.findViewById(R.id.nd_btn_favorite);
	// favoriteManage();
	// break;
	// }
	//
	// }
	//
	// private void favoriteManage() {
	// if (component.isOnline(this)) {
	// Log.d("test", " on line detail");
	// NewsModel news = NewsMain.news.get(NewsFragment.mCurrentPage);
	// if (news.getFavorite().equals("0")) {
	// if (component.setFavorite(this, news.getId(), "N")) {
	// favorite.setImageResource(R.drawable.favorite);
	// news.setFavorite("1");
	// }
	// } else {
	// if (component.deleteFavorite(this, news.getId(), "N")) {
	// favorite.setImageResource(R.drawable.nonfavorite);
	// news.setFavorite("0");
	// }
	// }
	// } else {
	// Log.d("NewsFragment", "No connect internet.");
	// }
	// }
}
