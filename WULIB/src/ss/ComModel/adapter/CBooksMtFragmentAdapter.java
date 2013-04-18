package ss.ComModel.adapter;

import ss.ComModel.component;
import ss.member.books.mt.CBooksMtFragment; 
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class CBooksMtFragmentAdapter extends FragmentPagerAdapter {

	int PAGE_COUNT = 10;
	private Activity context;

	public CBooksMtFragmentAdapter(Activity context, FragmentManager fm) {
		super(fm);
		this.context = context;
		this.PAGE_COUNT = component.books.size();
	}

	@Override
	public Fragment getItem(int position) {

		CBooksMtFragment fragment = new CBooksMtFragment(context);
		try {

			Bundle data = new Bundle();
			// CSearchDetailInfoFragment.bookDetailCurrentPage = position;
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
