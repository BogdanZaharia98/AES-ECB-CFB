package com.company;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

public class Node {

    static byte iV[][];
    static byte iV2[][];
    int mode;
    byte[] K;
    byte[] k1;




    public void setK(byte[] k) {
        K = k;
    }

    public void setK1(byte[] k1) {
        this.k1 = k1;
    }

    public static void setInitialization() {
        byte[] iv = new byte[16];
        byte[][] iv2=new byte[4][4];
        int ind=0;
        new SecureRandom().nextBytes(iv);
        for(int i=0;i<4;i++)
            for(int j=0;j<4;j++){
                iv2[i][j]=iv[ind];
            }
        Node.iV = iv2;
        Node.iV2 = iv2;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    public String toBinary(int num, int length) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(((num & 1) == 1) ? '1' : '0');
            num >>= 1;
        }

        return sb.reverse().toString();
    }

    public byte[] decrypt()
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(K,"AES"));
            return cipher.doFinal(Base64.getDecoder().decode(k1));
        }
        catch (Exception e)
        {
            System.out.println("Eroare: " + e.toString());
        }
        return null;
    }

    public byte[] rotateWord(byte[] k){
        byte tmp;
        tmp=k[0];
        for(int i=0;i<3;i++){
            k[i]=k[i+1];
        }
        k[3]=tmp;
        return k;
    }


    public byte getInverse(byte b){
        int[][] matrice=new int[16][16];
        matrice[0]=new int[]{ 0x00, 0x01, 0x8D, 0xF6, 0xCB, 0x52, 0x7B, 0xD1, 0xE8, 0x4F, 0x29, 0xC0, 0xB0, 0xE1, 0xE5, 0xC7 };
        matrice[1]=new int[]{ 0x74, 0xB4, 0xAA, 0x4B, 0x99, 0x2B, 0x60, 0x5F, 0x58, 0x3F, 0xFD, 0xCC, 0xFF, 0x40, 0xEE, 0xB2 };
        matrice[2]=new int[]{ 0x3A, 0x6E, 0x5A, 0xF1, 0x55, 0x4D, 0xA8, 0xC9, 0xC1, 0x0A, 0x98, 0x15, 0x30, 0x44, 0xA2, 0xC2 };
        matrice[3]=new int[]{ 0x2C, 0x45, 0x92, 0x6C, 0xF3, 0x39, 0x66, 0x42, 0xF2, 0x35, 0x20, 0x6F, 0x77, 0xBB, 0x59, 0x19 };
        matrice[4]=new int[]{ 0x1D, 0xFE, 0x37, 0x67, 0x2D, 0x31, 0xF5, 0x69, 0xA7, 0x64, 0xAB, 0x13, 0x54, 0x25, 0xE9, 0x09 };
        matrice[5]=new int[]{ 0xED, 0x5C, 0x05, 0xCA, 0x4C, 0x24, 0x87, 0xBF, 0x18, 0x3E, 0x22, 0xF0, 0x51, 0xEC, 0x61, 0x17 };
        matrice[6]=new int[]{ 0x16, 0x5E, 0xAF, 0xD3, 0x49, 0xA6, 0x36, 0x43, 0xF4, 0x47, 0x91, 0xDF, 0x33, 0x93, 0x21, 0x3B };
        matrice[7]=new int[]{ 0x79, 0xB7, 0x97, 0x85, 0x10, 0xB5, 0xBA, 0x3C, 0xB6, 0x70, 0xD0, 0x06, 0xA1, 0xFA, 0x81, 0x82 };
        matrice[8]=new int[]{ 0x83, 0x7E, 0x7F, 0x80, 0x96, 0x73, 0xBE, 0x56, 0x9B, 0x9E, 0x95, 0xD9, 0xF7, 0x02, 0xB9, 0xA4 };
        matrice[9]=new int[]{ 0xDE, 0x6A, 0x32, 0x6D, 0xD8, 0x8A, 0x84, 0x72, 0x2A, 0x14, 0x9F, 0x88, 0xF9, 0xDC, 0x89, 0x9A };
        matrice[10]=new int[]{ 0xFB, 0x7C, 0x2E, 0xC3, 0x8F, 0xB8, 0x65, 0x48, 0x26, 0xC8, 0x12, 0x4A, 0xCE, 0xE7, 0xD2, 0x62 };
        matrice[11]=new int[]{ 0x0C, 0xE0, 0x1F, 0xEF, 0x11, 0x75, 0x78, 0x71, 0xA5, 0x8E, 0x76, 0x3D, 0xBD, 0xBC, 0x86, 0x57 };
        matrice[12]=new int[]{ 0x0B, 0x28, 0x2F, 0xA3, 0xDA, 0xD4, 0xE4, 0x0F, 0xA9, 0x27, 0x53, 0x04, 0x1B, 0xFC, 0xAC, 0xE6 };
        matrice[13]=new int[]{ 0x7A, 0x07, 0xAE, 0x63, 0xC5, 0xDB, 0xE2, 0xEA, 0x94, 0x8B, 0xC4, 0xD5, 0x9D, 0xF8, 0x90, 0x6B };
        matrice[14]=new int[]{ 0xB1, 0x0D, 0xD6, 0xEB, 0xC6, 0x0E, 0xCF, 0xAD, 0x08, 0x4E, 0xD7, 0xE3, 0x5D, 0x50, 0x1E, 0xB3 };
        matrice[15]=new int[]{ 0x5B, 0x23, 0x38, 0x34, 0x68, 0x46, 0x03, 0x8C, 0xDD, 0x9C, 0x7D, 0xA0, 0xCD, 0x1A, 0x41 ,0x1C };

        String s2= Integer.toHexString(b);
        String s;
        if (s2.length()==1){
             s="0"+s2;
        }
        else s=s2;

        for(int i=0;i<16;i++){
            for (int j=0;j<16;j++){
                if(s.charAt(0)=='a'&&i==10){
                    if(s.charAt(1)==((char)j)&&j<10){
                        b= (byte) matrice[10][j];
                    }
                    else if (s.charAt(1)=='a') b= (byte) matrice[10][10];
                    else if (s.charAt(1)=='b') b= (byte) matrice[10][11];
                    else if (s.charAt(1)=='c') b= (byte) matrice[10][12];
                    else if (s.charAt(1)=='d') b= (byte) matrice[10][13];
                    else if (s.charAt(1)=='e') b= (byte) matrice[10][14];
                    else if (s.charAt(1)=='f') b= (byte) matrice[10][15];
                }
                else if(s.charAt(0)=='b'&&i==11){
                    if(s.charAt(1)==((char)j)&&j<10){
                        b= (byte) matrice[11][j];
                    }
                    else if (s.charAt(1)=='a') b= (byte) matrice[11][10];
                    else if (s.charAt(1)=='b') b= (byte) matrice[11][11];
                    else if (s.charAt(1)=='c') b= (byte) matrice[11][12];
                    else if (s.charAt(1)=='d') b= (byte) matrice[11][13];
                    else if (s.charAt(1)=='e') b= (byte) matrice[11][14];
                    else if (s.charAt(1)=='f') b= (byte) matrice[11][15];
                }
                else if(s.charAt(0)=='c'&&i==12){
                    if(s.charAt(1)==((char)j)&&j<10){
                        b= (byte) matrice[12][j];
                    }
                    else if (s.charAt(1)=='a') b= (byte) matrice[12][10];
                    else if (s.charAt(1)=='b') b= (byte) matrice[12][11];
                    else if (s.charAt(1)=='c') b= (byte) matrice[12][12];
                    else if (s.charAt(1)=='d') b= (byte) matrice[12][13];
                    else if (s.charAt(1)=='e') b= (byte) matrice[12][14];
                    else if (s.charAt(1)=='f') b= (byte) matrice[12][15];
                }
                else if(s.charAt(0)=='d'&&i==13){
                    if(s.charAt(1)==((char)j)&&j<10){
                        b= (byte) matrice[13][j];
                    }
                    else if (s.charAt(1)=='a') b= (byte) matrice[13][10];
                    else if (s.charAt(1)=='b') b= (byte) matrice[13][11];
                    else if (s.charAt(1)=='c') b= (byte) matrice[13][12];
                    else if (s.charAt(1)=='d') b= (byte) matrice[13][13];
                    else if (s.charAt(1)=='e') b= (byte) matrice[13][14];
                    else if (s.charAt(1)=='f') b= (byte) matrice[13][15];
                }
                else if(s.charAt(0)=='e'&&i==14){
                    if(s.charAt(1)==((char)j)&&j<10){
                        b= (byte) matrice[14][j];
                    }
                    else if (s.charAt(1)=='a') b= (byte) matrice[14][10];
                    else if (s.charAt(1)=='b') b= (byte) matrice[14][11];
                    else if (s.charAt(1)=='c') b= (byte) matrice[14][12];
                    else if (s.charAt(1)=='d') b= (byte) matrice[14][13];
                    else if (s.charAt(1)=='e') b= (byte) matrice[14][14];
                    else if (s.charAt(1)=='f') b= (byte) matrice[14][15];
                }
                else if(s.charAt(0)=='f'&&i==15){
                    if(s.charAt(1)==((char)j)&&j<10){
                        b= (byte) matrice[15][j];
                    }
                    else if (s.charAt(1)=='a') b= (byte) matrice[15][10];
                    else if (s.charAt(1)=='b') b= (byte) matrice[15][11];
                    else if (s.charAt(1)=='c') b= (byte) matrice[15][12];
                    else if (s.charAt(1)=='d') b= (byte) matrice[15][13];
                    else if (s.charAt(1)=='e') b= (byte) matrice[15][14];
                    else if (s.charAt(1)=='f') b= (byte) matrice[15][15];
                }
                else if(s.charAt(0)==((char)i)&&i<10){
                    if(s.charAt(1)==((char)j)&&j<10){
                        b= (byte) matrice[i][j];
                    }
                    else if (s.charAt(1)=='a') b= (byte) matrice[i][10];
                    else if (s.charAt(1)=='b') b= (byte) matrice[i][11];
                    else if (s.charAt(1)=='c') b= (byte) matrice[i][12];
                    else if (s.charAt(1)=='d') b= (byte) matrice[i][13];
                    else if (s.charAt(1)=='e') b= (byte) matrice[i][14];
                    else if (s.charAt(1)=='f') b= (byte) matrice[i][15];

                }

            }
        }

        return b;
    }

    public byte[][] expandKeyEnc(){

        int rcon[]= new int[44];

        int i=0,j=0;
        for(;i<11;i++) {
            for (j=1; j < 4; j++) {
                if(j>0)
                    rcon[i*4+j]=0x0;
            }
        }
        rcon[0]=0x8d;
        rcon[4]=0x1;
        rcon[8]=0x2;
        rcon[12]=0x4;
        rcon[16]=0x8;
        rcon[20]=0x10;
        rcon[24]=0x20;
        rcon[28]=0x40;
        rcon[32]=0x80;
        rcon[36]=0x1B;
        rcon[40]=0x36;



        int[] V=new int[8];
        int[] s=new int[8];
        int N=4;
        byte[][] key=new byte[4][4];
        i=0;j=0;int ind=0;
        for(;i<4;i++) {
            for (; j < 4; j++) {
                key[i][j]=k1[ind];
                ind++;
            }
        }


        byte[][] expKey=new byte[44][4];
        for(i=0;i<44;i++){
            if(i<4) expKey[i]=key[i];
            else {
                if(i%N==0){
                    byte[] x =rotateWord(expKey[i-1]);
                    for(int l=0;l<4;l++){
                        if(x[l]!=0)
                            x[l]=getInverse(x[l]);
                    }

                    for ( j=0;j<4;j++){
                        x[j]= (byte) ( x[j] ^ (x[j]<<1) ^ (x[j]<<2) ^ (x[j]<<3) ^ (x[j]<<4) ^ 0x63 );
                        if(i<4)
                            x[j]= (byte) (x[j]^((byte)rcon[j]));
                        else  x[j]= (byte) (x[j]^((byte)rcon[(i/4)*4+j]));
                        expKey[i][j]= (byte) (x[j] ^ expKey[i - N][j]);

                    }
                }
                else {

                    byte[] x = expKey[i - 1];

                    for(int l=0;l<4;l++){
                        x[l]=getInverse(x[l]);
                    }
                    for (j = 0; j < 4; j++) {

                        expKey[i][j] = (byte) (x[j] ^ expKey[i - N][j]);

                    }

                }
            }

        }
        return expKey;
    }


    public byte[][] expandKeyDec(){

        int rcon[]= new int[44];

        int i=0,j=0;
        for(;i<11;i++) {
            for (j=1; j < 4; j++) {
                if(j>0)
                    rcon[i*4+j]=0x0;
            }
        }
        rcon[0]=0x8d;
        rcon[4]=0x1;
        rcon[8]=0x2;
        rcon[12]=0x4;
        rcon[16]=0x8;
        rcon[20]=0x10;
        rcon[24]=0x20;
        rcon[28]=0x40;
        rcon[32]=0x80;
        rcon[36]=0x1B;
        rcon[40]=0x36;


        int[] V=new int[8];
        int[] s=new int[8];
        int N=4;
        byte[][] key=new byte[4][4];
        i=0;j=0;int ind=0;
        for(;i<4;i++) {
            for (; j < 4; j++) {
                key[i][j]=k1[ind];
                ind++;
            }
        }


        byte[][] expKey=new byte[44][4];
        for(i=0;i<44;i++){
            if(i<4) expKey[i]=key[i];
            else {
                if(i%N==0){
                    byte[] x =rotateWord(expKey[i-1]);


                    for ( j=0;j<4;j++){
                        x[j]= (byte) ( (x[j]<<1) ^ (x[j]<<3)  ^ (x[j]<<6) ^ 0x5 );
                        if(i<4)
                        x[j]= (byte) (x[j]^((byte)rcon[j]));
                        else  x[j]= (byte) (x[j]^((byte)rcon[(i/4)*4+j]));
                        expKey[i][j]= (byte) (x[j] ^ expKey[i - N][j]);

                    }
                    for(int l=0;l<4;l++){
                        expKey[i][l]=getInverse(expKey[i][l]);
                    }
                }
                else {

                    byte[] x=expKey[i-1];
                    for ( j=0;j<4;j++){
                        expKey[i][j]= (byte) (x[j] ^ expKey[i - N][j]);
                    }


                    for(int l=0;l<4;l++){
                        expKey[i][l]=getInverse(expKey[i][l]);
                    }
                }
            }
        }

        return expKey;

    }


    public void sendECB(byte[][] m,Node B){
        System.out.println(" ");
        System.out.println("<--Block mesaj");
        int i,j;


        byte [][] expKey=expandKeyEnc();

        for(i=0;i<4;i++){
            for(j=0;j<4;j++) {
                m[j][i]= (byte) (m[j][i]^(expKey[i][j]));
            }
        }

        for (int r=1;r<=10;r++) {
            //SubTypes
            for (i = 0; i < 4; i++) {
               for(j=0;j<4;j++) {
                   m[i][j]=getInverse(m[i][j]);
                   byte t =m[i][j];
                   m[i][j]= (byte) ( t ^ (t<<1) ^ (t<<2) ^ (t<<3) ^ (t<<4) ^ 0x63);
               }
            }





            //PERMUTATIONS
            byte tmp = m[1][0];
            for (i = 0; i < 3; i++) {
                m[1][i] = m[1][i + 1];
            }
            m[1][3] = tmp;

            tmp = m[2][0];
            m[2][0] = m[2][2];
            m[2][2] = tmp;
            tmp = m[2][1];
            m[2][1] = m[2][3];
            m[2][3] = tmp;

            tmp = m[3][0];
            m[3][0] = m[3][3];
            m[3][3] = m[3][2];
            m[3][2] = m[3][1];
            m[3][1] = tmp;

            //MIX COLUMNS UP TO 9th ROUND

            if(r!=9){
            byte[][] matrice = new byte[4][4];
            matrice[0] = new byte[]{2, 3, 1, 1};
            matrice[1] = new byte[]{1, 2, 3, 1};
            matrice[2] = new byte[]{1, 1, 2, 3};
            matrice[3] = new byte[]{3, 1, 1, 2};

            byte[][] m2 = new byte[4][4];
            for (int ind = 0; ind < 4; ind++)
                for (i = 0; i < 4; i++) {
                    m2[i][ind] = (byte) (matrice[i][0] * m[0][ind]);
                    for (j = 1; j < 4; j++) {
                        m2[i][ind] = (byte) ((matrice[i][j] * m[j][ind]) ^ m2[i][ind]);
                    }
                }
            m = m2;
            }

            //ADD ROUND KEY
            for (int ind = 0; ind < 4; ind++){
                for (i = 0; i < 4; i++) {
                    m[i][ind]= (byte) (m[i][ind]^expKey[r*4+ind][i]);
                }
            }
        }

        B.decECB(m);



    }


    public void sendCFB(byte[][] m, Node B){
        System.out.println(" ");
        System.out.println("<--Block mesaj");
        int rcon[][]= new int[10][4];
        byte[][] m2 = new byte[4][4];
        int i,j;


        byte [][] expKey=expandKeyEnc();

        for(i=0;i<4;i++){
            for(j=0;j<4;j++) {
                iV[j][i]= (byte) (iV[j][i]^(expKey[i][j]));
            }
        }

        for (int r=1;r<=10;r++) {
            //SubTypes
            for (i = 0; i < 4; i++) {
                for(j=0;j<4;j++) {
                    m[i][j]=getInverse(m[i][j]);
                    byte t =m[i][j];
                    m[i][j]= (byte) ( t ^ (t<<1) ^ (t<<2) ^ (t<<3) ^ (t<<4) ^ 0x63);
                }
            }

            //PERMUTATIONS
            byte tmp = iV[1][0];
            for (i = 0; i < 3; i++) {
                iV[1][i] = iV[1][i + 1];
            }
            iV[1][3] = tmp;

            tmp = iV[2][0];
            iV[2][0] = iV[2][2];
            iV[2][2] = tmp;
            tmp = iV[2][1];
            iV[2][1] = iV[2][3];
            iV[2][3] = tmp;

            tmp = iV[3][0];
            iV[3][0] = iV[3][3];
            iV[3][3] = iV[3][2];
            iV[3][2] = iV[3][1];
            iV[3][1] = tmp;

            if(r!=9) {
                byte[][] matrice = new byte[4][4];
                matrice[0] = new byte[]{2, 3, 1, 1};
                matrice[1] = new byte[]{1, 2, 3, 1};
                matrice[2] = new byte[]{1, 1, 2, 3};
                matrice[3] = new byte[]{3, 1, 1, 2};


                for (int ind = 0; ind < 4; ind++)
                    for (i = 0; i < 4; i++) {
                        m2[i][ind] = (byte) (matrice[i][0] * iV[0][ind]);
                        for (j = 1; j < 4; j++) {
                            m2[i][ind] = (byte) ((matrice[i][j] * iV[j][ind]) ^ m2[i][ind]);
                        }
                    }
            }

            //ADD ROUND KEY
            for (int ind = 0; ind < 4; ind++){
                for (i = 0; i < 4; i++) {
                    m2[i][ind]= (byte) (m2[i][ind]^expKey[r*4+ind][i]);
                }
            }
        }


        for (int ind = 0; ind < 4; ind++)
            for (i = 0; i < 4; i++) {
                m2[ind][i]= (byte) (m2[ind][i]^m[ind][i]);
            }
        iV=m2;
        B.decCFB(m2);
    }


    public void decECB(byte[][] m){
        System.out.println("Mesaj decriptat: ");
        int i,j;


        byte [][] expKey=expandKeyDec();

        for(i=0;i<4;i++){
            for(j=0;j<4;j++) {
                m[j][i]= (byte) (m[j][i]^(expKey[i][j]));
            }
        }

        for (int r=1;r<=10;r++) {
            //SubTypes
            for (i = 0; i < 4; i++) {
                for(j=0;j<4;j++) {
                    m[i][j]=getInverse(m[i][j]);
                    byte t =m[i][j];
                    m[i][j]= (byte) ( (t<<1) ^ (t<<3) ^ (t<<6)  ^ 0x5);
                }
            }

            //PERMUTATIONS
            byte tmp = m[1][0];
            for (i = 0; i < 3; i++) {
                m[1][i] = m[1][i + 1];
            }
            m[1][3] = tmp;

            tmp = m[2][0];
            m[2][0] = m[2][2];
            m[2][2] = tmp;
            tmp = m[2][1];
            m[2][1] = m[2][3];
            m[2][3] = tmp;

            tmp = m[3][0];
            m[3][0] = m[3][3];
            m[3][3] = m[3][2];
            m[3][2] = m[3][1];
            m[3][1] = tmp;

            if(r!=9) {
                byte[][] matrice = new byte[4][4];
                matrice[0] = new byte[]{0x0E, 0x0B, 0x0D, 0x09};
                matrice[1] = new byte[]{0x09, 0x0E, 0x0B, 0x0D};
                matrice[2] = new byte[]{0x0D, 0x09, 0x0E, 0x0B};
                matrice[3] = new byte[]{0x0B, 0x0D, 0x09, 0x0E};

                byte[][] m2 = new byte[4][4];
                for (int ind = 0; ind < 4; ind++)
                    for (i = 0; i < 4; i++) {
                        m2[i][ind] = (byte) (matrice[i][0] * m[0][ind]);
                        for (j = 1; j < 4; j++) {
                            m2[i][ind] = (byte) ((matrice[i][j] * m[j][ind]) ^ m2[i][ind]);
                        }
                    }
                m = m2;
            }
            //ADD ROUND KEY
            for (int ind = 0; ind < 4; ind++){
                for (i = 0; i < 4; i++) {
                    m[i][ind]= (byte) (m[i][ind]^expKey[r*4+ind][i]);
                }
            }
        }

        for (i=0;i<4;i++)
            for (j=0;j<4;j++)
                System.out.print((char)m[j][i]);
        System.out.println(" ");
    }

    public void decCFB(byte[][] m){
        System.out.println(" ");
        int rcon[][]= new int[10][4];
        byte[][] m2 = new byte[4][4];
        int i,j;


        byte [][] expKey=expandKeyEnc();

        for(i=0;i<4;i++){
            for(j=0;j<4;j++) {
                iV2[j][i]= (byte) (iV2[j][i]^(expKey[i][j]));
            }
        }

        for (int r=1;r<=10;r++) {
            //SubTypes
            for (i = 0; i < 4; i++) {
                for(j=0;j<4;j++) {
                    m[i][j]=getInverse(m[i][j]);
                    byte t =m[i][j];
                    m[i][j]= (byte) ( (t<<1) ^ (t<<3) ^ (t<<6)  ^ 0x5);
                }
            }

            //PERMUTATIONS
            byte tmp = iV2[1][0];
            for (i = 0; i < 3; i++) {
                iV2[1][i] = iV2[1][i + 1];
            }
            iV2[1][3] = tmp;

            tmp = iV2[2][0];
            iV2[2][0] = iV2[2][2];
            iV2[2][2] = tmp;
            tmp = iV2[2][1];
            iV2[2][1] = iV2[2][3];
            iV2[2][3] = tmp;

            tmp = iV2[3][0];
            iV2[3][0] = iV2[3][3];
            iV2[3][3] = iV2[3][2];
            iV2[3][2] = iV2[3][1];
            iV2[3][1] = tmp;

            if(r!=9) {
                byte[][] matrice = new byte[4][4];
                matrice[0] = new byte[]{0x0E, 0x0B, 0x0D, 0x09};
                matrice[1] = new byte[]{0x09, 0x0E, 0x0B, 0x0D};
                matrice[2] = new byte[]{0x0D, 0x09, 0x0E, 0x0B};
                matrice[3] = new byte[]{0x0B, 0x0D, 0x09, 0x0E};


                for (int ind = 0; ind < 4; ind++)
                    for (i = 0; i < 4; i++) {
                        m2[i][ind] = (byte) (matrice[i][0] * iV2[0][ind]);
                        for (j = 1; j < 4; j++) {
                            m2[i][ind] = (byte) ((matrice[i][j] * iV2[j][ind]) ^ m2[i][ind]);
                        }
                    }

            }
            //ADD ROUND KEY
            for (int ind = 0; ind < 4; ind++){
                for (i = 0; i < 4; i++) {
                    m2[i][ind]= (byte) (m2[i][ind]^expKey[r*4+ind][i]);
                }
            }
        }


        for (int ind = 0; ind < 4; ind++)
            for (i = 0; i < 4; i++) {
                m2[ind][i]= (byte) (m2[ind][i]^m[ind][i]);
            }
        iV2=m2;
        for (i=0;i<4;i++)
            for (j=0;j<4;j++)
                System.out.print((char)iV2[j][i]);
        System.out.println(" ");


    }

    public void readInput(Node node){
        try {

            String workingDir = System.getProperty("user.dir");
            File f = new File(workingDir+"\\src\\com\\company\\input.txt");
            Scanner reader = new Scanner(f);
            int ind=0;
            int i=0,j=0;
            int isFull=0;
            byte[][] m=new byte[4][4];

            while (reader.hasNextLine()) {
                ind=0;
                String mesaj = reader.nextLine();
                byte[] mBytes=mesaj.getBytes(StandardCharsets.UTF_8);

                while(ind<mBytes.length) {

                    for (; i < 4 && ind < mBytes.length; i++) {
                        for (; j < 4 && ind < mBytes.length; j++) {
                            isFull++;
                            System.out.print((char)mBytes[ind]);
                            m[j][i] = mBytes[ind];
                            ind++;
                            if(isFull==16)
                                if(this.mode==0) {
                                    sendECB(m,node); isFull=0;

                                    for(int k=0;k<4;k++){
                                        for(int l=0;l<4;l++)
                                            m[k][l]=0;
                                    }
                                }
                                else {
                                    sendCFB(m,node); isFull=0;
                                    for(int k=0;k<4;k++){
                                        for(int l=0;l<4;l++)
                                            m[k][l]=0;
                                    }
                                }

                        }
                        if(j>3) j=0;
                        if(ind>=mBytes.length) break;

                    }




                    if (i > 3) {
                        i = 0;
                        j = 0;
                    }

                }

            }
            reader.close();

            if(this.mode==0)
            { sendECB(m,node); }
            else { sendCFB(m,node); }

        } catch (FileNotFoundException e) {
            System.out.println("Fisierul de input lipseste.");
            e.printStackTrace();
        }



    }
}




