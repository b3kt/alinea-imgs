package id.alinea.imgs.service;

import id.alinea.imgs.entity.SystemFile;
import id.alinea.imgs.vo.multipart.FileItem;

public interface ISystemFileService {

    void storeFile(FileItem file, String entityId, String entityName, String entityField, String createdBy) throws Exception; 

    void deleteFileByUuid(String uuid);

    SystemFile findSystemFileByUuid(String uuid);
}
