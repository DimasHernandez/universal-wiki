package co.com.bancolombia.api.auth.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

@Component
@Slf4j
public class PBKDF2Encoder implements PasswordEncoder {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
    private final SecureRandom secureRandom = new SecureRandom();
    @Value("${security.pbkdf2.iterations}")
    private int iterations;
    @Value("${security.pbkdf2.keyLength}")
    private int keyLength;
    @Value("${security.pbkdf2.saltLength}")
    private int saltLength;

    @Override
    public String encode(CharSequence rawPassword) {
        byte[] salt = new byte[saltLength];
        secureRandom.nextBytes(salt);

        byte[] hash = hashPassword(rawPassword.toString(), salt);
        return Base64.getEncoder().encodeToString(salt).concat(":")
                .concat(Base64.getEncoder().encodeToString(hash));
    }

    private byte[] hashPassword(String password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    salt,
                    iterations,
                    keyLength
            );
            return SecretKeyFactory.getInstance(ALGORITHM)
                    .generateSecret(spec)
                    .getEncoded();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while hashing password", e);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        log.info("matches rawPassword: {} encodedPassword: {}", rawPassword, encodedPassword);
        String[] parts = encodedPassword.split(":");
        if (parts.length != 2) {
            return false;
        }

        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] expectedHash = Base64.getDecoder().decode(parts[1]);

        byte[] actualHash = hashPassword(rawPassword.toString(), salt);

        if (actualHash.length != expectedHash.length) {
            return false;
        }
        return Arrays.equals(actualHash, expectedHash);
    }
}
