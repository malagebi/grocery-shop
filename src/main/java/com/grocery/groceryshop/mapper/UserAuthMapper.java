package com.grocery.groceryshop.mapper;

import com.grocery.groceryshop.entity.UserAuth;

public interface UserAuthMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserAuth record);

    int insertSelective(UserAuth record);

    UserAuth selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserAuth record);

    int updateByPrimaryKey(UserAuth record);
}