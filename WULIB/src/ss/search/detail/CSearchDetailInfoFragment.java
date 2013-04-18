package ss.search.detail;

import java.io.IOException;

import ss.ComModel.Books;
import ss.ComModel.component;
import ss.main.wulib.R;
import ss.member.LoginMember;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class CSearchDetailInfoFragment extends Fragment {

	// public static int bookDetailCurrentPage = 0;
	int position = 0;
	private Activity context;

	public CSearchDetailInfoFragment(Activity context) {
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
		View v = inflater.inflate(R.layout.csearch_detail_info, container,
				false);
		bindData(v);
		return v;
	}

	ImageView favorite;

	private void bindData(View v) {
		try {

			Books book = component.books.get(position);

			TextView sd_tv_title = (TextView) v.findViewById(R.id.sd_tv_title);
			TextView sd_tv_author = (TextView) v
					.findViewById(R.id.sd_tv_author);
			TextView sd_tv_publisher = (TextView) v
					.findViewById(R.id.sd_tv_publisher);
			TextView sd_tv_collection = (TextView) v
					.findViewById(R.id.sd_tv_collection);
			TextView sd_tv_callnumber = (TextView) v
					.findViewById(R.id.sd_tv_callnumber);
			TextView sd_tv_mttype = (TextView) v
					.findViewById(R.id.sd_tv_mttype);
			TextView sd_tv_status = (TextView) v
					.findViewById(R.id.sd_tv_status);
			TextView sd_tv_subject = (TextView) v
					.findViewById(R.id.sd_tv_subject);
			TextView sd_tv_description = (TextView) v
					.findViewById(R.id.sd_tv_description);

			favorite = (ImageView) v.findViewById(R.id.csd_btn_favorite);

			if (!book.getImgUrl().equals("") && !book.getImgUrl().equals(null)) {
				// component.showYesNoDialogBox(this, "", book.getImgUrl());
				ImageView iv = (ImageView) v.findViewById(R.id.csd_imgcover);
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

			LinearLayout btn_favorite = (LinearLayout) v
					.findViewById(R.id.csd_btn_lo_favorite);
			btn_favorite.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!component.username.equals(""))
						favoriteManage();
					else
						startActivity(new Intent(context, LoginMember.class));
				}
			});

		} catch (IOException e) {
			Log.d("CSearchDetailInfoFragment", e.toString());
		}
	}

	private void favoriteManage() {
		if (component.isOnline(context)) {
			Log.d("test", " on line detail");
			Books books = component.books.get(position);
			if (books.getFavorite().equals("0")) {
				if (component.setFavorite(context, books.getId(), "C")) {
					favorite.setImageResource(R.drawable.favorite);
					books.setFavorite("1");
				}
			} else {
				if (component.deleteFavorite(context, books.getId(), "C")) {
					favorite.setImageResource(R.drawable.nonfavorite);
					books.setFavorite("0");
				}
			}
		} else {
			component.showYesNoDialogBox(context, getString(R.string.warning),
					getString(R.string.nointernet));
		}
	}
}
