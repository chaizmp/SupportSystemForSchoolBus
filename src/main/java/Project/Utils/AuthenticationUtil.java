package Project.Utils;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

/* created by Chaiz */
@Service
public class AuthenticationUtil {

    private final SecureRandom random;

    public AuthenticationUtil()
    {
        this.random = new SecureRandom();
    }

    public String hash(String password, byte[] salt){
//        byte[] salt = new byte[16];
//        random.nextBytes(salt);
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = f.generateSecret(spec).getEncoded();
            Base64.Encoder enc = Base64.getEncoder();
            System.out.printf("salt: %s%n", enc.encodeToString(salt));
            System.out.printf("hash: %s%n", enc.encodeToString(hash));
            return enc.encodeToString(hash);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public byte[] generateSalt(){
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

}