package com.aip.targascan.common.database;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aip.targascan.common.util.Constants;
import com.aip.targascan.common.util.Util;
import com.aip.targascan.vo.Cachedjob;
import com.aip.targascan.vo.Chg;
import com.aip.targascan.vo.Company;
import com.aip.targascan.vo.CompanyRejection;
import com.aip.targascan.vo.DailyOrder;
import com.aip.targascan.vo.DriverLabel;
import com.aip.targascan.vo.DriverRoute;
import com.aip.targascan.vo.DriverRouteData;
import com.aip.targascan.vo.DriverRouteTimeStatus;
import com.aip.targascan.vo.Login;
import com.aip.targascan.vo.Redeliver;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 4;

	// Database Name
	private static final String DATABASE_NAME = "login.db";

	// Contacts table name
	static final String TABLE_LOGIN = "login";
	static final String TABLE_COMPANY = "company";
	static final String TABLE_CHG = "chg";
	static final String TABLE_REDELIVER = "redeliver";
	static final String TABLE_CACHEDJOB = "cachedjob";
	static final String TABLE_DAILY_ORDER = "daily_order";
	static final String TABLE_DRIVER_ROUTE = "driver_route";
	static final String TABLE_DRIVER_ROUTE_TIME_STATUS = "driver_route_time_status";
	static final String TABLE_DRIVER_ROUTE_TIME_STATUS_DETAILS = "driver_route_time_status_details";
	static final String TABLE_COMPANY_REJECTION = "company_rejection_list";
	static final String TABLE_DRIVER_LABEL = "driver_label";
	// Contacts Table Columns names
	private static final String KEY_LOGINID = "id";
	private static final String KEY_DRIVERID = "driverid";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_AUTOLOGIN = "autologin";
	private static final String KEY_REMEMBERME = "rememberme";
	private static final String KEY_COMPANYID = "companyid";
	private static final String KEY_COMPANYNAME = "name";
	private static final String KEY_TEXT = "text";
	private static final String KEY_DATA = "data";
	private static final String KEY_DATACHG = "data";
	private static final String KEY_TEXTCHG = "chg";
	private static final String KEY_CARTON_NUM = "carton_num";
	private static final String KEY_CO_TYPE = "co_type";
	// CACHEDJOB TABLE COLUMN NAME
	private static final String KEY_SIGNATURE = "sign";
	private static final String KEY_SIGNATURE_NAME = "signaturename";
	private static final String KEY_REDELIVER = "redelivery";
	private static final String KEY_CHG1 = "chg1";
	private static final String KEY_CHG2 = "chg2";
	private static final String KEY_CARTOON1 = "cartoon1";
	private static final String KEY_TO = "tofield";
	private static final String KEY_FROM = "fromfield";
	private static final String KEY_CACHED_DATE = "cacheddate";
	private static final String KEY_SYSTEM = "system";
	private static final String KEY_SERV_ID = "servid";
	private static final String KEY_ID = "id";
	// DRIVER_ROUTE TABLE COLUMN NAME
	private static final String KEY_ROUTE_ID = "id";
	private static final String KEY_ROUTE_NAME = "driver_route_name";
	// DRIVER_ROUTE_TIME_STATUS TABLE COLUMN NAME
	private static final String KEY_ROUTE_TIME_STATUS_ID = "id";
	private static final String KEY_ROUTE_TIME_STATUS_NAME = "place_name";
	// DRIVER_ROUTE_TIME_STATUS_DETAILS TABLE COLUMN NAME
	private static final String KEY_ROUTE_DETAILS_DRIVER_ROUTE_ID = "driver_route_id";
	private static final String KEY_ROUTE_DETAILS_ROUTE_TIME_STATUS_ID = "driver_route_timestatus_id";
	private static final String KEY_ROUTE_DETAILS_DRIVER_ID = "driver_id";
	private static final String KEY_ROUTE_DETAILS_TIMESTAMP = "timestamp";
	private static final String KEY_ROUTE_DETAILS_LAT = "gps_lat";
	private static final String KEY_ROUTE_DETAILS_LOG = "gps_log";
	// TABLE_COMPANY_REJECTION TABLE COLUMN NAME
	private static final String KEY_MASTER_COMPANY_ID = "master_company_id";
	private static final String KEY_MASTER_COMPANY_EDITING = "editing";
	// DRIVER_LABEL TABLE COLUMN NAME
	private static final String KEY_DRIVER_LABEL_DRIVER_ID = "driver_id";
	private static final String KEY_DRIVER_LABEL_CARTON_NUM = "carton_num";
	private static final String KEY_DRIVER_LABEL_BARCODE = "barcode";
	private static final String KEY_DRIVER_LABEL_CUST_NAME = "cust_name";

	private static DatabaseHandler mDatabaseHandler;

	public Context context;
	public Activity activity;

	public static DatabaseHandler getInstance(Context context) {
		if (null == mDatabaseHandler)
			mDatabaseHandler = new DatabaseHandler(context);

		mDatabaseHandler.context = context;
		return mDatabaseHandler;
	}

	private DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_LOGIN + "(" + KEY_LOGINID + " INTEGER PRIMARY KEY ," + KEY_DRIVERID
				+ " TEXT," + KEY_USERNAME + " TEXT," + KEY_PASSWORD + " TEXT," + KEY_AUTOLOGIN + " TEXT," + KEY_REMEMBERME + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);

		String CREATE_COMPANY_TABLE = "CREATE TABLE " + TABLE_COMPANY + "(" + KEY_COMPANYID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_COMPANYNAME + " TEXT" + ")";
		db.execSQL(CREATE_COMPANY_TABLE);

		String CREATE_CHG_TABLE = "CREATE TABLE " + TABLE_CHG + "(" + KEY_DATA + " INTEGER," + KEY_TEXT + " TEXT" + ")";
		db.execSQL(CREATE_CHG_TABLE);

		String CREATE_REDELIVER_TABLE = "CREATE TABLE " + TABLE_REDELIVER + "(" + KEY_DATACHG + " TEXT," + KEY_TEXTCHG + " TEXT" + ")";
		db.execSQL(CREATE_REDELIVER_TABLE);

		String CREATE_DAILY_ORDER_TABLE = "CREATE TABLE " + TABLE_DAILY_ORDER + "(" + KEY_CARTON_NUM + " TEXT," + KEY_CO_TYPE + " TEXT"
				+ ")";
		db.execSQL(CREATE_DAILY_ORDER_TABLE);

		String CREATE_CACHEDJOB_TABLE = "CREATE TABLE " + TABLE_CACHEDJOB + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_COMPANYID + " TEXT," + KEY_SIGNATURE + " TEXT," + KEY_SIGNATURE_NAME + " TEXT," + KEY_CHG1 + " TEXT," + KEY_CHG2
				+ " TEXT," + KEY_CARTOON1 + " TEXT," + KEY_REDELIVER + " TEXT," + KEY_FROM + " TEXT," + KEY_TO + " TEXT," + KEY_CACHED_DATE
				+ " TEXT," + KEY_SYSTEM + " TEXT," + KEY_SERV_ID + " TEXT" + ")";

		db.execSQL(CREATE_CACHEDJOB_TABLE);

		String CREATE_DRIVER_ROUTE_TABLE = "CREATE TABLE " + TABLE_DRIVER_ROUTE + "(" + KEY_ROUTE_ID + " TEXT," + KEY_ROUTE_NAME + " TEXT"
				+ ")";
		db.execSQL(CREATE_DRIVER_ROUTE_TABLE);

		String CREATE_DRIVER_ROUTE_TIME_STATUS_TABLE = "CREATE TABLE " + TABLE_DRIVER_ROUTE_TIME_STATUS + "(" + KEY_ROUTE_TIME_STATUS_ID
				+ " TEXT," + KEY_ROUTE_TIME_STATUS_NAME + " TEXT" + ")";
		db.execSQL(CREATE_DRIVER_ROUTE_TIME_STATUS_TABLE);

		String CREATE_DRIVER_ROUTE_DETAILS_TABLE = "CREATE TABLE " + TABLE_DRIVER_ROUTE_TIME_STATUS_DETAILS + "("
				+ KEY_ROUTE_DETAILS_DRIVER_ROUTE_ID + " TEXT," + KEY_ROUTE_DETAILS_ROUTE_TIME_STATUS_ID + " TEXT,"
				+ KEY_ROUTE_DETAILS_DRIVER_ID + " TEXT," + KEY_ROUTE_DETAILS_TIMESTAMP + " TEXT," + KEY_ROUTE_DETAILS_LAT + " TEXT,"
				+ KEY_ROUTE_DETAILS_LOG + " TEXT" + ")";
		db.execSQL(CREATE_DRIVER_ROUTE_DETAILS_TABLE);

		String CREATE_COMPANY_REJECTION = "CREATE TABLE " + TABLE_COMPANY_REJECTION + "(" + KEY_MASTER_COMPANY_ID + " TEXT,"
				+ KEY_MASTER_COMPANY_EDITING + " TEXT" + ")";
		db.execSQL(CREATE_COMPANY_REJECTION);

		String CREATE_DRIVER_LABEL = "CREATE TABLE " + TABLE_DRIVER_LABEL + "(" + KEY_DRIVER_LABEL_DRIVER_ID + " TEXT,"
				+ KEY_DRIVER_LABEL_CARTON_NUM + " TEXT," + KEY_DRIVER_LABEL_BARCODE + " TEXT," + KEY_DRIVER_LABEL_CUST_NAME + " TEXT" + ")";
		db.execSQL(CREATE_DRIVER_LABEL);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANY);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHG);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_REDELIVER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CACHEDJOB);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAILY_ORDER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRIVER_ROUTE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRIVER_ROUTE_TIME_STATUS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRIVER_ROUTE_TIME_STATUS_DETAILS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANY_REJECTION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRIVER_LABEL);
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new driver route time status
	public void addDriverRouteTimeStatusDetails(DriverRouteData driverRouteData) {
		SQLiteDatabase db = getWritableDatabase();

		ContentValues values = new ContentValues();
		// Contact Name
		values.put(KEY_ROUTE_DETAILS_DRIVER_ROUTE_ID, driverRouteData.getDriver_route_id());
		values.put(KEY_ROUTE_DETAILS_ROUTE_TIME_STATUS_ID, driverRouteData.getDriver_route_timestatus_id());
		values.put(KEY_ROUTE_DETAILS_DRIVER_ID, driverRouteData.getDriver_id());
		values.put(KEY_ROUTE_DETAILS_TIMESTAMP, driverRouteData.getTimestamp());
		values.put(KEY_ROUTE_DETAILS_LAT, driverRouteData.getGps_lat());
		values.put(KEY_ROUTE_DETAILS_LOG, driverRouteData.getGps_log());

		// Inserting Row
		db.insert(TABLE_DRIVER_ROUTE_TIME_STATUS_DETAILS, null, values);
		// db.close();
	}

	// Adding new driver label
	public void addDriverLabel(DriverLabel driverLabel) {
		SQLiteDatabase db = getWritableDatabase();

		ContentValues values = new ContentValues();
		// Contact Name
		values.put(KEY_DRIVER_LABEL_DRIVER_ID, driverLabel.getDriver_id());
		values.put(KEY_DRIVER_LABEL_CARTON_NUM, driverLabel.getCarton_num());
		values.put(KEY_DRIVER_LABEL_BARCODE, driverLabel.getBarcode());
		values.put(KEY_DRIVER_LABEL_CUST_NAME, driverLabel.getCust_name());

		// Inserting Row
		db.insert(TABLE_DRIVER_LABEL, null, values);
		// //db.close();
	}

	// Adding new driver route
	public void addDriverRoute(DriverRoute driverRoute) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		// Contact Name
		values.put(KEY_ROUTE_ID, driverRoute.getId());
		values.put(KEY_ROUTE_NAME, driverRoute.getDriver_route_name());

		// Inserting Row
		db.insert(TABLE_DRIVER_ROUTE, null, values);
		// //db.close();
	}

	// Adding new driver route time status
	public void addDriverRouteTimeStatus(DriverRouteTimeStatus driverRouteTimeStatus) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		// Contact Name
		values.put(KEY_ROUTE_TIME_STATUS_ID, driverRouteTimeStatus.getId());
		values.put(KEY_ROUTE_TIME_STATUS_NAME, driverRouteTimeStatus.getPlace_name());

		// Inserting Row
		db.insert(TABLE_DRIVER_ROUTE_TIME_STATUS, null, values);
		// db.close();
	}

	// Adding new driver route time status details
	public void addDriverRouteTimeStatuDetails(DriverRouteData driverRouteData) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		// Contact Name
		values.put(KEY_ROUTE_DETAILS_DRIVER_ROUTE_ID, driverRouteData.getDriver_route_id());
		values.put(KEY_ROUTE_DETAILS_ROUTE_TIME_STATUS_ID, driverRouteData.getDriver_route_timestatus_id());
		values.put(KEY_ROUTE_DETAILS_DRIVER_ID, driverRouteData.getDriver_id());
		values.put(KEY_ROUTE_DETAILS_TIMESTAMP, driverRouteData.getTimestamp());
		values.put(KEY_ROUTE_DETAILS_LAT, driverRouteData.getGps_lat());
		values.put(KEY_ROUTE_DETAILS_LOG, driverRouteData.getGps_log());

		// Inserting Row
		db.insert(TABLE_DRIVER_ROUTE_TIME_STATUS_DETAILS, null, values);
		// db.close();
	}

	// Adding new contact
	public void addData(Login login) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		// Contact Name
		values.put(KEY_DRIVERID, login.getDriverID()); // Contact Phone
		values.put(KEY_USERNAME, login.getuserName());
		values.put(KEY_PASSWORD, login.getPassword());
		values.put(KEY_AUTOLOGIN, login.getAutologin());
		values.put(KEY_REMEMBERME, login.getRememberme());
		// Inserting Row
		db.insert(TABLE_LOGIN, null, values);
		// db.close();
	}

	public void addDataCompany(String company) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_COMPANYNAME, company);

		// Inserting Row
		db.insert(TABLE_COMPANY, null, values);
		// db.close(); // Closing database connection
	}

	public void addCompanyRejection(CompanyRejection companyRejection) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_MASTER_COMPANY_ID, companyRejection.getMaster_company_id());
		values.put(KEY_MASTER_COMPANY_EDITING, companyRejection.getEditing());

		// Inserting Row
		db.insert(TABLE_COMPANY_REJECTION, null, values);
		// db.close(); // Closing database connection
	}

	public void addDataChg(Chg chg) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		// Contact Name
		values.put(KEY_TEXT, chg.getchgText()); // Contact Phone
		values.put(KEY_DATA, chg.getchgData());

		// Inserting Row
		db.insert(TABLE_CHG, null, values);
		// db.close(); // Closing database connection
	}

	public void addDataRedeliver(Redeliver redeliver) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		// Contact Name
		values.put(KEY_TEXTCHG, redeliver.getredeliverText()); // Contact Phone
		values.put(KEY_DATACHG, redeliver.getredeliverData());

		// Inserting Row
		db.insert(TABLE_REDELIVER, null, values);
		// db.close(); // Closing database connection
	}

	public void addDataDailyOrder(DailyOrder dailyOrder) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		// Contact Name
		values.put(KEY_CARTON_NUM, dailyOrder.getCartonNumText()); // Contact
																	// Phone
		values.put(KEY_CO_TYPE, dailyOrder.getCoTypeText());

		// Inserting Row
		db.insert(TABLE_DAILY_ORDER, null, values);
		// db.close(); // Closing database connection
	}

	public void addCachedjob(Cachedjob cachedjob) {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put(KEY_SIGNATURE_NAME, cachedjob.getSignatureName());// Contact
																			// Phone
			values.put(KEY_COMPANYID, cachedjob.getCompanyID());
			values.put(KEY_CHG1, cachedjob.getChg1());
			values.put(KEY_CHG2, cachedjob.getChg2());
			values.put(KEY_CARTOON1, cachedjob.getcartoon1());
			values.put(KEY_REDELIVER, cachedjob.getRedelivery());
			values.put(KEY_TO, cachedjob.getTo());
			values.put(KEY_FROM, cachedjob.getFrom());
			values.put(KEY_SIGNATURE, cachedjob.getEncodeSign());
			values.put(KEY_CACHED_DATE, cachedjob.getCachedDate());

			values.put(KEY_SYSTEM, Constants.SYSTEM_VALUE);
			values.put(KEY_SERV_ID, Constants.SERVID_VALUE);
			// Inserting Row
			db.insert(TABLE_CACHEDJOB, null, values);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// if (db != null) {
			// db.close(); // Closing database connection
			// }
		}
	}

	// Getting All Contacts
	public Login getContact() {
		String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

		Login login = null;
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				login = new Login();
				login.setDriverID((cursor.getString(1)));
				login.setuserName(cursor.getString(2));
				login.setPassword(cursor.getString(3));
				login.setAutologin(Integer.parseInt(cursor.getString(4)));
				login.setRememberme(Integer.parseInt(cursor.getString(5)));
			}

			// if (db != null)
			// db.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (login != null) {
			if (!Util.isSetUrl(context)) {
				login = null; // TODO url is not set
			}
		}

		return login;
	}

	public Cursor getDriverRouteTimeStatuDetails() {

		String selectQuery = "SELECT  * FROM " + TABLE_DRIVER_ROUTE_TIME_STATUS_DETAILS;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// if (db != null)
		// db.close();

		return cursor;
	}

	public Cursor getCartonNunber(String CartonNunber) {

		String selectQuery = "SELECT  * FROM " + TABLE_DAILY_ORDER + " WHERE carton_num='" + CartonNunber + "'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// if (db != null)
		// db.close();

		return cursor;
	}

	public Cursor getDriverLabel() {

		String selectQuery = "SELECT  * FROM " + TABLE_DRIVER_LABEL;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// if (db != null)
		// db.close();

		return cursor;
	}

	public Cursor getCompanyRejection(String id) {

		String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_REJECTION + " WHERE " + KEY_MASTER_COMPANY_ID + "='" + id + "' limit 1";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// if (db != null)
		// db.close();

		return cursor;
	}

	public Company getCompany() {
		String selectQuery = "SELECT  * FROM " + TABLE_COMPANY;

		Company company = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			company = new Company();

			company.setcompanyID(Integer.parseInt(cursor.getString(1)));
			company.setcompanyName((cursor.getString(2)));

		}

		// if (db != null)
		// db.close();

		return company;
	}

	public Chg getChg() {
		String selectQuery = "SELECT  * FROM " + TABLE_CHG;

		Chg chg = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			chg = new Chg();

			chg.setchgData(((cursor.getString(1))));
			chg.setchgText((cursor.getString(2)));

		}

		// if (db != null)
		// db.close();

		return chg;
	}

	public Redeliver getRedeliver() {
		String selectQuery = "SELECT  * FROM " + TABLE_REDELIVER;

		Redeliver redeliver = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			redeliver = new Redeliver();

			redeliver.setredeliverData(cursor.getString(1));
			redeliver.setredeliverText(cursor.getString(2));

		}

		// if (db != null)
		// db.close();

		return redeliver;
	}

	public DailyOrder getDailyOrder() {
		String selectQuery = "SELECT  * FROM " + TABLE_DAILY_ORDER;

		DailyOrder dailyOrder = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			dailyOrder = new DailyOrder();

			dailyOrder.setCartonNumText(cursor.getString(1));
			dailyOrder.setCoTypeText(cursor.getString(2));

		}

		// if (db != null)
		// db.close();

		return dailyOrder;
	}

	public ArrayList<DriverRoute> getAllDriverRoute() {

		ArrayList<DriverRoute> arrayList = new ArrayList<DriverRoute>();
		String selectQuery = "SELECT  * FROM " + TABLE_DRIVER_ROUTE;
		SQLiteDatabase db = this.getReadableDatabase();

		try {
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					try {
						DriverRoute driverRoute = new DriverRoute();
						driverRoute.setId(cursor.getString(cursor.getColumnIndex(KEY_ROUTE_ID)));
						driverRoute.setDriver_route_name(cursor.getString(cursor.getColumnIndex(KEY_ROUTE_NAME)));
						arrayList.add(driverRoute);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// if (db != null)
			// db.close();
		}

		return arrayList;
	}

	public ArrayList<DriverRouteTimeStatus> getAllDriverRouteTimeStatus() {

		ArrayList<DriverRouteTimeStatus> arrayList = new ArrayList<DriverRouteTimeStatus>();
		String selectQuery = "SELECT  * FROM " + TABLE_DRIVER_ROUTE_TIME_STATUS;
		SQLiteDatabase db = this.getReadableDatabase();

		try {
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					try {
						DriverRouteTimeStatus driverRouteTimeStatus = new DriverRouteTimeStatus();
						driverRouteTimeStatus.setId(cursor.getString(cursor.getColumnIndex(KEY_ROUTE_TIME_STATUS_ID)));
						driverRouteTimeStatus.setPlace_name(cursor.getString(cursor.getColumnIndex(KEY_ROUTE_TIME_STATUS_NAME)));
						arrayList.add(driverRouteTimeStatus);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// if (db != null)
			// db.close();
		}
		return arrayList;
	}

	public List<Cachedjob> getCachedjob() {
		List<Cachedjob> cachedjobs = new ArrayList<Cachedjob>();
		String selectQuery = "SELECT  * FROM " + TABLE_CACHEDJOB;
		SQLiteDatabase db = this.getReadableDatabase();

		try {
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					try {
						Cachedjob cachedjob = new Cachedjob();
						cachedjob.setID(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
						cachedjob.setEncodeSign(cursor.getString(cursor.getColumnIndex(KEY_SIGNATURE)));
						cachedjob.setSignatureName(cursor.getString(cursor.getColumnIndex(KEY_SIGNATURE_NAME)));
						cachedjob.setCompanyID(cursor.getString(cursor.getColumnIndex(KEY_COMPANYID)));
						cachedjob.setChg1(cursor.getString(cursor.getColumnIndex(KEY_CHG1)));
						cachedjob.setChg2(cursor.getString(cursor.getColumnIndex(KEY_CHG2)));
						cachedjob.setCartoon1(cursor.getString(cursor.getColumnIndex(KEY_CARTOON1)));
						cachedjob.setRedelivery(cursor.getString(cursor.getColumnIndex(KEY_REDELIVER)));
						cachedjob.setTo(cursor.getString(cursor.getColumnIndex(KEY_TO)));
						cachedjob.setFrom(cursor.getString(cursor.getColumnIndex(KEY_FROM)));
						cachedjob.setCachedDate(cursor.getString(cursor.getColumnIndex(KEY_CACHED_DATE)));
						cachedjob.setSystem(cursor.getString(cursor.getColumnIndex(KEY_SYSTEM)));
						cachedjob.setServId(cursor.getString(cursor.getColumnIndex(KEY_SERV_ID)));

						cachedjobs.add(cachedjob);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// if (db != null)
			// db.close();
		}

		return cachedjobs;
	}

	public List<Login> getAllLogin() {
		List<Login> loginList = new ArrayList<Login>();
		String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Login login = new Login();
				login.setID(Integer.parseInt(cursor.getString(0))); // Contact
																	// Phone
				login.setDriverID((cursor.getString(1)));
				login.setuserName((((cursor.getString(2)))));
				login.setPassword(((cursor.getString(3))));
				login.setAutologin(((Integer.parseInt(cursor.getString(4)))));
				login.setRememberme(Integer.parseInt(cursor.getString(5)));
				;

				// Adding contact to list
				loginList.add(login);
			} while (cursor.moveToNext());

		}

		// if (db != null)
		// db.close();

		// return contact list
		return loginList;
	}

	public List<String> getAllCompany() {
		List<String> companyList = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_COMPANY;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				/*
				 * Company company = new Company();
				 * company.setcompanyID((Integer
				 * .parseInt(cursor.getString(0)))); // Contact Phone
				 * company.setcompanyName((((cursor.getString(1)))));
				 */;

				// Adding contact to list
				companyList.add(cursor.getString(1));
			} while (cursor.moveToNext());

		}

		// if (db != null)
		// db.close();

		companyList.add(0, "Select Company");
		// return contact list
		return companyList;
	}

	public List<String> getAllCartonNumber() {
		List<String> cartonList = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_DAILY_ORDER;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				// Adding contact to list
				cartonList.add(cursor.getString(0));
			} while (cursor.moveToNext());

		}

		// if (db != null)
		// db.close();

		// return contact list
		return cartonList;
	}

	public List<String> getAllCartonCoType() {
		List<String> cartonList = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_DAILY_ORDER;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				// Adding contact to list
				cartonList.add(cursor.getString(1));
			} while (cursor.moveToNext());

		}

		// if (db != null)
		// db.close();

		// return contact list
		return cartonList;
	}

	public List<String> getAllChg() {
		List<String> chgList = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CHG;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				/*
				 * Chg chg = new Chg(); chg.setchgData((cursor.getString(0)));
				 * // Contact Phone chg.setchgText(((((cursor.getString(1))))));
				 */;

				// Adding contact to list
				chgList.add(cursor.getString(1));
			} while (cursor.moveToNext());

		}

		// if (db != null)
		// db.close();

		// return contact list
		return chgList;
	}

	public List<String> getAllRedeliver() {
		List<String> RedeliverList = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_REDELIVER;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				/*
				 * Redeliver redeliver = new Redeliver();
				 * redeliver.setredeliverData
				 * ((Integer.parseInt(cursor.getString(0)))); // Contact Phone
				 * redeliver.setredeliverText((((((cursor.getString(1)))))));
				 */;

				// Adding contact to list
				RedeliverList.add(cursor.getString(0));
			} while (cursor.moveToNext());

		}

		// if (db != null)
		// db.close();

		// return contact list
		return RedeliverList;
	}

	public List<DailyOrder> getAllDailyOrder() {
		List<DailyOrder> DailyOrderList = new ArrayList<DailyOrder>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_DAILY_ORDER;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				;
				// Adding contact to list
				DailyOrder dailyOrder = new DailyOrder();
				dailyOrder.setCartonNumText(cursor.getString(0));
				dailyOrder.setCoTypeText(cursor.getString(1));

				DailyOrderList.add(dailyOrder);
			} while (cursor.moveToNext());

		}

		// if (db != null)
		// db.close();

		// return contact list
		return DailyOrderList;
	}

	// Updating single contact
	public int updateContact(Login contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_DRIVERID, contact.getDriverID());
		values.put(KEY_USERNAME, contact.getuserName());
		values.put(KEY_PASSWORD, contact.getPassword());
		values.put(KEY_AUTOLOGIN, contact.getAutologin());
		values.put(KEY_REMEMBERME, contact.getRememberme());

		return db.update(TABLE_LOGIN, values, KEY_LOGINID + " = ?", new String[] { String.valueOf(getContactid()) });
	}

	public int updateCompany(Company company) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_COMPANYID, company.getcompanyID());
		values.put(KEY_COMPANYNAME, company.getcompanyName());

		return db.update(TABLE_COMPANY, values, KEY_COMPANYID + " = ?", new String[] { String.valueOf(company.getcompanyID()) });
	}

	public int updateChg(Chg chg) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_COMPANYID, chg.getchgText());
		values.put(KEY_COMPANYNAME, chg.getchgData());

		return db.update(TABLE_CHG, values, KEY_DATACHG + " = ?", new String[] { String.valueOf(chg.getchgData()) });
	}

	public int updateRedeliver(Redeliver redeliver) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_TEXT, redeliver.getredeliverText());
		values.put(KEY_COMPANYNAME, redeliver.getredeliverData());

		return db.update(TABLE_CHG, values, KEY_DATA + " = ?", new String[] { String.valueOf(redeliver.getredeliverData()) });
	}

	public int updateCachedjob(Cachedjob cachedjob) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_SIGNATURE_NAME, cachedjob.getSignatureName());
		values.put(KEY_COMPANYID, cachedjob.getCompanyID());
		values.put(KEY_CHG1, cachedjob.getChg1());
		values.put(KEY_CHG2, cachedjob.getChg2());
		values.put(KEY_CARTOON1, cachedjob.getcartoon1());
		values.put(KEY_REDELIVER, cachedjob.getRedelivery());
		values.put(KEY_TO, cachedjob.getTo());
		values.put(KEY_FROM, cachedjob.getFrom());

		return db.update(TABLE_CACHEDJOB, values, KEY_COMPANYID + " = ?", new String[] { String.valueOf(cachedjob.getCompanyID()) });
	}

	// Deleting single contact
	public void deleteContact() {
		deleteAll(TABLE_LOGIN);
	}

	public void deleteDriverRoute() {
		deleteAll(TABLE_DRIVER_ROUTE);
	}

	public void deleteDriverRouteTimeStatus() {
		deleteAll(TABLE_DRIVER_ROUTE_TIME_STATUS);
	}

	public void deleteDriverRouteTimeStatusDetails() {
		deleteAll(TABLE_DRIVER_ROUTE_TIME_STATUS_DETAILS);
	}

	public void deleteDriverLabel() {
		deleteAll(TABLE_DRIVER_LABEL);
	}

	public void deleteCompany() {
		deleteAll(TABLE_COMPANY);
	}

	public void deleteCompanyRejection() {
		deleteAll(TABLE_COMPANY_REJECTION);
	}

	public void deleteChg() {
		deleteAll(TABLE_CHG);
	}

	public void deleteRedeliver() {
		deleteAll(TABLE_REDELIVER);
	}

	public void deleteCachedJob() {
		deleteAll(TABLE_CACHEDJOB);
	}

	public void deleteDailyOrder() {
		deleteAll(TABLE_DAILY_ORDER);
	}

	public int getProfilesCount() {
		Cursor cursor = null;
		try {
			String countQuery = "SELECT  " + KEY_LOGINID + " FROM " + TABLE_LOGIN;
			SQLiteDatabase db = this.getReadableDatabase();
			cursor = db.rawQuery(countQuery, null);
			return cursor.getCount();

		} catch (Exception e) {
			return -1;
		} finally {
			if (cursor != null)
				cursor.close();
		}
	}

	// Getting contacts Count
	public int getContactsCount() {
		return getProfilesCount();
	}

	public int getContactid() {
		String q = "SELECT " + KEY_LOGINID + " FROM login";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(q, null);

		if (cursor != null && cursor.moveToFirst()) {
			return cursor.getInt(0);
		} else {
			return -1;
		}
	}

	public int updatePassword(String password) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_PASSWORD, password);

		// updating row
		return db.update(TABLE_LOGIN, values, KEY_LOGINID + " = ?", new String[] { String.valueOf(getContactid()) });
	}

	public String getPassword() {
		String q = "SELECT " + KEY_PASSWORD + " FROM login";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(q, null);

		if (cursor != null && cursor.moveToFirst()) {
			return cursor.getString(0);
		} else {
			return "";
		}
	}

	public List<String> getCompanyId() {
		List<String> list = new ArrayList<String>();
		String q = "SELECT " + KEY_COMPANYID + " FROM " + TABLE_CACHEDJOB;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(q, null);
		do {
			list.add(q);
		} while (cursor.moveToNext());

		// if (db != null)
		// db.close();

		return list;
	}

	private void deleteAll(String tableName) {
		try {
			getWritableDatabase().execSQL("delete from " + tableName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getCachedjobCount() {
		int count = 0;
		String selectQuery = "SELECT  " + KEY_CACHED_DATE + "  FROM " + TABLE_CACHEDJOB;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		try {
			if (cursor != null && cursor.moveToFirst()) {
				count = cursor.getCount();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// if (db != null)
			// db.close();
		}

		return count;
	}

	public int getDriverRouteTimeStatusDetailsCount() {
		int count = 0;
		String selectQuery = "SELECT * FROM " + TABLE_DRIVER_ROUTE_TIME_STATUS_DETAILS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		try {
			if (cursor != null && cursor.moveToFirst()) {
				count = cursor.getCount();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// if (db != null)
			// db.close();
		}

		return count;
	}

	public void deleteCachedJob(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			db.delete(TABLE_CACHEDJOB, KEY_ID + "=?", new String[] { Integer.toString(id) });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// if (db != null)
			// db.close();
		}
	}
}