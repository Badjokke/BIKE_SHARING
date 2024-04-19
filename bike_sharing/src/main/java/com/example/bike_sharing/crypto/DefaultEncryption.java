package com.example.bike_sharing.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class DefaultEncryption implements EncryptionEngine {

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @Override
    public String generateHash(String text) {
        if(text == null){
            return null;
        }
        MessageDigest digest = null;
        try{
            digest = MessageDigest.getInstance("SHA3-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        final byte[] hashBytes = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hashBytes);
    }

    @Override
    public String encodeData(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes());
    }

    @Override
    public String decodeData(String text) {
        if(text == null || text.length() == 0){
            throw new RuntimeException("Text: "+ text+ " is empty or null!");
        }
        byte[] decodedBytes = null;
        try{
             decodedBytes = Base64.getDecoder().decode(text);
        }
        catch (IllegalArgumentException exception){
            throw new RuntimeException("Text: "+ text+" is not encoded in base64!");
        }
        return new String(decodedBytes);
    }

    @Override
    public String encryptData(String text) {
        return null;
    }

    @Override
    public String decryptData(String text) {
        return null;
    }


}
