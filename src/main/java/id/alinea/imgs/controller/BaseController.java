package id.alinea.imgs.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import id.alinea.imgs.service.impl.SystemFileService;
import id.alinea.imgs.util.MultipartUtil;
import id.alinea.imgs.vo.multipart.FileItem;

public abstract class BaseController {
    
    protected Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected Map<String,String> multipartParams = new HashMap<>();
	protected List<FileItem> files = new ArrayList<>();
	
	protected MultipartUtil util = new MultipartUtil();

    protected Gson gson = new Gson();
    
    @Inject
    protected SystemFileService systemFileService;

}
