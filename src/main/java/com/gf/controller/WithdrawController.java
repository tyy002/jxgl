package com.gf.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.druid.support.logging.Log;
import com.example.demo.base.action.ProjBaseAction;
import com.example.demo.util.FileUtil;
import com.example.demo.util.json.JsonTools;
import com.gf.config.SecurityUtils;
import com.gf.entity.Faculty;
import com.gf.entity.Regulations;
import com.gf.entity.UploadFile;
import com.gf.entity.User;
import com.gf.entity.Withdraw;
import com.gf.service.FacultyService;
import com.gf.service.RegulationsService;
import com.gf.service.WithdrawService;

@Controller
@RequestMapping("/withdraw")
public class WithdrawController extends ProjBaseAction{
	
	@Autowired
	WithdrawService withdrawService;
	@Autowired
	FacultyService facultyService;
	
	@Autowired
	RegulationsService regulationsService;
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@RequestMapping("/index")
    public String withdraw_index() {
    	return "withdraw/list";
    }
	  
	  
	@RequestMapping("/showList")
    @ResponseBody
    public String list(Integer page, Integer limit, String searchText, HttpServletRequest request,
                           HttpServletResponse response) throws  Exception{
		User user = SecurityUtils.getUser();
		String roleName = user.getAuthorities().get(0).getName();
		List<Map<String, Object>> list;
		Integer total = 0;
		if(roleName.equals("ROLE_USER")) {
			 list = withdrawService.findWithdrawByPrincipal(page, limit, searchText,user.getKid().toString());
			 total = withdrawService.findWithdrawByPrincipalPageCount(searchText,user.getKid().toString());
		}else {
			list = withdrawService.list(page, limit, searchText);
			total = withdrawService.pageCount(searchText);
		}
        Map<String, Object> maps = new HashMap<>();
        maps.put("rows",list);
        maps.put("total",total);
        return JsonTools.toJson(maps);
	
	}
	
	
	@RequestMapping("/getWithdrawById")
	public String getRegulationsById(String kid,Model model) {
		Withdraw withdraw = bs.findById(kid, Withdraw.class);
		model.addAttribute("withdraw", withdraw);
		return "/withdraw/show";
	}
	
	
	
	@RequestMapping("/withdraw_del")
	@ResponseBody
	public String withdraw_del(String kid) {
		Withdraw findById = bs.findById(kid, Withdraw.class);
		findById.setStatus(1);
		int updateObj = bs.updateObj(findById);
		return	JsonTools.toJson(updateObj);
	}
	

	
	
	@RequestMapping("/file_del")
	@ResponseBody
	public String file_del(String kid) {
		UploadFile findById = bs.findById(kid, UploadFile.class);
		findById.setStatus(1);
		FileUtil.deleteFile(findById.getUrl());
		int updateObj = bs.updateObj(findById);
		return	JsonTools.toJson(updateObj);
	}
	
	@RequestMapping("/withdraw_retreat")
	public String withdraw_retreat(String kid,Model model) {
		Regulations regulations = bs.findById(kid, Regulations.class);
		Faculty faculty = bs.findById(regulations.getFaculty_id().toString(), Faculty.class);
		model.addAttribute("regulations", regulations);
		model.addAttribute("faculty", faculty);
		return	"/withdraw/retreat";
	
	}
	
	@RequestMapping("/withdraw_add")
	@ResponseBody
	public String withdraw_add(String kid,String name,String reason,Model model) {
		Date date = new Date();
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(date);
		Regulations regulations = bs.findById(kid, Regulations.class);
		regulations.setNumber_returns(regulations.getNumber_returns()+1);
		bs.updateObj(regulations);
		Faculty faculty = bs.findById(regulations.getFaculty_id().toString(), Faculty.class);
		Withdraw withdraw = new Withdraw();
		withdraw.setFaculty_name(faculty.getFacultyName());
		withdraw.setRegulation_kid(regulations.getKid());
		withdraw.setRegulation_name(regulations.getName());
		withdraw.setType(regulations.getType());
		withdraw.setUser_kid(faculty.getPrincipal());
		withdraw.setReason(reason);
		withdraw.setDateTime(format);
		withdraw.setDeal_with("未处理");
		withdraw.setStatus(0);
		int saveObj = bs.saveObj(withdraw);
		List<Map<String, Object>> findFilesByRegulationsId = regulationsService.findFilesByRegulationsId(kid);
		for(int i=0;i<findFilesByRegulationsId.size();i++) {
			FileUtil.deleteFile(findFilesByRegulationsId.get(i).get("url").toString());
			bs.deleteObjById(findFilesByRegulationsId.get(i).get("kid").toString(), UploadFile.class);
		}
		if(saveObj==1) {
			return	JsonTools.toJson(200);
		}
		return	JsonTools.toJson(500);
		
	}
	
	
	
}
