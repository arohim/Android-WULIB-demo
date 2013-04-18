package ss.ComModel.adapter;

import ss.news.NewsFragment;
import ss.news.NewsMain;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class NewsFragmentAdapter extends FragmentPagerAdapter {

	int PAGE_COUNT = 10;
	private Activity context;

	public NewsFragmentAdapter(Activity context, FragmentManager fm) {
		super(fm);
		PAGE_COUNT = NewsMain.news.size();
		this.context = context;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return super.getItemId(position);
	}

	@Override
	public Fragment getItem(int position) {

		NewsFragment myFragment = new NewsFragment(context);
		try {
//			NewsFragment.mCurrentPage = position;

			Bundle data = new Bundle();
			data.putInt("current_page", position);
			//
			myFragment.setArguments(data);
		} catch (Exception e) {
			Log.d("newsFragmentAdapter", e.toString());
		}
		return myFragment;
	}

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

}
