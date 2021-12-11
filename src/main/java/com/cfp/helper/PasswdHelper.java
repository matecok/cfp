package com.cfp.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class PasswdHelper {

	private final static Logger logger = LoggerFactory.getLogger(PasswdHelper.class);
	public static String md5Password(String password) {
		try {
			// 得到一个信息摘要器
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] result = digest.digest(password.getBytes());
			StringBuffer buffer = new StringBuffer();
			// 把没一个byte 做一个与运算 0xff;
			for (byte b : result) {
				// 与运算
				int number = b & 0xff;// 加盐
				String str = Integer.toHexString(number);
				if (str.length() == 1) {
					buffer.append("0");
				}
				buffer.append(str);
			}
			// 标准的md5加密后的结果
			return buffer.toString().toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			logger.error(ErrorHelper.getErrorMessage(e));
			return "";
		}
	}

	public static String radomChar(Integer length) {
		String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
	            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
	            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
	            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
	            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
	            "W", "X", "Y", "Z", "!", "@", "#", "$", "%", "&", "*", "+", "-",
	            "(", ")", "[", "]", "{", "}", ".", "?", "<", ">", ",", "_", "=" };
		StringBuffer shortBuffer = new StringBuffer(); //new passwd
	    String uuid = UUID.randomUUID().toString().replace("-", "");
	    for (int i = 0; i < length; i++) {
	    	int a=i * 4;
	    	if(i*4 +4>uuid.length()) {
	    		a=uuid.length()-4;
	    	}
	        String str = uuid.substring(a, a + 4);
	        int x = Integer.parseInt(str, 16);
	        shortBuffer.append(chars[x % 0x3E]);
	    }
	    return shortBuffer.toString();
	}
}
