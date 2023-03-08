package com.photo.service;

import com.photo.entity.fileManage.FileOutDTO;
import com.photo.entity.fileManage.FileQueryInDTO;
import com.photo.entity.ReturnMsgData;
import com.photo.entity.fileManage.FileUpdateInDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface FileManageService {
    ReturnMsgData uploadFile(MultipartFile multipartFile, Map<String, String> map) throws Exception;
    List<FileOutDTO> queryFileOutList(FileQueryInDTO fileQueryInDTO);
    void updateFileDeleteFlagByFileId(FileUpdateInDTO fileUpdateInDTO) throws Exception;
}
