package com.lhstack.config.proxy.encry;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密工具类
 */
public class DataDecodePlatform {

    /**
     * 对json字符串通过singingKey进行加密，服务端通过singingKey解密
     * @param jsonStr
     * @param singingKey
     * @return
     * @throws Exception
     */
    public String encode(String jsonStr, String singingKey) throws Exception {
        if(singingKey.trim().length() != 16) {
            throw new RuntimeException("签名长度应该为16位");
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(singingKey.getBytes(),"AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec);
        return Base64.encodeBase64URLSafeString(cipher.doFinal(jsonStr.getBytes()));
    }

}
