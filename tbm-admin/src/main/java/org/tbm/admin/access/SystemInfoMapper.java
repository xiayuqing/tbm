package org.tbm.admin.access;

import org.apache.ibatis.annotations.Param;
import org.tbm.common.bean.SystemInfo;

import java.util.List;

/**
 * Created by Jason.Xia on 17/7/13.
 */
public interface SystemInfoMapper {

    int insert(@Param("info") SystemInfo info);

    int remove(@Param("id") long id);

    List<SystemInfo> selectAll();

    SystemInfo selectById(@Param("id") long id);
}
