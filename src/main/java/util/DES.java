/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 *
 * @author Piero
 */
public class DES {

    public static void main(String[] args) {
        try {
            //Crea una clave DES.
            KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
            SecretKey myDesKey = keygenerator.generateKey();

            Cipher desCipher;

            // Create una instancia de cipher define:algoritmo/modo/esquema de relleno
            desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

            // Iinicializar el cipher para el cifrado
            desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);

            //Convierta String en formato de matriz Byte[].
            byte[] text = "Cuto bienvenido al grupo".getBytes();

            System.out.println("Text [Byte Format] : " + text);
            System.out.println("Text : " + new String(text));

            // Cifrar el texto
            byte[] textEncrypted = desCipher.doFinal(text);

            System.out.println("Texto cifrado : " + textEncrypted);

            // Inicializar el mismo cifrado para el descifrado
            desCipher.init(Cipher.DECRYPT_MODE, myDesKey);

            // Descifrar el texto
            byte[] textDecrypted = desCipher.doFinal(textEncrypted);

            System.out.println("Texto Decifrado : " + new String(textDecrypted));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
    }
}
