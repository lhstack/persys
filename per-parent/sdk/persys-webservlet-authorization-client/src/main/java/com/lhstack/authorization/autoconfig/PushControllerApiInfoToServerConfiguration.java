package com.lhstack.authorization.autoconfig;

import com.lhstack.authorization.annotation.DynAuthority;
import com.lhstack.authorization.client.ApiInfoClient;
import com.lhstack.authorization.pojo.ClientApiInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ClassName: PushControllerApiInfoToServerConfiguration <br/>
 * Description: <br/>
 * date: 2019/12/24 20:26<br/>
 *
 * @author lhstack<br />
 * @since
 */
@ConditionalOnExpression("${persys.enable:true}")
public class PushControllerApiInfoToServerConfiguration {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;


    @Autowired
    private ApiInfoClient apiInfoClient;

    @Value("${spring.application.name:default}")
    private String namespace;

    @Bean
    public ApplicationRunner applicationRunner(){
        return (applicationArguments) ->{
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
            handlerMethods.entrySet().forEach(item ->{
                ClientApiInfo clientApiInfo = getClientApiInfo(item);
                if(null != clientApiInfo){
                    apiInfoClient.pushApiInfoToServer(clientApiInfo);
                }
            });
        };
    }

    /**
    * Description: 解析requestMapping封装成ClientApiInfo
    * date: 2019/12/24 21:05
    * @author lhstack
    * @version
    * @since 1.8
    */
    private ClientApiInfo getClientApiInfo(Map.Entry<RequestMappingInfo, HandlerMethod> entry) {
        RequestMappingInfo info = entry.getKey();
        HandlerMethod method = entry.getValue();
        String requestMethods = info.getMethodsCondition().getMethods().stream().map(RequestMethod::name).collect(Collectors.joining(","));
        String pattenUrls = info.getPatternsCondition().getPatterns().stream().map(String::toString).collect(Collectors.joining(","));
        try{
            if(isPresent(method.getMethod())){
                ClientApiInfo apiInfo = new ClientApiInfo();
                apiInfo
                        .setHandlerMethod(getUniqueMethod(method))
                        .setPattenUrl(pattenUrls)
                        .setRequestMethod(requestMethods)
                        .setAuthorityType(getApiInfoType(method.getMethod()))
                        .setDescription(getApiDescription(method.getMethod()))
                        .setNamespace(namespace);
                return apiInfo;
            }else{
                return null;
            }
        }catch (Exception e){
            return null;
        }
    }

    private String getApiDescription(Method method) {
        DynAuthority annotation = AnnotationUtils.findAnnotation(method, DynAuthority.class);
        return annotation.description();
    }

    private String getUniqueMethod(HandlerMethod handlerMethod) {
        Method method = handlerMethod.getMethod();
        String uniqueMethod = handlerMethod.getBeanType().getName() + "." + method.getName();
        String parameter = Arrays.asList(handlerMethod.getMethodParameters()).stream().map(item -> item.getParameter().getName()).collect(Collectors.joining(","));
        uniqueMethod = uniqueMethod + "(" + parameter + ")";
        String returnType = handlerMethod.getReturnType().getParameterType().getName();
        return uniqueMethod + "---------->" + returnType;
    }

    private Integer getApiInfoType(Method method) {
        DynAuthority annotation = AnnotationUtils.findAnnotation(method, DynAuthority.class);
        return annotation.authorityType().getValue();
    }

    private boolean isPresent(Method method) {
        return AnnotationUtils.findAnnotation(method, DynAuthority.class) != null;
    }

}
