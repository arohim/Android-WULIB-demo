package ss.member.penalty;

import ss.ComModel.BookPenalty;
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
public class CPenaltyFragment extends Fragment {

	// public static int bookDetailCurrentPage = 0;
	int position = 0;
	private Activity context;

	public CPenaltyFragment(Activity context) {
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
			Log.d("CPenaltyFragment", e.toString());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.cpenalty_item_detail, container,
				false);
		bindData(v);
		return v;
	}

	ImageView favorite;

	private void bindData(View v) {
		BookPenalty bookPenalty = CPenaltyMain.bookPenalty.get(position);

		TextView title = (TextView) v
				.findViewById(R.id.cpenalty_detail_tv_title);
		TextView author = (TextView) v
				.findViewById(R.id.cpenalty_detail_tv_author);
		TextView publisher = (TextView) v
				.findViewById(R.id.cpenalty_detail_tv_publisher);
		TextView collection = (TextView) v
				.findViewById(R.id.cpenalty_detail_tv_collection);
		TextView callNum = (TextView) v
				.findViewById(R.id.cpenalty_detail_tv_callnumber);
		TextView mtType = (TextView) v
				.findViewById(R.id.cpenalty_detail_tv_mttype);
		TextView status = (TextView) v
				.findViewById(R.id.cpenalty_detail_tv_status);
		TextView subject = (TextView) v
				.findViewById(R.id.cpenalty_detail_tv_subject);
		TextView description = (TextView) v
				.findViewById(R.id.cpenalty_detail_tv_description);
		TextView startdate = (TextView) v
				.findViewById(R.id.cpenalty_detail_tv_sdate);
		TextView enddate = (TextView) v
				.findViewById(R.id.cpenalty_detail_tv_edate);
		TextView penalty = (TextView) v
				.findViewById(R.id.cpenalty_detail_tv_penalty);
		favorite = (ImageView) v.findViewById(R.id.cpenalty_detail_favorite);

		if (bookPenalty.getFavorite().equals("0")) {
			favorite.setImageResource(R.drawable.nonfavorite);
		} else {
			favorite.setImageResource(R.drawable.favorite);
		}

		if (!bookPenalty.getImgUrl().equals("")
				&& !bookPenalty.getImgUrl().equals(null)) {
			// component.showYesNoDialogBox(this, "", book.getImgUrl());
			try {
				component.setCover((ImageView) v
						.findViewById(R.id.cpenalty_detail_imgcover),
						bookPenalty.getImgUrl());
			} catch (Exception e) {
				e.printStackTrace();
				component.showMsg(context, e.toString());
			}
		}

		title.setText(bookPenalty.getTitle());
		author.setText(bookPenalty.getAuthor());
		publisher.setText(bookPenalty.getPublisher());
		collection.setText(bookPenalty.getCollection());
		callNum.setText(bookPenalty.getCallnumber());
		mtType.setText(bookPenalty.getMtType());
		status.setText(bookPenalty.getStatus());
		subject.setText(bookPenalty.getSubject());
		description.setText(bookPenalty.getDescription());
		startdate.setText(bookPenalty.getStartDate());
		enddate.setText(bookPenalty.getEndDate());
		penalty.setText(bookPenalty.getPenalty() + " บาท");

		v.findViewById(R.id.cpenalty_detail_lo_favorite).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						favoriteManage();

					}
				});

	}

	private void favoriteManage() {

		if (component.isOnline(context)) {
			BookPenalty bookHis = CPenaltyMain.bookPenalty.get(position);
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
