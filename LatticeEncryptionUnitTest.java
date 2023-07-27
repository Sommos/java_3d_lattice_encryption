import LatticeEncryption.java;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;

public class LatticeEncryptionUnitTest {

    @Test
    public void testEncryptionDecryption() {
        BigInteger[][] publicKey = LatticeEncryption.generatePublicKey();
        BigInteger[] privateKey = LatticeEncryption.generatePrivateKey();

        BigInteger message = BigInteger.valueOf(123456789);
        BigInteger[] cipherText = LatticeEncryption.encrypt(message, publicKey);
        BigInteger decryptedMessage = LatticeEncryption.decrypt(cipherText, privateKey);

        assertEquals(message, decryptedMessage);
    }

    @Test
    public void testPrivateKeyGeneration() {
        BigInteger[] privateKey = LatticeEncryption.generatePrivateKey();

        assertEquals(LatticeEncryption.N, privateKey.length);
        for (int i = 0; i < LatticeEncryption.N; i++) {
            assertTrue(privateKey[i].compareTo(BigInteger.ZERO) >= 0);
            assertTrue(privateKey[i].compareTo(BigInteger.valueOf(LatticeEncryption.Q)) < 0);
        }
    }

    @Test
    public void testPublicKeyGeneration() {
        BigInteger[][] publicKey = LatticeEncryption.generatePublicKey();

        assertEquals(LatticeEncryption.N, publicKey.length);
        for (int i = 0; i < LatticeEncryption.N; i++) {
            assertEquals(LatticeEncryption.N, publicKey[i].length);
            for (int j = 0; j < LatticeEncryption.N; j++) {
                assertTrue(publicKey[i][j].compareTo(BigInteger.ZERO) >= 0);
                assertTrue(publicKey[i][j].compareTo(BigInteger.valueOf(LatticeEncryption.Q)) < 0);
            }
        }
    }
}