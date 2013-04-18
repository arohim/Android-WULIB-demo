package ss.news;

import ss.ComModel.NewsModel;
import ss.ComModel.component;
import ss.main.wulib.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class NewsFragment extends Fragment {

//	public static int mCurrentPage;
	ImageView favorite;
	int position = 0;
	private Activity context;

	public NewsFragment(Activity context) {
		this.context = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {

			Bundle data = getArguments();

			position = data.getInt("current_page", 0);
//			mCurrentPage = position;
		} catch (Exception e) {
			Log.d("newsFragment", e.toString());
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.news_detail, container, false);

		// TextView tv = (TextView) v.findViewById(R.id.tv);
		// tv.setText("You are viewing the page #" + mCurrentPage + "\n\n"
		// + "Swipe Horizontally left / right");
		bindData(v);
		return v;

	}

	private void bindData(View v) {

		NewsModel news = NewsMain.news.get(position);

		TextView title = (TextView) v.findViewById(R.id.nd_title);
		TextView content = (TextView) v.findViewById(R.id.nd_content);
		TextView date = (TextView) v.findViewById(R.id.nd_date);
		favorite = (ImageView) v.findViewById(R.id.nd_btn_favorite);

		// component.showYesNoDialogBox(this, "", "position = "
		// + getIntent().getStringExtra("position"));
		if (news.getFavorite().equals("0")) {
			favorite.setImageResource(R.drawable.nonfavorite);
		} else {
			favorite.setImageResource(R.drawable.favorite);
		}

		title.setText(news.getTitle());
		content.setText(news.getContent());
		date.setText(component.getDate(news.getDate()));
		favorite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				favoriteManage();

			}
		});

	}

	private void favoriteManage() {
		if (component.isOnline(context)) {
			Log.d("test", " on line detail");
			NewsModel news = NewsMain.news.get(position);
			if (news.getFavorite().equals("0")) {
				if (component.setFavorite(context, news.getId(), "N")) {
					favorite.setImageResource(R.drawable.favorite);
					news.setFavorite("1");
				}
			} else {
				if (component.deleteFavorite(context, news.getId(), "N")) {
					favorite.setImageResource(R.drawable.nonfavorite);
					news.setFavorite("0");
				}
			}
		} else {
			Log.d("NewsFragment", "No connect internet.");
		}
	}
}
