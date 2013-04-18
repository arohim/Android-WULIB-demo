package ss.ComModel.adapter;

import ss.member.penalty.CPenaltyFragment;
import ss.member.penalty.CPenaltyMain;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class CPenaltyFragmentAdapter extends FragmentPagerAdapter {

	int PAGE_COUNT = 10;
	private Activity context;

	public CPenaltyFragmentAdapter(Activity context, FragmentManager fm) {
		super(fm);
		this.context = context;
		this.PAGE_COUNT = CPenaltyMain.bookPenalty.size();
	}

	@Override
	public Fragment getItem(int position) {

		CPenaltyFragment fragment = new CPenaltyFragment(context);
		try {

			Bundle data = new Bundle();
			data.putInt("current_page", position);

			fragment.setArguments(data);
		} catch (Exception e) {
			Log.d("CPenaltyFragmentAdapter", e.toString());
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

}
