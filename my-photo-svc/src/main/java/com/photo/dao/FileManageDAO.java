package com.photo.dao;

import com.photo.entity.fileManage.FileOutDTO;
import com.photo.entity.fileManage.FileQueryInDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileManageDAO {
    int addFileInfo(FileOutDTO fileOutDTO);
    List<FileOutDTO> queryFileOutList(FileQueryInDTO fileQueryInDTO);
}
