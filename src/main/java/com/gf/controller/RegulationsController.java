package com.gf.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.support.logging.Log;
import com.example.demo.base.action.ProjBaseAction;
import com.example.demo.util.FileUtil;
import com.example.demo.util.json.JsonTools;
import com.gf.config.SecurityUtils;
import com.gf.entity.Faculty;
import com.gf.entity.Regulations;
import com.gf.entity.Result;
import com.gf.entity.UploadFile;
import com.gf.entity.User;
import com.gf.service.RegulationsService;
import com.tmsps.ne4spring.orm.model.DataModel;

@Controller
@RequestMapping("/regulations")
public class RegulationsController extends ProjBaseAction{
	
	
	private static final Logger log = LoggerFactory.getLogger(RegulationsController.class);
	
	@Autowired
	RegulationsService regulationsService;
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@RequestMapping("/index")
    public String faculty_index(Model model) {
		
    	return "regulations/list";
    }
	  
	  
	@RequestMapping("/showList")
    @ResponseBody
    public String list(Integer page, Integer limit, String searchText, HttpServletRequest request,
                           HttpServletResponse response) throws  Exception{
        //在service通过条件查询获取指定页的数据的list
        List<Map<String, Object>> list = regulationsService.list(page, limit, searchText);
//        System.out.println(list.size());
        //根据查询条件，获取符合查询条件的数据总量
        Integer total = regulationsService.pageCount(searchText);
//        System.out.println(total);
        //自己封装的数据返回类型，bootstrap-table要求服务器返回的json数据必须包含：totlal，rows两个节点(sidePagination: "server"服务端分页)
        Map<String, Object> maps = new HashMap<>();
        maps.put("rows",list);
        maps.put("total",total);
//        System.out.println(maps.size());
        return JsonTools.toJson(maps);
	
	}
	
	@RequestMapping("regulations_add")
	public String regulations_add(Regulations regulations) {
		System.out.println(regulations);
		String rust;
		regulations.setStatus(0);
		int saveObj = bs.saveObj(regulations);
		if(saveObj==1) {
			return	"/regulations/list";
		}
	    return  rust="添加失败";
	}
	
	@RequestMapping("/getRegulationsById")
	public String getRegulationsById(String kid,Model model) {
		Regulations regulations = bs.findById(kid, Regulations.class);
		model.addAttribute("regulations", regulations);
		return "/regulations/update";
	}
	
	//自评
	@RequestMapping("/self_evaluation")
	public String self_evaluation(String kid,Model model) {
		Regulations regulations = bs.findById(kid, Regulations.class);
		model.addAttribute("regulations", regulations);
		return "/regulations/self_evaluation";
	}
	//编辑自评
	@RequestMapping("/updateSelf_evaluation")
	public String updateSelf_evaluation(String kid,Model model) {
		Regulations regulations = bs.findById(kid, Regulations.class);
		model.addAttribute("regulations", regulations);
		return "/regulations/updateSelf_evaluation";
	}
	//专家评分
	@RequestMapping("/expert_score")
	public String expert_score(String kid,Model model) {
		Regulations regulations = bs.findById(kid, Regulations.class);
		model.addAttribute("regulations", regulations);
		return "/regulations/expert_score";
	}
	//专家编辑评分
	@RequestMapping("/updateExpert_score")
	public String updateExpert_score(String kid,Model model) {
		Regulations regulations = bs.findById(kid, Regulations.class);
		User user = SecurityUtils.getUser();
		String roleName = user.getAuthorities().get(0).getName();
		if(roleName.equals("ROLE_EXPEROT1")) {
			model.addAttribute("expert_score", regulations.getExpert_scoreOne());
		}else if(roleName.equals("ROLE_EXPEROT2")){
			model.addAttribute("expert_score", regulations.getExpert_scoreTwo());
		}else if(roleName.equals("ROLE_EXPEROT3")){
			model.addAttribute("expert_score", regulations.getExpert_scoreThree());
		}else if(roleName.equals("ROLE_EXPEROT4")){
			model.addAttribute("expert_score", regulations.getExpert_scoreFour());
		}else if(roleName.equals("ROLE_EXPEROT5")){
			model.addAttribute("expert_score", regulations.getExpert_scoreFive());
		}
		model.addAttribute("regulations", regulations);
		return "/regulations/updateExpert_score";
	}
	
	@RequestMapping("/regulations_update")
	public String regulations_update(Regulations regulations) {
		String kid=regulations.getKid();
		Regulations findById = bs.findById(kid, Regulations.class);
	
		int updateObj = bs.updateObj(findById);
		if(updateObj==1) {
			return	"/regulations/list";
		}
		return	"修改失败";
	}
	
	@RequestMapping("/regulations_del")
	@ResponseBody
	public String regulations_del(String kid) {
		Regulations findById = bs.findById(kid, Regulations.class);
		findById.setStatus(1);
		int updateObj = bs.updateObj(findById);
		return	JsonTools.toJson(updateObj);
	}
	
	
	@RequestMapping("/upload_index")
    public String upload_index(String kid,Model model) {
		List<Map<String, Object>> findFilesByRegulationsId = regulationsService.findFilesByRegulationsId(kid);
		Regulations regulations = bs.findById(kid, Regulations.class);
		String sql = "select * from result  where faculty_kid=? ";
		Result result = bs.findObj(sql, new Object[] { regulations.getFaculty_id() }, Result.class);
		model.addAttribute("result", result);
		model.addAttribute("fileList", findFilesByRegulationsId);
    	return "regulations/uploadIndex";
    }
	@RequestMapping("/file_online")
	public String file_online(String kid,Model model) {
		UploadFile findById = bs.findById(kid, UploadFile.class);
		model.addAttribute("file", findById);
		return "regulations/online";
	}
	
	

	@RequestMapping(value="/upload",method = RequestMethod.POST)
	@ResponseBody
	public String fileUpload(@RequestParam(value="file", required=false) MultipartFile file,String kid) throws Exception {
		Regulations regulations = bs.findById(kid, Regulations.class);
		Faculty faculty = bs.findById(regulations.getFaculty_id(), Faculty.class);
		String originalFilename = file.getOriginalFilename();
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String year = regulations.getDate();//年度
		String wenjian = simpleDateFormat.format(date);//wenjian
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format2 = simpleDateFormat2.format(date);//时间
		String uploadDir = uploadPath+"/"+wenjian+"/";
        //如果目录不存在，自动创建文件夹
        File dir = new File(uploadDir);
        if(!dir.exists()) {
        	dir.mkdir();
        }
        String suffix = file.getOriginalFilename().substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID() + suffix;
        File serverFile = new File(uploadDir + filename);
        if(!serverFile.exists()) {
        	serverFile.getParentFile().mkdir();
        }
        serverFile.createNewFile();
        file.transferTo(serverFile);
    	User user = SecurityUtils.getUser();
    	String name=user.getUsername();
        UploadFile uploadFile = new UploadFile();
        uploadFile.setName(filename);
        uploadFile.setFirstName(originalFilename);
        uploadFile.setEndName(faculty.getFacultyName()+"_"+regulations.getType()+"_"+regulations.getSerial_number()+"_"+originalFilename);
        uploadFile.setUrl(uploadDir+filename);
        uploadFile.setSize(file.getSize()+"");
        uploadFile.setType(suffix);
        uploadFile.setYear(year);
        uploadFile.setDate(format2);
        uploadFile.setUserName(name);
        uploadFile.setRegulations_id(kid);
        uploadFile.setStatus(0);
		bs.saveObj(uploadFile);
		return	JsonTools.toJson("ok");
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
	
	@RequestMapping("/download")
    public String downLoad(HttpServletResponse response,String kid) throws UnsupportedEncodingException {
		UploadFile uploadFile = bs.findById(kid, UploadFile.class);
		String url = uploadFile.getUrl();
		String filename = uploadFile.getName();
		String firstfilename = uploadFile.getFirstName();
		String endName = uploadFile.getEndName();
        File file = new File(url);
        if(file.exists()){ //判断文件父目录是否存在
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
           // response.setContentType("application/force-download");
            if(endName==null) {
            	response.setHeader("Content-Disposition", "attachment;fileName=" +   java.net.URLEncoder.encode(firstfilename,"UTF-8"));
            }else {
            	response.setHeader("Content-Disposition", "attachment;fileName=" +   java.net.URLEncoder.encode(endName,"UTF-8"));
            }
            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;

            OutputStream os = null; //输出流
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while(i != -1){
                    os.write(buffer);
                    i = bis.read(buffer);
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            log.warn("----------file download---" + filename);
            try {
                bis.close();
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }
	
	
	
	
}
