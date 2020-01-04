package com.lhstack.entity.permission.api.remote;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

/**
 * ClassName: RemoteApiInfoToPermission
 * Description:
 * date: 2019/12/25 14:00
 *
 * @author lhstack
 * @since
 */
@Data
@Entity
@Table(name = "tb_remote_api_info_permission",indexes = {
        @Index(
                name = "uniqueKey",
                columnList = "api_info_id,permission_id",
                unique = true
        )
})
@Accessors(chain = true)
public class RemoteApiInfoToPermission implements Serializable {
    private static final long serialVersionUID = 271768461825928490L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "api_info_id")
    private Long apiInfoId;

    @Column(name = "permission_id")
    private Long permissionId;
}
