package ss.member.history;


import ss.ComModel.BookHistory;
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
public class CHistoryFragment extends Fragment {

	// public static int bookDetailCurrentPage = 0;
	int position = 0;
	private Activity context;

	public CHistoryFragment(Activity context) {
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
		View v = inflater.inflate(R.layout.chistory_item_detail, container,
				false);
		bindData(v);
		return v;
	}

	ImageView favorite;

	private void bindData(View v) {
		BookHistory bookHis = CMainHistory.bookHistory.get(position);

		TextView title = (TextView) v.findViewById(R.id.chis_detail_tv_title);
		TextView author = (TextView) v.findViewById(R.id.chis_detail_tv_author);
		TextView publisher = (TextView) v
				.findViewById(R.id.chis_detail_tv_publisher);
		TextView collection = (TextView) v
				.findViewById(R.id.chis_detail_tv_collection);
		TextView callNum = (TextView) v
				.findViewById(R.id.chis_detail_tv_callnumber);
		TextView mtType = (TextView) v.findViewById(R.id.chis_detail_tv_mttype);
		TextView status = (TextView) v.findViewById(R.id.chis_detail_tv_status);
		TextView subject = (TextView) v
				.findViewById(R.id.chis_detail_tv_subject);
		TextView description = (TextView) v
				.findViewById(R.id.chis_detail_tv_description);
		TextView startdate = (TextView) v
				.findViewById(R.id.chis_detail_tv_sdate);
		TextView enddate = (TextView) v.findViewById(R.id.chis_detail_tv_edate);
		favorite = (ImageView) v.findViewById(R.id.chis_detail_favorite);

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
						(ImageView) v.findViewById(R.id.chis_detail_imgcover),
						bookHis.getImgUrl());
			} catch (Exception e) {
				e.printStackTrace();
				component.showMsg(context, e.toString());
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

		v.findViewById(R.id.chis_detail_lo_favorite).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						favoriteManage();

					}
				});

	}

	private void favoriteManage() {

		if (component.isOnline(context)) {
			BookHistory bookHis = CMainHistory.bookHistory.get(position);
			if (bookHis.getFavorite().equals("0")) {
				if (component.setFavorite(context, bookHis.getId(), "C")) {
					favorite.setImageResource(R.drawable.favorite);
					bookHis.setFavorite("1");
				}
			} else {
				if (component.deleteFavorite(context, bookHis.getId(), "C")) {
					favorite.setImageResource(R.drawable.nonfavorite);
					bookHis.setFavorite("0");
				}
			}
		} else {
			component.showYesNoDialogBox(context, "Warning!",
					"No connect internet.");
		}
	}
}
