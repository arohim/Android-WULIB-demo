package ss.member;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import ss.ComModel.component;
import ss.main.wulib.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
@SuppressLint("NewApi")
public class LoginMember extends Activity {
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	// private static final String[] DUMMY_CREDENTIALS = new String[] {
	// "foo@example.com:hello", "bar@example.com:world" };

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mUsername;
	private String mPassword;

	// UI references.
	private EditText mUsernameView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_member);

		component.androidOS();

		// Set up the login form.
		// mUsername = getIntent().getStringExtra(EXTRA_EMAIL);
		mUsernameView = (EditText) findViewById(R.id.email);
		// mUsernameView.setText(mUsername);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		// findViewById(R.id.sign_in_button).setOnClickListener(
		// new View.OnClickListener() {
		// @Override
		// public void onClick(View view) {
		// attemptLogin();
		// }
		// });
	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.sign_in_button:
			if (component.isOnline(this))
				attemptLogin();
			else
				component.showYesNoDialogBox(this, getString(R.string.warning),
						getString(R.string.nointernet));
			// startActivity(new Intent(this, MainMember.class));
			// checkLogin();
			break;
		}
	}

	public boolean checkLogin() {
		try {
			if (component.isOnline(this)) {
				EditText username = (EditText) findViewById(R.id.email);
				EditText password = (EditText) findViewById(R.id.password);
				String url = this.getResources().getString(R.string.SERVERTEST)
						+ "login.php";

				// if (!username.getText().toString().isEmpty()
				// && !password.getText().toString().isEmpty()) {

				List<NameValuePair> ldata = new ArrayList<NameValuePair>();
				ldata.add(new BasicNameValuePair("username", username.getText()
						.toString()));
				ldata.add(new BasicNameValuePair("password", password.getText()
						.toString()));

				JSONArray ja = new JSONArray(component.convertStreamToString(
						this, component.connectDBPost(this, url, ldata)));
				JSONObject jb = ja.getJSONObject(0);
				String status = jb.getString("status");
				if (Integer.parseInt(status) == 1) {
					// component.showMsg(this, "Login pass");
					component.username = username.getText().toString();
					return true;
					// startActivity(new Intent(this, MainMember.class));
				} else {
					component.showMsg(this, "Login fail");

				}
			} else {
				component.showMsg(this, "Enter username and password");
			}
			// } else {
			// component.showYesNoDialogBox(this, "Warning!",
			// "No connect internet.");
			// }
		} catch (Exception e) {
			component.showMsg(this, e.toString());
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login_member, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mUsernameView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mUsername = mUsernameView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mUsername)) {
			mUsernameView.setError(getString(R.string.error_field_required));
			focusView = mUsernameView;
			cancel = true;
		}/*
		 * else if (!mUsername.contains("@")) {
		 * mUsernameView.setError(getString(R.string.error_invalid_email));
		 * focusView = mUsernameView; cancel = true; }
		 */

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);

		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				// Simulate network access.
				// Thread.sleep(2000);
				if (component.isOnline(LoginMember.this))
					return checkLogin();
				else {
					Log.d("Error", "No internet connect.");
					return false;
				}

			} catch (Exception e) {
				return false;
			}

			/*
			 * for (String credential : DUMMY_CREDENTIALS) { String[] pieces =
			 * credential.split(":"); if (pieces[0].equals(mUsername)) { //
			 * Account exists, return true if the password matches. return
			 * pieces[1].equals(mPassword); } }
			 */

			// TODO: register the new account here.
			// return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				// finish();

				component.username = mUsernameView.getText().toString();

				// component.showYesNoDialogBox(LoginMember.this, "test",
				// component.username);
				startActivity(new Intent(LoginMember.this, MainMember.class));
			} else {
				// mPasswordView
				// .setError(getString(R.string.error_incorrect_password));

				mUsernameView.setError(getString(R.string.error_login_fail));
				mUsernameView.requestFocus();
				component.showYesNoDialogBox(LoginMember.this,
						getString(R.string.warning),
						"Some thing wrong,again please.");

			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
