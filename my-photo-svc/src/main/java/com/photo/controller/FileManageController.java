package com.photo.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.photo.entity.admdvs.AdmdvsInDTO;
import com.photo.entity.fileManage.FileOutDTO;
import com.photo.entity.fileManage.FileQueryInDTO;
import com.photo.entity.ReturnMsgData;
import com.photo.entity.fileManage.FileUpdateInDTO;
import com.photo.service.CommonService;
import com.photo.service.FileManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("fileManageController")
@Slf4j
public class FileManageController {
    @Resource
    private FileManageService fileManageService;
    @Resource
    private CommonService commonService;

    @PostMapping("upload")
    @ResponseBody
    @CrossOrigin(originPatterns = "http://localhost:9006") // 解决跨域
    public ReturnMsgData upload(@RequestParam Map<String, String> map, @RequestParam("file")MultipartFile multipartFile) throws Exception {
        return fileManageService.uploadFile(multipartFile, map);
    }

    @PostMapping("queryFileOutList")
    public ReturnMsgData queryFileOutList(FileQueryInDTO fileQueryInDTO) {
        PageInfo<FileOutDTO> fileOutDTOPageInfo = PageHelper.startPage(fileQueryInDTO.getPageNum(), fileQueryInDTO.getPageSize()).doSelectPageInfo(() ->
                fileManageService.queryFileOutList(fileQueryInDTO));
        ReturnMsgData returnMsgData = new ReturnMsgData(fileOutDTOPageInfo);
        return returnMsgData;
    }

    @PostMapping("addAdmdvs")
    public ReturnMsgData addAdmdvs(AdmdvsInDTO admdvs) {
        commonService.addAdmdvs(admdvs);
        return new ReturnMsgData("插入成功！");
    }

    @PostMapping("updateFileDeleteFlagByFileId")
    public ReturnMsgData updateFileDeleteFlagByFileId(FileUpdateInDTO fileUpdateInDTO) {
        ReturnMsgData returnMsgData;
        try {
            fileManageService.updateFileDeleteFlagByFileId(fileUpdateInDTO);
            returnMsgData = new ReturnMsgData("操作成功！");
        } catch (Exception e) {
            returnMsgData = new ReturnMsgData("100", e.getMessage());
        }
        return returnMsgData;
    }
}
