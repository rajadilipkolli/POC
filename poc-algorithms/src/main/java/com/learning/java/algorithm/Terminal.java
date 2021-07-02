package com.learning.java.algorithm;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Terminal {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
        String url =
                "https://exxonmobil.captiveye002.com/desplaines/refresh/default_embed.asp?id=1542377888&signature=signature1";
        String hmac = "";
        SecretKey secretKey = null;
        // Create formatter
        DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

        // Local date time instance
        ZonedDateTime zonedTime = ZonedDateTime.now(ZoneId.of("America/New_York"));

        // Get formatted String
        String ldtString = FOMATTER.format(zonedTime) + "lockport";

        System.out.println(ldtString);

        String keyString = "LVxtKQSmfaVVLuc35Qgy36ZpTfWfdxtrMfVmBJm3";

        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secret = new SecretKeySpec(keyString.getBytes(), "HmacSHA1");
        mac.init(secret);
        byte[] digest = mac.doFinal(ldtString.getBytes());
        BigInteger hash = new BigInteger(1, digest);
        hmac = hash.toString(16);

        if (hmac.length() % 2 != 0) {
            hmac = "0" + hmac;
        }

        System.out.println(hmac);
    }
}
