package ss.member.borrows;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import ss.ComModel.component;
import ss.main.wulib.R; 
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;

public class MainBorrows extends Activity {

	public String br = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.member_borrows);

	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.btn_scaninfo:
			new LoadBarCodeScanner().execute((Void) null);
			break;
		}
	}

	private boolean scanBarcode() {
		try {
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			intent.putExtra("SCAN_MODE", "BAR_CODE_MODE");
			startActivityForResult(intent, 0);
			return true;
		} catch (Exception e) {
			Log.d("MainBorrows.java error", e.toString());
			// component.showYesNoDialogBox(this, getString(R.string.warning),
			// "Your mobile don't have Barcode Scanner. "
			// + "Please Install Barcode Scanner.");
			//
			// Intent intentToMain = new Intent(getBaseContext(),
			// MainMember.class);
			// startActivity(intentToMain);
		}
		return false;

	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		try {
			if (requestCode == 0) // ทำการตรวจสอบว่า requestCode ตรงกับที่
									// Barcode
									// Scanner คืนค่ามาหรือไม่
			{
				if (resultCode == RESULT_OK) // ถ้า Barcode Scanner ทำงานสมบูรณ์
				{
					String contents = intent.getStringExtra("SCAN_RESULT");
					finditem(contents);// เรียกใช้ฟังก์ชันfinditemเพื่อหาหนังสือในฐานข้อมูลโดยการเรียกใช้
										// Sebservice โดยส่งค่่าตัวเลขบาร์โค้ดไป
					setContentView(R.layout.member_borrows);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			component.showMsg(this, e.toString());
		}
	}

	public void finditem(String br) {// ค้นหาด้วนบาร์โค้ด
		// ทดสอบโชว์Dialog
		// component.showMsg(this, br);
		this.br = br;
		// getFromServer(br);
		if (component.isOnline(this))
			new LoadDataBook().execute((Void) null);
		else
			component.showYesNoDialogBox(this, getString(R.string.warning),
					getString(R.string.nointernet));
	}

	private void getFromServer(String br) {
		try {
			String url = getResources().getString(R.string.SERVERTEST)
					+ "getInfoByBarcode.php";
			List<NameValuePair> ldata = new ArrayList<NameValuePair>();
			ldata.add(new BasicNameValuePair("barcode", br));
			ldata.add(new BasicNameValuePair("username", component.username));
			String data = component.convertStreamToString(this,
					component.connectDBPost(this, url, ldata));

			// component.showYesNoDialogBox(this, "test", data);
			component.books = component.getArrayListBook(data);

			Intent in = new Intent(this, CBorrowDetailInfo.class);
			in.putExtra("position", "0");
			startActivity(in);

		} catch (Exception e) {
			component.showMsg(this, e.toString());
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.member_borrows, menu);
		return true;
	}

	private class LoadDataBook extends AsyncTask<Void, Void, Void> {

		private ProgressDialog dialog = new ProgressDialog(MainBorrows.this);

		protected void onPreExecute() {
			dialog.setMessage("Loading");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			dialog.dismiss();
		}

		@Override
		protected Void doInBackground(Void... params) {
			getFromServer(br);
			return null;
		}

	}

	private class LoadBarCodeScanner extends AsyncTask<Void, Void, Boolean> {

		private ProgressDialog dialog = new ProgressDialog(MainBorrows.this);

		protected void onPreExecute() {
			dialog.setMessage("Loading");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			if (!result)
				component.showYesNoDialogBox(MainBorrows.this,
						getString(R.string.warning),
						"Your mobile don't have Barcode Scanner. "
								+ "Please Install Barcode Scanner.");
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// getFromServer(br)
			return scanBarcode();
		}

	}

}
