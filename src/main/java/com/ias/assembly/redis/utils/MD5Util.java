package com.ias.assembly.redis.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * md5
 * 
 * @author sun create 2013-2-4上午9:35:50
 */
public class MD5Util {
	
	private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	
	public final static String md5(byte[] bs) {
        try {
            MessageDigest o = MessageDigest.getInstance("MD5");
            o.update(bs);
            byte[] r = o.digest();
            return toHex(r);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	private static String toHex(byte[] bs) {
        int j = bs.length;
        char rs[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = bs[i];
            rs[k++] = hexDigits[byte0 >>> 4 & 0xf];
            rs[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(rs);
    }
	
	public static String getMD5Str(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();
	}
	
	/**
	 * 对base64加密的byte[]类型的数据进行base64解密
	 * 
	 */
	public static byte[] decode(byte[] businessXmlData) {
		if (businessXmlData == null) {
			return null;
		}
		return Base64.getDecoder().decode(businessXmlData);
	}

	/**
	 * 对byte[]加密生成的String类型的数据进行base64解密
	 * 
	 */
	public static byte[] decode(String businessXmlData) {
		return decode(businessXmlData.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * 对byte[]类型的数据进行base64加密
	 * 
	 */
	public static String encode(byte[] byteData) {
		if (byteData == null) {
			return null;
		}
		return Base64.getEncoder().encodeToString(byteData);
	}

	/**
	 * 对String类型的数据进行base64加密
	 * 
	 */
	public static String encode(String strData) {
		if (strData == null) {
			return null;
		}
		return encode(strData.getBytes());
	}

	public static void main(String[] args) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		System.out.println(MD5Util.getMD5Str(MD5Util.getMD5Str("123456") + "admin"));
		System.out.println(MD5Util.getMD5Str("admin"));
		System.out.println(MD5Util.getMD5Str("jd"));
	}
}
