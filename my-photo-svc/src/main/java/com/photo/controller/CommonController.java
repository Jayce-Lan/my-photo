package com.photo.controller;

import com.photo.entity.ReturnMsgData;
import com.photo.entity.admdvs.AdmdvsProvOutDTO;
import com.photo.service.CommonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("commonController")
public class CommonController {
    @Resource
    private CommonService commonService;

    @GetMapping("queryAdmdvsList")
    public ReturnMsgData queryAdmdvsList() {
        List<AdmdvsProvOutDTO> admdvsProvOutDTOS = commonService.queryAdmdvsProvList();
        return new ReturnMsgData(admdvsProvOutDTOS);
    }

    @GetMapping("queryAdmdvsOnlyProvList")
    public ReturnMsgData queryAdmdvsOnlyProvList() {
        List<AdmdvsProvOutDTO> admdvsProvOutDTOS = commonService.queryAdmdvsOnlyProvList();
        return new ReturnMsgData(admdvsProvOutDTOS);
    }
}
