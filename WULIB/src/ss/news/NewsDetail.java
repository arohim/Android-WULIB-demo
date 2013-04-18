package ss.news;

import ss.ComModel.NewsModel;
import ss.ComModel.component;
import ss.main.wulib.R;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsDetail extends Activity {

	private int position = 0;
	ImageView favorite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_detail);
		bindData();
	}

	private void bindData() {
		position = Integer.parseInt(getIntent().getStringExtra("position"));
		NewsModel news = NewsMain.news.get(position);

		TextView title = (TextView) findViewById(R.id.nd_title);
		TextView content = (TextView) findViewById(R.id.nd_content);
		TextView date = (TextView) findViewById(R.id.nd_date);
		favorite = (ImageView) findViewById(R.id.nd_btn_favorite);

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

	}

	public void btnClick(View v) {

		switch (v.getId()) {
		case R.id.nd_btn_favorite:
			favoriteManage();
			break;
		}

	}

	private void favoriteManage() {
		if (component.isOnline(this)) {
			Log.d("test", " on line detail");
			NewsModel news = NewsMain.news.get(position);
			if (news.getFavorite().equals("0")) {
				if (component.setFavorite(this, news.getId(), "N")) {
					favorite.setImageResource(R.drawable.favorite);
					news.setFavorite("1");
				}
			} else {
				if (component.deleteFavorite(this, news.getId(), "N")) {
					favorite.setImageResource(R.drawable.nonfavorite);
					news.setFavorite("0");
				}
			}
		} else {
			component.showYesNoDialogBox(this, "Warning!",
					"No connect internet.");

			// Log.d("test", "not on line detail");
			// FavoriteDB fdb = new FavoriteDB(this);
			// Books book = component.books.get(position);
			// if (book.getFavorite().equals("0")) {
			// if (fdb.setFavoriteBook(book.getId(), "C")) {
			// favorite.setImageResource(R.drawable.favorite);
			// book.setFavorite("1");
			// }
			// } else {
			// if (fdb.deleteFavoriteBook(book.getId(), "C")) {
			// favorite.setImageResource(R.drawable.nonfavorite);
			// book.setFavorite("0");
			// }
			// }

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news_detail, menu);
		return true;
	}

}
