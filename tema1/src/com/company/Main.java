package com.company;

public class Main {

    public static void main(String[] args) {
            ManagerNode MC=new ManagerNode();
            MC.setKeys("K;J DHSIHUWFE;NJ DS? JVNE;VNVM A VP8UTM4C7N3498NTF4WEJFUFA8OFONAKQ LQ34FIOJA4OFJOHIEO'NDFVLSBi:hvB'NDF",
                    "O;FHDAS PFFWVAOE;NFN;JWE;JANSONDJSNjdFNVL/snkj CJ  JIWOEFJ'WJFASDOJNJCNUSOJNFNSDFHFWEFNBDOWOENFWDS98CS9SDV",
                    "gfUELWblfEWefkfelFO[iehnfc]QPEFJOAk'nj[WINFKLESCefIWKw'fei]E09J4JT90WNI4 T2PN]GI04TI2NNGVH08C -VYNTY4V7M4");



            Node A=new Node();
            double randNumber = Math.random();
            int d = (int)(randNumber * 100);
            A.setMode(d%2);
            A.setK(MC.K);

            byte[] encK;

            if(A.getMode()==0) {
                encK=MC.encryptKey(MC.k1);
                System.out.println(" ");
                System.out.println("Cheie modul ECB: ");
                for(int i=0;i<MC.k1.length;i++)
                    System.out.print(MC.k1[i]+" ");
            }
            else {
                Node.setInitialization();
                encK=MC.encryptKey(MC.k2);
                System.out.println(" ");
                System.out.println("Cheie modul CFB: ");
                for(int i=0;i<MC.k2.length;i++)
                    System.out.print(MC.k2[i]+" ");
            }

            System.out.println(" ");
            System.out.println("criptata:");
            for (int i=0;i<MC.k1.length;i++)
                System.out.print(encK[i]+" ");

            System.out.println(" ");
            System.out.println("decriptata:");

            A.setK1(encK);
            A.setK1(A.decrypt());


            for (int i=0;i<MC.k1.length;i++)
                System.out.print(A.k1[i]+" ");
            System.out.println(" ");System.out.println(" ");


            Node B=new Node();
            B.setK(MC.K);
            B.setMode(A.getMode());
            B.setK1(encK);
            B.setK1(B.decrypt());




            A.readInput(B);






            }



    }

