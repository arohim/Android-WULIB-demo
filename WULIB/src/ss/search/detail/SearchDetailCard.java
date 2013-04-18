package ss.search.detail;

import ss.main.wulib.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;

public class SearchDetailCard extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_detail_card);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_search_detail_card, menu);
		return true;
	}

}
