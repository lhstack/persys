package com.lhstack.entity.permission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "tb_user",indexes = {
        @Index(name = "nick_name",columnList = "nick_name",unique = true),
        @Index(name = "username",columnList = "username",unique = true),
        @Index(name = "email",columnList = "email",unique = true),
        @Index(name = "idcreateTime",columnList = "create_time,id",unique = true)
})
@NoArgsConstructor
@DynamicUpdate
public class User implements Serializable {

    private static final long serialVersionUID = -404075829452882017L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nick_name",unique = true)
    @Length(max = 12,min = 1,message = "昵称长度必须为5-12之间的任意字符")
    private String nickName;

    @Column(unique = true)
    @Pattern(regexp = "^[\\w]{5,18}$",message = "用户名必须为5-18位之间的字母数字")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonIgnore
    private String salt;

    private String icon;

    @Email(message = "邮箱格式不正确")
    @NotNull(message = "邮箱不能为空")
    private String email;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "is_del")
    private Boolean isDel;

    @Column(name = "is_lock")
    private Boolean isLock;

}
