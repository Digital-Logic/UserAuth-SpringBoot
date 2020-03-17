package net.digitallogic.UserLogin.shared;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class Utils {

    public static final int KEY_LENGTH = 30;
    private final static String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final static int ITERATIONS = 10000;

    private final Random random;

    public Utils() {
        this.random = new SecureRandom();
    }

    public String generateId() {
        return generateId(KEY_LENGTH);
    }

    public String generateId(int length) {
        StringBuilder key = new StringBuilder();

        for (int i = 0; i<length; ++i){
            key.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }

        return key.toString();
    }
}
