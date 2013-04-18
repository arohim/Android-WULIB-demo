package ss.member.borrows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import ss.ComModel.Books;
import ss.ComModel.component;
import ss.extra.notibox.ActionItem;
import ss.extra.notibox.QuickAction;
import ss.extra.notibox.QuickAction.OnActionItemClickListener;
import ss.main.wulib.R;
import ss.member.LoginMember;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class CBorrowDetailInfo extends Activity {

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
			if (component.books.size() > 0) {
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

				if (!book.getImgUrl().equals("")
						&& !book.getImgUrl().equals(null)) {
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
			} else {
				component.showMsg(this, "Something wrong, again please.");
			}
		} catch (IOException e) {
			e.printStackTrace();
			component.showMsg(this, e.toString());
		}
	}

	QuickAction quickAction;

	private void bindQuickActionBox() {
		ActionItem delete = new ActionItem(1, "Borrow", getResources()
				.getDrawable(R.drawable.borrow_icon));
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

						quickAction.dismiss();
						if (!component.username.equals("")) {
							if (actionId == 1) {
								dialog("br");
							} else if (actionId == 2) {
								dialog("b");
							}
						} else {
							startActivity(new Intent(CBorrowDetailInfo.this,
									LoginMember.class));
						}
					}
				});

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
									if (type.equals("br")) {
										borrow();
									} else if (type.equals("b")) {
										booksMt();
									}
								} catch (Exception e) {
									component.showMsg(CBorrowDetailInfo.this,
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

	private void booksMt() throws Exception {
		String url = getString(R.string.SERVERTEST) + "setBooksMt.php";
		Books book = component.books.get(position);
		List<NameValuePair> ldata = new ArrayList<NameValuePair>();
		ldata.add(new BasicNameValuePair("username", component.username));
		ldata.add(new BasicNameValuePair("mt_id", book.getId()));
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

	private void borrow() throws Exception {
		String url = getString(R.string.SERVERTEST) + "borrows.php";
		List<NameValuePair> ldata = new ArrayList<NameValuePair>();
		ldata.add(new BasicNameValuePair("username", component.username));
		ldata.add(new BasicNameValuePair("book_id", component.books.get(
				position).getId()));
		ldata.add(new BasicNameValuePair("type", "C"));
		String data = component.getStringFromServerPost(url, ldata, this);

		// component.showYesNoDialogBox(this, "test", data);
		if (data != null) {

			JSONArray ja = new JSONArray(data);
			JSONObject jo = ja.getJSONObject(0);
			if (jo.getString("borrowsStatus").equals("1")) {
				component.showYesNoDialogBox(this, "Information!", "Borrowed.");
			} else {
				component.showYesNoDialogBox(this, getString(R.string.warning),
						jo.getString("msg"));
			}

		}

	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.csd_btn_lo_favorite:
			if (!component.username.equals("")) {
				favoriteManage();
			} else {
				startActivity(new Intent(this, LoginMember.class));
			}
			break;
		case R.id.csd_btn_lo_books:
			bindQuickActionBox();
			quickAction.show(v);
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
