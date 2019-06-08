package mx.qsistemas.payments_transfer.utils;


import android.text.TextUtils;

import com.basewin.log.LogUtil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author liudeyu
 * @version 1.0.0
 * @function
 * @lastmodify
 */
public class StringHelper {
	private final String TAG = "StringHelper";
	private final static int MAXAMTNUM = 12;

	/**
	 * @param amtStr
	 */
	public static String changeAmout(String amtStr) {
		String str = amtStr.toString();
		String cuttedStr = str;
		for (int i = str.length() - 1; i >= 0; i--) {
			char c = str.charAt(i);
			if ('.' == c) {
				cuttedStr = str.substring(0, i) + str.substring(i + 1);
				break;
			}
		}
		int NUM = cuttedStr.length();
		int zeroIndex = -1;
		for (int i = 0; i < NUM - 2; i++) {
			char c = cuttedStr.charAt(i);
			if (c != '0') {
				zeroIndex = i;
				break;
			} else if (i == NUM - 3) {
				zeroIndex = i;
				break;
			}
		}
		if (zeroIndex != -1) {
			cuttedStr = cuttedStr.substring(zeroIndex);
		}
		if (cuttedStr.length() < 3) {
			cuttedStr = "0" + cuttedStr;
		}

		if (str.length() > MAXAMTNUM) {
			cuttedStr = cuttedStr.substring(0, MAXAMTNUM);
		}
		if (cuttedStr.length() > 2) {
			cuttedStr = cuttedStr.substring(0, cuttedStr.length() - 2) + "." + cuttedStr.substring(cuttedStr.length() - 2);
		}
		return cuttedStr;
	}

	public static String getStringWithoutDot(String srcString) {
		return srcString.replace(".", "");
	}

	/**
	 * @Title: formatCardNum
	 * @Description: TODO
	 * @param cardNum
	 * @param hidden
	 * @return
	 * @return: String
	 */
	public static String formatCardNum(String cardNum, boolean hidden) {
		if (cardNum == null)
			return "";
		if (cardNum.length() < 13 || cardNum.length() > 20)
			return "";

		String Number = "";
		String cardF = cardNum.substring(0, 6);
		String cardB = cardNum.substring(cardNum.length() - 4);
		String padd = "*******************";
		if (hidden) {
			Number = cardF + padd.substring(0, cardNum.length() - 10) + cardB;
		} else {
			Number = cardNum;
		}
		return Number;
	}
	
	/**
	 * @Title: fillFrontSpace 
	 * @param str
	 * @param num
	 * @author xieruihua
	 * @return: String
	 */
	public static String fillFrontSpace(Object obj, int num) {
		if (obj == null) {
			return "";
		}
		int max = String.valueOf(obj).replaceAll("[\u4e00-\u9fa5]", "aa").length();
		int min = String.valueOf(obj).length();
		return String.format("%1$" + (num + min - max) + "s", obj);
	}
	
	/**
	 * @Title: fillBackSpace 
	 * @param str
	 * @param num
	 * @author xieruihua
	 * @return: String
	 */
	public static String fillBackSpace(Object obj, int num) {
		if (obj == null) {
			return "";
		}
		int max = String.valueOf(obj).replaceAll("[\u4e00-\u9fa5]", "aa").length();
		int min = String.valueOf(obj).length();
		return String.format("%-" + (num + min - max) + "s", obj);
	}
	
	/**
	 * @Title: formatAmout 
	 * @param amt
	 * @author xieruihua
	 * @return: String
	 */
	public static String formatAmout(double amt) {
		DecimalFormat df = new DecimalFormat("#########0.00");
		String am = df.format(amt).replace(".", "");
		return String.format("%0" + Math.max(12 - am.length(), 1) + "d", 0)+ am;
	}

	/**
	 * @Title: formatTimeStamp 
	 * @param timestring
	 * @return: String
	 */
	public static String formatTimeStamp(String timestamp) {
		if (timestamp == null) {
			return "";
		}
		try {
			Date date = new SimpleDateFormat("yyyyMMddhhmmss").parse(timestamp);
			return new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * @return
	 */
	public static boolean ifBigerThan(String amt1,String amt2)
	{
		Long lamt1 = Long.parseLong(amt1.replace(".", ""));
		Long lamt2 = Long.parseLong(amt2.replace(".", ""));
		return (lamt1 >= lamt2 ? true:false);
	}
	
	/**
	 * @return
	 */
	public static boolean ifBigerThan2(String amt1,String amt2)
	{
		Long lamt1 = Long.parseLong(amt1.replace(".", ""));
		Long lamt2 = Long.parseLong(amt2.replace(".", ""));
		return (lamt1 > lamt2 ? true:false);
	}
	
	/**
	 * @Title: getNoRefundAmount 
	 * @param maxAmt
	 * @param minAmt
	 * @return: String
	 */
	public static String getNoRefundAmount(String maxAmt, String minAmt) {
		if (TextUtils.isEmpty(maxAmt) || TextUtils.isEmpty(minAmt)) {
			throw new IllegalArgumentException("the input parameters can't be empty!");
		}
		DecimalFormat df = new DecimalFormat("#########0.00");
		double differ = Double.valueOf(maxAmt) - Double.valueOf(minAmt);
		return df.format(Math.abs(differ));
	}
}
