package com.aip.targascan.common.util;

import android.util.Log;

public class JsonKey {

	public static final String DUMMY_URL = "https://api.yourcargoonline.com/index.php";
	private static final String API_POSTFIX = "/APP_CONTROLLER/index.php/api/";
	public static final String AUTH_KEY = "authKey";
	// private static final String MAIN_URL =
	// "http://apptechnolabs.com/projects/linehaul/www/";
	// private static final String MAIN_URL =
	// "http://apptechnolabs.com/projects/linehaul_pro/index.php/api/"; //
	// 10/12/2014
	public static String MAIN_URL = "https://api.yourcargoonline.com/index.php"; // 10/12/2014
	// private static final String MAIN_URL =
	// "http://cargotracking.solutions/APP_CONTROLLER/index.php/api/"; //
	// 10/12/2014

	private static final String LOGIN_URL = "login";
	private static final String CHANGEPWD_URL = "changePassword";
	private static final String CHECKOUT_URL = "checkOut";
	private static final String URL_MULTISCAN_DATA_LOADER = "getMultiScanFormFields";
	private static final String URL_CHECK_IN = "checkIn";
	private static final String URL_CHECK_OUT = "checkOut";
	private static final String URL_MULTI_SCAN = "multiScan";
	private static final String URL_MUST_SCAN_CO_TYPE = "getMustScanCoType";

	private static final String URL_MULTI_SCAN_CO_TYPE = "multiScanCoType";
	private static final String URL_MULTI_SCAN_CO_TYPE_DRIVER_ROUTE_LABEL = "insertDriverRouteLabel";
	private static final String URL_DRIVER_DETAIL = "driverDetail";
	private static final String URL_INSERT_REFERENCE = "insertReference";
	private static final String URL_GET_DAILY_ORDERS = "getDailyOrders";
	private static final String URL_CHECK_CARTON_NUMBER = "checkCartonNumbers";
	private static final String URL_MULTI_SCAN_CO_TYPE_WITH_DOCUMENT = "multiScanCoTypeWithDocument";
	private static final String URL_GET_CARTON_DETAILS = "getCartonDetails";

	//public static final String URL_GET_DOMAINS = "http://domains.linehaulpro.com/provide_domain_names.php?passphrase=0l1oepi1o71v5";

	public static final String URL_GET_DOMAINS = "https://api.yourcargoonline.com/provide_domain_names.php?passphrase=0l1oepi1o71v5";


	public static final String URL_ADDRESS_LIST = "getRoute";

	public static final String URL_REFERENCE_LIST = "getReference";

	public static final String URL_GET_DRIVERS_ROUTE = "getDriverRoute";

	public static final String URL_GET_DRIVERS_ROUTE_STATUS = "getDriverRouteTimeStatus";

	public static final String URL_GET_INSERT_DRIVERS_ROUTE = "insertDriverRoute";

	public static final String URL_GET_COMPANY_LIST_EDITING = "getCompanyListwithEditing";
	
	public static final String URL_COMPANY_REJECTION_LIST = "getCompanyRejectionList";

	public static String getMAIN_URL() {
		return MAIN_URL;
	}

	public static String getDriver_Route() {
		return MAIN_URL + URL_GET_DRIVERS_ROUTE;
	}

	public static String getCompanyRejectionList() {
		return MAIN_URL + URL_COMPANY_REJECTION_LIST;
	}
	
	public static String getInsert_Driver_Route() {
		return MAIN_URL + URL_GET_INSERT_DRIVERS_ROUTE;
	}

	public static String getDriver_Route_Status() {
		return MAIN_URL + URL_GET_DRIVERS_ROUTE_STATUS;
	}

	public static String getCompany_List_Editing() {
		return MAIN_URL + URL_GET_COMPANY_LIST_EDITING;
	}

	public static String getReference_LIST() {
		return MAIN_URL + URL_REFERENCE_LIST;
	}

	public static String getADDRESS_LIST() {
		return MAIN_URL + URL_ADDRESS_LIST;
	}

	public static void setMAIN_URL(String mAIN_URL) {
		MAIN_URL = mAIN_URL+"/api/";

		Log.e("#NEW MAIN URL", MAIN_URL);
	}

	public static String getCHANGEPWD_URL() {
		return MAIN_URL + CHANGEPWD_URL;
	}

	public static String getCHECKOUT_URL() {
		return MAIN_URL + CHECKOUT_URL;
	}

	public static String getLOGIN_URL() {
		return MAIN_URL + LOGIN_URL;
	}

	public static String getReference_URL() {
		return MAIN_URL + URL_INSERT_REFERENCE;
	}

	public static String getURL_MULTISCAN_DATA_LOADER() {
		return MAIN_URL + URL_MULTISCAN_DATA_LOADER;
	}

	public static String getURL_GET_DAILY_ORDERS() {
		return MAIN_URL + URL_GET_DAILY_ORDERS;
	}

	public static String getURL_CHECK_CARTON_NUMBER() {
		return MAIN_URL + URL_CHECK_CARTON_NUMBER;
	}

	public static String getURL_CHECK_IN() {
		return MAIN_URL + URL_CHECK_IN;
	}

	public static String getURL_CHECK_OUT() {
		return MAIN_URL + URL_CHECK_OUT;
	}

	public static String getURL_MULTI_SCAN() {
		return MAIN_URL + URL_MULTI_SCAN;
	}

	public static String getURL_MULTI_SCAN_CO_TYPE() {
		return MAIN_URL + URL_MULTI_SCAN_CO_TYPE_WITH_DOCUMENT;
	}
	public static String getURL_MULTI_SCAN_CO_TYPE_old() {
		return MAIN_URL + URL_MULTI_SCAN_CO_TYPE;
	}

	public static String getURL_MULTI_SCAN_CO_TYPE_DRIVER_ROUTE_LABEL() {
		return MAIN_URL + URL_MULTI_SCAN_CO_TYPE_DRIVER_ROUTE_LABEL;
	}
	public static String getURL_MUST_SCAN_CO_TYPE() {
		return MAIN_URL + URL_MUST_SCAN_CO_TYPE;
	}

	public static String getURL_DRIVER_DETAIL() {
		return MAIN_URL + URL_DRIVER_DETAIL;
	}

	public static String getCARTON_DETAILS_URL() {
		return MAIN_URL + URL_GET_CARTON_DETAILS;
	}
	public static final String KEY_DRIVER_ID = "driverId";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_AUTOLOGIN = "autologin";
	public static final String KEY_REMEMBERME = "rememberme";
	public static final String KEY_CURRENTPASSWORD = "oldPassword";
	public static final String KEY_NEWPASSWORD = "newPassword";
	public static final String KEY_CONFIRMNEWPASSWORD = "confirmPassword";
	public static final String KEY_VEHICLENAME = "vehicleName";
	public static final String KEY_ENDMILES = "endMiles";
	public static final String KEY_CHECKOUT = "checkOut";
	public static final String KEY_carton_num = "carton_num";

	// parameter for multiscaninner
	public static final String KEY_SIGNATURENAME = "signatureName";
	public static final String KEY_COMPANYID = "companyID";
	public static final String KEY_CHG1 = "chg1";
	public static final String KEY_CHG2 = "chg2";
	public static final String KEY_CARTON1 = "carton1";
	public static final String KEY_REDELIVERY = "reDelivery";
	public static final String KEY_TOADDRESS = "toAddress";
	public static final String KEY_FROMADDRESS = "fromAddress";
	public static final String KEY_RECORDDATE = "recordDate";
	public static final String KEY_AUTHKEY = "authKey";
	public static final String KEY_CARTON_NUMBERS = "carton_numbers";

	// CheckIn parameters
	public interface CheckIn {
		String vehicleName = "vehicleName";
		String startMiles = "startMiles";
		String checkIn = "checkIn";
	}

	// CheckOut parameters
	public interface CheckOut {
		String vehicleName = "vehicleName";
		String endMiles = "endMiles";
		String checkOut = "checkOut";
	}

	public interface MUTISCAN {
		String DID = "did";
		String redeliver = "redeliver";
		String cust_name = "cust_name";
		String cn = "cn";
		String signData = "signData";
		String co_type = "co_type";
		String cached_date = "cached_date";
		String to = "to";
		String from = "from";
		String ass1 = "ass1";
		String ass2 = "ass2";
		String system = "system";
		String servID = "servID";
		String documentData = "documentData";	}
	public interface MUTISCAN_old {
		String DID = "did";
		String redeliver = "redeliver";
		String cust_name = "cust_name";
		String cn = "cn";
		String signData = "signData";
		String co_type = "co_type";
		String cached_date = "cached_date";
		String to = "to";
		String from = "from";
		String ass1 = "ass1";
		String ass2 = "ass2";
		String system = "system";
		String servID = "servID";	}

}