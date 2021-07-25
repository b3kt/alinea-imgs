package id.alinea.imgs.service.impl;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import id.alinea.imgs.repository.SystemFileRepository;
import id.alinea.imgs.repository.SystemParameterRepository;

public abstract class BaseService {

	protected Logger logger = LoggerFactory.getLogger(BaseService.class);
	protected Gson gson = new Gson();

	@Inject
	protected SystemFileRepository systemFileRepository;
	
	@Inject
	protected SystemParameterRepository systemParameterRepository;
}
