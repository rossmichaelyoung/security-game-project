import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class DictionaryAttackPasswordCracker {
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

    public static byte[] hexStringToBytes(String s) {
        byte[] bytes = new byte[s.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(s.substring(index, index + 2), 16);
            bytes[i] = (byte) j;
        }
        return bytes;
    }

    public static void findAndPrintPassword() {
        boolean found = false;
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    "../Resources/rockyou.txt"
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
                    System.out.println("Password found in " + displayTime + " seconds\n");
                    break;
                }
            }
            reader.close();
            if(!found) {
                System.out.println("Password could not be cracked with dictionary attack");
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            System.out.println("Error opening and/or reading password dictionary");
        }
    }

    public static String findPasswordGivenHash(String hash) {
        boolean found = false;
        hashToFind = hexStringToBytes(hash);
        String output = "";
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    "../Resources/rockyou.txt"
            ));

            String currentPassword;
            start = System.currentTimeMillis();
            while ((currentPassword = reader.readLine()) != null) {
                byte[] currentPasswordHash = getHash(currentPassword);
                // System.out.println(currentPassword);
                if(Arrays.equals(hashToFind, currentPasswordHash)) {
                    found = true;
                    BigDecimal displayTime = new BigDecimal((System.currentTimeMillis() - start) / 1000.0);
                    output += "The hash " + bytestoHexString(hashToFind) + " = " + currentPassword + "\n" +
                              "Password found in " + displayTime.setScale(2, RoundingMode.CEILING) + " seconds\n";
                    break;
                }
            }
            reader.close();
            if(!found) {
                output += "Password could not be cracked with dictionary attack\n";
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            output += "Error opening and/or reading password dictionary\n";
        }

        return output;
    }

    public DictionaryAttackPasswordCracker() {
        hashingAlgorithm = "MD5";
        try {
            md = MessageDigest.getInstance(hashingAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Please Enter a Valid Hashing Algorithm for Your Password (MD5, SHA-1, SHA-256, etc)");
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        if (args.length < 2) {
            System.out.println(
                    "Please Input (in this order): \n" +
                            "1. Your Password \n" +
                            "2. The Hashing Algorithm You Wish To Use (MD5, SHA-1, SHA-256, etc) \n"
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

        System.out.println("Passwords are not stored in a database, but the hash of a user's password is\n" +
                "In order for an attacker to obtain your password, he or she will need to compare the hash of a given text string against the given hash stored in a database\n" +
                "If these values match, the attacker knows your password\n" +
                "To emulate this process, this algorithm will convert your password into its hash value using the hash algorithm of your choice\n" +
                "This algorithm will then compare the hash of each text string it generates – using the character space of your choice – to your password's hash value, as this is what an attacker has to do\n");

        System.out.println("The " + hashingAlgorithm + " hash of your password, " + password + ", is " + bytestoHexString(hashToFind));
        System.out.println("The value this algorithm is searching for is the hash of your password\n");

        System.out.println("This password cracking algorithm uses a dictionary attack, which uses a large list of known passwords and compares each password's " + hashingAlgorithm + " hash to your password's " + hashingAlgorithm + " hash\n" +
                "A dictionary attack is often faster than a brute force attack but has a distinct disadvantage – if the password is not in the dictionary file, then it will not be found\n");

        System.out.println("Press Enter to Begin");
        reader.readLine();
        reader.close();

        findAndPrintPassword();
    }
}