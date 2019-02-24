package ch.zhaw.its.lab.secretkey;

public class HoldRansom {

    private static final String IN_FILE = System.getProperty("user.dir") + "/myfile";
    private static final String OUT_FILE = System.getProperty("user.dir") + "/myransom";

    public static void main(String[] args) throws Exception {
        Ransom.main(new String[]{"-ransom", IN_FILE, OUT_FILE});
    }
}
