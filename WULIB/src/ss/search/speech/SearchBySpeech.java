package ss.search.speech;

import java.util.ArrayList;

import ss.ComModel.component;
import ss.main.wulib.R;
import ss.search.catalog.CSearchByKeyword;
import ss.search.journal.JMainSearch;
import ss.search.result.CSearchResult;
import ss.search.result.JSearchResult;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class SearchBySpeech extends Activity {

	protected static final int RESULT_SPEECH = 1;
	private EditText etResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_by_speech);

		etResult = (EditText) findViewById(R.id.et_sp_resultRec);
	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sp_record:
			recordSpeech();
			break;
		case R.id.btn_sp_search:
			String activity = getIntent().getStringExtra("for");
			EditText searchFor = (EditText) findViewById(R.id.et_sp_resultRec);
			if (activity.equals("Catalog")) {

				Intent in = new Intent(this, CSearchResult.class);

				in.putExtra("className", CSearchResult.class + "");
				in.putExtra("searchfor1", searchFor.getText().toString());
				in.putExtra("in1", "Title");
				startActivity(in);
			} else {
				Intent in = new Intent(this, JSearchResult.class);
				in.putExtra("className", CSearchResult.class + "");
				in.putExtra("searchfor1", searchFor.getText().toString());
				in.putExtra("in1", "Title");
				startActivity(in);
			}
			break;
		default:
			break;
		}
	}

	private void recordSpeech() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
		try {
			startActivityForResult(intent, RESULT_SPEECH);
			etResult.setText("");
		} catch (ActivityNotFoundException a) {
			showDialog("Warning!",
					"Your device doesn't support Speech to Text, Are you go to standard search?");

		}

	}

	public void showDialog(String title, String msg) {
		try {
			AlertDialog.Builder dia = new AlertDialog.Builder(this).setTitle(
					title).setMessage(msg);
			dia.setPositiveButton("Yes", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					SearchBySpeech.this.startActivity(new Intent(
							SearchBySpeech.this, JMainSearch.class));
				}
			});
			dia.setNegativeButton("Cancel", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});

			dia.create().show();
		} catch (Exception e) {
			component.showYesNoDialogBox(this, "Sorry!", e.toString());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SPEECH: {
			if (resultCode == RESULT_OK && null != data) {
				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				etResult.setText(text.get(0));
				// Button search = (Button) findViewById(R.id.btn_sp_search);
				// search.setEnabled(true);
			}
			break;
		}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.search_by_speech, menu);
		return true;
	}

}
