package id.alinea.imgs.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;

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
				SystemFile sysFile = new SystemFile();
				sysFile.setFileName(filename);
				sysFile.setEntityId(entityId);
				sysFile.setEntityType(entityName);
				sysFile.setEntityField(entityField);
				sysFile.setCreatedBy(StringUtils.isNotBlank(createdBy) ? createdBy : Constant.DEFAULT_USER);
				sysFile.setCreatedDate(new Date());
				sysFile.setVersion(0L);
				sysFile.setFileType(file.getContentType());
				sysFile.setFileUuid(result.getFileId());

				sysFile.setFilePath(result.getFilePath());
				sysFile.setTags(gson.toJson(result.getTags()));
				sysFile.setUrl(result.getUrl());

				final JsonObject jsonObject = gson.fromJson(result.getRaw(), JsonObject.class);
				if (jsonObject != null && jsonObject.has(Constant.FIELD_THUMBNAIL_URL)) {
					final String thumbnailUrl = jsonObject.get(Constant.FIELD_THUMBNAIL_URL).getAsString();
					sysFile.setThumbnailUrl(thumbnailUrl);
				}
				sysFile.setImagekitRawResponse(result.getRaw());

				logger.info("data to be save {}", sysFile);

				systemFileRepository.persist(sysFile);

				logger.info("File meta {} stored to DB with id {}", file.getFileName(), sysFile.getId());
				logger.info("File {} stored to disk with path ", result.getRaw());
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
