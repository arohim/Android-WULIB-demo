package ss.member.history;

import ss.ComModel.JournalsHistory;
import ss.ComModel.component;
import ss.main.wulib.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class JHistoryItemDetail extends Activity {

	private int position = 0;
	private ImageView favorite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.jhistory_item_detail);

		bindData();
	}

	private void bindData() {
		try {

			int position = getIntent().getIntExtra("position", 0);
			JournalsHistory journalHis = JMainHistory.journalHistory
					.get(position);
			// component.showYesNoDialogBox(this, "", position + "");

			TextView author = (TextView) findViewById(R.id.jhis_detail_tv_author);
			TextView title = (TextView) findViewById(R.id.jhis_detail_tv_title);
			TextView subject = (TextView) findViewById(R.id.jhis_detail_tv_subject);
			TextView source = (TextView) findViewById(R.id.jhis_detail_tv_source);
			TextView startdate = (TextView) findViewById(R.id.jhis_detail_tv_startdate);
			TextView enddate = (TextView) findViewById(R.id.jhis_detail_tv_enddate);
			favorite = (ImageView) findViewById(R.id.jhis_detail_favorite);

			// component.showMsg(this, journal.getFavorite());
			if (journalHis.getFavorite().equals("0")) {
				favorite.setImageResource(R.drawable.nonfavorite);
			} else {
				favorite.setImageResource(R.drawable.favorite);
			}
			// component.showYesNoDialogBox(this, "test", "sDate : " +
			// journalHis.getStartDate() + "  eDate" + journalHis.getEndDate());

			author.setText(journalHis.getAuthor());
			title.setText(journalHis.getTitle());
			subject.setText(journalHis.getSubject());
			source.setText(journalHis.getSource());
			startdate.setText(journalHis.getStartDate());
			enddate.setText(journalHis.getEndDate());
		} catch (Exception e) {
			component.showMsg(this, e.toString());
		}
	}

	public void btnClick(View v) {

		switch (v.getId()) {
		case R.id.jhis_detail_lo_favorite:
			favoriteManage();
			break;
		}

	}

	private void favoriteManage() {

		if (component.isOnline(this)) {
			JournalsHistory journalHis = JMainHistory.journalHistory
					.get(position);
			if (journalHis.getFavorite().equals("0")) {
				if (component.setFavorite(this, journalHis.getId(), "J")) {
					favorite.setImageResource(R.drawable.favorite);
					journalHis.setFavorite("1");
				}
			} else {
				if (component.deleteFavorite(this, journalHis.getId(), "J")) {
					favorite.setImageResource(R.drawable.nonfavorite);
					journalHis.setFavorite("0");
				}
			}
		} else {
			component.showYesNoDialogBox(this, "Warning!",
					"No connect internet.");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.history_item_detail, menu);
		return true;
	}

}
