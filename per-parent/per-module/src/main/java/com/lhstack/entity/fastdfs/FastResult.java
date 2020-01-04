package com.lhstack.entity.fastdfs;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FastResult {
    private Long size;
    private String fileName;
    private String path;
}
