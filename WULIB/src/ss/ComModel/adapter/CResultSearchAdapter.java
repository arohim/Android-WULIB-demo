package ss.ComModel.adapter;

import java.util.ArrayList;

import ss.ComModel.Books;
import ss.ComModel.component;
import ss.main.wulib.R;
import ss.newsbook.NewsBookMain;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CResultSearchAdapter extends BaseAdapter {

	ArrayList<Books> books = null;
	protected ListView mListView;
	private static Activity context;
	private LayoutInflater l_Inflater;

	// private int positionItem = 0;

	@SuppressWarnings("static-access")
	public CResultSearchAdapter(Activity context, ArrayList<Books> results) {
		try {

			this.context = context;
			books = results;
			l_Inflater = LayoutInflater.from(context);
			if (context.getClass() == NewsBookMain.class)
				mListView = (ListView) context.findViewById(R.id.news_books_lv);
			else {
				mListView = (ListView) context.findViewById(R.id.sresult_lv);
			}
		} catch (Exception e) {
		}
	}

	@Override
	public int getCount() {
		return books.size();
	}

	@Override
	public Object getItem(int position) {
		return books.get(position);
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
				convertView = l_Inflater.inflate(R.layout.lv_it_csresult, null);
				holder = new ViewHolder();

				holder.title = (TextView) convertView
						.findViewById(R.id.lv_it_rs_title);
				holder.author = (TextView) convertView
						.findViewById(R.id.lv_it_rs_author);
				holder.publisher = (TextView) convertView
						.findViewById(R.id.lv_it_rs_publisher);
				holder.collection = (TextView) convertView
						.findViewById(R.id.lv_it_rs_collection);
				holder.callNumber = (TextView) convertView
						.findViewById(R.id.lv_it_rs_cnumber);
				holder.itemImg = (ImageView) convertView
						.findViewById(R.id.lv_it_rs_cover);

				holder.btnFavorite = (ImageView) convertView
						.findViewById(R.id.lv_it_rs_favorite);

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

			Books book = this.books.get(position);

			holder.title.setText(book.getTitle());
			holder.author.setText(book.getAuthor());
			holder.publisher.setText(book.getPublisher());
			holder.collection.setText(book.getCollection());
			holder.callNumber.setText(book.getCallnumber());
			if (!book.getImgUrl().equals(""))
				component.setCover(holder.itemImg, book.getImgUrl());

			if (Integer.parseInt(book.getFavorite()) == 1) {
				holder.btnFavorite.setImageResource(R.drawable.favorite);
			} else {
				holder.btnFavorite.setImageResource(R.drawable.nonfavorite);
			}

			holder.btnFavorite.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
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
						.findViewById(R.id.lv_it_rs_favorite);
				Books book = component.books.get(position);

				if (book.getFavorite().equals("0")) {
					if (component.setFavorite(context, book.getId(), "C")) {
						fv.setImageResource(R.drawable.favorite);
						book.setFavorite("1");
					}
				} else {
					if (component.deleteFavorite(context, book.getId(), "C")) {
						fv.setImageResource(R.drawable.nonfavorite);
						book.setFavorite("0");
					}
				}
			}
		} catch (Exception e) {
			Log.d("listview  favoriteManage error", e.toString());
		}
	}

	private class ViewHolder {
		ImageView itemImg;
		TextView title;
		TextView author;
		TextView publisher;
		TextView collection;
		TextView callNumber;
		ImageView btnFavorite;
	}

}
