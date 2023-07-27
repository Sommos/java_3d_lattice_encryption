import java.math.BigInteger;
import java.security.SecureRandom;

public class LatticeEncryption {
    // lattice parameters

    // dimension of the lattice
    private static final int N = 256;
    // a large prime modulus 
    private static final int Q = 12289;
    // standard deviation for generating keys
    private static final int STANDARD_DEVIATION = 8;

    public static void main(String[] args) {
        // generate random public and private keys
        BigInteger[][] publicKey = generatePublicKey();
        BigInteger[] privateKey = generatePrivateKey();
        // message to be encrypted
        BigInteger message = new BigInteger("1234567890");
        // encrypt the message
        BigInteger[] cipherText = encrypt(message, publicKey);
        // decrypt the message
        BigInteger decryptedMessage = decrypt(cipherText, privateKey);
        // print results
        System.out.println("Original Message: " + message);
        System.out.println("Encrypted Message: " + cipherText[0]);
        System.out.println("Decrypted Message: " + decryptedMessage);
    }

    // generates a random public key matrix
    private static BigInteger[][] generatePublicKey() {
        BigInteger[][] publicKey = new BigInteger[N][N];
        SecureRandom random = new SecureRandom();

        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                publicKey[i][j] = new BigInteger(STANDARD_DEVIATION * 2, random).subtract(BigInteger.valueOf(STANDARD_DEVIATION));
            }
        }
        return publicKey;
    }
    
    // generates a random private key vector
    private static BigInteger[] generatePrivateKey() {
        BigInteger[] privateKey = new BigInteger[N];
        SecureRandom random = new SecureRandom();

        for(int i = 0; i < N; i++) {
            privateKey[i] = new BigInteger(STANDARD_DEVIATION * 2, random).subtract(BigInteger.valueOf(STANDARD_DEVIATION));
        }
        return privateKey;
    }

    // encrypts a message using the public key
    private static BigInteger[] encrypt(BigInteger message, BigInteger[][] publicKey) {
        SecureRandom random = new SecureRandom();
        BigInteger[] cipherText = new BigInteger[N];

        for(int i = 0; i < N; i++) {
            cipherText[i] = BigInteger.ZERO;

            for(int j = 0; j < N; j++) {
                BigInteger noise = new BigInteger(STANDARD_DEVIATION * 2, random).subtract(BigInteger.valueOf(STANDARD_DEVIATION));
                cipherText[i] = cipherText[i].add(publicKey[i][j].multiply(noise));
            }
            // add message
            cipherText[i] = cipherText[i].add(message);
            cipherText[i] = cipherText[i].mod(BigInteger.valueOf(Q));
        }
        return cipherText;
    }

    // decrypts a message using the private key
    private static BigInteger decrypt(BigInteger[] cipherText, BigInteger[] privateKey) {
        BigInteger message = BigInteger.ZERO;

        for(int i = 0; i < N; i++) {
            message = message.add(cipherText[i].multiply(privateKey[i]));
        }

        // reduce modulo Q to get the decrypted message
        message = message.mod(BigInteger.valueOf(Q));

        // Since we added the message scaled by Q/2 in the encryption process,
        // we need to subtract the scaled value to get the original message back.
        BigInteger scaledMessage = message.multiply(BigInteger.valueOf(Q / 2)).mod(BigInteger.valueOf(Q));
        if (scaledMessage.compareTo(message) <= 0) {
            message = message.subtract(scaledMessage);
        } else {
            message = message.add(BigInteger.valueOf(Q)).subtract(scaledMessage);
        }

        return message;
    }
}
