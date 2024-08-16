package de.marius.fnvw.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class KeyGeneratorUtility {

    private KeyGeneratorUtility() {
        throw new IllegalStateException("Utility class");
    }

    public static KeyPair generateRsaKey() {
        KeyPair keypair;

        try {
            KeyPairGenerator keypairGenerator = KeyPairGenerator.getInstance("RSA");
            keypairGenerator.initialize(4096);
            keypair = keypairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException("Error generating RSA key pair.", e);
        }

        return keypair;
    }
}
