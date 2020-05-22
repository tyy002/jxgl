package com.example.demo.base.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.tmsps.ne4spring.base.IBaseService;

public class BaseService {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	//@Qualifier("primaryJdbcTemplate")
	protected JdbcTemplate jt;
	@Autowired
	protected IBaseService bs;
}
