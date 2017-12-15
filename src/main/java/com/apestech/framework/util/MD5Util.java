package com.apestech.framework.util;

import java.security.MessageDigest;

/**
 * 功能：MD5工具
 *
 * @author xul
 * @create 2017-12-14 17:33
 */
public class MD5Util {

    public static String encrypt(String text) {

        try {
            MessageDigest alg = MessageDigest.getInstance("MD5");
            alg.update(text.getBytes("UTF-8"));
            byte[] digesta = alg.digest();
            return byte2hex(digesta);
        } catch (Exception NsEx) {
            return null;
        }
    }

    private static String byte2hex(byte[] bstr) {
        StringBuffer hs = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < bstr.length; n++) {
            stmp = (java.lang.Integer.toHexString(bstr[n] & 0XFF));
            if (stmp.length() == 1){
                hs.append("0");
                hs.append(stmp);
            }else{
                hs.append(stmp);
            }
        }
        return hs.toString();
    }

}