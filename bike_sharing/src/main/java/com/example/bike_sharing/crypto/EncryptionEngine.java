package com.example.bike_sharing.crypto;

import java.security.NoSuchAlgorithmException;

public interface EncryptionEngine {
    String generateHash(String text);
    String encodeData(String text);
    String decodeData(String text);
    String encryptData(String text);
    String decryptData(String text);



}
