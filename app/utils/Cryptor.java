package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Created by pedro on 10/02/15.
 */
public class Cryptor {

    public static String encrypt(String password){
        MessageDigest messageDigest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.reset();
            byte[] buffer = password.getBytes();
            md.update(buffer);
            byte[] digest = md.digest();

            String hexStr = "";
            for (int i = 0; i < digest.length; i++) {
                hexStr +=  Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 );
            }
            return hexStr;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return password;
    }

}
