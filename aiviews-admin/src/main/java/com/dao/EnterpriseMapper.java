package com.dao;

import java.util.List;

import com.model.Enterprise;

public interface EnterpriseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Enterprise record);

    int insertSelective(Enterprise record);

    Enterprise selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Enterprise record);

    int updateByPrimaryKey(Enterprise record);
    
    List<Enterprise> selectAllEnterprise(Enterprise enterprise);
}