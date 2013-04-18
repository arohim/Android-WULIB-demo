package ss.ComModel.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class LoadThread extends AsyncTask<Integer, Void, Void> {
	private ProgressDialog dialog = null;

	public LoadThread(Context context) {
		dialog = new ProgressDialog(context);
	}

	protected void onPreExecute() {
		dialog.setMessage("Loading");
		dialog.setCancelable(false);
		dialog.show();
	}

	@Override
	protected void onPostExecute(Void result) {
		dialog.dismiss();
		super.onPostExecute(result);

	}

	@SuppressWarnings("static-access")
	@Override
	protected Void doInBackground(Integer... time) {
		try {
			new Thread().sleep(time[0]);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}