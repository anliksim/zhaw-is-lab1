package ch.zhaw.its.lab.secretkey;

public class PayRansom {

    private static final String IN_FILE = System.getProperty("user.dir") + "/mystery";
    private static final String OUT_FILE = System.getProperty("user.dir") + "/decrypted";

    public static void main(String[] args) throws Exception {

        String possibleKey = FileDecrypter.findKey(IN_FILE);
        if (possibleKey != null) {
            Ransom.main(new String[]{"-pay", IN_FILE, OUT_FILE, possibleKey});
        } else {
            System.out.println("No key found using brute force");
        }
    }
}
