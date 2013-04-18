package ss.member.history;

import ss.ComModel.JournalsHistory;
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
public class JHistoryFragment extends Fragment {

	// public static int bookDetailCurrentPage = 0;
	int position = 0;
	private Activity context;

	public JHistoryFragment(Activity context) {
		this.context = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			Bundle data = getArguments();

			position = data.getInt("current_page", 0);
			// bookDetailCurrentPage = position;
		} catch (Exception e) {
			Log.d("CSearchDetailInfoFragment", e.toString());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.jhistory_item_detail, container,
				false);
		bindData(v);
		return v;
	}

	ImageView favorite;

	private void bindData(View v) {
		try {
			JournalsHistory journalHis = JMainHistory.journalHistory
					.get(position);
			// component.showYesNoDialogBox(this, "", position + "");

			TextView author = (TextView) v
					.findViewById(R.id.jhis_detail_tv_author);
			TextView title = (TextView) v
					.findViewById(R.id.jhis_detail_tv_title);
			TextView subject = (TextView) v
					.findViewById(R.id.jhis_detail_tv_subject);
			TextView source = (TextView) v
					.findViewById(R.id.jhis_detail_tv_source);
			TextView startdate = (TextView) v
					.findViewById(R.id.jhis_detail_tv_startdate);
			TextView enddate = (TextView) v
					.findViewById(R.id.jhis_detail_tv_enddate);
			favorite = (ImageView) v.findViewById(R.id.jhis_detail_favorite);

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

			v.findViewById(R.id.jhis_detail_lo_favorite).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							favoriteManage();

						}
					});
		} catch (Exception e) {
			component.showMsg(context, e.toString());
		}
	}

	private void favoriteManage() {

		if (component.isOnline(context)) {
			JournalsHistory journalHis = JMainHistory.journalHistory
					.get(position);
			if (journalHis.getFavorite().equals("0")) {
				if (component.setFavorite(context, journalHis.getId(), "J")) {
					favorite.setImageResource(R.drawable.favorite);
					journalHis.setFavorite("1");
				}
			} else {
				if (component.deleteFavorite(context, journalHis.getId(), "J")) {
					favorite.setImageResource(R.drawable.nonfavorite);
					journalHis.setFavorite("0");
				}
			}
		} else {
			component.showYesNoDialogBox(context, "Warning!",
					"No connect internet.");
		}
	}
}
