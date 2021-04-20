package org.feng.persistent.service;

import org.feng.persistent.entity.Test;

import java.util.List;

public interface TestService {

    List<Test> queryAllByLimit(int offset, int limit);

}