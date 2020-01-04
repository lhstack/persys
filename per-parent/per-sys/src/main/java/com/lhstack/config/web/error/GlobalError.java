package com.lhstack.config.web.error;

import com.lhstack.entity.layui.LayuiResut;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理
 */
@Component
@Order(-2)
@Slf4j
public class GlobalError extends AbstractErrorWebExceptionHandler {

    @Value("${server.error.path:/error}")
    private String errpath;

    @Value("${spring.freemarker.template-loader-path:/templates/}")
    private String[] prefixPath;

    @Autowired
    private Configuration configuration;

    public GlobalError(ServerCodecConfigurer serverCodecConfigurer,ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, applicationContext);
        super.setMessageReaders(serverCodecConfigurer.getReaders());
        super.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(
                RequestPredicates.all(),
                serverRequest -> {
                    Throwable error = errorAttributes.getError(serverRequest);
                    if(error instanceof ResponseStatusException){
                        ResponseStatusException responseStatusException = (ResponseStatusException) error;
                        HttpStatus status = responseStatusException.getStatus();
                        try {
                            return statusHandle(status,serverRequest,errorAttributes);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    log.error("error as {}",errorAttributes.getErrorAttributes(serverRequest,true));
                    LayuiResut<Throwable> layuiResut = new LayuiResut<>();
                    layuiResut.setMsg(error.getMessage())
                            .setCode(403)
                            .setData(null);
                    return ServerResponse.status(HttpStatus.BAD_REQUEST)
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .body(BodyInserters.fromValue(layuiResut));
                }
        );
    }

    private Mono<ServerResponse> statusHandle(HttpStatus status, ServerRequest serverRequest,ErrorAttributes errorAttributes) throws IOException, TemplateException {
        Template template = configuration.getTemplate(errpath + "/" + status.value() + ".ftl");
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        ServerWebExchange exchange = serverRequest.exchange();
        template.process(initMapData(errorAttributes),new OutputStreamWriter(arrayOutputStream));
        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
        byte[] bytes = arrayOutputStream.toByteArray();
        ByteArrayResource byteArrayResource = new ByteArrayResource(bytes);
        return ServerResponse.status(status)
                .header("content-type","text/html;charset=utf-8")
                .body(BodyInserters.fromDataBuffers(DataBufferUtils.read(byteArrayResource,dataBufferFactory,bytes.length)));
    }

    private Map<String,Object> initMapData(ErrorAttributes errorAttributes) {
        Map<String,Object> map = new HashMap<>();
        map.put("error",errorAttributes);
        return map;
    }
}
