package com.boot.base.service.impl;

import com.boot.base.dao.LogDao;
import com.boot.base.service.LogService;
import com.boot.common.dto.LogDO;
import com.boot.common.utils.Query;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@Mapper
public class LogServiceImpl implements LogService {
    @Autowired
    LogDao logMapper;
    @Override
    public int save(LogDO logDO) {
        return logMapper.save(logDO);
    }

    @Override
    public List<LogDO> queryList(Query query) {
       List<LogDO> logs=logMapper.list(query);
       return logs;
    }

    @Override
    public int count(Query query) {
        return logMapper.count(query);
    }

    @Override
    public int remove(Long id) {
        int count= logMapper.remove(id);
        return count;
    }

    @Override
    public int batchRemove(Long[] ids) {
        return logMapper.batchRemove(ids);
    }
}
