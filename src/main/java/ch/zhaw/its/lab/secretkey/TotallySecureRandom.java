package ch.zhaw.its.lab.secretkey;

import java.security.SecureRandom;

public class TotallySecureRandom extends SecureRandom {

    @Override
    public void nextBytes(byte[] bytes) {
        long now = System.currentTimeMillis();

        for (int i = 0; i < bytes.length; i++) {
            System.out.println(now);
            bytes[i] = (byte) (now & 0xff);
            System.out.println(bytes[i]);
            now >>= 8;
        }
    }
}
