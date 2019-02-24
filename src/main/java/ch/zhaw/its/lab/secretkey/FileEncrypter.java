package ch.zhaw.its.lab.secretkey;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.GeneralSecurityException;

class FileEncrypter {

    private static final String KALGORITHM = "AES";
    private static final String CALGORITHM = KALGORITHM + "/CBC/PKCS5Padding";
    private String inFile;
    private String outFile;

    FileEncrypter(String inFile, String outFile) {
        this.inFile = inFile;
        this.outFile = outFile;
    }

    private static void crypt(InputStream is, OutputStream os, Cipher cipher) throws IOException, GeneralSecurityException {
        boolean more = true;
        byte[] input = new byte[cipher.getBlockSize()];

        while (more) {
            int inBytes = is.read(input);

            if (inBytes > 0) {
                os.write(cipher.update(input, 0, inBytes));
            } else {
                more = false;
            }
        }
        os.write(cipher.doFinal());
    }

    void encrypt() throws IOException, GeneralSecurityException {
        KeyGenerator keyGen = KeyGenerator.getInstance(KALGORITHM);
        keyGen.init(128, new TotallySecureRandom());
        SecretKey key = keyGen.generateKey();

        Cipher cipher = Cipher.getInstance(CALGORITHM);

        byte[] rawIv = new byte[cipher.getBlockSize()];
        new TotallySecureRandom().nextBytes(rawIv);
        IvParameterSpec iv = new IvParameterSpec(rawIv);

        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        try (InputStream is = new FileInputStream(inFile);
             OutputStream os = new FileOutputStream(outFile)) {
            writeIv(os, iv);
            crypt(is, os, cipher);
        }
    }

    private static IvParameterSpec readIv(InputStream is, Cipher cipher) throws IOException {
        byte[] rawIv = new byte[cipher.getBlockSize()];
        int inBytes = is.read(rawIv);
        if (inBytes != cipher.getBlockSize()) {
            throw new IllegalStateException("can't read IV from file");
        }
        return readIv(rawIv, cipher);
    }

    private static IvParameterSpec readIv(byte[] rawIv, Cipher cipher) {
        if (rawIv.length != cipher.getBlockSize()) {
            throw new IllegalStateException("can't read IV");
        }
        return new IvParameterSpec(rawIv);
    }

    private void writeIv(OutputStream os, IvParameterSpec iv) throws IOException {
        os.write(iv.getIV());
    }

    void decrypt(byte[] rawKey) throws IOException, GeneralSecurityException {
        try (InputStream is = new FileInputStream(inFile);
             OutputStream os = new FileOutputStream(outFile)) {
            decrypt(rawKey, is, os);
        }
    }

    static void decrypt(byte[] rawKey, InputStream in, OutputStream out) throws GeneralSecurityException, IOException {

        SecretKey key = new SecretKeySpec(rawKey, 0, rawKey.length, KALGORITHM);
        Cipher cipher = Cipher.getInstance(CALGORITHM);

        IvParameterSpec ivParameterSpec = readIv(in, cipher);

        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
        crypt(in, out, cipher);
    }
}
