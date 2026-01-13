package com.cloud.work.dao.mapper;

import com.cloud.work.dao.model.TaskInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskInfoMapper {
    List<TaskInfo> findAll();
}
