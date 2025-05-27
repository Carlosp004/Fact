/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class DES1 {

    private static final String ALGORITHM = "DES/ECB/PKCS5Padding";

    // Método para cifrar texto plano con clave DES (clave de 8 bytes)
    public static String cifrar(String textoPlano, String clave) throws Exception {
        if (clave == null || clave.length() != 8) {
            throw new IllegalArgumentException("La clave DES debe tener exactamente 8 caracteres");
        }
        byte[] keyBytes = clave.getBytes("UTF-8");
        SecretKey secretKey = new SecretKeySpec(keyBytes, "DES");

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] textoPlanoBytes = textoPlano.getBytes("UTF-8");
        byte[] encryptedBytes = cipher.doFinal(textoPlanoBytes);

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Método para descifrar texto cifrado (Base64) con clave DES
    public static String decifrar(String textoCifrado, String clave) throws Exception {
        if (clave == null || clave.length() != 8) {
            throw new IllegalArgumentException("La clave DES debe tener exactamente 8 caracteres");
        }
        byte[] keyBytes = clave.getBytes("UTF-8");
        SecretKey secretKey = new SecretKeySpec(keyBytes, "DES");

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] encryptedBytes = Base64.getDecoder().decode(textoCifrado);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes, "UTF-8");
    }
}
