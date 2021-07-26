package id.alinea.imgs.controller;

import java.io.IOException;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import id.alinea.imgs.config.Constant;
import id.alinea.imgs.exception.MultipartException;
import id.alinea.imgs.exception.StorageFileNotFoundException;
import id.alinea.imgs.vo.ResponseMessage;
import id.alinea.imgs.vo.multipart.FileItem;
import id.alinea.imgs.vo.multipart.PartHandler;
import io.quarkus.security.identity.SecurityIdentity;

@RestController
@RequestMapping("/imgs")
public class FileUploadController extends BaseController {

	@Context
	private HttpServletRequest request;
	
    @Inject
    SecurityIdentity identity;
    
    @Inject
    JsonWebToken jwt;

	private PartHandler partHandler;

	@RolesAllowed("/user")
	@GetMapping("/test")
	public @ResponseBody ResponseEntity<String> defaultPage(){
		final String msg = "Woy!";
		logger.info("------------------------------token {} ", jwt.getSubject());
		logger.info("------------------------------indentity {} ", identity);
		return ResponseEntity.ok(gson.toJson(new ResponseMessage(HttpStatus.SC_OK, msg)));
	}

	@RolesAllowed("/user")
	@DeleteMapping("/delete/{uuid}")
	public @ResponseBody ResponseEntity<String> deleteFile(@PathVariable(value = Constant.FIELD_UUID) String uuid) throws IOException {
		String msg = null;
		if(StringUtils.isNotBlank(uuid)) {
			try {
				systemFileService.deleteFileByUuid(uuid);
				msg = "File successfully deleted";
			}catch(Exception ex) {
				msg = ex.getMessage();
			}
		}
		return ResponseEntity.ok(gson.toJson(new ResponseMessage(HttpStatus.SC_OK, msg)));
	}

	@RolesAllowed("/user")
	@PostMapping("/upload")
	public @ResponseBody ResponseEntity<String> handleFileUpload() {
		initRequest();

		String msg = null;
		if(validateRequest()){
			final String createdBy = jwt.getSubject();
			
			logger.info("asdasd {}", systemFileService);
			
			msg = "file successfully uploaded " + files.get(0).getFileName() + "!";
			
			try {
				systemFileService.storeFile(files.get(0), 
						multipartParams.get(Constant.FIELD_ID), 
						multipartParams.get(Constant.FIELD_NAME), 
						multipartParams.get(Constant.FIELD_FIELD), 
						createdBy);
			}catch(Exception ex) {
				msg = ex.getMessage();
			}
		} else {
			msg = "Unable to upload file !"; 
		}
		return ResponseEntity.ok(gson.toJson(new ResponseMessage(HttpStatus.SC_OK, msg)));
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<Object> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleException(Exception exc) {
		logger.info("generic error {} ", exc.getClass().getName());
		return ResponseEntity.badRequest().build();
	}

	private boolean validateRequest(){
		final boolean hasAuthz = jwt != null;
		final boolean hasData = multipartParams != null 
				&& multipartParams.containsKey(Constant.FIELD_ID) 
				&& multipartParams.containsKey(Constant.FIELD_NAME) 
				&& multipartParams.containsKey(Constant.FIELD_FIELD);
		final boolean hasFile = !CollectionUtils.isEmpty(files); 
		return hasAuthz && hasData && hasFile;
	}

	private void initRequest() {

		// RESET PARAMS
		files.clear();
		multipartParams.clear();

		try{
			util.parse(request, getPartHandler());
		}catch(MultipartException ex){
			ex.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

    private PartHandler getPartHandler() {
		if(partHandler == null){
			partHandler = new PartHandler() {
				@Override
				public void handleFormItem(String name, String value) {
					multipartParams.put(name, value);
				}
	
				@Override
				public void handleFileItem(String name, FileItem fileItem) {
					logger.info("{}", fileItem.getFile());
					
					files.add(fileItem);
					
				}
			};
		}	
        return partHandler;
    }

}
