package com.kurakura.posblockchain.backend.service;

import com.kurakura.posblockchain.backend.exception.BusinessLogicFailureException;
import com.kurakura.posblockchain.infra.constants.ErrorCode;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CyptoService {

    public Map<String, String> createEncodedKeys() {
        final Map<String, String> keys = new HashMap<>();
        try {
            final KeyPairGenerator kg = KeyPairGenerator.getInstance("RSA");
            kg.initialize(1024);
            final KeyPair keyPair = kg.generateKeyPair();
            final KeyFactory factory = KeyFactory.getInstance("RSA");
            final RSAPublicKeySpec publicKeySpec = factory.getKeySpec(keyPair.getPublic(), RSAPublicKeySpec.class);
            final RSAPrivateKeySpec privateKeySpec = factory.getKeySpec(keyPair.getPrivate(), RSAPrivateKeySpec.class);

            final PublicKey publicKey = factory.generatePublic(publicKeySpec);
            final PrivateKey privateKey = factory.generatePrivate(privateKeySpec);

            final String publicK = Base64.encodeBase64String(publicKey.getEncoded());
            final String privateK = Base64.encodeBase64String(privateKey.getEncoded());

            keys.put("publicKey", publicK);
            keys.put("privateKey", privateK);
            return keys;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(String.format("Failed to create encoded private key & publick key. %s: %s", e.getClass(), e.getMessage()));
            throw new BusinessLogicFailureException(ErrorCode.ER_500_1, e.getMessage());
        }
    }

    public PublicKey decodPublicKey(String publicK) {
        final byte[] publicBytes = Base64.decodeBase64(publicK);
        final X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(publicBytes);
        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePublic(keySpecX509);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(String.format("Failed to decode public key. %s: %s", e.getClass(), e.getMessage()));
            throw new BusinessLogicFailureException(ErrorCode.ER_500_1, e.getMessage());
        }
    }

    public PrivateKey decodePrivateKey(String privateK) {
        final byte[] privateBytes = Base64.decodeBase64(privateK);
        final PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(privateBytes);
        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePrivate(keySpecPKCS8);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(String.format("Failed to decode private key. %s: %s", e.getClass(), e.getMessage()));
            throw new BusinessLogicFailureException(ErrorCode.ER_500_1, e.getMessage());
        }
    }

    public byte[] encrypto(final String plainText, final PrivateKey privatekey) {
        try {
            Cipher encrypter = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            encrypter.init(Cipher.ENCRYPT_MODE, privatekey);
            return encrypter.doFinal(plainText.getBytes());
        } catch (GeneralSecurityException e) {
            log.error(String.format("Failed to encrypto texts with private key.  %s: %s", e.getClass(), e.getMessage()));
            throw new BusinessLogicFailureException(ErrorCode.ER_500_1, e.getMessage());
        }
    }

    public String decrypto(byte[] cryptoText, PublicKey publickey) {
        try {
            Cipher decrypter = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            decrypter.init(Cipher.DECRYPT_MODE, publickey);
            return new String(decrypter.doFinal(cryptoText));
        } catch (GeneralSecurityException e) {
            log.error(String.format("Failed to decrypto cryptoText with public key.  %s: %s", e.getClass(), e.getMessage()));
            throw new BusinessLogicFailureException(ErrorCode.ER_500_1, e.getMessage());
        }
    }
}