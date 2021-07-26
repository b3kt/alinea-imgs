package id.alinea.imgs.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import id.alinea.imgs.config.Constant;
import id.alinea.imgs.entity.SystemFile;
import id.alinea.imgs.service.ISystemFileService;
import id.alinea.imgs.vo.multipart.FileItem;
import io.imagekit.sdk.models.results.Result;

@Named
@Transactional
public class SystemFileService extends BaseService implements ISystemFileService {

	@Inject
	ImageKitService imageKitService;

	@Override
	public void storeFile(FileItem file, String entityId, String entityName, String entityField, String createdBy)
			throws Exception {
		if (file != null && StringUtils.isNotEmpty(entityId) && StringUtils.isNotEmpty(entityName)
				&& StringUtils.isNotEmpty(entityField)) {

			final String filename = file.getFileName();
			final List<String> tags = Arrays.asList(entityId, entityName, entityField, createdBy);
			final Result result = imageKitService.upload(file, filename, file.getContentType(), tags);

			if (result != null && result.isSuccessful()) {
				String fileId = result.getFileId();
				final Map<String, Object> mapResult = result.getMap();
				
				if(fileId == null && mapResult != null) {
					fileId = String.class.cast(mapResult.get(Constant.FIELD_FILE_ID));
					logger.info("repopulate from raw response and got {} ", fileId);
				}
				
				if(fileId != null) {
					SystemFile sysFile = new SystemFile();
					sysFile.setFileName(filename);
					sysFile.setEntityId(entityId);
					sysFile.setEntityType(entityName);
					sysFile.setEntityField(entityField);
					sysFile.setCreatedBy(StringUtils.isNotBlank(createdBy) ? createdBy : Constant.DEFAULT_USER);
					sysFile.setCreatedDate(new Date());
					sysFile.setVersion(0L);
					sysFile.setFileType(file.getContentType());
					sysFile.setFileUuid(fileId);

					final String raw = result.getRaw();
					if(StringUtils.isNotBlank(raw)) {
						final JsonObject jsonObject = gson.fromJson(raw, JsonObject.class);
						if (jsonObject != null) {
							if(jsonObject.has(Constant.FIELD_THUMBNAIL_URL)) {
								final String thumbnailUrl = jsonObject.get(Constant.FIELD_THUMBNAIL_URL).getAsString();
								sysFile.setThumbnailUrl(thumbnailUrl);
							}
							
							if(jsonObject.has(Constant.FIELD_FILE_PATH)) {
								final String fieldPath = jsonObject.get(Constant.FIELD_FILE_PATH).getAsString();
								sysFile.setFilePath(fieldPath);
							}

							if(jsonObject.has(Constant.FIELD_TAGS)) {
								final JsonElement tmptags = jsonObject.get(Constant.FIELD_TAGS);
								if(tmptags != null) {									
									sysFile.setTags(gson.toJson(tmptags));
								}
							}

							if(jsonObject.has(Constant.FIELD_URL)) {
								final String url = jsonObject.get(Constant.FIELD_URL).getAsString();
								sysFile.setUrl(url);
							}
						}
						
						sysFile.setImagekitRawResponse(raw);
					}

					logger.info("data to be save {}", sysFile);

					try {
						systemFileRepository.persist(sysFile);
						logger.info("File meta {} stored to DB with id {}", filename, sysFile.getId());
						logger.info("File {} stored to disk with path ", raw);
					}catch (Exception e) {
						// ROLLBACK UPLOADED IMAGES
						imageKitService.delete(fileId);
						logger.info("rollback upload for  {}", fileId);
						
						throw e;
					}
				} else {
					logger.info("unable to fetch file Id {} ", fileId);
				}
			} else {
				logger.info("unable to upload {} ", result.getRaw());
			}
		}
	}

	@Override
	public void deleteFileByUuid(String fileId) {
		if (StringUtils.isNotBlank(fileId)) {

			final SystemFile systemFile = systemFileRepository.findByFileUid(fileId);
			if (systemFile != null) {
				Result result = imageKitService.delete(systemFile.getFileUuid());
				if(result != null && result.isSuccessful()) {					
					systemFileRepository.delete(systemFile);
					logger.info("file successfully delete {} ", fileId);
				} else {
					logger.info("unable to delete from imagekit {} ", fileId);
				}
			} else {
				logger.info("unable to delete {} ", fileId);
			}
		}
	}

	@Override
	public SystemFile findSystemFileByUuid(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

}
