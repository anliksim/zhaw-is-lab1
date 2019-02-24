package ch.zhaw.its.lab.secretkey;

import javax.crypto.BadPaddingException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static ch.zhaw.its.lab.secretkey.DatatypeConverter.printHexBinary;

class FileDecrypter {

    static String findKey(String filePath) throws Exception {

        byte[] fileContent = Files.readAllBytes(
                Paths.get(filePath));

        byte[] fileSequence = Arrays.copyOf(fileContent, 16);

        String possibleKey = null;

        brute:
        for (int i = 0; i < fileSequence.length; i++) {

            while (fileSequence[i] != 0) {
                fileSequence[i]--;
                if (tryDecrypt(fileContent, fileSequence)) {
                    possibleKey = printHexBinary(fileSequence);
                    break brute;
                }
            }
        }
        return possibleKey;
    }

    private static boolean tryDecrypt(byte[] encryptedInput, byte[] testKey) {
        boolean properKey = false;
        try {
            FileEncrypter.decrypt(testKey, new ByteArrayInputStream(encryptedInput),
                    new ByteArrayOutputStream(encryptedInput.length));
            // entropy check not really required here
            properKey = true;
        } catch (BadPaddingException e) {
            // expected when bad key is used
        } catch (Exception e) {
            // not expected if brute-force runs fine
            System.out.println(e.getMessage());
        }
        return properKey;
    }
}
