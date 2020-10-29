import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class DictionaryAttackPasswordCracker {
    public static long start;
    public static MessageDigest md;
    public static byte[] hashToFind;
    public static String hashingAlgorithm;
    public static boolean found = false;

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

    public static void findPassword() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    "./rockyou.txt"
            ));

            String currentPassword;
            start = System.currentTimeMillis();
            while ((currentPassword = reader.readLine()) != null) {
                byte[] currentPasswordHash = getHash(currentPassword);
                System.out.println("\nCurrent Password Attempt: " + currentPassword);
                System.out.println("Current Password Attempt's " + hashingAlgorithm + " Hash: " + bytestoHexString(currentPasswordHash));

                if(Arrays.equals(hashToFind, currentPasswordHash)) {
                    found = true;
                    long timeToFind = System.currentTimeMillis() - start;
                    double displayTime = timeToFind / 1000.0;
                    System.out.println("Password found in " + displayTime + " seconds");
                    break;
                }
            }
            reader.close();
        } catch (IOException | NoSuchAlgorithmException e) {
            System.out.println("Error opening and/or reading password dictionary");
        }

        if(!found) {
            System.out.println("Password could not be cracked with dictionary attack");
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        if (args.length < 2) {
            System.out.println(
                    "Please Input (in this order): \n" +
                            "Your Password \n" +
                            "The Hashing Algorithm You Wish To Use (MD5, SHA-1, SHA-256, etc) \n"
            );
            return;
        }

        String password = args[0];
        hashingAlgorithm = args[1];
        try {
            md = MessageDigest.getInstance(hashingAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Please Enter a Valid Hashing Algorithm for Your Password (MD5, SHA-1, SHA-256, etc)");
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        hashToFind = getHash(password);
        System.out.println("The " + hashingAlgorithm + " Hash of Your Password, " + password + ", is " + bytestoHexString(hashToFind));
        System.out.println("The value this algorithm is searching for is the hash of your password\n");
        System.out.println("Press any key to begin");
        reader.readLine();

        findPassword();
    }
}