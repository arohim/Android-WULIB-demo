package ss.callcenter;

import ss.main.wulib.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CallCenter extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_center);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.call_center, menu);
		return true;
	}

}
