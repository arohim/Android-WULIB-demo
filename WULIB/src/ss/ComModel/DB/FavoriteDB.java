package ss.ComModel.DB;

import java.util.ArrayList;

import ss.ComModel.Books;
import ss.ComModel.MembersModel;
import ss.ComModel.component;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FavoriteDB extends SQLiteOpenHelper {

	private SQLiteDatabase sqlDB;

	public FavoriteDB(Context context) {
		super(context, "favorite.db", null, 1);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE TB_MEMBERS ( M_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ " M_USERNAME TEXT, "
					+ " M_PASSWORD TEXT, "
					+ " M_NAME TEXT, " + " MG_ID TEXT); ");

			db.execSQL("CREATE TABLE TB_BOOKS ( B_ID INTEGER PRIMARY KEY, "
					+ " B_TITLE TEXT, " + "B_SUBJECT TEXT," + "B_SOURCE TEXT,"
					+ " B_AUTHOR TEXT," + "B_DATE DATE," + "B_BG TEXT, "
					+ " B_LANGUAGE TEXT," + "B_BARCODE TEXT,"
					+ "B_ITEMTYPE TEXT, " + " B_COLLECTION TEXT,"
					+ "B_LOCATION TEXT, " + " B_PUBLISHER TEXT,"
					+ "B_ISBN TEXT," + "B_STATUS TEXT, "
					+ " B_DESCRIPTION TEXT," + "B_IMAGEURL TEXT); ");

			db.execSQL("CREATE TABLE TB_JOURNALS ( J_ID INTEGER PRIMARY KEY ,"
					+ "J_TITLE TEXT, J_SUBJECT TEXT,J_SOURCE TEXT,J_AUTHOR_ID TEXT,"
					+ "J_DATE DATE,J_JG TEXT,J_LANGUAGE TEXT,"
					+ "J_BARCODE TEXT,J_ISSN TEXT,J_STATUS TEXT);");

			db.execSQL("CREATE TABLE TB_FAVORITE ( FV_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "FV_M_USERNAME TEXT, "
					+ "FV_B_ID TEXT, "
					+ "FV_MT_TYPE TEXT);");

			db.execSQL("CREATE TABLE  TB_BORROWS ( "
					+ " br_Id INTEGER PRIMARY KEY, " + " br_bId TEXT, "
					+ " br_username TEXT , " + " br_startDate DATETIME  , "
					+ " br_endDate DATETIME  , " + " br_type TEXT  );");

			db.execSQL("CREATE TABLE  TB_AUTHOR ( "
					+ " AT_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ " AT_FNAME TEXT, " + "AT_LNAME TEXT) ;");

			db.execSQL(" CREATE TABLE TB_NEWS (NEWS_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ " NEWS_TITLE TEXT, "
					+ " NEWS_DATE DATE, "
					+ " NEWS_CONTENT TEXT);");

			// Create table Log
			/*
			 * db.execSQL(
			 * "CREATE TABLE LOGINLOG ( LG_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
			 * + "LG_USERNAME TEXT,LG_SRID TEXT, " + "LG_CASEID TEXT, " +
			 * "LG_DATETIME DATETIME,FLAG TEXT);");
			 */
			Log.d("favoriteDB", " create favorite.db complete");
		} catch (Exception e) {
			Log.d("favoriteDB", " in onCreate function " + e.toString());
		}

	}

	// ----------------------------- ////// GET ARRAYLIST

	public ArrayList<Books> getArrayListBooks() {
		try {
			sqlDB = getReadableDatabase();
			ArrayList<Books> books = new ArrayList<Books>();
			Cursor c = findAll("TB_BOOKS");
			while (c.moveToNext()) {
				Books b = new Books();

				b.setId(c.getString(0)); // 1
				b.setTitle(c.getString(1)); // 2
				b.setSubject(c.getString(2)); // 3
				b.setSource(c.getString(3)); // 4
				b.setAuthor(c.getString(4)); // 5
												// 6
												// 7
				b.setLanguage(c.getString(7)); // 8
												// 9
				b.setMtType(c.getString(9)); // 10
				b.setCollection(c.getString(10)); // 11
				b.setLocation(c.getString(11)); // 12
				b.setPublisher(c.getString(12)); // 13
				b.setCallnumber(c.getString(13)); // 14
				b.setStatus(c.getString(14)); // 15
				b.setDescription(c.getString(15)); // 16
				b.setImgUrl(c.getString(16)); // 17
				books.add(b);
			}
			return books;
		} catch (Exception e) {
			Log.d("favoriteDB",
					" in getArrayListBooks function " + e.toString());
		}
		return null;
	}

	// ----------------------------- ////// SYNCRONICE

	// ----------------------------- ////// SET & delete FAVORITE

	public boolean setFavoriteBook(String id, String type) {
		sqlDB = getReadableDatabase();
		try {

			String sql = "INSERT INTO tb_favorite(fv_m_username,fv_b_id,fv_mt_type)"
					+ "VALUES('"
					+ component.username
					+ "','"
					+ id
					+ "', '"
					+ type + "')";
			sqlDB.execSQL(sql);
			return true;
		} catch (Exception e) {
			Log.d("favoriteDB", " in setFavoriteBook function " + e.toString());
		}
		return false;
	}

	public boolean deleteFavoriteBook(String id, String type) {
		sqlDB = getWritableDatabase();
		try {

			String sql = " DELETE FROM tb_favorite "
					+ " WHERE fv_m_username = '" + component.username + "' "
					+ " AND fv_b_id = " + id + " AND  fv_mt_type = '" + type
					+ "' ";
			sqlDB.execSQL(sql);
			return true;
		} catch (Exception e) {
			Log.d("favoriteDB", " in setFavoriteBook function " + e.toString());
		}
		return false;
	}

	// ----------------------------- ////// INSERT BATCH TABLE

	public int insertBatchJournal(ArrayList<String[]> sql) {
		try {
			sqlDB.beginTransaction();
			for (int i = 0; i < sql.size(); i++) {
				insertRowJournal(sql.get(i));
			}
			sqlDB.setTransactionSuccessful();
			Log.d("FavoriteDB", "insert insertBatchJournal complete");
			return query("TB_FAVORITE", null, null, null, null, null, null)
					.getCount();
		} catch (Exception e) {
			Log.d("FavoriteDB", "error in insertBatchJournal " + e.toString());
		} finally {
			sqlDB.endTransaction();
			// // sqlDB.close();

		}
		return 0;
	}

	public int insertBatchFavorite(ArrayList<String[]> sql) {
		try {
			sqlDB.beginTransaction();
			for (int i = 0; i < sql.size(); i++) {
				insertRowFavorite(sql.get(i));
			}
			sqlDB.setTransactionSuccessful();
			Log.d("FavoriteDB", "insert insertBatchFavorite complete");
			return query("TB_FAVORITE", null, null, null, null, null, null)
					.getCount();
		} catch (Exception e) {
			Log.d("FavoriteDB", "error in insertBatchFavorite " + e.toString());
		} finally {
			sqlDB.endTransaction();
			// // sqlDB.close();
		}
		return 0;
	}

	public int insertBatchBooks(ArrayList<String[]> sql) {
		try {
			sqlDB = getWritableDatabase();
			sqlDB.beginTransaction();
			for (int i = 0; i < sql.size(); i++) {
				// String[] sqlStr = sql.get(i);
				insertRowBook(sql.get(i));
			}
			sqlDB.setTransactionSuccessful();
			Log.d("FavoriteDB", "insert insertBatchBooks complete");
			return query("TB_BOOKS", null, null, null, null, null, null)
					.getCount();
		} catch (Exception e) {
			Log.d("FavoriteDB", "error in insertBatchBooks " + e.toString());
		} finally {
			sqlDB.endTransaction();
			// sqlDB.close();
		}
		return 0;
	}

	// ----------------------------- ////// INSERT ROW TABLE

	public void insertRowFavorite(String[] sql) {
		try {
			sqlDB = getWritableDatabase();
			sqlDB.execSQL("INSERT INTO TB_FAVORITE( " + "FV_M_USERNAME, "
					+ "FV_B_ID, " + "FV_MT_TYPE) VALUES('" + sql[0] + "','"
					+ sql[1] + "','" + sql[2] + "')");
		} catch (Exception e) {
			Log.d("favoriteDB", "in insertRowFavorite function " + e.toString());
		}
	}

	public int insertUser(String M_USERNAME, String M_PASSWORD, String M_NAME,
			String MG_ID) {

		try {
			sqlDB = getWritableDatabase();
			String sql = "INSERT INTO TB_MEMBERS(M_USERNAME,M_PASSWORD,M_NAME,MG_ID) "
					+ " VALUES('"
					+ M_USERNAME
					+ "','"
					+ M_PASSWORD
					+ "','"
					+ M_NAME + "','" + MG_ID + "');";
			sqlDB.execSQL(sql);
			Log.d("favoriteDB", "insert user " + M_USERNAME + " complete");
			return query("TB_MEMBERS", null, null, null, null, null, null)
					.getCount();
		} catch (Exception e) {
			Log.d("favoriteDB", "in insertUser function " + e.toString());
		}
		return 0;
	}

	public void insertRowJournal(String[] sql) {
		try {
			sqlDB = getWritableDatabase();
			String sqlSTMT = "INSERT INTO TB_JOURNALS (J_ID, J_TITLE, "
					+ "J_SUBJECT,J_SOURCE,J_DATE,J_JG,"
					+ "J_LANGUAGE,J_BARCODE,J_ISSN,J_STATUS) VALUES(" + sql[0]
					+ ",'" + sql[1] + "','" + sql[2] + "','" + sql[3] + "','"
					+ sql[4] + "','" + sql[5] + "','" + sql[6] + "','" + sql[7]
					+ "','" + sql[8] + "','" + sql[9] + "')";
			sqlDB.execSQL(sqlSTMT);
		} catch (Exception e) {
			Log.d("favoriteDB", "in insertRowJournal function " + e.toString());
		}

	}

	public void insertRowBook(String[] sqlStr) {
		try {
			sqlDB = getWritableDatabase();
			sqlDB.execSQL("INSERT INTO TB_BOOKS( B_ID,B_TITLE , "
					+ "B_SUBJECT ,B_SOURCE , B_AUTHOR_ID ,"
					+ "B_DATE ,B_BG ,B_LANGUAGE ,B_BARCODE ,"
					+ "B_ITTYPE ,B_COLLECTION ,B_LOCATION ,"
					+ "B_PUBLISHER ,B_ISBN ,B_STATUS ,"
					+ "B_DESCRIPTION ,B_IMAGEURL )" + "VALUES("
					+ sqlStr[0]
					+ ",'"
					+ sqlStr[1]
					+ "','"
					+ sqlStr[2]
					+ "','"
					+ sqlStr[3]
					+ "','"
					+ sqlStr[4]
					+ "','"
					+ sqlStr[5]
					+ "','"
					+ sqlStr[6]
					+ "','"
					+ sqlStr[7]
					+ "','"
					+ sqlStr[8]
					+ "','"
					+ sqlStr[9]
					+ "','"
					+ sqlStr[10]
					+ "','"
					+ sqlStr[11]
					+ "','"
					+ sqlStr[12]
					+ "','"
					+ sqlStr[13]
					+ "','"
					+ sqlStr[14]
					+ "','"
					+ sqlStr[15]
					+ "','"
					+ sqlStr[16]
					+ "');");
		} catch (Exception e) {
			Log.d("favoriteDB", "in insertRowBook function " + e.toString());
		}
	}

	// ----------------------------- ///////// OTHER

	public boolean checkLogin(String username, String password) {

		try {
			sqlDB = getReadableDatabase();
			String selection = " M_USERNAME = ? AND M_PASSWORD = ?";
			Cursor c = query("tb_members", null, selection, new String[] {
					username, password }, null, null, null);
			if (c.getCount() > 0)
				return true;
		} catch (Exception e) {
			Log.d("favoriteDB",
					"ERROR in queryMembers function " + e.toString());
		} finally {
			// sqlDB.close();
		}
		return false;
	}

	public ArrayList<MembersModel> queryMembers() {

		try {
			sqlDB = getReadableDatabase();
			Cursor c = query("TB_MEMBERS", null, null, null, null, null, null);
			ArrayList<MembersModel> arrMm = new ArrayList<MembersModel>();
			while (c.moveToNext()) {
				MembersModel md = new MembersModel();
				md.setId(c.getString(0));
				md.setUsername(c.getString(1));
				md.setPassword(c.getString(2));
				md.setName(c.getString(3));
				md.setMg_id(c.getString(4));
				arrMm.add(md);
			}
			Log.d("favoriteDB", "queryMembers Complete");
			return arrMm;

		} catch (Exception e) {
			Log.d("favoriteDB",
					"ERROR in queryMembers function " + e.toString());
		} finally {
			// sqlDB.close();
		}
		return null;
	}

	public int getCountRowByTable(String tableName) {
		sqlDB = getReadableDatabase();
		return query(tableName, null, null, null, null, null, null).getCount();
	}

	public void clearTable(String tableName) {
		try {
			sqlDB = getWritableDatabase();
			String sql = "DELETE FROM " + tableName;
			sqlDB.execSQL(sql);
			Log.d("favoriteDB", "Clear table " + tableName + " Complete");
		} catch (Exception e) {
			Log.d("favoriteDB", "ERROR in clear table function " + tableName
					+ " " + e.toString());
		} finally {
			// sqlDB.close();
		}

	}

	public Cursor findAll(String tableName) {
		return query(tableName, null, null, null, null, null, null);
	}

	public Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {

		try {
			sqlDB = getReadableDatabase();
			return sqlDB.query(table, columns, selection, selectionArgs,
					groupBy, having, orderBy);
		} catch (Exception e) {
			Log.d("favoriteDB", "ERROR in query function  " + e.toString());
		} finally {
			// sqlDB.close();
		}
		return null;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
