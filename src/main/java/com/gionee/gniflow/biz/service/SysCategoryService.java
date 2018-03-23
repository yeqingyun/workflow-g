package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gniflow.biz.model.SysCategory;

public interface SysCategoryService {
   void add(SysCategory sysCategory );
   List<Integer> getAllByPid(Integer pid);
   Integer query(SysCategory sysCategory);
}
