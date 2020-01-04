package com.lhstack.authorization.client;

import com.alibaba.fastjson.JSONObject;
import com.lhstack.authorization.encry.EncodePlatform;
import com.lhstack.authorization.pojo.ClientApiInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClassName: ApiInfoClientUtils
 * Description:
 * date: 2019/12/24 20:30
 *
 * @author lhstack
 * @since
 */
@Slf4j
public class ApiInfoClient {

    /**
     * 签名
     */
    @Value("${persys.client.authority.singingKey:sdfgasghhsdfghws}")
    private String singingKey;

    /**
     * 接受接口信息的url
     */
    @Value("${persys.client.authority.receiveUri:https://admin.lhstack.xyz/remote/api/receive}")
    private String serverReceiveUri;

    /**
     * 校验权限的url
     */
    @Value("${persys.client.authority.checkAuthorityUri:https://admin.lhstack.xyz/remote/api/check}")
    private String checkAuthorityUri;

    @Value("${spring.application.name:default}")
    private String namespace;

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void pushApiInfoToServer(ClientApiInfo clientApiInfo){
        try {
            String jsonStr = EncodePlatform.encode(JSONObject.toJSONString(clientApiInfo),singingKey);
            executorService.execute(() ->{
                try {
                    push(jsonStr);
                } catch (IOException e) {
                    log.error("ApiInfoClient -- > pushApiInfoToServer -- > push -> fail ,clientApiInfo is [{}],ex message is [{}]",clientApiInfo,e.getMessage());
                }
            });
        } catch (Exception e) {
            log.error("ApiInfoClient -- > pushApiInfoToServer -- > fail ,clientApiInfo is [{}],ex message is [{}]",clientApiInfo,e.getMessage());
        }
    }

    private void push(String encodeStr) throws IOException {
        HttpPost httpPost = new HttpPost();
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(2000)
                .setConnectTimeout(2000)
                .setSocketTimeout(2000).build();
        httpPost.setConfig(requestConfig);
        httpPost.addHeader("PERSYS_CLIENT_VERSION","0.0.1");
        httpPost.setURI(URI.create(serverReceiveUri + "?c=" + encodeStr));
        CloseableHttpResponse execute =
                HttpClients.createDefault().execute(httpPost);
        String s = EntityUtils.toString(execute.getEntity());
        log.debug("push apiInfo success --> return info is [{}]",s);
        execute.close();
    }

    public void checkRemoteAuthority(String uniqueMethodKey, String username) throws Exception {
        Map<String,String> map = new HashMap<>();
        map.put("uniqueMethodKey",uniqueMethodKey);
        map.put("username",username);
        map.put("namespace",namespace);
        String encode = EncodePlatform.encode(JSONObject.toJSONString(map), singingKey);
        HttpPost httpPost = new HttpPost();
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(2000)
                .setConnectTimeout(2000)
                .setSocketTimeout(2000).build();
        httpPost.setConfig(requestConfig);
        httpPost.addHeader("PERSYS_CLIENT_VERSION","0.0.1");
        httpPost.setURI(URI.create(checkAuthorityUri + "?c=" + encode));
        CloseableHttpResponse execute =
                HttpClients.createDefault().execute(httpPost);
        String s = EntityUtils.toString(execute.getEntity());
        JSONObject jsonObject = (JSONObject) JSONObject.parse(s);
        Integer code = jsonObject.getInteger("code");
        log.debug("push apiInfo success --> return info is [{}]",s);
        execute.close();
        if(code != 200){
            throw new AuthenticationException(jsonObject.getString("msg"));
        }
    }
}
