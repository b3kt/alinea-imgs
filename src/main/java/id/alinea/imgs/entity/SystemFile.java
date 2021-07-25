package id.alinea.imgs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="system_files")
public class SystemFile extends BaseEntity{
    
    @Column(name="file_name")
    private String fileName;
    
    @Column(name="file_type")
    private String fileType;
    
    @Column(name="entity_id")
    private String entityId;
    
    @Column(name="entity_type")
    private String entityType;

    @Column(name="entity_field")
    private String entityField;

    @Column(name="file_uuid")
    private String fileUuid;

    @Column(name="url")
    private String url;
    
    @Column(name="thumbnail_url")
    private String thumbnailUrl;
    
    @Column(name="file_path")
    private String filePath;
    
    @Column(name="tags")
    private String tags;
    
    @Column(name="imagekit_raw_response")
    private String imagekitRawResponse;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntityField() {
		return entityField;
	}

	public void setEntityField(String entityField) {
		this.entityField = entityField;
	}

	public String getFileUuid() {
		return fileUuid;
	}

	public void setFileUuid(String fileUuid) {
		this.fileUuid = fileUuid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getImagekitRawResponse() {
		return imagekitRawResponse;
	}

	public void setImagekitRawResponse(String imagekitRawResponse) {
		this.imagekitRawResponse = imagekitRawResponse;
	} 

}
