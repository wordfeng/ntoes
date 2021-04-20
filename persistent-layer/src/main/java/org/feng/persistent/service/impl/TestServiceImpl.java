package org.feng.persistent.service.impl;

import org.feng.persistent.dao.TestDao;
import org.feng.persistent.entity.Test;
import org.feng.persistent.service.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("testService")
public class TestServiceImpl implements TestService {
    @Resource
    private TestDao testDao;

    @Override
    public List<Test> queryAllByLimit(int offset, int limit) {
        return this.testDao.queryAllByLimit(offset, limit);
    }


}