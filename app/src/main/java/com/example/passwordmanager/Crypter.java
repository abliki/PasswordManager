package com.example.passwordmanager;

import android.content.Context;
import android.util.Base64;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Crypter {

    public String encrypt(Context context, String text, String id) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        SecretKeySpec key = generateKey(id);
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] textBytes = c.doFinal(text.getBytes());
        String cipherText = Base64.encodeToString(textBytes,Base64.DEFAULT);
//        Toast.makeText(context, cipherText,Toast.LENGTH_SHORT).show(); // for demonstration
        return cipherText;
    }

    public String decrypt(String cipherText, String id) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        SecretKeySpec key = generateKey(id);
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.decode(cipherText,Base64.DEFAULT);
        byte[] decValue = c.doFinal(decodedValue);
        String decryptedText = new String(decValue);
        return decryptedText;
    }

    private SecretKeySpec generateKey(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = text.getBytes("UTF-8");
        digest.update(bytes, 0 , bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }
}
