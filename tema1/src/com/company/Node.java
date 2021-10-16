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


    public byte[][] expandKeyEnc(){

        int rcon[]= new int[40];

        int i=0,j=0;
        for(;i<10;i++) {
            for (; j < 4; j++) {
                if(j>0)
                    rcon[i*4+j]=0x0;
            }
        }
        rcon[0]=0x1;
        rcon[4]=0x2;
        rcon[8]=0x4;
        rcon[12]=0x8;
        rcon[16]=0x10;
        rcon[20]=0x20;
        rcon[24]=0x40;
        rcon[28]=0x80;
        rcon[32]=0x1B;
        rcon[36]=0x36;


        int[] V=new int[8];
        int[] s=new int[8];
        int N=4;
        byte[][] key=new byte[4][4];
        i=0;j=0;int ind=0;
        for(;i<10;i++) {
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
                    int y;
                    for(int l=0;l<4;l++){
                        if(x[l]!=0)
                            y=1/(int)x[l];
                        else y=0;
                        x[l]=(byte)y;
                    }

                    for ( j=0;j<4;j++){
                        x[j]= (byte) ( x[j] ^ (x[j]<<1) ^ (x[j]<<2) ^ (x[j]<<3) ^ (x[j]<<4) ^ 0x63 );
                        x[j]= (byte) (x[j]^((byte)rcon[i/N]));
                        expKey[i][j]= (byte) (x[j] ^ expKey[i - N][j]);

                    }
                }
                else {

                    byte[] x = expKey[i - 1];
                    int y;
                    for(int l=0;l<4;l++){
                        if(x[l]!=0)
                            y=1/(int)x[l];
                        else y=0;
                        x[l]=(byte)y;
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

        int rcon[]= new int[40];

        int i=0,j=0;
        for(;i<10;i++) {
            for (; j < 4; j++) {
                if(j>0)
                    rcon[i*4+j]=0x0;
            }
        }
        rcon[0]=0x1;
        rcon[4]=0x2;
        rcon[8]=0x4;
        rcon[12]=0x8;
        rcon[16]=0x10;
        rcon[20]=0x20;
        rcon[24]=0x40;
        rcon[28]=0x80;
        rcon[32]=0x1B;
        rcon[36]=0x36;


        int[] V=new int[8];
        int[] s=new int[8];
        int N=4;
        byte[][] key=new byte[4][4];
        i=0;j=0;int ind=0;
        for(;i<10;i++) {
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
                        x[j]= (byte) ( x[j] ^ (x[j]<<1) ^ (x[j]<<2) ^ (x[j]<<3) ^ (x[j]<<4) ^ 0x63 );
                        x[j]= (byte) (x[j]^((byte)rcon[i/N]));
                        expKey[i][j]= (byte) (x[j] ^ expKey[i - N][j]);

                    }
                    int y;
                    for(int l=0;l<4;l++){
                        if(expKey[i][l]!=0)
                            y=1/(int)expKey[i][l];
                        else
                            y=0;
                        expKey[i][l]=(byte)y;
                    }
                }
                else {

                    byte[] x=expKey[i-1];
                    for ( j=0;j<4;j++){
                        expKey[i][j]= (byte) (x[j] ^ expKey[i - N][j]);
                    }

                    int y;
                    for(int l=0;l<4;l++){
                        if(expKey[i][l]!=0)
                            y=1/(int)expKey[i][l];
                        else
                            y=0;
                        expKey[i][l]=(byte)y;
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




