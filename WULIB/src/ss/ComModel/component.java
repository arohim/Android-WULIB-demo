package ss.ComModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import ss.main.wulib.R;
import ss.member.history.CMainHistory;
import ss.member.history.JMainHistory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressLint({ "NewApi", "Registered", "DefaultLocale" })
public class component extends Activity {

	public static String username = "";
	public static boolean notificationShow = true;
	public static ArrayList<Books> books = null;
	public static ArrayList<Journals> journals = null;

	public static void showMsg(Activity act, String str) {
		Toast.makeText(act, str, Toast.LENGTH_LONG).show();
	}

	public static String[] getYear(int count) {
		final Calendar c = Calendar.getInstance();
		int currentYear = c.get(Calendar.YEAR);
		String year[] = new String[count];
		year[0] = "ALL";
		year[1] = currentYear + "";
		currentYear++;
		for (int i = 2; i < count; i++) {
			year[i] = (currentYear - i) + "";
		}

		return year;
	}

	public static String convertFormatMoney(String money) {
		DecimalFormat df = new DecimalFormat("000,000");
		return df.format(Double.valueOf(money));
	}

	public static String getDate(String date) {
		String[] dSplit = date.split("-");
		String strDate = dSplit[2];

		if (dSplit[1].equals("01")) {
			strDate += " ม.ค. ";
		} else if (dSplit[1].equals("02")) {
			strDate += " ก.พ. ";
		} else if (dSplit[1].equals("03")) {
			strDate += " มี.ค. ";
		} else if (dSplit[1].equals("04")) {
			strDate += " เม.ย. ";
		} else if (dSplit[1].equals("05")) {
			strDate += " พ.ค. ";
		} else if (dSplit[1].equals("06")) {
			strDate += " มิ.ย. ";
		} else if (dSplit[1].equals("07")) {
			strDate += " ก.ค. ";
		} else if (dSplit[1].equals("08")) {
			strDate += " ส.ค. ";
		} else if (dSplit[1].equals("09")) {
			strDate += " ก.ย. ";
		} else if (dSplit[1].equals("10")) {
			strDate += " ต.ค. ";
		} else if (dSplit[1].equals("11")) {
			strDate += " พ.ย. ";
		} else if (dSplit[1].equals("12")) {
			strDate += " ธ.ค. ";
		}
		strDate += dSplit[0].substring(2, 4);
		return strDate;
	}

	public static void showInplater(Activity act, int selectHistory) {
		LayoutInflater lf = LayoutInflater.from(act);
		View v = lf.inflate(selectHistory, null);
		AlertDialog.Builder bl = new AlertDialog.Builder(act);
		bl.setView(v);
		bl.create().show();
	}

	public static void setCover(ImageView iv, String urlStr) throws IOException {
		URL url = new URL(urlStr);
		Bitmap bmp = BitmapFactory.decodeStream(url.openConnection()
				.getInputStream());
		iv.setImageBitmap(bmp);
	}

	@SuppressLint("NewApi")
	public static void androidOS() {
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	}

	@SuppressLint("DefaultLocale")
	public static String getSearchIn(String searchIn) {
		if (searchIn.equals("Article"))
			searchIn = "KEYWORD";
		else
			searchIn = searchIn.toUpperCase();
		return searchIn;
	}

	public static String[] getTitleNews(ArrayList<NewsModel> news) {
		String[] News = new String[news.size()];
		for (int i = 0; i < news.size(); i++) {
			News[i] = news.get(i).getTitle();
		}
		return News;
	}

	public static String[] getTitleBooks(ArrayList<Books> books) {
		String[] Books = new String[books.size()];
		for (int i = 0; i < books.size(); i++) {
			Books[i] = books.get(i).getTitle();
		}
		return Books;
	}

	public static String[] getTitleJournals(ArrayList<Journals> journals) {
		String[] Books = new String[journals.size()];
		for (int i = 0; i < journals.size(); i++) {
			Books[i] = journals.get(i).getTitle();
		}
		return Books;
	}

	public static String[] getTitleJournalsHistory(
			ArrayList<JournalsHistory> journals) {
		String[] Books = new String[journals.size()];
		for (int i = 0; i < journals.size(); i++) {
			Books[i] = journals.get(i).getTitle();
		}
		return Books;
	}

	public static String[] getTitleBooksHistory(ArrayList<BookHistory> books) {
		String[] Books = new String[books.size()];
		for (int i = 0; i < books.size(); i++) {
			Books[i] = books.get(i).getTitle();
		}
		return Books;
	}

	public static ArrayList<Books> getSearchKey(Activity actClass, String url,
			List<NameValuePair> ldata) {
		// component.showYesNoDialogBox(actClass, "", ldata.toString());
		try {
			HttpResponse res = connectDBPost(actClass, url, ldata);
			int resCode = res.getStatusLine().getStatusCode();
			if (resCode == 200) {
				String data = convertStreamToString(actClass, res);
				// showYesNoDialogBox(actClass, "", data);

				return getArrayListBook(data);

			} else if (resCode == 404) {
				showMsg(actClass, "File not found");
			} else if (resCode == 500) {
				showMsg(actClass, "Compile error");
			}

		} catch (Exception e) {
			showYesNoDialogBox(actClass, "", e.toString());
		}
		return null;
	}

	public static ArrayList<BookHistory> getBookHistoryArrayList(
			Activity actClass, String url, List<NameValuePair> ldata) {

		try {
			HttpResponse res = connectDBPost(actClass, url, ldata);
			int resCode = res.getStatusLine().getStatusCode();
			if (resCode == 200) {
				String data = convertStreamToString(actClass, res);
				// showYesNoDialogBox(actClass,"", data);

				return getArrayListBookHistory(data);

			} else if (resCode == 404) {
				showMsg(actClass, "File not found");
			} else if (resCode == 500) {
				showMsg(actClass, "Compile error");
			}

		} catch (Exception e) {
			showYesNoDialogBox(actClass, "", e.toString());
		}
		return null;
	}

	public static ArrayList<BookHistory> getArrayListBookHistory(String data)
			throws JSONException {
		ArrayList<BookHistory> booksData = new ArrayList<BookHistory>();
		
		JSONArray ja = new JSONArray(data);
		
		
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			BookHistory b = new BookHistory();

			b.setId(jo.getString("id"));
			b.setTitle(jo.getString("title"));
			b.setAuthor(jo.getString("author"));
			b.setPublisher(jo.getString("publisher"));
			b.setCollection(jo.getString("collection"));
			b.setCallnumber(jo.getString("callnumber"));
			b.setMtType(jo.getString("mtType"));
			b.setStatus(getStatusString(jo.getString("status")));
			b.setSubject(jo.getString("subject"));
			b.setImgUrl(jo.getString("imgUrl"));
			b.setDescription(jo.getString("description"));
			b.setStartDate(jo.getString("startDate"));
			b.setEndDate(jo.getString("endDate"));
			b.setFavorite(jo.getString("favorite"));

			booksData.add(b);
		}
		return booksData;
	}

	public static ArrayList<Books> getArrayListBook(String data)
			throws JSONException {
		ArrayList<Books> booksData = new ArrayList<Books>();
		JSONArray ja = new JSONArray(data);
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			Books b = new Books();
			b.setId(jo.getString("id"));
			b.setTitle(jo.getString("title"));
			b.setAuthor(jo.getString("author"));
			b.setPublisher(jo.getString("publisher"));
			b.setCollection(jo.getString("collection"));
			b.setCallnumber(jo.getString("callnumber"));
			b.setImgUrl(jo.getString("imgUrl"));
			b.setMtType(jo.getString("mtType"));
			b.setStatus(getStatusString(jo.getString("status")));
			b.setSubject(jo.getString("subject"));
			b.setDescription(jo.getString("description"));
			b.setFavorite(jo.getString("favorite"));
			booksData.add(b);
			// showMsg(actClass,
			// jo.getString("id") + "  " + jo.getString("subject")
			// + "  " + jo.getString("title") + "  "
			// + jo.getString("source") + "  "
			// + jo.getString("author") + "  "
			// + jo.getString("date") + "  "
			// + jo.getString("group") + "  "
			// + jo.getString("language"));
		}
		return booksData;
	}

	public static ArrayList<Journals> getJournalArrayList(Activity actClass,
			String url, List<NameValuePair> ldata) {
		// component.showYesNoDialogBox(actClass, "", ldata.toString());
		try {
			HttpResponse res = connectDBPost(actClass, url, ldata);
			int resCode = res.getStatusLine().getStatusCode();
			if (resCode == 200) {
				String data = convertStreamToString(actClass, res);
				// showYesNoDialogBox(actClass, "", data);

				return getArrayListJournals(data);

			} else if (resCode == 404) {
				showMsg(actClass, "File not found");
			} else if (resCode == 500) {
				showMsg(actClass, "Compile error");
			}

		} catch (Exception e) {
			showYesNoDialogBox(actClass, "", e.toString());
		}
		return null;
	}

	public static ArrayList<Journals> getArrayListJournals(String data)
			throws JSONException {
		ArrayList<Journals> booksData = new ArrayList<Journals>();
		JSONArray ja = new JSONArray(data);
		for (int i = 0; i < ja.length(); i++) {

			JSONObject jo = ja.getJSONObject(i);
			Journals b = new Journals();

			b.setId(jo.getString("id"));
			b.setTitle(jo.getString("title"));
			b.setAuthor(jo.getString("author"));
			b.setStatus(getStatusString(jo.getString("status")));
			b.setSubject(jo.getString("subject"));
			b.setSource(jo.getString("source"));
			b.setLanguage(jo.getString("language"));
			b.setIssn(jo.getString("issn"));
			b.setStatus(jo.getString("status"));
			b.setFavorite(jo.getString("favorite"));
			booksData.add(b);
		}
		return booksData;
	}

	public static ArrayList<JournalsHistory> getJournalHistoryArrayList(
			Activity actClass, String url, List<NameValuePair> ldata) {
		// component.showYesNoDialogBox(actClass, "", ldata.toString());
		try {
			HttpResponse res = connectDBPost(actClass, url, ldata);
			int resCode = res.getStatusLine().getStatusCode();
			if (resCode == 200) {
				String data = convertStreamToString(actClass, res);
				// showYesNoDialogBox(actClass,"", data);

				return getArrayListJournalsHistory(data);

			} else if (resCode == 404) {
				showMsg(actClass, "File not found");
			} else if (resCode == 500) {
				showMsg(actClass, "Compile error");
			}

		} catch (Exception e) {
			showYesNoDialogBox(actClass, "", e.toString());
		}
		return null;
	}

	public static ArrayList<JournalsHistory> getArrayListJournalsHistory(
			String data) throws JSONException {
		ArrayList<JournalsHistory> booksData = new ArrayList<JournalsHistory>();
		JSONArray ja = new JSONArray(data);
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			JournalsHistory jHis = new JournalsHistory();
			jHis.setId(jo.getString("id"));
			jHis.setTitle(jo.getString("title"));
			jHis.setAuthor(jo.getString("author"));
			jHis.setStatus(getStatusString(jo.getString("status")));
			jHis.setSubject(jo.getString("subject"));
			jHis.setSource(jo.getString("source"));
			jHis.setLanguage(jo.getString("language"));
			jHis.setIssn(jo.getString("issn"));
			jHis.setStatus(jo.getString("status"));
			jHis.setStartDate(jo.getString("startDate"));
			jHis.setEndDate(jo.getString("endDate"));
			jHis.setFavorite(jo.getString("favorite"));
			booksData.add(jHis);
		}
		return booksData;
	}

	public static ArrayList<NewsModel> getNewsArrayList(Activity actClass,
			String url, List<NameValuePair> ldata) {
		// component.showYesNoDialogBox(actClass, "", ldata.toString());
		try {
			HttpResponse res = connectDBPost(actClass, url, ldata);
			int resCode = res.getStatusLine().getStatusCode();
			if (resCode == 200) {
				String data = convertStreamToString(actClass, res);
				// showYesNoDialogBox(actClass, "", data);

				return getArrayListNews(data);

			} else if (resCode == 404) {
				showMsg(actClass, "File not found");
			} else if (resCode == 500) {
				showMsg(actClass, "Compile error");
			}

		} catch (Exception e) {
			showYesNoDialogBox(actClass, "", e.toString());
		}
		return null;
	}

	public static ArrayList<NewsModel> getNewsFavoriteArrayList(
			Activity actClass, String url, List<NameValuePair> ldata) {

		try {
			HttpResponse res = connectDBPost(actClass, url, ldata);
			int resCode = res.getStatusLine().getStatusCode();
			if (resCode == 200) {
				String data = convertStreamToString(actClass, res);
				// showYesNoDialogBox(actClass, "", data);

				return getArrayListNews(data);

			} else if (resCode == 404) {
				showMsg(actClass, "File not found");
			} else if (resCode == 500) {
				showMsg(actClass, "Compile error");
			}

		} catch (Exception e) {
			showYesNoDialogBox(actClass, "", e.toString());
		}
		return null;
	}

	public static ArrayList<NewsModel> getArrayListNews(String data)
			throws JSONException {
		ArrayList<NewsModel> arrListNews = new ArrayList<NewsModel>();
		JSONArray ja = new JSONArray(data);
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			NewsModel news = new NewsModel();
			news.setId(jo.getString("news_id"));
			news.setTitle(jo.getString("news_title"));
			news.setContent(jo.getString("news_content"));
			news.setDate(jo.getString("news_date"));
			news.setFavorite(jo.getString("favorite"));

			arrListNews.add(news);
		}
		return arrListNews;
	}

	public static void showProgress(final boolean show, View loading,
			View listview) {
		listview.setVisibility(show ? View.GONE : View.VISIBLE);
		loading.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	public static String getStatusString(String status) {
		if (status.equals("1")) {
			return "Available";
		} else {
			return "Not Available";
		}
	}

	public static HttpResponse connectDBPost(Activity actClass, String url,
			List<NameValuePair> ldata) {
		try {
			HttpClient hc = new DefaultHttpClient();
			HttpPost hp = new HttpPost(url);
			hp.setEntity(new UrlEncodedFormEntity(ldata));
			return hc.execute(hp);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			showMsg(actClass, e.toString()
					+ "  in service.java in connectDBPost function ");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			showMsg(actClass, e.toString()
					+ "  in service.java in connectDBPost function ");
		} catch (IOException e) {
			e.printStackTrace();
			showMsg(actClass, e.toString()
					+ "  in service.java in connectDBPost function ");
		}
		return null;
	}

	public static HttpResponse connectDBGet(Activity actClass, String url) {
		try {
			return new DefaultHttpClient().execute(new HttpGet(url));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			showMsg(actClass, e.toString()
					+ "  in service.java in connectDBGet function ");
		} catch (IOException e) {
			e.printStackTrace();
			showMsg(actClass, e.toString()
					+ "  in service.java in connectDBGet function ");
		}
		return null;
	}

	public static SoapObject soapConnect(Activity act, SoapObject request,
			String SOAP_ACTION) throws IOException, XmlPullParserException {
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE ht = new HttpTransportSE(act.getResources().getString(
				R.string.URL));

		ht.call(SOAP_ACTION, envelope);
		SoapObject result = (SoapObject) envelope.bodyIn;
		return result;
	}

	public static Spinner spinner(Activity act, Spinner spin, String[] arr) {
		ArrayAdapter<String> arrAd = new ArrayAdapter<String>(act,
				android.R.layout.simple_spinner_item, arr);
		arrAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(arrAd);
		return spin;
	}

	public static void showYesNoDialogBox(Activity actClass, String title,
			String message) {
		Builder setupAlert;
		setupAlert = new AlertDialog.Builder(actClass)
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton("ตกลง",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						});

		setupAlert.show();
	}

	public static String getUID(Activity act) {
		TelephonyManager tManager = (TelephonyManager) act
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tManager.getDeviceId();
	}

	public static String convertStreamToString(Activity act, HttpResponse res) {
		try {
			// HttpEntity entity = res.getEntity();
			// showYesNoDialogBox(act, "", EntityUtils.toString(entity,
			// HTTP.UTF_8));

			BufferedReader br = new BufferedReader(new InputStreamReader(res
					.getEntity().getContent()));
			String line;
			StringBuffer sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showMsg(act, e.toString());
		}
		return null;
	}

	public static void retryConnectNet(final Activity act, String title,
			String msg) {
		try {
			AlertDialog.Builder dia = new AlertDialog.Builder(act).setTitle(
					title).setMessage(msg);
			dia.setCancelable(false);
			dia.setPositiveButton("Retry", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (!isOnline(act))
						retryConnectNet(act, "Warning!", "No internet connect.");
				}

			});
			dia.create().show();
		} catch (Exception e) {
			component.showYesNoDialogBox(act, "Sorry!", e.toString());
		}
	}

	public static boolean isOnlineWithDialogBox(Activity act) {
		if (isOnline(act)) {
			return true;
		} else {
			showYesNoDialogBox(act, act.getString(R.string.warning),
					act.getString(R.string.nointernet));
		}
		return false;
	}

	public static boolean isOnline(Activity act) {
		ConnectivityManager conMgr = (ConnectivityManager) act
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

		if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
			// Toast.makeText(act, "No Internet connection!",
			// Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	public static void addFavorite(final Activity act, final int position,
			final boolean history, final String type) {
		try {
			AlertDialog.Builder dia = new AlertDialog.Builder(act).setTitle(
					"Information!")
					.setMessage("Are you sure for add favorite?");
			dia.setNegativeButton("Cancel", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			dia.setPositiveButton("Yes", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					component.addFavoriteFromHistoryDo(act, position, history,
							type);
				}

			});

			dia.create().show();
		} catch (Exception e) {
			component.showYesNoDialogBox(act, "Sorry!", e.toString());
		}
	}

	public static boolean setFavorite(Activity act, String id, String type) {
		try {
			String url = act.getResources().getString(R.string.SERVERTEST)
					+ "setFavorite.php";

			List<NameValuePair> ldata = new ArrayList<NameValuePair>();
			ldata.add(new BasicNameValuePair("username", component.username));
			ldata.add(new BasicNameValuePair("book_id", id));
			ldata.add(new BasicNameValuePair("type", type));

			HttpResponse res = connectDBPost(act, url, ldata);
			int resCode = res.getStatusLine().getStatusCode();
			if (resCode == 200) {
				String data = convertStreamToString(act, res);
				// showYesNoDialogBox(act, "", data);
				JSONArray ja = new JSONArray(data);
				JSONObject jo = ja.getJSONObject(0);
				String status = jo.getString("setStatus");
				if (Integer.parseInt(status) == 1) {
					// showYesNoDialogBox(act, "Information!",
					// "Added you favorite.");
					return true;
				} else if (Integer.parseInt(status) == 2) {
					// showYesNoDialogBox(act, "Information!", "Then you add.");
					return true;
				} else {
					// showYesNoDialogBox(act, "Warning!", jo.getString("msg"));
				}

			} else if (resCode == 404) {
				showMsg(act, "File not found");
			} else if (resCode == 500) {
				showMsg(act, "Compile error");
			}

		} catch (Exception e) {
			showMsg(act,
					e.toString()
							+ "  in component.java and in addFavoriteFromHistoryDo function");
		}
		return true;
	}

	public static void addFavoriteFromHistoryDo(Activity act,
			int positionSelected, boolean history, String type) {
		try {
			String url = act.getResources().getString(R.string.SERVERTEST)
					+ "setFavorite.php";
			String mt_id = "0";
			if (type.equals("C")) {
				if (history)
					mt_id = CMainHistory.bookHistory.get(positionSelected)
							.getId();
				else
					mt_id = component.books.get(positionSelected).getId();
			} else {
				if (history)
					mt_id = JMainHistory.journalHistory.get(positionSelected)
							.getId();
				else
					mt_id = component.journals.get(positionSelected).getId();
			}

			List<NameValuePair> ldata = new ArrayList<NameValuePair>();
			ldata.add(new BasicNameValuePair("username", component.username));
			ldata.add(new BasicNameValuePair("book_id", mt_id));
			ldata.add(new BasicNameValuePair("type", type));

			HttpResponse res = connectDBPost(act, url, ldata);
			int resCode = res.getStatusLine().getStatusCode();
			if (resCode == 200) {
				String data = convertStreamToString(act, res);
				// showYesNoDialogBox(act, "", data);
				JSONArray ja = new JSONArray(data);
				JSONObject jo = ja.getJSONObject(0);
				String status = jo.getString("setStatus");
				if (Integer.parseInt(status) == 1) {
					showYesNoDialogBox(act, "Information!",
							"Added you favorite.");
				} else if (Integer.parseInt(status) == 2) {
					showYesNoDialogBox(act, "Information!", "Then you add.");
				} else {
					showYesNoDialogBox(act, "Warning!", jo.getString("msg"));
				}

			} else if (resCode == 404) {
				showMsg(act, "File not found");
			} else if (resCode == 500) {
				showMsg(act, "Compile error");
			}

		} catch (Exception e) {
			showMsg(act,
					e.toString()
							+ "  in component.java and in addFavoriteFromHistoryDo function");
		}
	}

	public static boolean deleteFavorite(Activity act, String id, String type) {
		try {
			if (component.isOnline(act)) {
				String url = act.getResources().getString(R.string.SERVERTEST)
						+ "deleteFavorite.php";
				List<NameValuePair> ldata = new ArrayList<NameValuePair>();
				ldata.add(new BasicNameValuePair("username", component.username));
				ldata.add(new BasicNameValuePair("book_id", id));
				ldata.add(new BasicNameValuePair("type", type));

				// set books variable for public
				JSONArray ja = new JSONArray(component.convertStreamToString(
						act, component.connectDBPost(act, url, ldata)));
				JSONObject jo = ja.getJSONObject(0);

				if (Integer.parseInt(jo.getString("deleteFavoriteStatus")) == 1) {
					// showYesNoDialogBox(act, "Information!", "Deleted");
					return true;
				} else {
					showYesNoDialogBox(act, "Warning!",
							"Delete fail. : " + jo.getString("msg"));
				}

			} else {
				showYesNoDialogBox(act, "Warning!", "No connect internet");
			}
		} catch (Exception e) {
			showYesNoDialogBox(act, "", e.toString());
		}
		return false;
	}

	public static boolean deleteItemFavoriteDo(Activity act,
			int positionSelected, String type) {
		try {
			if (component.isOnline(act)) {
				String url = act.getResources().getString(R.string.SERVERTEST)
						+ "deleteFavorite.php";
				List<NameValuePair> ldata = new ArrayList<NameValuePair>();
				String id = "0";
				if (type.equals("C")) {
					id = component.books.get(positionSelected).getId();
				} else {
					id = component.journals.get(positionSelected).getId();
				}

				ldata.add(new BasicNameValuePair("username", component.username));
				ldata.add(new BasicNameValuePair("book_id", id));
				ldata.add(new BasicNameValuePair("type", type));

				// set books variable for public
				JSONArray ja = new JSONArray(component.convertStreamToString(
						act, component.connectDBPost(act, url, ldata)));
				JSONObject jo = ja.getJSONObject(0);

				if (Integer.parseInt(jo.getString("deleteFavoriteStatus")) == 1) {
					showYesNoDialogBox(act, "Information!", "Deleted");
					return true;
				} else {
					showYesNoDialogBox(act, "Warning!",
							"Delete fail. : " + jo.getString("msg"));
				}

			} else {
				showYesNoDialogBox(act, "Warning!", "No connect internet");
			}
		} catch (Exception e) {
			showYesNoDialogBox(act, "", e.toString());
		}
		return false;
	}

	static boolean status = false;

	public static boolean deleteItemFavorite(final Activity act,
			final int positionSelected, final String type) {

		try {
			AlertDialog.Builder dia = new AlertDialog.Builder(act).setTitle(
					"Warning!").setMessage("Are you sure?");
			dia.setNegativeButton("Cancel", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});

			dia.setPositiveButton("Yes", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					status = deleteItemFavoriteDo(act, positionSelected, type);
				}
			});
			dia.create().show();

		} catch (Exception e) {
			component.showYesNoDialogBox(act, "Sorry!", e.toString());
		}
		return status;
	}

	public void showDialog(String title, String msg) {
		try {
			AlertDialog.Builder dia = new AlertDialog.Builder(this).setTitle(
					title).setMessage(msg);
			dia.setNegativeButton("Cancel", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			});
			dia.setPositiveButton("Yes", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

				}

			});
			dia.create().show();
		} catch (Exception e) {
			component.showYesNoDialogBox(this, "Sorry!", e.toString());
		}
	}

	public static void showYesNoDialogBox(Context context, String title,
			String message) {
		Builder setupAlert;
		setupAlert = new AlertDialog.Builder(context)
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton("ตกลง",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						});

		setupAlert.show();

	}

	public static String getStringFromServerPost(String url,
			List<NameValuePair> ldata, Activity shareClass) {
		HttpResponse res = connectDBPost(shareClass, url, ldata);
		int resCode = res.getStatusLine().getStatusCode();
		if (resCode == 200) {
			return convertStreamToString(shareClass, res);
		} else if (resCode == 404) {
			showMsg(shareClass, "File not found");
		} else if (resCode == 500) {
			showMsg(shareClass, "Compile error");
		}

		return null;
	}

	public static String getArrayList(Activity act, String url,
			List<NameValuePair> ldata) {

		try {
			HttpResponse res = component.connectDBPost(act, url, ldata);
			int resCode = res.getStatusLine().getStatusCode();
			if (resCode == 200) {
				String data = component.convertStreamToString(act, res);
				// showYesNoDialogBox(actClass,"", data);

				return data;

			} else if (resCode == 404) {
				component.showMsg(act, "File not found");
			} else if (resCode == 500) {
				component.showMsg(act, "Compile error");
			}

		} catch (Exception e) {
			component.showYesNoDialogBox(act, "", e.toString());
		}
		return null;
	}

}
