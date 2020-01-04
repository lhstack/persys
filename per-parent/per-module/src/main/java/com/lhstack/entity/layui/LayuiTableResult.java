package com.lhstack.entity.layui;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@ToString
@Data
@Accessors(chain = true)
public class LayuiTableResult<T> extends LayuiResut<List<T>> {
    private Long count;

}
