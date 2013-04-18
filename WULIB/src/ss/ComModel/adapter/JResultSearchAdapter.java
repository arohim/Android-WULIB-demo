package ss.ComModel.adapter;

import java.util.ArrayList;

import ss.ComModel.Journals;
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
import android.widget.TextView;

public class JResultSearchAdapter extends BaseAdapter {

	ArrayList<Journals> journals = null;
	private Activity context;
	private LayoutInflater l_Inflater;

	// private int positionItem = 0;

	public JResultSearchAdapter(Activity context, ArrayList<Journals> results) {
		try {
			this.context = context;
			journals = results;
			l_Inflater = LayoutInflater.from(context);
		} catch (Exception e) {
		}
	}

	@Override
	public int getCount() {
		return journals.size();
	}

	@Override
	public Object getItem(int position) {
		return journals.get(position);
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
				convertView = l_Inflater.inflate(R.layout.lv_it_jsresult, null);
				holder = new ViewHolder();

				holder.artcle = (TextView) convertView
						.findViewById(R.id.lv_it_jsr_article);
				holder.author = (TextView) convertView
						.findViewById(R.id.lv_it_jsr_author);
				holder.source = (TextView) convertView
						.findViewById(R.id.lv_it_jsr_source);
				holder.language = (TextView) convertView
						.findViewById(R.id.lv_it_jsr_language);
				holder.favorite = (ImageView) convertView
						.findViewById(R.id.lv_it_jsr_favorite);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Journals j = this.journals.get(position);

			holder.artcle.setText(j.getTitle());
			holder.author.setText(j.getAuthor());
			holder.source.setText(j.getSource());
			holder.language.setText(j.getLanguage());

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

	private void favoriteManage(View v, int position) {
		try {
			if (component.isOnlineWithDialogBox(context)) {

				ImageView fv = (ImageView) v
						.findViewById(R.id.lv_it_jsr_favorite);
				Journals journal = journals.get(position);

				if (journal.getFavorite().equals("0")) {
					if (component.setFavorite(context, journal.getId(), "J")) {
						fv.setImageResource(R.drawable.favorite);
						journal.setFavorite("1");
					}
				} else {
					if (component.deleteFavorite(context, journal.getId(), "J")) {
						fv.setImageResource(R.drawable.nonfavorite);
						journal.setFavorite("0");
					}
				}
			}
		} catch (Exception e) {
			Log.d("listview  favoriteManage error", e.toString());
		}
	}

	private class ViewHolder {
		TextView artcle;
		TextView author;
		TextView source;
		TextView language;
		ImageView favorite;
	}

}
