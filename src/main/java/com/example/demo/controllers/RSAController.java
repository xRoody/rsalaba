package com.example.demo.controllers;

import com.example.demo.dtos.KeyDTO;
import com.example.demo.services.RSAService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


@RestController
@AllArgsConstructor
@RequestMapping("/rsa")
public class RSAController {
    private final RSAService rsaService;

    @GetMapping("/generate")
    public ResponseEntity<KeyDTO> generateKey() throws NoSuchAlgorithmException {
        return ResponseEntity.ok(
                KeyDTO
                        .builder()
                        .key(rsaService.generateKeys())
                        .build()
        );
    }

    @PostMapping("/decode")
    public ResponseEntity<byte[]> decode(
            @RequestParam(value = "file") MultipartFile file,
            @RequestParam(value = "key") String key
    ) throws NoSuchAlgorithmException, IOException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return ResponseEntity.ok(rsaService.decode(file.getBytes(), key));
    }

    @PostMapping(value = "/encode")
    public ResponseEntity<byte[]> encode(
            @RequestParam(value = "file") MultipartFile file,
            @RequestParam(value = "key") String key
    ) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        return ResponseEntity.ok(rsaService.encode(file.getInputStream().readAllBytes(), key));
    }
}
