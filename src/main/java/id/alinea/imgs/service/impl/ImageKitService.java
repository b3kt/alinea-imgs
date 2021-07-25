package id.alinea.imgs.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import id.alinea.imgs.config.Constant;
import id.alinea.imgs.service.IImageKitService;
import id.alinea.imgs.vo.multipart.FileItem;
import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.config.Configuration;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import io.imagekit.sdk.utils.Utils;

@Named
public class ImageKitService extends BaseService implements IImageKitService{
	
	private Configuration configuration;
	private ImageKit imageKit;
	
	@ConfigProperty(name = "PublicKey")
	String publicKey;
	
	@ConfigProperty(name = "PrivateKey")
	String privateKey;
	
	@ConfigProperty(name = "UrlEndpoint")
	String urlEndpoint;
	
	@Override
	public ImageKit getImageKitInstance(){
		try{
			if(imageKit == null) {				
				if(configuration == null) {
					configuration = new Configuration(publicKey, privateKey, urlEndpoint);
				}				
				imageKit = ImageKit.getInstance();
				imageKit.setConfig(configuration);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return imageKit;
	}

	@Override
	public Result upload(FileItem file, String filename, String mimeType, List<String> tags) throws IOException {

		logger.info("file {} {} {} {} ", file.getFile(), filename, mimeType, tags);
		
		if(file != null && StringUtils.isNotBlank(filename) && StringUtils.isNotBlank(mimeType)){

            final String uploadDir = systemParameterRepository.getValueByCode(Constant.IMAGEKIT_UPLOAD_DIR);

    		logger.info("uploadDir {} ", uploadDir);
            
    		byte[] byteFile =  FileUtils.readFileToByteArray(file.getFile());
            String base64 = Utils.bytesToBase64(byteFile);
            
            logger.info("fileBase64 {} ", base64);
            
            
            FileCreateRequest fileCreateRequest = new FileCreateRequest(base64, filename);
            fileCreateRequest.setTags(tags); // optional
            fileCreateRequest.setFolder(StringUtils.isNotBlank(uploadDir) ? uploadDir : "alinea/uploads");  // optional
            fileCreateRequest.setPrivateFile(false);  // optional
            fileCreateRequest.setUseUniqueFileName(true);  // optional

            List<String> responseFields=new ArrayList<>();
            responseFields.add(Constant.FIELD_TAGS);
            fileCreateRequest.setResponseFields(responseFields); // optional
            Result result = getImageKitInstance().upload(fileCreateRequest);
            
    		logger.info("result {} ", result);
    		return result;
    		
        } else {
            return null;
        }
	}
	
	@Override
	public Result delete(String fileId) {
		
		if(StringUtils.isNotBlank(fileId)){
            Result result = getImageKitInstance().deleteFile(fileId);
            
    		logger.info("result {} ", result);
    		return result;
    		
        } else {
            return null;
        }
	}

}
