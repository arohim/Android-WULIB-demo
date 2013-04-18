package ss.ComModel.adapter;

import java.util.ArrayList;

import ss.ComModel.NewsModel;
import ss.ComModel.component;
import ss.main.wulib.R;
import ss.news.NewsMain;

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

public class NewsAdapter extends BaseAdapter {

	ArrayList<NewsModel> news = null;
	protected ListView mListView;
	private static Activity context;
	// private static Context context;
	private LayoutInflater l_Inflater;

	// private int positionItem = 0;

	@SuppressWarnings("static-access")
	public NewsAdapter(Activity context, ArrayList<NewsModel> results) {
		try {
			// this.context = context;
			news = results;
			l_Inflater = LayoutInflater.from(context);
			this.context = context;
			mListView = (ListView) context.findViewById(R.id.news_lv);
		} catch (Exception e) {
		}
	}

	@Override
	public int getCount() {
		return news.size();
	}

	@Override
	public Object getItem(int position) {
		return news.get(position);
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
				convertView = l_Inflater.inflate(R.layout.lv_it_news2, null);
				holder = new ViewHolder();

				holder.title = (TextView) convertView
						.findViewById(R.id.lv_it_news_title);
				holder.date = (TextView) convertView
						.findViewById(R.id.lv_it_news_date);
				holder.favorite = (ImageView) convertView
						.findViewById(R.id.lv_it_news_favorite);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			NewsModel n = this.news.get(position);

			if (Integer.parseInt(n.getFavorite()) == 1) {
				holder.favorite.setImageResource(R.drawable.favorite);
			} else {
				holder.favorite.setImageResource(R.drawable.nonfavorite);
			}
			

			holder.favorite.setOnClickListener(favoriteClick);

			holder.title.setText(n.getTitle());
			holder.date.setText(component.getDate(n.getDate()));

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
						.findViewById(R.id.lv_it_news_favorite);
				NewsModel news = NewsMain.news.get(position);

				if (news.getFavorite().equals("0")) {
					if (component.setFavorite(context, news.getId(), "N")) {
						fv.setImageResource(R.drawable.favorite);
						news.setFavorite("1");
					}
				} else {
					if (component.deleteFavorite(context, news.getId(), "N")) {
						fv.setImageResource(R.drawable.nonfavorite);
						news.setFavorite("0");
					}
				}
			}
		} catch (Exception e) {
			Log.d("listview  favoriteManage error", e.toString());
		}
	}

	private class ViewHolder {
		TextView title;
		TextView date;
		ImageView favorite;

	}

}
