package com.lhstack.authorization.annotation;

/**
 * ClassName: AuthorityType
 * Description:
 * date: 2019/12/24 20:48
 *
 * @author lhstack
 * @since
 */
public enum AuthorityType {
    HAS_ROLE(0), HAS_PERMISSION(1), AUTHORITY_ANY(2), AUTHORITY_ALL(3);
    private Integer value;

    AuthorityType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
