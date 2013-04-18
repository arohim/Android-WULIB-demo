package ss.member.renews;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import ss.ComModel.BookHistory;

import ss.ComModel.component;
import ss.extra.notibox.ActionItem;
import ss.extra.notibox.QuickAction;
import ss.extra.notibox.QuickAction.OnActionItemClickListener;
import ss.main.wulib.R;
import ss.member.history.CMainHistory;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class CRenewsItemDetail extends Activity {

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

	QuickAction quickAction;

	private void bindQuickActionBox() {
		ActionItem delete = new ActionItem(1, "Renews", getResources()
				.getDrawable(R.drawable.renews));
		ActionItem books = new ActionItem(2, "Books", getResources()
				.getDrawable(R.drawable.book));
		delete.setSticky(true);

		quickAction = new QuickAction(this, QuickAction.HORIZONTAL);

		quickAction.addActionItem(delete);
		quickAction.addActionItem(books);
		quickAction
				.setOnActionItemClickListener(new OnActionItemClickListener() {

					@Override
					public void onItemClick(QuickAction source, int pos,
							int actionId) {
						if (actionId == 1) {
							dialog("r");
						} else if (actionId == 2) {
							dialog("b");
						}
						quickAction.dismiss();
					}
				});

	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.chis_detail_lo_favorite:
			favoriteManage();
			break;
		case R.id.chis_detail_renews:
			try {
				// renews();
				bindQuickActionBox();
				quickAction.show(v);
			} catch (Exception e) {
				component.showMsg(this, e.toString());
			}
			break;
		}
	}

	private void dialog(final String type) {
		Builder setupAlert;
		setupAlert = new AlertDialog.Builder(this)
				.setTitle(getString(R.string.warning))
				.setMessage("Are you sure?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								try {
									if (type.equals("r")) {
										renews();
									} else if (type.equals("b")) {
										booksMt();
									}
								} catch (Exception e) {
									component.showMsg(CRenewsItemDetail.this,
											e.toString());
								}
							}
						}).setNegativeButton("No", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});

		setupAlert.show();
	}

	private void renews() throws Exception {
		String url = getString(R.string.SERVERTEST) + "renews.php";
		BookHistory bookHis = CMainHistory.bookHistory.get(position);

		List<NameValuePair> ldata = new ArrayList<NameValuePair>();
		ldata.add(new BasicNameValuePair("username", component.username));
		ldata.add(new BasicNameValuePair("book_id", bookHis.getId()));
		ldata.add(new BasicNameValuePair("endDate", bookHis.getEndDate()));
		String data = component.getStringFromServerPost(url, ldata, this);
		// component.showYesNoDialogBox(this, "test", data);

		if (data != null) {

			JSONArray ja = new JSONArray(data);
			JSONObject jo = ja.getJSONObject(0);
			if (jo.getString("renewsStatus").equals("1")) {
				component
						.showYesNoDialogBox(
								this,
								"Information!",
								"Renews ok.\nReturn date : "
										+ jo.getString("newsDate"));
			} else {
				component.showYesNoDialogBox(this, getString(R.string.warning),
						"Something error. renews again please.");
			}

		}

	}

	private void booksMt() throws Exception {
		String url = getString(R.string.SERVERTEST) + "setBooksMt.php";
		BookHistory bookHis = CMainHistory.bookHistory.get(position);
		List<NameValuePair> ldata = new ArrayList<NameValuePair>();
		ldata.add(new BasicNameValuePair("username", component.username));
		ldata.add(new BasicNameValuePair("mt_id", bookHis.getId()));
		ldata.add(new BasicNameValuePair("type", "C"));
		String data = component.getStringFromServerPost(url, ldata, this);
		// component.showYesNoDialogBox(this, "test", data);
		if (data != null) {

			JSONArray ja = new JSONArray(data);
			JSONObject jo = ja.getJSONObject(0);
			if (jo.getString("setStatus").equals("1")) {
				component.showYesNoDialogBox(this, "Information!", "Books ok.");
			} else if (jo.getString("setStatus").equals("2")) {
				component.showYesNoDialogBox(this, "Information!",
						"You have booked.");
			} else {
				component.showYesNoDialogBox(this, getString(R.string.warning),
						"Something error. books again please.");
			}

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
