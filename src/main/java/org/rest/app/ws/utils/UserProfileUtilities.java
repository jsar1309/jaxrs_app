package org.rest.app.ws.utils;

import org.rest.app.ws.exceptions.MissingRequiredFieldException;
import org.rest.app.ws.shared.dto.UserDTO;
import org.rest.app.ws.ui.model.response.ErrorMessages;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

public class UserProfileUtilities {

    private final Random random = new SecureRandom();
    private final String Alphabet = "0123456789ZXCVBNMASDFGHJKLQWERTYUIOPzxcvbnmasdfghjklqwertyuiop";
    private static final int iterations = 10000;
    private static final int key = 256;

    public void validateRequiredFields(UserDTO userDTO) throws MissingRequiredFieldException {
        if (userDTO.getFirstName() == null || userDTO.getFirstName().isEmpty()
                || userDTO.getLastName() == null || userDTO.getLastName().isEmpty()
                || userDTO.getEmail() == null || userDTO.getEmail().isEmpty()
                || userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            throw new MissingRequiredFieldException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }
    }

    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private String generateRandomString(int length){
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(Alphabet.charAt(random.nextInt(Alphabet.length())));
        }
        return new String(sb);
    }

    public String generateSalt(int length){
        return generateRandomString(length);
    }

    public static String generateSecurePassword(String password, String salt){
        byte[] securePassword = hash(password.toCharArray(), salt.getBytes());
        String value = Base64.getEncoder().encodeToString(securePassword);
        return value;
    }

    public static byte[] hash(char[] password, byte[] salt){
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, key);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return secretKeyFactory.generateSecret(spec).getEncoded();
        } catch(NoSuchAlgorithmException n){
            throw new AssertionError("Error while hashing a password: " + n.getMessage(), n);
        } catch (InvalidKeySpecException i){
            throw new AssertionError("Error while hashing a password: " + i.getMessage(), i);
        } finally {
            spec.clearPassword();
        }
    }

    public static byte[] encrypt(String securePassword, String accessTokenMaterial) throws InvalidKeySpecException {
        return hash(securePassword.toCharArray(), accessTokenMaterial.getBytes());
    }

}
