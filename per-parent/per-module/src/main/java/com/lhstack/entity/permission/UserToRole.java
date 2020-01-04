package com.lhstack.entity.permission;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

/**
 * CLASSNAME: ${CLASS_NAME}
 * author: hp
 * date: 2019/12/27
 **/
@Data
@Accessors(chain = true)
@Entity
@Table(name = "tb_user_and_role",indexes = {
        @Index(name = "uniqueKey",unique = true,columnList = "uid,rid")
})
public class UserToRole implements Serializable {

    private static final long serialVersionUID = -8062343277073234127L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long uid;

    private Long rid;
}
