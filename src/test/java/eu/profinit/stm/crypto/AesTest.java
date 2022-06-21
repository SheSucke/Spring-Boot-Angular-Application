package eu.profinit.stm.crypto;

import org.junit.Assert;
import org.junit.Test;


public class AesTest {


    /**
     * Testing if encryption will return correct value
     */
    @Test
    public void encryptWillCorrectlyEncryptMessage() {
        String result = Aes.encrypt("Pavel");
        Assert.assertEquals("TSSXmG0ldrtwU4EO+rAg0A==",result);
    }

    /**
     * Testing if decryption will return correct value
     */
    @Test
    public void decryptWillCorrectlyDecryptMessage() {
        String result = Aes.decrypt("TSSXmG0ldrtwU4EO+rAg0A==");
        Assert.assertEquals("Pavel",result);
    }
}


