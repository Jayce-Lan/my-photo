package com.photo;

import com.photo.dao.FileManageDAO;
import com.photo.entity.fileManage.FileOutDTO;
import com.photo.entity.fileManage.FileQueryInDTO;
import com.photo.entity.admdvs.AdmdvsInDTO;
import com.photo.entity.admdvs.AdmdvsProvOutDTO;
import com.photo.service.CommonService;
import com.photo.service.FileManageService;
import com.photo.utils.AdmdvsToPTUtil;
import com.photo.utils.DictConst;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@Slf4j
class MyPhotoSvcApplicationTests {
	@Resource
	private StringRedisTemplate stringRedisTemplate;
	@Resource
	private FileManageDAO fileManageDAO;
	@Resource
	private CommonService commonService;
	@Resource
	private FileManageService fileManageService;

	@Value("${myfile.save.path}")
	private String filePath;

	@Test
	void contextLoads() {
		stringRedisTemplate.opsForValue().set("hello", "testttt");
	}

	@Test
	void testFilePath() {
		log.info("filePath, {}", filePath);
	}

	@Test
	void addFileINfo() {
		FileOutDTO fileOutDTO = new FileOutDTO();
		fileOutDTO.setFileId("1");
		fileOutDTO.setUserName("Tony");
		fileOutDTO.setFileDeleteFlag("1");
		int count = fileManageDAO.addFileInfo(fileOutDTO);
		log.info("count: {}", count);
	}

	@Test
	void addTarSet() {
		String[] tar = {"美食", "旅游"};
		stringRedisTemplate.opsForSet().add(DictConst.TAR_SET_PATH, tar);
	}

	@Test
	void testAdmdvsToPt() {
		String admdvsToPT = AdmdvsToPTUtil.admdvsToPT("玉林");
		log.info("admdvsToPT: {}", admdvsToPT);
//		admdvsToPT = AdmdvsToPTUtil.admdvsToPT("陕西");
//		log.info("admdvsToPT: {}", admdvsToPT);
	}

	@Test
	void addAdmdvs() {
		AdmdvsInDTO admdvs = new AdmdvsInDTO();
		admdvs.setAdmdvsProv("海南省");
		admdvs.setAdmdvsName("三亚市");
		commonService.addAdmdvs(admdvs);
	}

	@Test
	void queryAdmdvsTest() {
		List<AdmdvsProvOutDTO> admdvsProvOutDTOS = commonService.queryAdmdvsProvList();
		log.info("admdvsProvOutDTOS : {}", admdvsProvOutDTOS);
	}

	@Test
	void testLastIndex() {
		FileQueryInDTO fileQueryInDTO = new FileQueryInDTO();
		List<FileOutDTO> fileOutDTOS = fileManageService.queryFileOutList(fileQueryInDTO);
		fileOutDTOS.forEach(item -> {
			log.info("item: {}", item);
		});
	}
}
