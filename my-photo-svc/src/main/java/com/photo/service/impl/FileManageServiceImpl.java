package com.photo.service.impl;

import com.photo.dao.FileManageDAO;
import com.photo.entity.fileManage.FileOutDTO;
import com.photo.entity.fileManage.FileQueryInDTO;
import com.photo.entity.ReturnMsgData;
import com.photo.entity.admdvs.AdmdvsInDTO;
import com.photo.entity.admdvs.AdmdvsOutDTO;
import com.photo.entity.fileManage.FileUpdateInDTO;
import com.photo.service.CommonService;
import com.photo.service.FileManageService;
import com.photo.utils.ChangeUtil;
import com.photo.utils.DictConst;
import com.photo.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 文件管理的 Sevrice 层
 */

@Service("fileManageService")
@Slf4j
@Transactional
public class FileManageServiceImpl implements FileManageService {
    @Resource
    private FileManageDAO fileManageDAO;
    @Resource
    private CommonService commonService;
    @Resource
    private RedisUtil redisUtil;
    @Value("${myfile.save.path}")
    private String filePath;

    @Override
    public ReturnMsgData uploadFile(MultipartFile multipartFile, Map<String, String> map) throws Exception {
        ReturnMsgData returnMsgData;
        // 判定文件是否存在
        if (multipartFile.isEmpty()) {
            throw new RuntimeException("文件不能为空！");
        }
        // 生成写表的对象
        FileOutDTO fileOutInfo = getFileOutInfo(map, multipartFile);
        log.info("fileOutInfo: {}", fileOutInfo);
        String originalFilename = multipartFile.getOriginalFilename();
        int endIndex = originalFilename.lastIndexOf(".");
        // 重新生成文件名，用FILE_+序列+格式的形式
        String fileSavePath = filePath + DictConst.FILE_INDEX_FIRST
                + redisUtil.nextId(DictConst.INCR_ID_KEY + DictConst.FILE_NAME_KEY)
                + originalFilename.substring(endIndex);
        File dest = new File(fileSavePath);
        fileOutInfo.setFilePath(fileSavePath);
        int count = fileManageDAO.addFileInfo(fileOutInfo);
        if (count != 1) {
            throw new Exception("数据插入失败！");
        }
        // 判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }
        multipartFile.transferTo(dest);
        returnMsgData = new ReturnMsgData("成功上传，请在文件管理或文件展示中查询！");
        return returnMsgData;
    }

    /**
     * 文件查询
     * @param fileQueryInDTO
     * @return
     */
    @Override
    public List<FileOutDTO> queryFileOutList(FileQueryInDTO fileQueryInDTO) {
        // 只查询有效的文件
        fileQueryInDTO.setFileDeleteFlag(DictConst.FILE_DELETE_FLAG_VALID);
        List<String> selectDate = fileQueryInDTO.getSelectDate();
        // 使用 CollectionUtils.isEmpty(list) 对可能为null的List进行判定，相当于 list != null && list.size() > 0
        if (!CollectionUtils.isEmpty(selectDate)) {
            fileQueryInDTO.setStartDate(selectDate.get(0));
            fileQueryInDTO.setEndDate(selectDate.get(1));
        }
        List<FileOutDTO> fileOutDTOS = fileManageDAO.queryFileOutList(fileQueryInDTO);
        fileOutDTOS.forEach(item -> {
            // 如果是图片类型，转成base64
            if (item.getFileType().toLowerCase().startsWith("image")) {
                try {
                    item.setFilePath("data:" + item.getFileType() + ";base64," + ChangeUtil.ImageToBase64Stream(item.getFilePath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                item.setFilePath("");
            }
            item.setFileSize(item.getFileSize().divide(new BigDecimal("1048576"), 2, BigDecimal.ROUND_HALF_UP));
        });
        return fileOutDTOS;
    }

    /**
     * 根据传入的id+删除标识判定将文件放入回收站/还原
     *      fileDeleteFlag = 0: 将文件放入回收站，并设置30天后自动删除（这里只是设置时间，删除动作由定时任务触发实现）
     *      fileDeleteFlag = 1: 将文件从回收站放回，并删除剩余时间
     * @param fileUpdateInDTO
     */
    @Override
    public void updateFileDeleteFlagByFileId(FileUpdateInDTO fileUpdateInDTO) throws Exception {
        if (DictConst.FILE_DELETE_FLAG_DELETE.equals(fileUpdateInDTO.getFileDeleteFlag())) {
            fileUpdateInDTO.setDeleteDay(DictConst.FILE_DELETE_DAY);
        } else {
            fileUpdateInDTO.setDeleteDay(null);
        }
        int count = fileManageDAO.updateFileDeleteFlagByFileId(fileUpdateInDTO);
        if (count != 1) {
            throw new Exception("数据插入失败");
        }
    }

    /**
     * 单个生成可以写表的文件
     * 修改：由于并发产生的问题，不再在此处做添加区划/文件夹的新增处理，防止出现错误
     * @param map 文件的夹带对象
     * @param multipartFile 文件本体
     * @return 返回写表对象
     */
    private FileOutDTO getFileOutInfo(Map<String, String> map, MultipartFile multipartFile) {
        FileOutDTO fileOutDTO = new FileOutDTO();
        // 判定map中的值
        String admdvs = map.get("admdvs");
//        if (StringUtils.hasText(admdvs)) {
            String[] split = admdvs.split(",");
            String admdvsProv = split[0];
            String admdvsName = split[1];
            AdmdvsInDTO admdvsInDTO = new AdmdvsInDTO();
            // 查询省
            admdvsInDTO.setAdmdvsProvPt(admdvsProv);
            AdmdvsOutDTO admdvsOutDTOProv = commonService.queryAdmdvsProvInfo(admdvsInDTO);
            fileOutDTO.setAdmdvsProv(admdvsOutDTOProv.getAdmdvsProv());
            // 查询市
            admdvsInDTO.setAdmdvsProvPt(null);
            admdvsInDTO.setAdmdvsNamePt(admdvsName);
            AdmdvsOutDTO admdvsOutDTOName = commonService.queryAdmdvsProvInfo(admdvsInDTO);
            fileOutDTO.setAdmdvsCity(admdvsOutDTOName.getAdmdvsName());
            fileOutDTO.setAdmdvsPt(admdvsProv + admdvsName);
//        } else {
//            String admdvsProv = map.get("admdvsProv");
//            String admdvsName = map.get("admdvsName");
//            // 组织数据写区划表
//            AdmdvsInDTO admdvsInDTO = new AdmdvsInDTO();
//            admdvsInDTO.setAdmdvsProv(admdvsProv);
//            admdvsInDTO.setAdmdvsName(admdvsName);
//            commonService.addAdmdvs(admdvsInDTO);
//            // 组织数据写入file表
//            fileOutDTO.setAdmdvsProv(admdvsProv);
//            fileOutDTO.setAdmdvsCity(admdvsName);
//            fileOutDTO.setAdmdvsPt(AdmdvsToPTUtil.admdvsToPT(admdvsProv) + AdmdvsToPTUtil.admdvsToPT(admdvsName));
//        }
        // 文件类型
        fileOutDTO.setFileType(multipartFile.getContentType());
        // 文件大小
        fileOutDTO.setFileSize(new BigDecimal(multipartFile.getSize()));
        // 文件名
        fileOutDTO.setFileName(multipartFile.getOriginalFilename());
        // 文件状态
        fileOutDTO.setFileDeleteFlag(DictConst.FILE_DELETE_FLAG_VALID);
        // 获取文件ID
        fileOutDTO.setFileId(redisUtil.nextId(DictConst.INCR_ID_KEY + DictConst.FILE_ID_KEY));
        // 用户名，暂时用系统用户做处理
        fileOutDTO.setUserName("sys");
        return fileOutDTO;
    }
}
