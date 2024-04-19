package com.example.bike_sharing_location.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Logger;

/**
 * Class used for hashing user passwords - salting is not included, rainbow table attack is therefore possible
 * doesnt really matter for semester project
 */
public class DefaultEncryption implements EncryptionEngine {
    private Logger logger = Logger.getLogger(DefaultEncryption.class.getName());
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
        logger.info("Generating hash for text: "+text);
        MessageDigest digest = null;
        try{
            digest = MessageDigest.getInstance("SHA3-256");
        } catch (NoSuchAlgorithmException e) {
            logger.severe("Invalid hashing algorithm.");
            throw new RuntimeException(e);
        }
        final byte[] hashBytes = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        String hash = bytesToHex(hashBytes);
        logger.info("Generated hash: "+hash);
        return hash;
    }

    @Override
    public String encodeData(String text) {
        logger.info("Encoding data to base64 format. Original data: "+text);
        return Base64.getEncoder().encodeToString(text.getBytes());
    }

    @Override
    public String decodeData(String text) {
        logger.info("Decoding data from base64. Base64 data: "+text);
        if(text == null || text.length() == 0){
            return null;
        }
        byte[] decodedBytes = null;
        try{
             decodedBytes = Base64.getDecoder().decode(text);
        }
        catch (IllegalArgumentException exception){
            logger.severe("Text: "+ text+" is not encoded in base64!");
            throw new RuntimeException("Text: "+ text+" is not encoded in base64!");
        }
        logger.info("Decoded base64 data: "+new String(decodedBytes));

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
