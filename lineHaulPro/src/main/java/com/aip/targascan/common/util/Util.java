package com.aip.targascan.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aip.targascan.R;

public class Util {

	private static DecimalFormat formater = new DecimalFormat("00.00");

	/**
	 * Check for SD card availability.
	 * 
	 * @return: <i>True</i> if media is mounted, <i>false</i> othervise.
	 */
	public static boolean isMediaAvailable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/**
	 * This method checks if the internet connection is available or not.Returns
	 * True is network is connected else false
	 * 
	 * @param context
	 *            Object of Context, context from where the activity is going to
	 *            start.
	 * @return boolean
	 */
	public static synchronized boolean isNetAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null || !ni.isConnected())
			return false;
		else
			return true;
	}

	public static void copyFile(File in, File out) throws Exception {
		FileInputStream fis = new FileInputStream(in);
		FileOutputStream fos = new FileOutputStream(out);
		try {
			byte[] buf = new byte[1024];
			int i = 0;
			while ((i = fis.read(buf)) != -1) {
				fos.write(buf, 0, i);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (fis != null)
				fis.close();
			if (fos != null)
				fos.close();
		}
	}

	/**
	 * scaleImage method is used for rotating the image to 90 degrees.
	 * 
	 * @author kunal kathrotia
	 * @param context
	 *            is the activity context
	 * @param photoUri
	 *            is the uri of the photo going to be operate.
	 * @param file
	 *            is the image file in SD card
	 * @return {@link Bitmap} is the rotated bitmap at 90 degrees.
	 * @throws Exception
	 */
	public static Bitmap scaleImage(Context context, Uri photoUri, File file) throws Exception {
		ByteArrayOutputStream baos = null;
		FileOutputStream fos = null;
		Bitmap srcBitmap = null;
		try {
			InputStream is = context.getContentResolver().openInputStream(photoUri);
			BitmapFactory.Options dbo = new BitmapFactory.Options();
			dbo.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(is, null, dbo);
			is.close();

			int orientation = 90;// getOrientation(context, photoUri);

			is = context.getContentResolver().openInputStream(photoUri);
			srcBitmap = BitmapFactory.decodeStream(is);
			is.close();
			is = null;

			/*
			 * if the orientation is not 0 (or -1, which means we don't know),
			 * we have to do a rotation.
			 */
			if (orientation > 0) {
				Matrix matrix = new Matrix();
				matrix.postRotate(orientation);

				srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
				matrix = null;
			}

			baos = new ByteArrayOutputStream();
			fos = new FileOutputStream(file);

			srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

			byte[] bMapArray = baos.toByteArray();
			return BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			if (baos != null)
				baos.close();
			if (fos != null)
				fos.close();
			baos = null;
			fos = null;
			srcBitmap = null;
		}
	}

	public static String getTwoDecimalPoint(double value) {
		String val = formater.format(value);
		return val;
	}

	/**
	 * Returns decodes bitmap image and scales it to reduce memory consumption.
	 * 
	 * @param f
	 *            - File path wants to decode
	 * 
	 * @return Bitmap
	 */
	public static Bitmap decodeFile(File f, final int requiredSize) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream stream1 = new FileInputStream(f);
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();

			// Find the correct scale value. It should be the power of 2.
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			if (scale >= 2) {
				scale /= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			FileInputStream stream2 = new FileInputStream(f);
			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
			stream2.close();
			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			f = null;
		}
		return null;
	}

	public static void saveBitmap(Bitmap bitmap, File destFile) throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
		destFile.createNewFile();
		// write the bytes in file
		FileOutputStream fo = new FileOutputStream(destFile);
		fo.write(bytes.toByteArray());

		// remember close de FileOutput
		fo.close();
	}

	public static void sendMail(Context context, String subject, String filePath) {
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		emailIntent.setType("image/jpg");
		File file = new File(filePath);
		Uri uri = Uri.fromFile(file);
		emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
		// emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);

		/*
		 * ((Activity)context).startActivityForResult(Intent.createChooser(
		 * emailIntent,
		 * context.getString(R.string.title_send_mail)),Constants.EMAIL_SEND);
		 */
		file = null;
	}

	/**
	 * Check for network connection.
	 * 
	 * @param: Context for non-activity class.
	 * 
	 * @return: true if Internet connection is available.
	 * */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null || !ni.isConnected())
			return false;
		else
			return true;
	}

	/**
	 * @author hitesh
	 * 
	 *         To show Toast to user
	 * 
	 * @param context
	 *            is the activity context
	 * @param msg
	 *            is Toast Message
	 */
	public static void showToast(Context context, String msg) {

		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 
	 * @param valueInCM
	 *            is value in centimeter
	 * 
	 * @return convert into milimeter
	 */
	public static String getMilimeterValue(double valueInCM) {

		return String.valueOf((valueInCM * 10));
	}

	public static void hideKeyboard(Context context, EditText myEditText) {
		try {
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void hideKeyboard(Context context, View view) {
		try {
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("SimpleDateFormat")
	public static final long getDayDiff(String dateString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		try {
			java.util.Date date = dateFormat.parse(dateString);

			Calendar calendar1 = Calendar.getInstance();
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTimeInMillis(date.getTime());

			long milliseconds1 = calendar1.getTimeInMillis();
			long milliseconds2 = calendar2.getTimeInMillis();
			long diff = Math.abs(milliseconds2 - milliseconds1);
			long diffDays = diff / (24 * 60 * 60 * 1000);

			return diffDays;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static boolean isEmpty(EditText etText) {
		return etText.getText().toString().trim().length() == 0;
	}

	public static void showError(Context activity, JSONObject response) {
		try {
			L.alert(activity, getErrorMessage(activity, response));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getErrorMessage(Context activity, JSONObject response) {
		String message = activity.getResources().getString(R.string.error_server);
		try {
			if (response != null && response.has("errors")) {
				try {
					JSONArray cast = response.getJSONArray("errors");
					for (int i = 0; i < cast.length(); i++) {
						JSONObject actor = cast.getJSONObject(i);
						message = actor.getString("message");
						Log.d("errors:", message);
					}
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return message;
	}

	public static int getErrorCode(JSONObject response) {
		try {
			if (response != null && response.has("errors")) {
				try {
					JSONArray cast = response.getJSONArray("errors");
					for (int i = 0; i < cast.length(); i++) {
						JSONObject actor = cast.getJSONObject(i);
						return actor.getInt("code");
					}
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1;

	}

	public static long getCurrentTime() {
		/*
		 * SimpleDateFormat dateformat = new
		 * SimpleDateFormat("yyyy-MM-dd kk:mm:ss"); Calendar calendar =
		 * Calendar.getInstance(); String date = dateformat.format(new
		 * Date(calendar.getTimeInMillis())); Log.e("GetCurrentDate",
		 * ">>"+date);
		 */
		return System.currentTimeMillis() / 1000;
	}

	public static void openApp(Activity activity, String pkgName) {
		try {
			Intent intent = new Intent(pkgName);
			activity.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void openUrl(Activity activity, String url) {
		try {
			if (!url.startsWith("http://") && !url.startsWith("https://"))
				url = "http://" + url;
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			activity.startActivity(browserIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void openUrlOption(final Activity mActivity, final List<String> categories, final IOnUrlSelcted OnUrlSelcted) {

		final Dialog dialog = new Dialog(mActivity, R.style.DialogSlideAnim);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_list);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

		TextView txtHeader = (TextView) dialog.findViewById(R.id.categoryHead);

		ListView listCategory = (ListView) dialog.findViewById(R.id.dialogList);

		listCategory.setAdapter(new ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1, categories));

		listCategory.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				OnUrlSelcted.onUrlSelected(categories.get(position));
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public interface IOnUrlSelcted {
		public void onUrlSelected(String cateogryName);
	}

	public static boolean isSetUrl(Context context) {
		String urlName = SharedPrefrenceUtil.getPrefrence(context, Constants.PREF_SELECTED_URL_NAME, null);
		return urlName != null ? true : false;
	}

	public static void showLoginDialog(Context context) {
		if (isNetAvailable(context))
			L.alert(context, "Your internet connection is available. You must have login to use online access.");
	}

	public static JSONArray cur2Json(Cursor cursor) {

		JSONArray resultSet = new JSONArray();
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			int totalColumn = cursor.getColumnCount();
			JSONObject rowObject = new JSONObject();
			for (int i = 0; i < totalColumn; i++) {
				if (cursor.getColumnName(i) != null) {
					try {
						rowObject.put(cursor.getColumnName(i), cursor.getString(i));
					} catch (Exception e) {
						Log.d("Error", e.getMessage());
						e.printStackTrace();
					}
				}
			}
			resultSet.put(rowObject);
			cursor.moveToNext();
		}

		cursor.close();
		return resultSet;

	}

}
