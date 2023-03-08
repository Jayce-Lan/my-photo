package com.photo.dao;

import com.photo.entity.fileManage.FileOutDTO;
import com.photo.entity.fileManage.FileQueryInDTO;
import com.photo.entity.fileManage.FileUpdateInDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileManageDAO {
    int addFileInfo(FileOutDTO fileOutDTO);
    List<FileOutDTO> queryFileOutList(FileQueryInDTO fileQueryInDTO);
    int updateFileByFileId(FileUpdateInDTO fileUpdateInDTO);

    /**
     * 此处根据传入值修改
     *  fileDeleteFlag = 0: 将文件放入回收站，并设置30天后自动删除（这里只是设置时间，删除动作由定时任务触发实现）
     *  fileDeleteFlag = 1: 将文件从回收站放回，并删除剩余时间
     * @param fileUpdateInDTO
     * @return
     */
    int updateFileDeleteFlagByFileId(FileUpdateInDTO fileUpdateInDTO);

    /**
     * 物理删除
     * @param fileUpdateInDTO
     * @return
     */
    int deleteFileByFileId(FileUpdateInDTO fileUpdateInDTO);
}
