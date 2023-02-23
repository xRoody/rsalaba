package com.example.demo.services;

import com.example.demo.repositories.KeyPairRepository;
import jakarta.persistence.NoResultException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
@AllArgsConstructor
public class RSAService {
    private KeyPairRepository keyPairRepository;

    public String generateKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        while (keyPairRepository.existsByPublicKey(pair.getPublic().toString())){
            pair=generator.generateKeyPair();
        }
        keyPairRepository.save(com.example.demo.entities.KeyPair.builder().publicKey(new String(pair.getPublic().getEncoded())).privateKey(pair).build());
        return Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
    }

    public byte[] decode(byte[] bytes, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        bytes=Base64.getDecoder().decode(bytes);
        key=new String(Base64.getDecoder().decode(key));
        KeyPair privateKey=keyPairRepository.findByPublicKey(key).orElseThrow(NoResultException::new).getPrivateKey();
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey.getPrivate());
        return decryptCipher.doFinal(bytes);
    }

    public byte[] encode(byte[] bytes, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] encoded=Base64.getDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] b=encryptCipher.doFinal(bytes);
        b=Base64.getEncoder().encode(b);
        return b;
    }
}
