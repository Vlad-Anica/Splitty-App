package commons;

import lombok.Builder;

import java.util.Random;

@Builder
public class Admin{
    private static String generatedPassword;

    public Admin() {

    }

    public static String generateRandomPassword(Random random) {
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*_";
        StringBuilder string = new StringBuilder();
        for(int i = 0; i<32; i++) {
            string.append(characters.charAt((int) (random.nextFloat() * characters.length())));
        }
        String result = string.toString();
        generatedPassword = result;
        return result;
    }

    /**
     * checks whether the attempt is the same as the generated password
     * @param attempt attempted password
     * @return correct or not
     */
    public static boolean isCorrectGeneratedPassword(String attempt){
        return generatedPassword.equals(attempt);
    }


}
