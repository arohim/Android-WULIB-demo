package ss.ComModel.adapter;

import java.util.ArrayList;

import ss.ComModel.BookHistory;
import ss.ComModel.component;
import ss.main.wulib.R;
import ss.member.history.CMainHistory;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CHistoryAdapter extends BaseAdapter {

	ArrayList<BookHistory> booksHis = null;
	protected ListView mListView;
	public static Activity context;
	private LayoutInflater l_Inflater;

	// private int positionItem = 0;

	@SuppressWarnings("static-access")
	public CHistoryAdapter(Activity context, ArrayList<BookHistory> results) {
		try {
			this.context = context;
			booksHis = results;
			l_Inflater = LayoutInflater.from(context);
			mListView = (ListView) context.findViewById(R.id.history_lv);
		} catch (Exception e) {
		}
	}

	@Override
	public int getCount() {
		return booksHis.size();
	}

	@Override
	public Object getItem(int position) {
		return booksHis.get(position);
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
				convertView = l_Inflater.inflate(R.layout.lv_it_chis, null);
				holder = new ViewHolder();

				holder.title = (TextView) convertView
						.findViewById(R.id.lv_it_chis_title);
				holder.callNumber = (TextView) convertView
						.findViewById(R.id.lv_it_chis_cnumber);
				holder.cover = (ImageView) convertView
						.findViewById(R.id.lv_it_chis_cover);
				holder.sDate = (TextView) convertView
						.findViewById(R.id.lv_it_chis_sdate);
				holder.eDate = (TextView) convertView
						.findViewById(R.id.lv_it_chis_edate);

				holder.favorite = (ImageView) convertView
						.findViewById(R.id.lv_it_chis_favorite);
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

			BookHistory book = this.booksHis.get(position);

			holder.title.setText(book.getTitle());
			holder.callNumber.setText(book.getCallnumber());
			holder.sDate.setText(component.getDate(book.getStartDate()));
			holder.eDate.setText(component.getDate(book.getEndDate()));

			if (!book.getStatusbor().equals("")) {

				if (book.getStatusbor().equals("2")) { // "\$now >
														// \date";
					holder.title.setTextColor(Color.RED);
				} else if (book.getStatusbor().equals("3")) { // "\$now <
																// \date";
					holder.title.setTextColor(Color.YELLOW);
				}
			}

			if (!book.getImgUrl().equals(""))
				component.setCover(holder.cover, book.getImgUrl());

			if (Integer.parseInt(book.getFavorite()) == 1) {
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
				Log.d("listview error", e.toString());
			}

		}
	};

	private void favoriteManage(View v, int position) {
		try {
			if (component.isOnlineWithDialogBox(context)) {

				ImageView fv = (ImageView) v
						.findViewById(R.id.lv_it_chis_favorite);
				BookHistory bh = CMainHistory.bookHistory.get(position);

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
		ImageView favorite;

	}

}
