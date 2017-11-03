package org.tbm.server.access;

import org.apache.ibatis.annotations.Param;
import org.tbm.common.bean.WorkNode;

/**
 * Created by Jason.Xia on 17/10/26.
 */
public interface WorkNodeMapper {

    WorkNode select(@Param("identity") String identity);

    int insert(@Param("node") WorkNode node);

    int updateStatus(@Param("identity") String identity, @Param("status") int status);

    void create(@Param("schema") WorkNodeAccessor schema);
}
