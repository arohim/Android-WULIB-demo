package ss.ComModel.adapter;

import java.util.ArrayList;

import ss.ComModel.Journals;
import ss.ComModel.component;
import ss.main.wulib.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class JBooksMtAdapter extends BaseAdapter {

	ArrayList<Journals> journals = null;
	// private static Context context;
	private LayoutInflater l_Inflater;

	// private int positionItem = 0;

	public JBooksMtAdapter(Context context, ArrayList<Journals> results) {
		try {
			// this.context = context;
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
				convertView = l_Inflater.inflate(R.layout.lv_it_jbooksmt, null);
				holder = new ViewHolder();

				holder.artcle = (TextView) convertView
						.findViewById(R.id.lv_it_jsr_article);
				holder.author = (TextView) convertView
						.findViewById(R.id.lv_it_jsr_author);
				holder.source = (TextView) convertView
						.findViewById(R.id.lv_it_jsr_source);
				holder.language = (TextView) convertView
						.findViewById(R.id.lv_it_jsr_language);
				holder.booksDate = (TextView) convertView
						.findViewById(R.id.lv_it_jsr_booksdate);
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
			holder.booksDate.setText(component.getDate(j.getBarcode()
					.split(" ")[0]));

			if (Integer.parseInt(j.getFavorite()) == 1) {
				holder.favorite.setImageResource(R.drawable.favorite);
			} else {
				holder.favorite.setImageResource(R.drawable.nonfavorite);
			}

		} catch (Exception e) {
			e.printStackTrace();
			// Toast.makeText(this,
			// "Error in ResultSearchAdapter.java in getView function " +
			// e.toString(), Toast.LENGTH_LONG).show();
		}
		// positionItem++;
		return convertView;
	}

	private class ViewHolder {
		TextView artcle;
		TextView author;
		TextView source;
		TextView language;
		TextView booksDate;
		ImageView favorite;
	}

}
