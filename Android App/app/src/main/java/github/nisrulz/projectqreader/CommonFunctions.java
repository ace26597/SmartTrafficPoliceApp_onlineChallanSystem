package github.nisrulz.projectqreader;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("SimpleDateFormat")
public class CommonFunctions {

	private Context _context;
	
	
	public CommonFunctions(Context context) {
		_context = context;
	}

	/**
	 * This is used to check weather Internet is on or off

	 * @return
	 */
	public boolean checkInternet() {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
			
			if (connectivity != null) {
				NetworkInfo[] info = connectivity.getAllNetworkInfo();
				if (info != null)
					for (int i = 0; i < info.length; i++)
						//check if network is connected or device is in range
						if (info[i].getState() == NetworkInfo.State.CONNECTED) {
							return true;
						}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * This is used to check is gps enable or not

	 * @return
	 */
	public boolean checkGPS() {
		//create result variable
    	boolean canGetLocation = false;
        try {
        	//create location manager class
            LocationManager locationManager = (LocationManager) _context.getSystemService(Context.LOCATION_SERVICE);
            // getting GPS status
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
 
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            	canGetLocation = false;
            } else {
                canGetLocation = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return
        return canGetLocation;
    }
	
	/**
	 * This is used to get normal font.
	 * @author Swapnil 
	 * @return AppleGaramond-Light font
	 */
	public Typeface getNormalFont()
	{
		return Typeface.createFromAsset(_context.getAssets(),"fonts/AppleGaramond-Light.ttf");
	}
	
	/**
	 * This is used to get Bold font.

	 * @return AppleGaramond-Light font
	 */
	public Typeface getBoldFont()
	{
		return Typeface.createFromAsset(_context.getAssets(),
				"fonts/AppleGaramond-Light.ttf");
	}
	
	/**
	 * This is used to check email format

	 * @param email
	 * @return
	 */
	public boolean checkEmail(String email)
	{
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches())
		{
			isValid = true;
		}
		return isValid;
	}
	
	/**
	 * This is used to show No Internet connection Toast
	 * @author Swapnil 
	 */
	public void showNoInternetConnection()
	{
		//get message from string.xml
		Toast.makeText(_context, "No Internet Connection", Toast.LENGTH_LONG).show();
	}
	
	/**
	 * This is used to show Data Found Toast

	 */
	public void showNoDataFound()
	{
		//get message from string.xml
		Toast.makeText(_context, "No Data Found", Toast.LENGTH_LONG).show();
	}
	
	/**
	 * This is used to show Messages from Resource file

	 * @param msgId
	 */
	public void showMessage(int msgId)
	{
		//get message from string.xml
		String message = _context.getString(msgId);
		Toast.makeText(_context, message, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * This is used to show String Message

	 * @param message
	 */
	public void showMessage(String message)
	{
		Toast.makeText(_context, message, Toast.LENGTH_LONG).show();
	}
	
	
	
	/**
	 * This is used to get Date in specified format

	 * @param pattern in which date string is return
	 * @return date in String format
	 */
	@SuppressLint("SimpleDateFormat")
	public String getDateString(String pattern)
	{
		//create date format
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String dateString = sdf.format(new Date());
		//return date
		return dateString;
	}
	
	/**
	 * This is used to convert date and time in MM-dd-yyyy HH:mm PM/AM

	 */
	public String[] getDateInFormate(String dateTimeString)
	{
		Log.e("Incoming Date", dateTimeString);
		//store date in 0 index and time in 1st index
		String dateTime[] = dateTimeString.split("\\s");
		//create result
		String result[] = new String[2];
		
		String date[] = dateTime[0].split("-");
		String time = dateTime[1];
		
		//create date format
		DateFormat outputFormat = new SimpleDateFormat("hh:mm:ss a");
		DateFormat inputFormat = new SimpleDateFormat("hh:mm:ss");
		
		Date d1 = null;
		try {
			d1 = inputFormat.parse(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		result[0] = date[1] + "-" + date[2] + "-" + date[0];
		result[1] = (d1 == null)?"":outputFormat.format(d1);
		
		// This is for display purpose only
		ArrayList<String> list = new ArrayList<String>();
		list.add(result[0]);
		list.add(result[1]);
		System.out.println("Outgoing Date"+list);
		
		return result;
	}
	
	/**
	 * This is used to get GMT date format

	 * @return date in gmt format
	 */
	public String getGMTDate()
	{
		DateFormat TWENTY_FOUR_TF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TWENTY_FOUR_TF.setTimeZone(TimeZone.getTimeZone("gmt"));
		String gmtDate = TWENTY_FOUR_TF.format(new Date());
		
		return gmtDate;
	}
	
	
	/**
	 * This is used to format double value into 4 digits
	 * @param value 
	 * @return String value upto 4 decimal places
	 */
	public String formatTo4Digit(double value)
	{
		//create formatter
		DecimalFormat format = new DecimalFormat("##.####");
		//return formatted value
		return format.format(value);
	}
	
	/**
	 * This is used to format double value into 4 digits
	 * @param
	 * @return String value upto 4 decimal places
	 */
	public String formatTo4Digit(String string)
	{
		Log.e("Incoming value", string);
		// convert number into 
		double value = Double.parseDouble(string);
		//create formatter
		DecimalFormat format = new DecimalFormat("##.####");
		
		Log.e("Outgoing value", format.format(value));
		//return formatted value
		return format.format(value);
	}
	
	
	/**
	 * This is used to add marquee effect to textview
     * @param textView
	 */
	public void addMarqueeEffect(TextView textView)
	{
		TextView tf = textView;
		tf.setEllipsize(TruncateAt.MARQUEE);
		tf.setMarqueeRepeatLimit(-1);
		tf.setSingleLine(true);
		tf.setSelected(true);
	}
	
	
	/**
	 * This is used to add marquee effect to textview

	 * @param
	 */
	public  boolean checkMobileNumber(String mob)
	{
		boolean flag=false;
		
		if(mob.length()==10)
			flag=true;
		
		return flag;
	}


    public  boolean checkEditText(EditText et)
    {
        boolean flag=false;

        if(et.getText().toString().trim().length()> 0)
            flag=true;

        return flag;
    }

	public  boolean check_Pin_Code(String pinCode)
	{
		boolean flag=false;
		
		if(pinCode.length()==6)
			flag=true;
		
		return flag;
	}


}
