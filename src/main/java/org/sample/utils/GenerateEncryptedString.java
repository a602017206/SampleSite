package org.sample.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 简单加密处理
 *
 * @author dw
 */
public class GenerateEncryptedString {


    public static String SHA1 = "SHA-1";
    public static String SHA256 = "SHA-256";
    public static String SHA384 = "SHA-384";
    public static String SHA512 = "SHA-512";
    public static String MD5 = "MD5";

    public static void main(String args[]) {
        String signString = "admin";
        String type = SHA1;
        String result = sign(signString, type);
        System.out.println("采用" + type + "加密之后的串为：" + result);
        type = MD5;
        result = sign(signString, type);
        System.out.println("采用" + type + "加密之后的串为：" + result);
        type = SHA256;
        result = sign(signString, type);
        System.out.println("采用" + type + "加密之后的串为：" + result);
        type = SHA384;
        result = sign(signString, type);
        System.out.println("采用" + type + "加密之后的串为：" + result);
        type = SHA512;
        result = sign(signString, type);
        System.out.println("采用" + type + "加密之后的串为：" + result);
    }

    // 签名
    public static String sign(String str, String type) {
        String s = Encrypt(str, type);
        return s;
    }

    public static String Encrypt(String strSrc, String encName) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance(encName);
            md.update(bt);
            // to HexString
            strDes = bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

    /**
     * gb2312转unicode
     *
     * @param gbString
     * @return
     */
    public static String gbEncoding(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }

    /**
     * unicode解码
     *
     * @param dataStr
     * @return
     */
    public static String decodeUnicode(final String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
            buffer.append(new Character(letter));
            start = end;
        }
        return buffer.toString();
    }

}
