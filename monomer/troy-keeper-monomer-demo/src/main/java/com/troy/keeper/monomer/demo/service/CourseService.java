package com.troy.keeper.monomer.demo.service;


import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.core.base.service.BaseService;
import com.troy.keeper.monomer.demo.domain.Course;
import org.springframework.stereotype.Service;

/**
 * Created by yg on 2017/6/8.
 */
@Service
public interface CourseService extends BaseService<Course, BaseDTO> {
}
