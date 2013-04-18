package ss.member.history;

import ss.ComModel.BookHistory;

import ss.ComModel.component;
import ss.main.wulib.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class CHistoryItemDetail extends Activity {

	private int position = 0;
	private ImageView favorite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chistory_item_detail);

		bindData();
	}

	private void bindData() {
		position = getIntent().getIntExtra("position", 0);
		BookHistory bookHis = CMainHistory.bookHistory.get(position);
		// component.showYesNoDialogBox(this, "", position + "");

		TextView title = (TextView) findViewById(R.id.chis_detail_tv_title);
		TextView author = (TextView) findViewById(R.id.chis_detail_tv_author);
		TextView publisher = (TextView) findViewById(R.id.chis_detail_tv_publisher);
		TextView collection = (TextView) findViewById(R.id.chis_detail_tv_collection);
		TextView callNum = (TextView) findViewById(R.id.chis_detail_tv_callnumber);
		TextView mtType = (TextView) findViewById(R.id.chis_detail_tv_mttype);
		TextView status = (TextView) findViewById(R.id.chis_detail_tv_status);
		TextView subject = (TextView) findViewById(R.id.chis_detail_tv_subject);
		TextView description = (TextView) findViewById(R.id.chis_detail_tv_description);
		TextView startdate = (TextView) findViewById(R.id.chis_detail_tv_sdate);
		TextView enddate = (TextView) findViewById(R.id.chis_detail_tv_edate);
		favorite = (ImageView) findViewById(R.id.chis_detail_favorite);

		if (bookHis.getFavorite().equals("0")) {
			favorite.setImageResource(R.drawable.nonfavorite);
		} else {
			favorite.setImageResource(R.drawable.favorite);
		}

		if (!bookHis.getImgUrl().equals("")
				&& !bookHis.getImgUrl().equals(null)) {
			// component.showYesNoDialogBox(this, "", book.getImgUrl());
			try {
				component.setCover(
						(ImageView) findViewById(R.id.chis_detail_imgcover),
						bookHis.getImgUrl());
			} catch (Exception e) {
				e.printStackTrace();
				component.showMsg(this, e.toString());
			}
		}

		title.setText(bookHis.getTitle());
		author.setText(bookHis.getAuthor());
		publisher.setText(bookHis.getPublisher());
		collection.setText(bookHis.getCollection());
		callNum.setText(bookHis.getCallnumber());
		mtType.setText(bookHis.getMtType());
		status.setText(bookHis.getStatus());
		subject.setText(bookHis.getSubject());
		description.setText(bookHis.getDescription());
		startdate.setText(bookHis.getStartDate());
		enddate.setText(bookHis.getEndDate());

	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.chis_detail_lo_favorite:
			favoriteManage();
			break;
		}
	}

	private void favoriteManage() {

		if (component.isOnline(this)) {
			BookHistory bookHis = CMainHistory.bookHistory.get(position);
			if (bookHis.getFavorite().equals("0")) {
				if (component.setFavorite(this, bookHis.getId(), "C")) {
					favorite.setImageResource(R.drawable.favorite);
					bookHis.setFavorite("1");
				}
			} else {
				if (component.deleteFavorite(this, bookHis.getId(), "C")) {
					favorite.setImageResource(R.drawable.nonfavorite);
					bookHis.setFavorite("0");
				}
			}
		} else {
			component.showYesNoDialogBox(this, "Warning!",
					"No connect internet.");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history_item_detail, menu);
		return true;
	}

}
