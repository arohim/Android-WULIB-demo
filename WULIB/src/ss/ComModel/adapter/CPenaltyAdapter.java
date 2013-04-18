package ss.ComModel.adapter;

import java.util.ArrayList;

import ss.ComModel.BookPenalty;
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

public class CPenaltyAdapter extends BaseAdapter {

	ArrayList<BookPenalty> booksPenalty = null;
	protected ListView mListView;
	public static Activity context;
	private LayoutInflater l_Inflater;

	// private int positionItem = 0;

	@SuppressWarnings("static-access")
	public CPenaltyAdapter(Activity context, ArrayList<BookPenalty> results) {
		try {
			this.context = context;
			booksPenalty = results;
			l_Inflater = LayoutInflater.from(context);
			mListView = (ListView) context.findViewById(R.id.penalty_lo_lv);
		} catch (Exception e) {
		}
	}

	@Override
	public int getCount() {
		return booksPenalty.size();
	}

	@Override
	public Object getItem(int position) {
		return booksPenalty.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		return super.getItemViewType(position);
	}

	static ViewHolder holder;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		try {
			holder = new ViewHolder();
			if (convertView == null) {
				convertView = l_Inflater.inflate(R.layout.lv_it_cpenalty, null);
				holder = new ViewHolder();

				holder.title = (TextView) convertView
						.findViewById(R.id.lv_it_cpenalty_title);
				holder.callNumber = (TextView) convertView
						.findViewById(R.id.lv_it_cpenalty_cnumber);
				holder.cover = (ImageView) convertView
						.findViewById(R.id.lv_it_cpenalty_cover);
				holder.sDate = (TextView) convertView
						.findViewById(R.id.lv_it_cpenalty_sdate);
				holder.eDate = (TextView) convertView
						.findViewById(R.id.lv_it_cpenalty_edate);
				holder.penalty = (TextView) convertView
						.findViewById(R.id.lv_it_cpenalty_value);
				holder.favorite = (ImageView) convertView
						.findViewById(R.id.lv_it_cpenalty_favorite);
				// holder.btnFavorite.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// holder.btnFavorite
				// .setImageResource(R.drawable.favorite);
				// }
				// });

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			BookPenalty bookPenalty = this.booksPenalty.get(position);

			holder.title.setText(bookPenalty.getTitle());
			holder.callNumber.setText(bookPenalty.getCallnumber());
			holder.sDate.setText(component.getDate(bookPenalty.getStartDate()));
			holder.eDate.setText(component.getDate(bookPenalty.getEndDate()));
			holder.penalty.setText(bookPenalty.getPenalty() + " บาท");

			if (!bookPenalty.getImgUrl().equals(""))
				component.setCover(holder.cover, bookPenalty.getImgUrl());

			if (Integer.parseInt(bookPenalty.getFavorite()) == 1) {
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

				new LoadThread(context).execute(2000);
				favoriteManage(v, position);

			} catch (Exception e) {
				Log.d("listview error", e.toString());
			}

		}
	};

	private void favoriteManage(View v, int position) {
		try {
			if (component.isOnlineWithDialogBox(context)) {

				ImageView fv = (ImageView) v
						.findViewById(R.id.lv_it_cpenalty_favorite);
				BookPenalty bh = booksPenalty.get(position);

				if (bh.getFavorite().equals("0")) {
					if (component.setFavorite(context, bh.getId(), "C")) {
						fv.setImageResource(R.drawable.favorite);
						bh.setFavorite("1");
					}
				} else {
					if (component.deleteFavorite(context, bh.getId(), "C")) {
						fv.setImageResource(R.drawable.nonfavorite);
						bh.setFavorite("0");
					}
				}
			}
		} catch (Exception e) {
			Log.d("listview  favoriteManage error", e.toString());
		}
	}

	private class ViewHolder {
		ImageView cover;
		TextView title;
		TextView callNumber;
		TextView sDate;
		TextView eDate;
		TextView penalty;
		ImageView favorite;

	}

}
