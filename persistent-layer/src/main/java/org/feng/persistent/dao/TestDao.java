package org.feng.persistent.dao;

import org.apache.ibatis.annotations.Param;
import org.feng.persistent.entity.Test;

import java.util.List;


public interface TestDao {

    List<Test> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

}