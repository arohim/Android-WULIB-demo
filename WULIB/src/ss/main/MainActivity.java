package ss.main;

import ss.ComModel.DB.FavoriteDB;
import ss.main.wulib.R;
import ss.member.LoginMember;
import ss.member.borrows.MainBorrows;
import ss.news.NewsMain;
import ss.search.SearchSelectType;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main4);

		FavoriteDB fb = new FavoriteDB(this);
		fb.queryMembers();
		// TextView tv_footer=(TextView)findViewById(R.id.tv_footer);
		// tv_footer.setText("ยินดีต้อนรับเข้าสู่โปรแกรมสำรวจทางภูมิศาสตร์ SurveyTool 1.0 สอบถามเพิ่มเติม tehnnn@gmail.com");
		// tv_footer.setSelected(true);

	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.btn_main_search:
			goToActivity(SearchSelectType.class);
			// startActivity(new Intent(this, SearchSelectType.class));
			break;
		case R.id.btn_main_member:
			goToActivity(LoginMember.class);
			// startActivity(new Intent(this, LoginMember.class));
			break;
		case R.id.btn_main_news:
			goToActivity(NewsMain.class);
			// startActivity(new Intent(this, NewsMain.class));
			break;
		case R.id.callcenter:
			goToActivity(MainBorrows.class);
			// startActivity(new Intent(this, CallCenter.class));
			break;
		case R.id.btn_main_scanBookInfo:
			goToActivity(MainBorrows.class);
			break;
		}
	}

	public void goToActivity(Class<?> class1) {
		new LoadActivityThread().execute(class1);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		// case R.id.main
		}
		return super.onOptionsItemSelected(item);

	}

	private class LoadActivityThread extends AsyncTask<Class<?>, Void, Void> {

		private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

		protected void onPreExecute() {
			dialog.setMessage("Loading");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Class<?>... params) {
			startActivity(new Intent(MainActivity.this, params[0]));
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			dialog.dismiss();
		}

	}
}
