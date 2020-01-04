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
@Table(name = "tb_role_and_permission",indexes = {
        @Index(name = "uniqueKey",unique = true,columnList = "pid,rid")
})
public class RoleToPermission implements Serializable {
    private static final long serialVersionUID = -6268510296253414391L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long pid;

    private Long rid;
}
