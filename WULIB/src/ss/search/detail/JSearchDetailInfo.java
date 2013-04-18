package ss.search.detail;

import ss.ComModel.Journals;
import ss.ComModel.component;
import ss.main.wulib.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class JSearchDetailInfo extends Activity {

	private int position = 0;
	ImageView favorite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.jsearch_detail_info);

		bindData();
	}
	


	private void bindData() {
		position = getIntent().getIntExtra("position", 0);
		Journals journal = component.journals.get(position);
		// component.showYesNoDialogBox(this, "", position+"");
		TextView author = (TextView) findViewById(R.id.jsd_tv_author);
		TextView title = (TextView) findViewById(R.id.jsd_tv_title);
		TextView subject = (TextView) findViewById(R.id.jsd_tv_subject);
		TextView source = (TextView) findViewById(R.id.jsd_tv_source);
		TextView language = (TextView) findViewById(R.id.jsd_tv_source);
		TextView issn = (TextView) findViewById(R.id.jsd_tv_issn);
		TextView status = (TextView) findViewById(R.id.jsd_tv_status);
		favorite = (ImageView) findViewById(R.id.jsd_tv_favorite);

//		component.showMsg(this, journal.getFavorite());
		if (journal.getFavorite().equals("0")) {
			favorite.setImageResource(R.drawable.nonfavorite);
		} else {
			favorite.setImageResource(R.drawable.favorite);
		}
		author.setText(journal.getAuthor());
		title.setText(journal.getTitle());
		subject.setText(journal.getSubject());
		source.setText(journal.getSource());
		language.setText(journal.getLanguage());
		issn.setText(journal.getIssn());
		status.setText(component.getStatusString(journal.getStatus()));
	}

	public void btnClick(View v) {

		switch (v.getId()) {
		case R.id.jsd_tv_lo_favorite:
			favoriteManage();
			break;
		}

	}

	private void favoriteManage() {
		
		if (component.isOnline(this)) {
			Journals journal = component.journals.get(position);
			if (journal.getFavorite().equals("0")) {
				if (component.setFavorite(this, journal.getId(), "J")) {
					favorite.setImageResource(R.drawable.favorite);
					journal.setFavorite("1");
				}
			} else {
				if (component.deleteFavorite(this, journal.getId(), "J")) {
					favorite.setImageResource(R.drawable.nonfavorite);
					journal.setFavorite("0");
				}
			}
		} else {
			component.showYesNoDialogBox(this, "Warning!",
					"No connect internet.");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search_detail_info, menu);
		return true;
	}

}
