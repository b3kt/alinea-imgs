package id.alinea.imgs.service;

import id.alinea.imgs.vo.multipart.FileItem;
import java.io.IOException;
import java.util.List;

import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.models.results.Result;

public interface IImageKitService {

    Result upload(FileItem file, String filename, String mimeType, List<String> tags) throws IOException;

	ImageKit getImageKitInstance();

	Result delete(String fileId);
    
}
