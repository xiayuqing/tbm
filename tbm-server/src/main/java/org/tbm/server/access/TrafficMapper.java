package org.tbm.server.access;

import org.apache.ibatis.annotations.Param;
import org.tbm.common.bean.Traffic;

import java.util.List;

/**
 * Created by Jason.Xia on 17/10/29.
 */
public interface TrafficMapper {
    int insert(@Param("table") String table, @Param("list") List<Traffic> list);

    void create(@Param("schema") TrafficAccessor schema);

    void createNext(@Param("schema") TrafficAccessor schema);
}
