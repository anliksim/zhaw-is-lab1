package ch.zhaw.its.lab.secretkey;

public final class Entropy {

    private static final double HEX_ENTROPY_PER_SYMBOL = 4;
    private static final double ASCII_ENTROPY_PER_SYMBOL = 7.768;

    private Entropy() {
        throw new AssertionError("utility constructor");
    }

    public static boolean isAlphanumeric(double entropy) {
        return HEX_ENTROPY_PER_SYMBOL < entropy && entropy < ASCII_ENTROPY_PER_SYMBOL;
    }

    public static double calcuate(byte[] fileContent) {

        double[] counts = new double[256];
        for (byte b : fileContent) {
            counts[Byte.toUnsignedInt(b)]++;
        }

        double entropy = 0;
        for (double count : counts) {
            double p = count / fileContent.length;
            if (p > 0) {
                entropy -= p * log2(p);
            }
        }
        return entropy;
    }


    private static double log2(double value) {
        return Math.log(value) / Math.log(2);
    }
}
