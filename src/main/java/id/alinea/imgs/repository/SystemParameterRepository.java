package id.alinea.imgs.repository;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import id.alinea.imgs.entity.SystemParameter;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class SystemParameterRepository implements PanacheRepository<SystemParameter> {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
    
	private Map<String, String> systemParameterCodeMap = new HashMap<>();
	
    public String getValueByCode(String code) {
    	if(systemParameterCodeMap.containsKey(code)) {
    		logger.info("fetch data from memory for code {} ", code);
    		return systemParameterCodeMap.get(code);
    	}else {    		
    		final SystemParameter sp =  find("code", code).firstResult();
    		if(sp != null) {
        		logger.info("fetch data from DB for code {} and store to memory", code);
    			systemParameterCodeMap.put(code, sp.getValue());
    			return sp.getValue();
    		}else {
    			return null;
    		}
    	}
    }

}
