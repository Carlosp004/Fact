package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {
    
    private static final String SALT = "1nf0rm4t1c4"; // Sal solicitado por el examen
    
    public String hash(String input) {
        try {
            // Aplicar sal a la entrada
            String inputWithSalt = input + SALT;

            // Crear una instancia de MessageDigest para SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Aplicar el algoritmo SHA-256 a la cadena con sal
            byte[] encodedhash = digest.digest(inputWithSalt.getBytes());

            // Convertir el hash resultante a una cadena hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al aplicar SHA-256", e);
        }
    }

    public static void main(String[] args) {
        SHA256 obj = new SHA256();
        String originalString = "ShakiraNilton";
        String hashedString = obj.hash(originalString);
        System.out.println("Texto original: " + originalString);
        System.out.println("Texto cifrado en SHA-256: " + hashedString);
    }
}
