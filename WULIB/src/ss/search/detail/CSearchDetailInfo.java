package ss.search.detail;

import java.io.IOException;

import ss.ComModel.Books;
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

public class CSearchDetailInfo extends Activity {

	private int position = 0;
	ImageView favorite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.csearch_detail_info);

		bindData();
	}

	private void bindData() {
		try {
			position = getIntent().getIntExtra("position", 0);
			Books book = component.books.get(position);

			TextView sd_tv_title = (TextView) findViewById(R.id.sd_tv_title);
			TextView sd_tv_author = (TextView) findViewById(R.id.sd_tv_author);
			TextView sd_tv_publisher = (TextView) findViewById(R.id.sd_tv_publisher);
			TextView sd_tv_collection = (TextView) findViewById(R.id.sd_tv_collection);
			TextView sd_tv_callnumber = (TextView) findViewById(R.id.sd_tv_callnumber);
			TextView sd_tv_mttype = (TextView) findViewById(R.id.sd_tv_mttype);
			TextView sd_tv_status = (TextView) findViewById(R.id.sd_tv_status);
			TextView sd_tv_subject = (TextView) findViewById(R.id.sd_tv_subject);
			TextView sd_tv_description = (TextView) findViewById(R.id.sd_tv_description);

			favorite = (ImageView) findViewById(R.id.csd_btn_favorite);

			if (!book.getImgUrl().equals("") && !book.getImgUrl().equals(null)) {
				// component.showYesNoDialogBox(this, "", book.getImgUrl());
				ImageView iv = (ImageView) findViewById(R.id.csd_imgcover);
				component.setCover(iv, book.getImgUrl());
			}

			if (book.getFavorite().equals("0")) {
				favorite.setImageResource(R.drawable.nonfavorite);
			} else {
				favorite.setImageResource(R.drawable.favorite);
			}

			sd_tv_author.setText(book.getAuthor());
			sd_tv_title.setText(book.getTitle());
			sd_tv_publisher.setText(book.getPublisher());
			sd_tv_collection.setText(book.getCollection());
			sd_tv_callnumber.setText(book.getCallnumber());
			sd_tv_mttype.setText(book.getMtType());
			sd_tv_status.setText(book.getStatus());
			sd_tv_subject.setText(book.getSubject());
			sd_tv_description.setText(book.getDescription());
		} catch (IOException e) {
			e.printStackTrace();
			component.showMsg(this, e.toString());
		}
	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.csd_btn_lo_favorite:
			favoriteManage();
			break;
		}

	}

	private void favoriteManage() {
		if (component.isOnline(this)) {
			Log.d("test", " on line detail");
			Books book = component.books.get(position);
			if (book.getFavorite().equals("0")) {
				if (component.setFavorite(this, book.getId(), "C")) {
					favorite.setImageResource(R.drawable.favorite);
					book.setFavorite("1");
				}
			} else {
				if (component.deleteFavorite(this, book.getId(), "C")) {
					favorite.setImageResource(R.drawable.nonfavorite);
					book.setFavorite("0");
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
		getMenuInflater().inflate(R.menu.search_detail_info, menu);
		return true;
	}

}
