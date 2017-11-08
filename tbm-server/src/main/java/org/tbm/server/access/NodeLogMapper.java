package org.tbm.server.access;

import org.apache.ibatis.annotations.Param;
import org.tbm.common.bean.NodeLog;

import java.util.List;

/**
 * Created by Jason.Xia on 17/10/26.
 */
public interface NodeLogMapper {

    List<NodeLog> select(@Param("identity") String identity);

    int insert(@Param("node") NodeLog log);

    void create(@Param("schema") NodeLogAccessor schema);
}
