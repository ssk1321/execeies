package com.example.demo.encorder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

//ハッシュ化するクラス
@Component
public class HashEncorder implements EncordPassword {

	@Override
 public String hushString(String input) throws NoSuchAlgorithmException {
     MessageDigest digest = MessageDigest.getInstance("SHA-256");
     byte[] encodedHash = digest.digest(input.getBytes());
     // inputをByte型に変換

     // バイト配列を16進数文字列に変換
     StringBuilder hexString = new StringBuilder();
     for (byte b : encodedHash) {
         String hex = Integer.toHexString(0xff & b);
         if (hex.length() == 1) {
             hexString.append('0');
         }
         hexString.append(hex);
     }
     return hexString.toString();
 }

}