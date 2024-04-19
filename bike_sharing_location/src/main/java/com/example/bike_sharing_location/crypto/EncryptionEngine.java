package com.example.bike_sharing_location.crypto;

/**
 * interface for encryption engine providing hashing, base64 coding and encryption
 */
public interface EncryptionEngine {
    String generateHash(String text);
    String encodeData(String text);
    String decodeData(String text);
    String encryptData(String text);
    String decryptData(String text);



}
