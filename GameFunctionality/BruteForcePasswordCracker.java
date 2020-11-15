import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.io.BufferedReader;
import java.math.BigInteger;

public class BruteForcePasswordCracker {
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

    public static void findAndPrintPassword(int index, int length, StringBuilder sb, String cs) throws NoSuchAlgorithmException {
        if(index == length) {
            String currentPassword = sb.toString();
            byte[] currentPasswordHash = getHash(currentPassword);
            System.out.println("\nCurrent Password Attempt: " + currentPassword);
            System.out.println("Current Password Attempt's " + hashingAlgorithm + " Hash: " + bytestoHexString(currentPasswordHash));

            if(Arrays.equals(hashToFind, currentPasswordHash)) {
                found = true;
                BigDecimal displayTime = new BigDecimal((System.currentTimeMillis() - start) / 1000.0);
                System.out.println("Password found in " + displayTime.setScale(2, RoundingMode.CEILING) + " seconds\n");
            }
        }

        for(int i = 0; i < cs.length() && !found && index < length; i++) {
            sb.setCharAt(index, cs.charAt(i));
            findAndPrintPassword(index+1, length, sb, cs);
        }
    }

    public static void findPasswordGivenHashRecursive(int index, int length, StringBuilder sb, String cs, byte[] hashToFind, StringBuilder output, long start) throws NoSuchAlgorithmException {
        if(index == length) {
            String currentPassword = sb.toString();
            byte[] currentPasswordHash = getHash(currentPassword);

            if(Arrays.equals(hashToFind, currentPasswordHash)) {
                found = true;
                BigDecimal displayTime = new BigDecimal((System.currentTimeMillis() - start) / 1000.0);
                output.append("The hash " + bytestoHexString(hashToFind) + " = " + currentPassword + "\n" +
                        "Password found in " + displayTime.setScale(2, RoundingMode.CEILING) + " seconds\n" +
                        "Using character space " + cs);
                return;
            }
        }

        for(int i = 0; i < cs.length() && !found && index < length; i++) {
            sb.setCharAt(index, cs.charAt(i));
            findPasswordGivenHashRecursive(index+1, length, sb, cs, hashToFind, output, start);
        }
    }

    public static String findPasswordGivenHash(String password, String selectedCharacterSpace) {
        String characterSpace;
        switch(selectedCharacterSpace) {
            case "Lowercase Characters Only":
                characterSpace = LOWER_CASE_LETTERS;
                break;
            case "Lowercase and Uppercase Characters":
                characterSpace = LOWER_AND_UPPER_CASE_LETTERS;
                break;
            case "Lowercase Characters and Numbers":
                characterSpace = LOWER_CASE_LETTERS_AND_DIGITS;
                break;
            case "Lowercase and Uppercase Characters and Numbers":
                characterSpace = LOWER_AND_UPPER_CASE_LETTERS_AND_DIGITS;
                break;
            default:
                characterSpace = LOWER_AND_UPPER_CASE_LETTERS_AND_DIGITS;
                break;
        }

        StringBuilder result = new StringBuilder();
        try {
            byte[] passwordHash = getHash(password);
            StringBuilder sb = new StringBuilder();
            sb.setLength(password.length());
            long start = System.currentTimeMillis();
            try {
                findPasswordGivenHashRecursive(0, password.length(), sb, characterSpace, passwordHash, result, start);
            } catch (NoSuchAlgorithmException e) {
                result.append("Error Finding Password");
            }
        } catch (NoSuchAlgorithmException e) {
            result.append("Error Finding Password");
        }

        return result.length() > 0 ? result.toString() : "Could not crack password with character space " + characterSpace;
    }

    public boolean getFound() {
        return found;
    }

    public void setFound(boolean f) {
        found = f;
    }

    public BruteForcePasswordCracker() {
        hashingAlgorithm = "MD5";
        try {
            md = MessageDigest.getInstance(hashingAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Please Enter a Valid Hashing Algorithm for Your Password (MD5, SHA-1, SHA-256, etc)");
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        if (args.length < 4) {
            System.out.println(
                    "Please Input (in this order): \n" +
                            "1. Your Password \n" +
                            "2. The Hashing Algorithm You Wish To Use (MD5, SHA-1, SHA-256, etc) \n" +
                            "3. The Character Space of Your Password: \n" +
                            "   l   (Lower Case Letters) \n" +
                            "   lu  (Lower and Upper Case Letters) \n" +
                            "   ld  (Lower Case Letters and Digits) \n" +
                            "   lud (Lower and Upper Case Letters and Digits) \n" +
                            "4. The Length of Your Password As An Integer"
            );
            return;
        }

        hashingAlgorithm = args[1];
        try {
            md = MessageDigest.getInstance(hashingAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Please Enter a Valid Hashing Algorithm for Your Password (MD5, SHA-1, SHA-256, etc)");
            return;
        }

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
                                "Please Enter One of the Following Character Spaces: \n" +
                                "   l   (Lower Case Letters) \n" +
                                "   lu  (Lower and Upper Case Letters) \n" +
                                "   ld  (Lower Case Letters and Digits) \n" +
                                "   lud (Lower and Upper Case Letters and Digits)"
                );
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

        BigInteger n = new BigInteger(Integer.toString(cs.length()));
        BigInteger numToCheck = n.pow(length);
        String possibilities = numToCheck.toString();
        System.out.println("This brute force password cracking algorithm will need to check at most " + possibilities + " passwords to find your password");
        System.out.println("The number of possible passwords for a given password and the character space it uses is (the character space's length) ^ (the password's length)\n");
        System.out.println("Example: \n" +
                "Your password, " + password + ", has length " + length + " and the character space you said you are using has length " + cs.length() + "\n" +
                cs.length() + "^" + length + " = " + possibilities + "\n");

        System.out.println("Press Enter to Begin");
        reader.readLine();
        reader.close();

        StringBuilder sb = new StringBuilder();
        sb.setLength(length);
        start = System.currentTimeMillis();
        findAndPrintPassword(0, length, sb, cs);
    }

}