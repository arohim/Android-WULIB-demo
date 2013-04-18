package ss.member;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import ss.ComModel.component;
import ss.callcenter.CallCenter;
import ss.main.MainActivity;
import ss.main.wulib.R;
import ss.member.books.mt.BooksMtMain;
import ss.member.borrowing.BorrowingMain;
import ss.member.borrows.MainBorrows;
import ss.member.favorite.CMainFavorite;
import ss.member.favorite.JMainFavorite;
import ss.member.favorite.MainFavorite;
import ss.member.history.CMainHistory;
import ss.member.history.JMainHistory;
import ss.member.history.MainHistory;
import ss.member.penalty.PenaltyMain;
import ss.member.renews.MainRenew;
import ss.member.setting.setting;
import ss.news.NewsMain;
import ss.newsbook.NewsBookMain;
import ss.search.SearchSelectType;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class MainMember extends Activity {

	private AlertDialog inflater = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.main_member3);
			component.androidOS();

			// getStatus();
		} catch (Exception e) {
			component.showYesNoDialogBox(this, "", e.toString());
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.btn_memmain_search:
			startActivity(new Intent(this, SearchSelectType.class));
			break;
		case R.id.btn_memmain_borrows:
			// component.showYesNoDialogBox(this, "",
			// fdb.getCountRowByTable("TB_BOOKS") + "");
			goToActivity(MainBorrows.class);
			break;
		case R.id.btn_memmain_renews:
			goToActivity(MainRenew.class);
			break;
		case R.id.btn_memmain_exit:
			goToActivity(MainActivity.class);
			finish();
			break;
		case R.id.btn_memmain_favorite:
			goToActivity(MainFavorite.class);
			// selectFavorite();
			break;
		case R.id.btn_memmain_history:
			goToActivity(MainHistory.class);
			// selectHistory();
			break;
		case R.id.btn_memmain_newsbook:
			goToActivity(NewsBookMain.class);

			break;
		case R.id.btn_memmain_news:
			goToActivity(NewsMain.class);
			break;
		case R.id.btn_sf_catalog:
			inflater.hide();
			goToActivity(CMainFavorite.class);
			break;
		case R.id.btn_sf_journal:
			inflater.hide();
			goToActivity(JMainFavorite.class);
			break;
		case R.id.btn_his_catalog:
			inflater.hide();
			goToActivity(CMainHistory.class);
			break;
		case R.id.btn_his_journal:
			inflater.hide();
			goToActivity(JMainHistory.class);
			break;
		}
	}

	public void statusClick(View v) {
		switch (v.getId()) {
		case R.id.tv_memmain_status_lo_favorite:
			goToActivity(MainFavorite.class);
			break;
		case R.id.tv_memmain_status_lo_panalty:
			goToActivity(PenaltyMain.class);
			break;
		case R.id.tv_memmain_status_lo_borrow:
			goToActivity(BorrowingMain.class);
			break;
		case R.id.tv_memmain_status_lo_setting:
			goToActivity(setting.class);
			// startActivity(new Intent(this, setting.class));
			break;
		case R.id.callcenter:
			goToActivity(CallCenter.class);
			// startActivity(new Intent(this, CallCenter.class));
			break;
		case R.id.tv_memmain_status_lo_books:
			goToActivity(BooksMtMain.class);
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		getStatus();
		// component.showYesNoDialogBox(this, "test", component.username);
	}

	public void getStatus() {
		try {
			String url = getResources().getString(R.string.SERVERTEST)
					+ "getCountOnStatus2.php";

			List<NameValuePair> ldata = new ArrayList<NameValuePair>();
			ldata.add(new BasicNameValuePair("username", component.username));

			String data = component.convertStreamToString(this,
					component.connectDBPost(this, url, ldata));
			JSONObject jo = new JSONArray(data).getJSONObject(0);

			// component.showYesNoDialogBox(this, "panalty count",
			// jo.getString("penalty"));

			if (Integer.parseInt(jo.getString("numfavorite")) > 0) {

				TextView favoriteStatus = (TextView) findViewById(R.id.tv_memmain_status_favorite);
				favoriteStatus.setText(jo.getString("numfavorite"));

				ImageView Sfavorite = (ImageView) findViewById(R.id.tv_memmain_status_symfavorite);
				Sfavorite.setImageResource(R.drawable.favorite);
			}

			if (Integer.parseInt(jo.getString("penalty")) > 0) {

				TextView panaltyStatus = (TextView) findViewById(R.id.tv_memmain_status_panalty);
				panaltyStatus.setText(jo.getString("penalty"));

				ImageView Sfavorite = (ImageView) findViewById(R.id.tv_memmain_status_sympanalty);
				Sfavorite.setImageResource(R.drawable.panalty);
			}

			if (Integer.parseInt(jo.getString("overdue")) > 0
					&& Integer.parseInt(jo.getString("borrowed")) > 0) {

				ImageView Sfavorite = (ImageView) findViewById(R.id.tv_memmain_status_symborrow);
				Sfavorite.setImageResource(R.drawable.borrowshalf);

				TextView borrowStatus = (TextView) findViewById(R.id.tv_memmain_status_borrow);
				borrowStatus.setText(jo.getString("borrowed"));

				TextView overDueStatus = (TextView) findViewById(R.id.tv_memmain_status_overdue);
				overDueStatus.setText(jo.getString("overdue"));

			} else if (Integer.parseInt(jo.getString("overdue")) > 0) {

				ImageView Sfavorite = (ImageView) findViewById(R.id.tv_memmain_status_symborrow);
				Sfavorite.setImageResource(R.drawable.overdue);

				TextView borrowStatus = (TextView) findViewById(R.id.tv_memmain_status_overdue);
				borrowStatus.setText(jo.getString("overdue"));

			} else if (Integer.parseInt(jo.getString("borrowed")) > 0) {

				ImageView Sfavorite = (ImageView) findViewById(R.id.tv_memmain_status_symborrow);
				Sfavorite.setImageResource(R.drawable.borrowed);

				TextView borrowStatus = (TextView) findViewById(R.id.tv_memmain_status_borrow);
				borrowStatus.setText(jo.getString("borrowed"));

			}

			if (Integer.parseInt(jo.getString("books")) > 0) {

				ImageView Bfavorite = (ImageView) findViewById(R.id.tv_memmain_status_symbooks);
				Bfavorite.setImageResource(R.drawable.book);

				TextView booksStatus = (TextView) findViewById(R.id.tv_memmain_status_books);
				booksStatus.setText(jo.getString("books"));

			}
			// component.showYesNoDialogBox(this, "test",
			// jo.getString("books"));
			getNotification(Integer.parseInt(jo.getString("overdue")),
					Integer.parseInt(jo.getString("borrowed")),
					Integer.parseInt(jo.getString("cb")));
		} catch (Exception e) {
			component.showMsg(
					this,
					"error in file MainMember.java in getStatus function "
							+ e.toString());
		}
	}

	private void getNotification(int overdue, int borrowed, int cb) {
		if (component.notificationShow) {
			component.notificationShow = false;
			if (overdue > 0 || borrowed > 0) {
				notificationTransaction(overdue, borrowed, cb);

			}
		}
	}

	private void notificationTransaction(int overdue, int borrowed, int cb) {
		try {

			LayoutInflater lf = LayoutInflater.from(this);
			View v = lf.inflate(R.layout.notification_member, null);
			// LinearLayout rowLink = (LinearLayout)
			// getLayoutInflater().inflate(
			// R.layout.notification_member, null);

			AlertDialog.Builder bl = new AlertDialog.Builder(this);
			// bl.setTitle("Notification");

			bl.setView(v);

			((TextView) v.findViewById(R.id.noti_status_overdue))
					.setText(overdue + "");
			((TextView) v.findViewById(R.id.noti_status_borrowed))
					.setText(borrowed + "");
			((TextView) v.findViewById(R.id.noti_status_cb)).setText(cb + "");
			inflater = bl.create();
			inflater.show();

		} catch (Exception e) {
			component.showMsg(this, e.toString());
		}

	}

	@SuppressWarnings("unused")
	private void selectHistory() {
		try {
			LayoutInflater lf = LayoutInflater.from(this);
			View v = lf.inflate(R.layout.select_history, null);
			AlertDialog.Builder bl = new AlertDialog.Builder(this);
			bl.setView(v);
			inflater = bl.create();
			inflater.show();
		} catch (Exception e) {
			component.showMsg(this, e.toString());
		}

	}

	@SuppressWarnings("unused")
	private void selectFavorite() {
		try {
			LayoutInflater lf = LayoutInflater.from(this);
			View v = lf.inflate(R.layout.select_favorite, null);
			AlertDialog.Builder bl = new AlertDialog.Builder(this);
			bl.setView(v);
			bl.setPositiveButton("OK.", new OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {

				}
			});
			inflater = bl.create();
			inflater.show();
		} catch (Exception e) {
			component.showMsg(this, e.toString());
		}

	}

	public void goToActivity(Class<?> class1) {
		if (component.isOnline(this)) {
			new LoadActivityThread().execute(class1);
		} else
			component.showYesNoDialogBox(this,
					getResources().getString(R.string.warning), getResources()
							.getString(R.string.nointernet));
	}

	private class LoadActivityThread extends AsyncTask<Class<?>, Void, Void> {

		private ProgressDialog dialog = new ProgressDialog(MainMember.this);

		protected void onPreExecute() {
			dialog.setMessage("Loading");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Class<?>... params) {
			startActivity(new Intent(MainMember.this, params[0]));
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			dialog.dismiss();
		}

	}

}
