package com.demo.xmppchat.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.demo.xmppchat.R;

import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Util {

	/**
	 * Tag for Logging and other purpose
	 */
	private static final String TAG = Util.class.getSimpleName();
	/**
	 * Default interval in millisecond between two subsequent network calls
	 */
	public static final long DEFAULT_INTERVAL_IN_MILLIS = 0;
	/**
	 * Default Displacement in meter
	 */
	public static float DEFAULT_DISPLACEMENT = 0.0f;
	/**
	 * GCM Project ID
	 */
	public static final String PROJECT_ID = "616210210539";

	public static final int DEFAULT_ZOOM_LEVEL = 13;
	/**
	 * Id of user's twitter profile
	 */
	public static String twitterId;

	/**
	 * Twitter Username
	 */
	public static String twitterUserName;
	private static String root;
	private static File createDir;
	public static String STATUS = "Online";
	private final static String AVIARY_PACKAGE_NAME = "com.hinter.hinteraviary";
	/**
	 * Count of notification, We have one arraylist of messages in MainTabGroup,
	 * and this count will reflect its size.
	 */
	public static int NOTIFICATION_COUNT = 0;
	public static final int MAX_DEFAULT_SCREEN_OFFSET = 150;
	// The encoded character of each character escape.
	// This array functions as the keys of a sorted map, from encoded characters
	// to decoded characters.
	static final char[] ENCODED_ESCAPES = { '\"', '\'', '\\', 'b', 'f', 'n', 'r', 't' };

	// The decoded character of each character escape.
	// This array functions as the values of a sorted map, from encoded
	// characters to decoded characters.
	static final char[] DECODED_ESCAPES = { '\"', '\'', '\\', '\b', '\f', '\n', '\r', '\t' };

	// A pattern that matches an escape.
	// What follows the escape indicator is captured by group 1=character
	// 2=octal 3=Unicode.
	static final Pattern PATTERN = Pattern.compile("\\\\(?:(b|t|n|f|r|\\\"|\\\'|\\\\)|((?:[0-3]?[0-7])?[0-7])|u+(\\p{XDigit}{4}))");

	public static CharSequence decodeString(CharSequence encodedString) {
		Matcher matcher = PATTERN.matcher(encodedString);
		StringBuffer decodedString = new StringBuffer();
		// Find each escape of the encoded string in succession.
		while (matcher.find()) {
			char ch;
			if (matcher.start(1) >= 0) {
				// Decode a character escape.
				ch = DECODED_ESCAPES[Arrays.binarySearch(ENCODED_ESCAPES, matcher.group(1).charAt(0))];
			} else if (matcher.start(2) >= 0) {
				// Decode an octal escape.
				ch = (char) (Integer.parseInt(matcher.group(2), 8));
			} else /* if (matcher.start(3) >= 0) */{
				// Decode a Unicode escape.
				ch = (char) (Integer.parseInt(matcher.group(3), 16));
			}
			// Replace the escape with the decoded character.
			matcher.appendReplacement(decodedString, Matcher.quoteReplacement(String.valueOf(ch)));
		}
		// Append the remainder of the encoded string to the decoded string.
		// The remainder is the longest suffix of the encoded string such that
		// the suffix contains no escapes.
		matcher.appendTail(decodedString);
		return decodedString;
	}

	/**
	 * Queries whether internet network is available
	 * 
	 * @param context
	 *            : Required to query internet availibility
	 * @param canShowErrorDialogOnFail
	 *            : if <code>true</code> it will show an error dialog , if
	 *            <code>false</code> it will pass an error message, Please pass
	 *            <code>false</code> and call
	 *            handleNoNetwork(withProperArguments) if you want list view to
	 *            show related emptyview
	 * 
	 * @return <code>true</code> in case if network is available,
	 *         <code>false</code> otherwise. Will show an error dialog if
	 *         network is not available
	 */
	public static boolean isNetworkAvailable(final Context context, boolean canShowErrorDialogOnFail) {
		boolean isNetAvailable = false;
		if (context != null) {
			final ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

			if (mConnectivityManager != null) {
				boolean mobileNetwork = false;
				boolean wifiNetwork = false;

				boolean mobileNetworkConnecetd = false;
				boolean wifiNetworkConnecetd = false;

				final NetworkInfo mobileInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				final NetworkInfo wifiInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

				if (mobileInfo != null) {
					mobileNetwork = mobileInfo.isAvailable();
				}

				if (wifiInfo != null) {
					wifiNetwork = wifiInfo.isAvailable();
				}

				if (wifiNetwork || mobileNetwork) {
					if (mobileInfo != null)
						mobileNetworkConnecetd = mobileInfo.isConnectedOrConnecting();
					wifiNetworkConnecetd = wifiInfo.isConnectedOrConnecting();
				}

				isNetAvailable = (mobileNetworkConnecetd || wifiNetworkConnecetd);
			}
			if (!isNetAvailable && canShowErrorDialogOnFail) {
				// Log.v("TAG", "context : " + context.toString());
				if (context instanceof Activity) {
					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							displayDialog(context.getString(R.string.app_name),"No internet available ", context, false);

						}
					});

				}
			}
		}

		return isNetAvailable;
	}

	/**
	 * Method to use for compress images
	 * 
	 * @param filePath
	 *            - current image file path
	 * @param outputFilepath
	 *            - destination image file path
	 * @param inputFile
	 *            - bitmap of image
	 * @param inputFileData
	 *            - byte array of image
	 * @return String
	 **/
	public static String compressImage(String filePath, String outputFilepath, Bitmap inputFile, byte[] inputFileData) {

		// String filePath = getRealPathFromURI(imageUri);

		Bitmap scaledBitmap = null;

		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inJustDecodeBounds = true;

		Bitmap bmp;

		if (filePath != null) {

			bmp = BitmapFactory.decodeFile(filePath, options);

		} else if (inputFile != null) {

			bmp = inputFile;

		} else {

			bmp = BitmapFactory.decodeByteArray(inputFileData, 0, inputFileData.length, options);

		}

		int actualHeight = options.outHeight;

		int actualWidth = options.outWidth;

		float maxHeight = 816.0f;

		float maxWidth = 612.0f;

		float imgRatio = actualWidth / actualHeight;

		float maxRatio = maxWidth / maxHeight;

		if (actualHeight > maxHeight || actualWidth > maxWidth) {

			if (imgRatio < maxRatio) {

				imgRatio = maxHeight / actualHeight;

				actualWidth = (int) (imgRatio * actualWidth);

				actualHeight = (int) maxHeight;

			} else if (imgRatio > maxRatio) {

				imgRatio = maxWidth / actualWidth;

				actualHeight = (int) (imgRatio * actualHeight);

				actualWidth = (int) maxWidth;

			} else {

				actualHeight = (int) maxHeight;

				actualWidth = (int) maxWidth;

			}

		}

		options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

		options.inJustDecodeBounds = false;

		options.inDither = false;

		options.inPurgeable = true;

		options.inInputShareable = true;

		options.inTempStorage = new byte[16 * 1024];

		if (filePath != null) {

			bmp = BitmapFactory.decodeFile(filePath, options);

		} else if (inputFile != null) {

			bmp = inputFile;

		} else {

			bmp = BitmapFactory.decodeByteArray(inputFileData, 0, inputFileData.length, options);

		}

		scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);

		float ratioX = actualWidth / (float) options.outWidth;

		float ratioY = actualHeight / (float) options.outHeight;

		float middleX = actualWidth / 2.0f;

		float middleY = actualHeight / 2.0f;

		Matrix scaleMatrix = new Matrix();

		scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

		Canvas canvas = new Canvas(scaledBitmap);

		canvas.setMatrix(scaleMatrix);

		canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

		if (filePath != null) {

			ExifInterface exif;

			try {

				exif = new ExifInterface(filePath);

				int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);

				Log.d("EXIF", "Exif: " + orientation);

				Matrix matrix = new Matrix();

				if (orientation == 6) {

					matrix.postRotate(90);

					Log.d("EXIF", "Exif: " + orientation);

				} else if (orientation == 3) {

					matrix.postRotate(180);

					Log.d("EXIF", "Exif: " + orientation);

				} else if (orientation == 8) {

					matrix.postRotate(270);

					Log.d("EXIF", "Exif: " + orientation);

				}

				scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

			} catch (Exception e) {

				scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight());

				e.printStackTrace();

			}

		} else {

			scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight());

		}

		FileOutputStream out = null;

		// String filename = getFilename();

		try {

			out = new FileOutputStream(outputFilepath);

			scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		}

		return outputFilepath;

	}

	/**
	 * Calculate sample size for above method decodeSampledBitmapFromResource()
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return int
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	/**
	 * Check if default language is english or not.
	 * 
	 * @return : True if default language is english, false otherwise
	 */
	public static boolean checkIfEnglish() {
		return Locale.getDefault().getDisplayLanguage().toLowerCase().startsWith("eng");
	}

	/**
	 * Converts date from given Dateformat to desired date format.
	 * 
	 * @param date
	 *            : Date in string, it must follow pattern of <code>from</code>
	 *            format. If not followed that format, exception is thrown.
	 * @param from
	 *            : DateFormat of <code>date</code> from which it's been
	 *            converted.
	 * @param to
	 *            : DateFormat to which <code>date</code> is being converted/
	 * @return : Formated <code>date</code> according to <code>to</code> format.
	 * @throws ParseException
	 *             : if <code>date</code> is not parse-able according to
	 *             <code>from</code> format
	 */
	public static String convertDate(String date, SimpleDateFormat from, SimpleDateFormat to) throws ParseException {
		Date d1 = from.parse(date);
		String resultFormat = to.format(d1);
		return resultFormat;
	}

	/**
	 * Resizing listview according to its items
	 * 
	 * @param listView
	 */
	// public static ListView setListViewHeightBasedOnChildren(ListView
	// listView) {
	// ListAdapter listAdapter = listView.getAdapter();
	// if (listAdapter == null) {
	// // pre-condition
	// return listView;
	// }
	//
	// int totalHeight = 0;
	// int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
	// MeasureSpec.AT_MOST);
	// for (int i = 0; i < listAdapter.getCount(); i++) {
	// View listItem = listAdapter.getView(i, null, listView);
	// listItem.measure(0, 0);
	// totalHeight += listItem.getMeasuredHeight();
	// }
	//
	// ViewGroup.LayoutParams params = listView.getLayoutParams();
	// params.height = totalHeight
	// + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	// listView.setLayoutParams(params);
	// listView.requestLayout();
	// return listView;
	// }

	public static String changeWSDateFormatToDisplayFormat(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat sendFormat = new SimpleDateFormat("yyyy-MM-dd");
		String resultFormat = "";
		try {
			Date d1 = sendFormat.parse(date);
			resultFormat = formatter.format(d1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultFormat;
	}

	/**
	 * Queries whether internet network is available
	 * 
	 * @param context
	 *            : Required to query internet availibility
	 * 
	 * @return <code>true</code> in case if network is available,
	 *         <code>false</code> otherwise. Will show an error dialog if
	 *         network is not available
	 */
	public static boolean isNetworkAvailable(Context context) {
		return isNetworkAvailable(context, false);
	}

	public static Bitmap decodeFile(String path) {
		int orientation;
		try {
			if (path == null) {
				return null;
			}
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 4;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale++;
			}
			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			Bitmap bm = BitmapFactory.decodeFile(path, o2);

			Bitmap bitmap = bm;

			ExifInterface exif = new ExifInterface(path);
			orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
			// Log.e("orientation", "" + orientation);
			Matrix m = new Matrix();

			if ((orientation == 3)) {

				m.postRotate(180);
				m.postScale((float) bm.getWidth(), (float) bm.getHeight());

				// if(m.preRotate(90)){
				// Log.e("in orientation", "" + orientation);

				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
				return bitmap;
			} else if (orientation == 6) {

				m.postRotate(90);

				// Log.e("in orientation", "" + orientation);

				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
				return bitmap;
			}

			else if (orientation == 8) {

				m.postRotate(270);

				// Log.e("in orientation", "" + orientation);

				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
				return bitmap;
			}
			return bitmap;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Convert dp in pixels
	 * 
	 * @param dp
	 * @param context
	 * @return
	 */
	public static int getPxFromDp(int dp, Context context) {
		float scale = context.getResources().getDisplayMetrics().density;
		return ((int) (dp * scale + 0.5f));
	}

	public static void changeImageOrientation(String sourcepath, Bitmap bitmap) {
		int rotate = 0;
		try {
			File imageFile = new File(sourcepath);
			ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Matrix matrix = new Matrix();
		matrix.postRotate(rotate);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}

	public static boolean imageDownload(Context mContext, String imgUrl, String couponId) {
		boolean isImageExists = true;
		try {
			// Log.v(TAG, imgUrl);
			root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
			createDir = new File(root + mContext.getString(R.string.app_name) + File.separator + "Coupons");
			if (!createDir.exists()) {
				createDir.mkdir();
			}

			String path = root + mContext.getString(R.string.app_name) + File.separator + "Coupons" + File.separator + "coupon_" + couponId + ".jpg";

			URL url = new URL(imgUrl); // you can write here any link
			File file = new File(path);

			URLConnection ucon = url.openConnection();
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(ucon.getContentLength());
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baf.toByteArray());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			isImageExists = false;
		}
		return isImageExists;
	}

	// public static void getLowerCaseExtension(File file) {
	// String fileName = file.getName();
	// String lowerCaseExtension = fileName.substring(fileName.indexOf(".") +
	// 1).toLowerCase();
	// String name = fileName.substring(0, fileName.indexOf("."));
	// String filePathWithLowercaseExtension = file.getParent() + File.separator
	// + name + "."
	// + lowerCaseExtension;
	// Log.w("File", "File " + filePathWithLowercaseExtension);
	// Log.w("File", "File " + file.renameTo(new
	// File(filePathWithLowercaseExtension)));
	// }

	/**
	 * Checks whether given charSequence is empty or not. It will check whether
	 * String is null, or empty or having "null" as a value
	 * 
	 * @param charSequence
	 *            : charSequence to check.
	 * @return <code>true</code> in case of given charSequence is null, empty or
	 *         having "null" as a value
	 */
	public static boolean isEmpty(CharSequence charSequence) {
		return TextUtils.isEmpty(charSequence) || charSequence.toString().equalsIgnoreCase("null");
	}

	/**
	 * Hides soft keyboard,
	 * 
	 * @param context
	 *            : Context to get SoftKeyboard service.
	 * @param textView
	 *            : {@link TextView} in reference to which soft keyboard is
	 *            hidden
	 */
	public static void hideSoftKeyboard(Context context, TextView textView) {
		try {
			if (context != null && textView != null) {
				final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(textView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getValueByKey(JSONObject jSonObj, String strKey) {
		String strOutput = null;
		strOutput = jSonObj.optString(strKey);
		return strOutput;
	}

	public static String setPadding(int data) {
		String result = "";
		if (data < 10)
			result = "0" + data;
		else
			result = String.valueOf(data);
		return result;

	}

	public static String inputstreamToString(InputStream is1) {
		BufferedReader rd = null;
		try {
			rd = new BufferedReader(new InputStreamReader(is1, HTTP.UTF_8), 4096);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String line;
		StringBuilder sb = new StringBuilder();
		try {
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		String contentOfMyInputStream = sb.toString();
		return contentOfMyInputStream;
	}

	/**
	 * Display dialog with given message.
	 * 
	 * @param title
	 *            : Title of the dialog.
	 * @param msg
	 *            : message to show in dialog.
	 * @param context
	 * @param isFinish
	 *            : pass <code>true</code> if you want the host activity to be
	 *            finished once the dialog is disposed. Pass <code>false</code>
	 *            otherwise.
	 * @param listener
	 *            : Click listeners to handle dialog dismiss events. Pass
	 *            <code>null</code> to let default listeners handle the
	 *            dismissal
	 */
	public static void displayDialog(String title, String msg, final Context context, final boolean isFinish, DialogInterface.OnClickListener listener, boolean isCancelable) {

		// final AlertDialog alertDialog = new AlertDialog.Builder(context)
		// .create();
		//
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setCancelable(isCancelable);
		builder.setMessage(msg);
		DialogInterface.OnClickListener finishClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				((Activity) context).finish();
			}
		};
		DialogInterface.OnClickListener defaultClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				dialog.cancel();
			}
		};

		DialogInterface.OnClickListener clickListener = (isFinish) ? finishClickListener : (listener != null) ? listener : defaultClickListener;

		builder.setPositiveButton(context.getString(android.R.string.ok), clickListener);
		if (isCancelable) {
			builder.setNegativeButton(context.getString(android.R.string.cancel), defaultClickListener);
		}
		Dialog dialog = builder.create();
		if (!((Activity) context).isFinishing()) {
			if (!dialog.isShowing()) {
				dialog.show();
			}
		}
	}

	/**
	 * Display dialog with given message.
	 * 
	 * @param titleResource
	 *            String resource of dialog title
	 * @param msgResource
	 *            String resource of dialog message
	 * @param context
	 * @param isFinish
	 *            : pass <code>true</code> if you want the host activity to be
	 *            finished once the dialog is disposed. Pass <code>false</code>
	 *            otherwise.
	 * @param listener
	 *            : Click listeners to handle dialog dismiss events. Pass
	 *            <code>null</code> to let default listeners handle the
	 *            dismissal
	 */
	public static void displayDialog(int titleResource, int msgResource, final Context context, final boolean isFinish, DialogInterface.OnClickListener listener, boolean isCancelable) {
		String title = context.getString(titleResource);
		String messege = context.getString(msgResource);
		displayDialog(title, messege, context, isFinish, listener, isCancelable);
	}

	/**
	 * Display dialog with given message and default dialog listeners
	 * 
	 * @param titleResource
	 *            String resource of dialog title
	 * @param msgResource
	 *            String resource of dialog message
	 * @param context
	 * 
	 * @param isFinish
	 *            pass <code>true</code> if you want the host activity to be
	 *            finished once the dialog is disposed. Pass <code>false</code>
	 *            otherwise.
	 */
	public static void displayDialog(int titleResource, int msgResource, final Context context, final boolean isFinish) {
		String title = context.getString(titleResource);
		String messege = context.getString(msgResource);
		displayDialog(title, messege, context, isFinish, null, false);
	}

	/**
	 * Display dialog with given message and default dialog listeners
	 * 
	 * @param title
	 *            : Title of the dialog.
	 * @param msg
	 *            : message to show in dialog.
	 * @param context
	 * @param isFinish
	 *            pass <code>true</code> if you want the host activity to be
	 *            finished once the dialog is disposed. Pass <code>false</code>
	 *            otherwise.
	 */
	public static void displayDialog(String title, String msg, final Context context, final boolean isFinish) {
		displayDialog(title, msg, context, isFinish, null, false);
	}

	public static boolean sdCardMounted() {
		boolean isMediaMounted = false;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			isMediaMounted = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			isMediaMounted = false;
		} else if (Environment.MEDIA_CHECKING.equals(state)) {
			isMediaMounted = false;
		} else if (Environment.MEDIA_NOFS.equals(state)) {
			isMediaMounted = false;
		} else if (Environment.MEDIA_REMOVED.equals(state)) {
			isMediaMounted = false;
		} else if (Environment.MEDIA_SHARED.equals(state)) {
			isMediaMounted = false;
		} else if (Environment.MEDIA_UNMOUNTABLE.equals(state)) {
			isMediaMounted = false;
		} else if (Environment.MEDIA_UNMOUNTED.equals(state)) {
			isMediaMounted = false;
		}
		return isMediaMounted;
	}


	

	/**
	 * Generate Intent to open camera
	 * 
	 * @param context
	 *            : {@link Context} to create intent
	 * @return : {@link Intent} to open camera.
	 */
	public static Intent generateCameraPickerIntent(Context context) {

		File root = null;

		if (Util.sdCardMounted()) {
			root = new File(Environment.getExternalStorageDirectory() + File.separator + context.getString(R.string.app_name));

			if (!root.exists()) {
				root.mkdirs();
			}
		}

		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		return intent;
	}

	/**
	 * Generate intent to open gallery to choose image
	 * 
	 * @param context
	 *            :{@link Context} to create intent
	 * @return : {@link Intent} to open gallery
	 */
	public static Intent generateGalleryPickerIntent(Context context) {
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.setType("image/*");
		return intent;
	}

	/**
	 * Returns a string describing 'time' as a time relative to 'now'.
	 * 
	 * @param postTime
	 *            : Time to compare with now.
	 * @return : "0s" if future date is passed, Abbreviated date in case of
	 *         postTime is more than a week old. In default cases it will return
	 *         values like "10s" "20m" "5h" "3d"
	 */
	public static String getTimeSince(String postTime) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

		return getTimeSince(postTime, dateFormat);
	}

	/**
	 * Returns a string describing 'time' as a time relative to 'now'.
	 * 
	 * @param postTime
	 *            : Time to compare with now.
	 * @param dateFormat
	 *            : Date format of posttime
	 * @return : "0s" if future date is passed, Abbreviated date in case of
	 *         postTime is more than a week old. In default cases it will return
	 *         values like "10s" "20m" "5h" "3d"
	 */
	public static String getTimeSince(String postTime, SimpleDateFormat dateFormat) {
		TimeZone timeZone = TimeZone.getDefault();
		// dateFormat.setTimeZone(timeZone);
		try {

			Date postdate = dateFormat.parse(postTime);

			long postTimeStamp = postdate.getTime() + timeZone.getRawOffset();

			long now = System.currentTimeMillis();
			boolean past = (now > postTimeStamp);
			long duration = Math.abs(now - postTimeStamp);

			long count;
			String suffix;

			String date;
			if (!past)
				return "0s";
			else {
				if (duration < DateUtils.MINUTE_IN_MILLIS) {
					count = duration / DateUtils.SECOND_IN_MILLIS;
					suffix = "s";
					date = count + suffix;

				} else if (duration < DateUtils.HOUR_IN_MILLIS) {
					count = duration / DateUtils.MINUTE_IN_MILLIS;
					suffix = "min";
					date = count + suffix;
				} else if (duration < DateUtils.DAY_IN_MILLIS) {
					count = duration / DateUtils.HOUR_IN_MILLIS;
					suffix = "h";
					date = count + suffix;
				} else if (duration < DateUtils.WEEK_IN_MILLIS) {
					count = duration / DateUtils.DAY_IN_MILLIS;
					suffix = "d";
					date = count + suffix;
				} else {
					// We know that we won't be showing the time, so it is safe
					// to pass
					// in a null context.
					date = DateUtils.formatDateRange(null, postTimeStamp, postTimeStamp, DateUtils.FORMAT_ABBREV_ALL);
				}
			}

			return date;

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Replace last occurence of last subString in String 
	 * @param String
	 * @param Sub String
	 * @param replacement
	 * @return
	 */
	public static String replaceLast(String string, String subString, String replacement) {
		return string.replaceFirst("(?s)" + subString + "(?!.*?" + subString + ")", replacement);
	}

}