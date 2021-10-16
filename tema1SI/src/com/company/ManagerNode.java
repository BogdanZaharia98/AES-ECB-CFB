package com.company;

public class ManagerNode{

    byte K=new byte(128);
    byte k1=new byte(128);
    byte k2=new byte(128);
    static byte initialization[];

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }


}