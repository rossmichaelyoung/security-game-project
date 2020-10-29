import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.io.BufferedReader;

public class PasswordCracker {
    public static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    public static final String LOWER_AND_UPPER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWER_CASE_LETTERS_AND_DIGITS = "abcdefghijklmnopqrstuvwxyz0123456789";
    public static final String LOWER_AND_UPPER_CASE_LETTERS_AND_DIGITS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static boolean found = false;
    public static long start;
    public static MessageDigest md;
    public static byte[] hashToFind;
    public static String hashingAlgorithm;

    public static byte[] getHash(String password) throws NoSuchAlgorithmException {
        return md.digest(password.getBytes());
    }

    public static String bytestoHexString(byte[] hash) {
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    public static void findPassword(int index, int length, StringBuilder sb, String cs) throws NoSuchAlgorithmException {
        if(index == length) {
            String currentPassword = sb.toString();
            byte[] currentPasswordHash = getHash(currentPassword);
            if(Arrays.equals(hashToFind, currentPasswordHash)) {
                found = true;
                long timeToFind = System.currentTimeMillis() - start;
                System.out.println("\nPassword found in " + TimeUnit.MILLISECONDS.toSeconds(timeToFind) + " seconds");
                System.out.println("Your Password was " + currentPassword);
            } else {
                System.out.println("\nCurrent Password Attempt: " + currentPassword);
                System.out.println("Current Password Attempt's " + hashingAlgorithm + " Hash: " + bytestoHexString(currentPasswordHash));
            }
        }

        for(int i = 0; i < cs.length() && !found && index < length; i++) {
            sb.setCharAt(index, cs.charAt(i));
            findPassword(index+1, length, sb, cs);
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        if (args.length < 4) {
            System.out.println(
                    "Please Input (in this order): \n" +
                            "Your Password \n" +
                            "The Hashing Algorithm You Wish To Use (MD5 or SHA256) \n" +
                            "The Character Space of Your Password: \n" +
                            "   l (Lower Case Letters) \n" +
                            "   lu (Lower and Upper Case Letters) \n" +
                            "   ld (Lower Case Letters and Digits) \n" +
                            "   lud (Lower and Upper Case Letters and Digits) \n" +
                            "The Length of Your Password As An Integer"
            );
            return;
        }

        hashingAlgorithm = args[1];
        if(!(hashingAlgorithm.equals("md5") || hashingAlgorithm.equals("MD5") || hashingAlgorithm.equals("sha256") || hashingAlgorithm.equals("SHA256"))) {
            System.out.println("Please Enter a Valid Hashing Algorithm for Your Password (MD5 or SHA256)");
            return;
        }
        md = MessageDigest.getInstance("MD5");

        if (!args[3].matches("^[1-9]\\d*$")) {
            System.out.println("Please Enter an Integer Greater Than Zero for Your Password Length");
            return;
        }

        int length = Integer.parseInt(args[3]);
        String cs = args[2];
        String password = args[0];
        switch (cs) {
            case "l":
                if (!password.matches("^[a-z]{" + length + "}$")) {
                    System.out.println("Password and Character Space and/or Password and Password Length Do Not Match");
                    return;
                }
                cs = LOWER_CASE_LETTERS;
                break;
            case "lu":
                if (!password.matches("^[a-zA-Z]{" + length + "}$")) {
                    System.out.println("Password and Character Space and/or Password and Password Length Do Not Match");
                    return;
                }
                cs = LOWER_AND_UPPER_CASE_LETTERS;
                break;
            case "ld":
                if (!password.matches("^[a-z0-9]{" + length + "}$")) {
                    System.out.println("Password and Character Space and/or Password and Password Length Do Not Match");
                    return;
                }
                cs = LOWER_CASE_LETTERS_AND_DIGITS;
                break;
            case "lud":
                if (!password.matches("^[a-zA-Z0-9]{" + length + "}$")) {
                    System.out.println("Password and Character Space and/or Password and Password Length Do Not Match");
                    return;
                }
                cs = LOWER_AND_UPPER_CASE_LETTERS_AND_DIGITS;
                break;
            default:
                System.out.println(
                        "Valid Character Space for Password Not Entered \n" +
                                "Please Enter One of the Following: \n" +
                                "   l (Lower Case Letters) \n" +
                                "   lu (Lower and Upper Case Letters) \n" +
                                "   ld (Lower Case Letters and Digits) \n" +
                                "   lud (Lower and Upper Case Letters and Digits)"
                );
                return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        hashToFind = getHash(password);
        System.out.println("The " + hashingAlgorithm + " Hash of Your Password, " + password + ", is " + bytestoHexString(hashToFind));
        System.out.println("The value this algorithm is searching for is the hash of your password\n");
        System.out.println("Press any key to begin");
        reader.readLine();

        start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.setLength(length);
        findPassword(0, length, sb, cs);
    }

}