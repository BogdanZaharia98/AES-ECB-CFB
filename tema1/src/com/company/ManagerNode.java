package com.company;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class ManagerNode{

    byte[] K;
    byte[] k1;
    byte[] k2;
    static byte initialization[];

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }


    public void setKeys(String myKey0,String myKey1,String myKey2)
    {
        byte[] key;
        SecretKeySpec secretKey;
        MessageDigest sha = null;
        try {
            key = myKey0.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
            K=secretKey.getEncoded();

            k1=Arrays.copyOf(myKey1.getBytes(StandardCharsets.UTF_8),16);
            k2=Arrays.copyOf(myKey2.getBytes(StandardCharsets.UTF_8),16);

        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public byte[] encryptKey(byte[] key)
    {
        try
        {

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(K,"AES"));
            return Base64.getEncoder().encode(cipher.doFinal(key));

        }
        catch (Exception e)
        {
            System.out.println("Eroare: " + e.toString());
        }
        return null;
    }


}