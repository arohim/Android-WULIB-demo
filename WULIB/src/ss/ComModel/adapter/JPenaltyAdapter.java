package ss.ComModel.adapter;

import java.util.ArrayList;

import ss.ComModel.JournalPenalty;
import ss.ComModel.component;
import ss.main.wulib.R;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class JPenaltyAdapter extends BaseAdapter {

	ArrayList<JournalPenalty> jPen = null;
	protected ListView mListView;
	private static Activity context;
	private LayoutInflater l_Inflater;

	// private int positionItem = 0;

	@SuppressWarnings("static-access")
	public JPenaltyAdapter(Activity context, ArrayList<JournalPenalty> results) {
		try {
			this.context = context;
			jPen = results;
			l_Inflater = LayoutInflater.from(context);
			mListView = (ListView) context.findViewById(R.id.penalty_lo_lv);
		} catch (Exception e) {
		}
	}

	@Override
	public int getCount() {
		return jPen.size();
	}

	@Override
	public Object getItem(int position) {
		return jPen.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		return super.getItemViewType(position);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		try {
			ViewHolder holder = new ViewHolder();
			if (convertView == null) {
				convertView = l_Inflater.inflate(R.layout.lv_it_jpenalty, null);
				holder = new ViewHolder();

				holder.title = (TextView) convertView
						.findViewById(R.id.lv_it_jpenalty_title);
				holder.sDate = (TextView) convertView
						.findViewById(R.id.lv_it_jpenalty_sdate);
				holder.eDate = (TextView) convertView
						.findViewById(R.id.lv_it_jpenalty_edate);
				holder.favorite = (ImageView) convertView
						.findViewById(R.id.lv_it_jpenalty_favorite);
				holder.penalty = (TextView) convertView
						.findViewById(R.id.lv_it_jpenalty_value);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			JournalPenalty j = this.jPen.get(position);

			holder.title.setText(j.getTitle());
			holder.sDate.setText(component.getDate(j.getStartDate()));
			holder.eDate.setText(component.getDate(j.getEndDate()));
			holder.penalty.setText(j.getPenalty());

			if (Integer.parseInt(j.getFavorite()) == 1) {
				holder.favorite.setImageResource(R.drawable.favorite);
			} else {
				holder.favorite.setImageResource(R.drawable.nonfavorite);
			}

			holder.favorite.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new LoadThread(context).execute(2000);
					favoriteManage(v, position);

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
			// Toast.makeText(this,
			// "Error in ResultSearchAdapter.java in getView function " +
			// e.toString(), Toast.LENGTH_LONG).show();
		}
		// positionItem++;
		return convertView;
	}

	public OnClickListener favoriteClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			final int position = mListView.getPositionForView((View) v
					.getParent());

			Log.d("listview", "Title clicked, row " + position);
			try {
				// ImageView fv = (ImageView) v
				// .findViewById(R.id.lv_it_news_favorite);
				// //
				// fv.setImageResource(R.drawable.nonfavorite);

				new LoadThread(context).execute(2000);
				favoriteManage(v, position);

			} catch (Exception e) {
				Log.d("JHistoryAdapter favoriteClick onclick error",
						e.toString());
			}

		}
	};

	private void favoriteManage(View v, int position) {
		try {
			// Log.d("JHistoryAdapter  favoriteManage error",
			// JMainHistory.journalHistory.size() + "");

			if (component.isOnlineWithDialogBox(context)) {

				ImageView fv = (ImageView) v
						.findViewById(R.id.lv_it_jpenalty_favorite);
				JournalPenalty jh = jPen.get(position);

				if (jh.getFavorite().equals("0")) {
					if (component.setFavorite(context, jh.getId(), "J")) {
						fv.setImageResource(R.drawable.favorite);
						jh.setFavorite("1");
					}
				} else {
					if (component.deleteFavorite(context, jh.getId(), "J")) {
						fv.setImageResource(R.drawable.nonfavorite);
						jh.setFavorite("0");
					}
				}
			}
		} catch (Exception e) {
			Log.d("JPenaltyAdapter  favoriteManage error", e.toString());
		}
	}

	private class ViewHolder {
		TextView title;
		TextView sDate;
		TextView eDate;
		TextView penalty;
		ImageView favorite;
	}

}
