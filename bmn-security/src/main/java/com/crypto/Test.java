package com.crypto;

import bean.TicketBean;
import com.bmn.util.JsonUtils;
import com.bmn.util.RandomStringUtils;
import com.digest.SHACoder;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.stream.events.Characters;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class Test {

    public static void main(String[] args)
        throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException,
        InvalidKeyException, InvalidKeySpecException, InvalidAlgorithmParameterException, DecoderException,
        UnsupportedEncodingException {
        desCoder();

        aesCoder();

        pbeCoder();

        testSha128withAesTicket();
    }

    public static String aesKey = "";

    private static void testSha128withAesTicket()
        throws NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException,
        InvalidKeySpecException, InvalidKeyException, DecoderException, UnsupportedEncodingException {
        String ticket = RandomStringUtils.randomAlphanumeric(30);

        System.out.println("ticket : " + ticket);

        byte[] data = ticket.getBytes("utf-8");

        byte[] digest = SHACoder.encodeSHA256(data);
        //String v = Hex.encodeHexString(digest);

        byte[] key = AESCoder.initKey();

        aesKey = Base64.getEncoder().encodeToString(key);

        byte[] encrypt = AESCoder.encrypt(data, key);

        TicketBean bean = new TicketBean();
        String v = Hex.encodeHexString(digest);
        bean.setSha256(v);
        bean.setTicket(Hex.encodeHexString(encrypt));

        String m = JsonUtils.toJson(bean);

        System.out.println("加密前消息：\n" + m);

        String msg = Base64.getEncoder().encodeToString(m.getBytes());
        System.out.println("加密消息: " + msg);

        byte[] mr = Base64.getDecoder().decode(msg.getBytes());
        String ss = new String(mr);

        System.out.println("加密消息：\n" + ss);

        TicketBean bean2 = JsonUtils.fromJson(ss, TicketBean.class);
        byte[] encrypt2 = Hex.decodeHex(bean2.getTicket().toCharArray());

        byte[] key2 = Base64.getDecoder().decode(aesKey.getBytes());
        byte[] data2 = AESCoder.decrypt(encrypt2, key2);

        System.out.println("解密ticket: " + new String(data2));

        byte[] digest2 = SHACoder.encodeSHA256(data2);
        String v2 = Hex.encodeHexString(digest2);
        System.out.println(v.equals(v2));
    }

    private static void desCoder()
        throws NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        String str = "DES";
        byte[] input = str.getBytes();

        byte[] key = DESCoder.initKey();

        Base64.Encoder encoder = Base64.getEncoder();
        String base64Key = encoder.encodeToString(key);
        System.out.println(base64Key);

        byte[] key2 = Base64.getDecoder().decode(base64Key);

        System.out.println(Arrays.equals(key, key2));

        byte[] encrypt = DESCoder.encrypt(input, key);

        System.out.println(encoder.encodeToString(encrypt));

        byte[] decrypt = DESCoder.decrypt(encrypt, key);

        System.out.println(new String(decrypt));
    }

    private static void aesCoder()
        throws NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        String str = "AES";

        byte[] input = str.getBytes();

        byte[] key = AESCoder.initKey();

        Base64.Encoder encoder = Base64.getEncoder();
        String base64Key = encoder.encodeToString(key);
        System.out.println(base64Key);

        String hexAesKey = Hex.encodeHexString(key);
        System.out.println("hexAesKey = " + hexAesKey);

        byte[] key2 = Base64.getDecoder().decode(base64Key);

        System.out.println(Arrays.equals(key, key2));

        byte[] encrypt = AESCoder.encrypt(input, key);

        System.out.println(encoder.encodeToString(encrypt));

        try {
            byte[] key3 = Hex.decodeHex(hexAesKey.toCharArray());

            byte[] decrypt = AESCoder.decrypt(encrypt, key3);

            System.out.println(new String(decrypt));
        } catch (DecoderException e) {
            e.printStackTrace();
        }


    }


    public static  void pbeCoder()
        throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException,
        BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        String str = "PBE";
        System.out.println("原文: "  + str);

        byte[] data = str.getBytes();

        String passwd = "zhangyuqiang";
        System.out.println("密码: " + passwd);

        byte[] slat = PBECoder.initSlat();
        System.out.println("盐：" + Base64.getEncoder().encodeToString(slat));

        byte[] encrypt = PBECoder.encrypt(data, passwd, slat);
        System.out.println("加密后: " + Base64.getEncoder().encodeToString(encrypt));

        byte[] decrypt = PBECoder.decrypt(encrypt, passwd, slat);
        System.out.println("解密后: " + new String(decrypt));

    }
}
