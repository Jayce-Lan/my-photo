package com.photo.service;

import com.photo.entity.fileManage.FileOutDTO;
import com.photo.entity.fileManage.FileQueryInDTO;
import com.photo.entity.ReturnMsgData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FileManageService {
    ReturnMsgData uploadFile(MultipartFile multipartFile, Map<String, String> map) throws IOException;
    List<FileOutDTO> queryFileOutList(FileQueryInDTO fileQueryInDTO);
}
