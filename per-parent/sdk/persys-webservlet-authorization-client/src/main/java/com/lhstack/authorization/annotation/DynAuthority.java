package com.lhstack.authorization.annotation;

import java.lang.annotation.*;

/**
 * ClassName: DynAuthority <br/>
 * Description: <br/>
 * date: 2019/12/24 20:27<br/>
 *
 * @author lhstack<br />
 * @since
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DynAuthority {

    /**
    * Description: 初始化授权类型
    * date: 2019/12/24 20:47
    * @author lhstack
    * @version
    * @since 1.8
    */
    AuthorityType authorityType() default AuthorityType.HAS_ROLE;

    String description() default "";
}
