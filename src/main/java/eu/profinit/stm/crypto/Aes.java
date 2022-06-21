package eu.profinit.stm.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class Aes {

    private static SecretKeySpec secretKey;

    private static final String keyInOpenText = "AES";

    private Aes() { throw new IllegalStateException("Utility class"); }

    /**
     * Transform given string to SecretKey used for encryption and decryption
     * @param myKey
     */
    public static void setKey(String myKey)
    {
        MessageDigest sha;
        try {
            byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Encrypt given string with AES algorithm
     * @param strToEncrypt String that should be encrypted
     * @return Encrypted string
     */
    public static String encrypt(String strToEncrypt)
    {
        try
        {
            setKey(keyInOpenText);
            Cipher cipher = Cipher.getInstance("Aes/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e);
        }
        return null;
    }

    /**
     * Decrypt given string with AES algorithm
     * @param strToDecrypt String to decrypt
     * @return String in open text
     */
    public static String decrypt(String strToDecrypt)
    {
        try
        {
            setKey(keyInOpenText);
            Cipher cipher = Cipher.getInstance("Aes/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e);
        }
        return null;
    }
}
