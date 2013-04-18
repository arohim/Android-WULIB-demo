package ss.ComModel.adapter;

import ss.member.history.CHistoryFragment;
import ss.member.history.CMainHistory;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class CHistoryFragmentAdapter extends FragmentPagerAdapter {

	int PAGE_COUNT = 10;
	private Activity context;

	public CHistoryFragmentAdapter(Activity context, FragmentManager fm) {
		super(fm);
		this.context = context;
		this.PAGE_COUNT = CMainHistory.bookHistory.size();
	}

	@Override
	public Fragment getItem(int position) {

		CHistoryFragment fragment = new CHistoryFragment(context);
		try {

			Bundle data = new Bundle();
			data.putInt("current_page", position);

			fragment.setArguments(data);
		} catch (Exception e) {
			Log.d("newsFragmentAdapter", e.toString());
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

}
