package org.tbm.server.access;


import org.apache.ibatis.annotations.Param;
import org.tbm.common.bean.LogData;

import java.util.List;

/**
 * Created by Jason.Xia on 17/10/25.
 */
public interface LogDataMapper {
    int insert(@Param("table") String table, @Param("list") List<LogData> list);

    List<LogData> select(@Param("table") String table);

    void create(@Param("schema") LogDataAccessor schema);

    void createNext(@Param("schema") LogDataAccessor schema);
}
