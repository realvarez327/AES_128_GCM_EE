package ee;

public record EncryptionReturnPackage(String ciphertext, String tag, String AAD){}