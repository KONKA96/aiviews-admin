package com.service;

import java.util.List;

import com.model.Enterprise;

public interface EnterpriseService {
	
	int insertSelective(Enterprise enterprise);

    Enterprise selectByPrimaryKey(Enterprise enterprise);

    int updateByPrimaryKeySelective(Enterprise enterprise);
    
    List<Enterprise> selectAllEnterprise(Enterprise enterprise);
    
    int deleteByPrimaryKey(Enterprise enterprise);

}
