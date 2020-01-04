package com.lhstack.entity.permission.api.remote;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

/**
 * ClassName: RemoteApiInfoToRole
 * Description:
 * date: 2019/12/25 14:03
 *
 * @author lhstack
 * @since
 */
@Table(name = "tb_remote_api_info_role",indexes = {
        @Index(
                name = "uniqueKey",
                columnList = "api_info_id,role_id",
                unique = true
        )
})
@Entity
@Data
@Accessors(chain = true)
public class RemoteApiInfoToRole implements Serializable {
    private static final long serialVersionUID = 3526934967875399296L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "api_info_id")
    private Long apiInfoId;

    @Column(name = "role_id")
    private Long roleId;
}
