package com.aip.targascan.common.util;

import java.util.Arrays;
import java.util.List;


public class Constants {

	public static final String APP_NAME = "LineHaul Pro";
	public static final String PKG_NAME = "com.aip.targascan";
	public static final String DEBUG = "Debug";
	
	//SharedPreference keys
	public static final String PREF_AUTH_KEY = "authKey";
	public static final String MSG_WAIT = "Please wait..";
	
	public static final int TIME_SPLASH_SMALL = 1000;
	public static final int TIME_SPLASH = 3000;
	
	public static final String  TIME_FORMAT = "yyyy-MM-dd hh:mm a";
	//SimpleDateFormat date_format_long = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
	
	public static final String SYSTEM_VALUE = "";
	public static final String SERVID_VALUE = "";
	
	public static final String APP_GROUP_PHOTO = "com.htech.groupphoto";
	public static final String APP_ROUTE_APP = "com.targa.merapp";
	
	public static final int MAX_CARTOON_COUNT = 40;
	
	public static final String PREF_IS_VALID_AUTH_KEY = "isValidAuthKey";
	
	public static final String PREF_MASTER_COMPANY_KEY = "master_company";
	
//	public static final String WEB_URL = "http://www.targadev.com";

	public static final String WEB_URL = "http://appsoft.us";
	
	public static final String STATIC_DATA = "{\"error\":false,\"success\":true,\"errors\":[],\"response\":{\"co_type\":[{\"name\":\"offcon\"},{\"name\":\"rrdfla\"},{\"name\":\"graflo\"},{\"name\":\"bwflo\"},{\"name\":\"anflo\"},{\"name\":\"fqflo\"},{\"name\":\"offpri\"},{\"name\":\"pplflo\"},{\"name\":\"triflo\"},{\"name\":\"natbev\"},{\"name\":\"vecnyc\"},{\"name\":\"zep\"},{\"name\":\"demo\"},{\"name\":\"lret\"},{\"name\":\"bagcft\"},{\"name\":\"mascon\"},{\"name\":\"ampr\"},{\"name\":\"ctsflo\"},{\"name\":\"offltl\"},{\"name\":\"expflo\"},{\"name\":\"keet\"},{\"name\":\"fast\"},{\"name\":\"gracar\"},{\"name\":\"sysco\"},{\"name\":\"pncl\"},{\"name\":\"save\"},{\"name\":\"edda\"},{\"name\":\"iline\"},{\"name\":\"pmls\"},{\"name\":\"offdep\"},{\"name\":\"bios\"}],\"chg\":[{\"text\":\"None\",\"data\":\"\"},{\"text\":\"Inside Delivery\",\"data\":\"900\"},{\"text\":\"Lift Gate\",\"data\":\"016\"},{\"text\":\"Residential\",\"data\":\"040\"},{\"text\":\"Waiting Time\",\"data\":\"002\"},{\"text\":\"2Man Job\",\"data\":\"041\"}],\"redeliver\":[{\"text\":\"No\",\"data\":\"Completed\"},{\"text\":\"CrossDock\",\"data\":\"CrossDock\"},{\"text\":\"PickupCollected\",\"data\":\"PickupCollected\"},{\"text\":\"Damaged\",\"data\":\"Damaged\"},{\"text\":\"Not Home\",\"data\":\"Not Home\"},{\"text\":\"Wrong Address\",\"data\":\"Wrong Address\"},{\"text\":\"Refused\",\"data\":\"Refused\"},{\"text\":\"Closed\",\"data\":\"Closed\"},{\"text\":\"Third Attempt\",\"data\":\"Third Attempt\"},{\"text\":\"Cancelled\",\"data\":\"Cancelled\"},{\"text\":\"Restage\",\"data\":\"Restage\"}]}}";
	
	
	public static final List<String> WEB_URLS = Arrays.asList(
			"https://www.yourcargoonline.com",
			"http://cargotracking.solutions");
	
	public static final String PREF_SELECTED_URL_NAME = "selctedWS";
	public static final String PREF_SELECTED_URL = "selctedWSUrl";
	
	public static final String STATIC_URL_DATA = "[{\"name\":\"YCOL\",\"url\":\"https://yourcargoonline.com\"},{\"name\":\"CTS\",\"url\":\"http://cargotracking.solutions\"}]";
	
	public static final String PREF_STATIC_WEB_URL_DATA = "staticWebUrlData";
	
	public static final String PREF_SELECTED_ITEM = "selectedItemPosition";
	
	
}
