package com.lhstack.entity.permission;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@Entity
@NoArgsConstructor
@Table(name = "tb_role",indexes = {
        @Index(name = "uniqueKey",columnList = "role_name",unique = true)
})
public class Role implements Serializable {
    private static final long serialVersionUID = -4338855139098244435L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name",unique = true)
    private String roleName;

    private String icon;

    @Column(name = "logogram_name",unique = true)
    private String logogramName;
}
