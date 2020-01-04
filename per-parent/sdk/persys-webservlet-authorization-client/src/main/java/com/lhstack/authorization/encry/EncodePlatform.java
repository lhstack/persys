package com.lhstack.authorization.encry;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * ClassName: EncodePlatfrom
 * Description:
 * date: 2019/12/24 20:36
 *
 * @author lhstack
 * @since
 */
public class EncodePlatform {

    /**
    * Description: 加密jsonStr
    * date: 2019/12/24 20:37
    * @author lhstack
    * @version
    * @since 1.8
    */
    public static String encode(String jsonStr,String singingKey) throws Exception {
        if(singingKey.length() != 16){
            throw new IllegalArgumentException("singingKey 应该是16位的字符串");
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(singingKey.getBytes(),"AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec);
        byte[] bytes = cipher.doFinal(jsonStr.getBytes());
        return Base64.encodeBase64URLSafeString(bytes);
    }
}
