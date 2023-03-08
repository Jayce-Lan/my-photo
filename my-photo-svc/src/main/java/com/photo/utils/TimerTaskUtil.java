package com.photo.utils;

import com.photo.dao.FileManageDAO;
import com.photo.entity.fileManage.FileOutDTO;
import com.photo.entity.fileManage.FileQueryInDTO;
import com.photo.entity.fileManage.FileUpdateInDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * 定时任务的任务工具类
 */

@Component
@EnableAsync
@Slf4j
public class TimerTaskUtil {
    @Resource
    private FileManageDAO fileManageDAO;

    /**
     * 定时任务，每日1点执行
     * 1、文件fileDeleteFlag=0及在回收站中的文件，其删除日期 deleteDay做递减操作
     * 2、当deleteDay=0时的文件，直接做物理删除（库表删除+文件夹删除）
     */
    @Scheduled(cron = "0 0 1 * * ?")
    @Async
    public void deleteFileWhenDeleteDayZero() throws Exception {
        FileQueryInDTO fileQueryInDTO = new FileQueryInDTO();
        fileQueryInDTO.setFileDeleteFlag(DictConst.FILE_DELETE_FLAG_DELETE);
        List<FileOutDTO> fileOutDTOS = fileManageDAO.queryFileOutList(fileQueryInDTO);
        for (FileOutDTO item : fileOutDTOS) {
            Integer deleteDay = item.getDeleteDay();
            deleteDay--;

            FileUpdateInDTO fileUpdateInDTO = new FileUpdateInDTO();
            fileUpdateInDTO.setFileId(item.getFileId());
            fileUpdateInDTO.setFileDeleteFlag(DictConst.FILE_DELETE_FLAG_DELETE);
            fileUpdateInDTO.setDeleteDay(deleteDay);

            if (deleteDay == 0) {
                int count = fileManageDAO.deleteFileByFileId(fileUpdateInDTO);
                if (count != 1) {
                    throw new Exception("删除错误！");
                }
                FileSystemUtils.deleteRecursively(new File(item.getFilePath()));
            } else {
                int updateCount = fileManageDAO.updateFileDeleteFlagByFileId(fileUpdateInDTO);
                if (updateCount != 1) {
                    throw new Exception("修改错误！");
                }
            }
            log.info("执行成功！ + {} + {}", deleteDay, item.getFilePath());
        }
    }
}
