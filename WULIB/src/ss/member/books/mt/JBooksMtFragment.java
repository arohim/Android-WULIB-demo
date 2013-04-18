package ss.member.books.mt;

import ss.ComModel.Journals;
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
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class JBooksMtFragment extends Fragment {

	// public static int bookDetailCurrentPage = 0;
	int position = 0;
	private Activity context;

	public JBooksMtFragment(Activity context) {
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
		View v = inflater.inflate(R.layout.jbooksmt_detail_info, container,
				false);
		bindData(v);
		return v;
	}

	ImageView favorite;

	private void bindData(View v) {
		try {
			Journals journal = component.journals.get(position);
			// component.showYesNoDialogBox(this, "", position+"");
			TextView author = (TextView) v.findViewById(R.id.jsd_tv_author);
			TextView title = (TextView) v.findViewById(R.id.jsd_tv_title);
			TextView subject = (TextView) v.findViewById(R.id.jsd_tv_subject);
			TextView source = (TextView) v.findViewById(R.id.jsd_tv_source);
			TextView language = (TextView) v.findViewById(R.id.jsd_tv_source);
			TextView issn = (TextView) v.findViewById(R.id.jsd_tv_issn);
			TextView status = (TextView) v.findViewById(R.id.jsd_tv_status);
			TextView booksmt = (TextView) v.findViewById(R.id.jsd_tv_booksmt);
			favorite = (ImageView) v.findViewById(R.id.jsd_tv_favorite);

			// component.showMsg(this, journal.getFavorite());
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
			booksmt.setText(component
					.getDate(journal.getBooksDate().split(" ")[0]));

			LinearLayout btn_favorite = (LinearLayout) v
					.findViewById(R.id.jsd_tv_lo_favorite);
			btn_favorite.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					favoriteManage();
				}
			});

		} catch (Exception e) {
			Log.d("CSearchDetailInfoFragment", e.toString());
		}
	}

	private void favoriteManage() {
		if (component.isOnline(context)) {
			// Log.d("test", " on line detail");
			Journals journal = component.journals.get(position);
			if (journal.getFavorite().equals("0")) {
				if (component.setFavorite(context, journal.getId(), "J")) {
					favorite.setImageResource(R.drawable.favorite);
					journal.setFavorite("1");
				}
			} else {
				if (component.deleteFavorite(context, journal.getId(), "J")) {
					favorite.setImageResource(R.drawable.nonfavorite);
					journal.setFavorite("0");
				}
			}
		} else {
			component.showYesNoDialogBox(context, getString(R.string.warning),
					getString(R.string.nointernet));
		}
	}
}
