package org.wanderingnet.service.user;

import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Created by guillermoblascojimenez on 08/03/16.
 */
@Service
public class SecurityService {

    public SecurityPair secure(char[] password) {
        byte[] salt = newSalt();
        String hash = hash(password, salt);
        return new SecurityPair(hash, bytesToHex(salt));
    }

    public boolean match(char[] password, String passwordHash, String passwordSalt) {
        String hash = hash(password, hexToBytes(passwordSalt.toCharArray()));
        return hash.equals(passwordHash);
    }

    private byte[] newSalt() {
        return newSalt(16);
    }

    private byte[] newSalt(int sizeInBytes) {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            byte[] saltByte = new byte[sizeInBytes];
            secureRandom.nextBytes(saltByte);
            return saltByte;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String hash(char[] password, byte[] salt) {
        try {
            byte[] passwordBytes = toBytes(password);

            byte[] saltedPasswordBytes = concat(passwordBytes, salt);
            return hash(saltedPasswordBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String hash(byte[] bytes) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] hashedPasswordBytes = digest.digest(bytes);
        return bytesToHex(hashedPasswordBytes);
    }

    public static class SecurityPair {

        private final String hash;
        private final String salt;

        private SecurityPair(String hash, String salt) {
            this.hash = hash;
            this.salt = salt;
        }

        public String getHash() {
            return hash;
        }

        public String getSalt() {
            return salt;
        }

    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    public static byte[] hexToBytes(char[] s) {
        int len = s.length;
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s[i], 16) << 4)
                    + Character.digit(s[i+1], 16));
        }
        return data;
    }
    private byte[] toBytes(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
                byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(charBuffer.array(), '\u0000'); // clear sensitive data
        Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
        return bytes;
    }
    private byte[] concat(byte[] a1, byte[] a2) {

        byte[] all = new byte[a1.length + a2.length];

        // copy first half
        System.arraycopy(a1, 0, all, 0, a1.length);

        // copy second half
        System.arraycopy(a2, 0, all, a1.length, a2.length);

        return all;
    }

}
