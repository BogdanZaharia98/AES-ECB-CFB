package com.company;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

public class Node {

    static byte initialization[];
    int mode;
    byte[] K;
    byte[] k1;




    public void setK(byte[] k) {
        K = k;
    }

    public void setK1(byte[] k1) {
        this.k1 = k1;
    }

    public static void setInitialization(byte[] initialization) {
        Node.initialization = initialization;
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

        int rcon[][]= new int[10][4];

        int i=0,j=0;
        for(;i<10;i++) {
            for (; j < 4; j++) {
                if(j>0)
                    rcon[i][j]=0x0;
            }
        }
        rcon[0][0]=0x1;
        rcon[1][0]=0x2;
        rcon[2][0]=0x4;
        rcon[3][0]=0x8;
        rcon[4][0]=0x10;
        rcon[5][0]=0x20;
        rcon[6][0]=0x40;
        rcon[7][0]=0x80;
        rcon[8][0]=0x1B;
        rcon[9][0]=0x36;

        int matrice[][]=new int[8][8];
        matrice[0]=new int[]{ 1,0,0,0,1,1,1,1 };
        matrice[1]=new int[]{ 1,1,0,0,0,1,1,1 };
        matrice[2]=new int[]{ 1,1,1,0,0,0,1,1 };
        matrice[3]=new int[]{ 1,1,1,1,0,0,0,1 };
        matrice[4]=new int[]{ 1,1,1,1,1,0,0,0 };
        matrice[5]=new int[]{ 0,1,1,1,1,1,0,0 };
        matrice[6]=new int[]{ 0,0,1,1,1,1,1,0 };
        matrice[7]=new int[]{ 0,0,0,1,1,1,1,1 };

        int[] mask=new int[8];
        mask[0]=1;
        mask[1]=2;
        mask[2]=4;
        mask[3]=8;
        mask[4]=16;
        mask[5]=32;
        mask[6]=64;
        mask[7]=128;



        int[] v=new int[]{ 1,1,0,0,0,1,1,0 };
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


        byte[][] expKey=new byte[11][4];
        for(i=0;i<11;i++){
            if(i<4) expKey[i]=key[i];
            else {
                if(i%N==0){

                    byte[] x =rotateWord(expKey[i-1]);
                    int y;
                    for(int l=0;l<4;l++){
                        y=1/(int)x[l];
                        x[l]=(byte)y;
                    }


                    byte[] w=new byte[1];
                    ///////byte to 8 int bits
                    for ( j=0;j<4;j++){

                        w[0]=x[j];
                        String str=toBinary(w[0],8);
                        for (int l=0;l<8;l++){
                            V[l]=Character.getNumericValue(str.charAt(l));
                        }

                        for (int l=0;l<8;l++){
                            s[l]=0;
                            for (int n=0;n<8;n++){
                                s[l]+=matrice[l][n]*V[n];
                            }
                            s[l]+=v[l];
                        }
                        byte X=0;
                        for(int l=0;l<8;l++){
                            X+=(byte)s[l]*mask[l];
                        }

                        x[j]=X;
                        x[j]= (byte) (x[j]^((byte)rcon[i][i/N]));
                        expKey[i][j]= (byte) (x[j] ^ expKey[i - N][j]);



                    }



                }
                else {



                    byte[] x=expKey[i];
                    byte[] w=new byte[1];
                    ///////byte to 8 int bits
                    for ( j=0;j<4;j++){

                        w[0]=x[j];
                        String str=toBinary(w[0],8);
                        for (int l=0;l<8;l++){
                            V[l]=Character.getNumericValue(str.charAt(l));
                        }

                        for (int l=0;l<8;l++){
                            s[l]=0;
                            for (int n=0;n<8;n++){
                                s[l]+=matrice[l][n]*V[n];
                            }
                            s[l]+=v[l];
                        }
                        byte X=0;
                        for(int l=0;l<8;l++){
                            X+=(byte)s[l]*mask[l];
                        }

                        x[j]=X;
                        x[j]= (byte) (x[j]^((byte)rcon[i][i/N]));
                        expKey[i][j]= (byte) (x[j] ^ expKey[i - N][j]);

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
                m[j][i]= (byte) (m[j][i]^(expKey[0][j]));
            }
        }



        B.decECB(m);



    }


    public void sendCFB(byte[][] m, Node B){
        System.out.println(" ");
        System.out.println("<--Block mesaj");
        int rcon[][]= new int[10][4];

        B.decCFB(m);
    }


    public void decECB(byte[][] m){
        System.out.println(" ");

    }

    public void decCFB(byte[][] m){
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
                            System.out.print(mBytes[ind]+" ");
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




