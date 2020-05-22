package com.gf.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.base.action.ProjBaseAction;
import com.example.demo.util.FileUtil;
import com.example.demo.util.json.JsonTools;
import com.gf.config.SecurityUtils;
import com.gf.entity.UploadFile;
import com.gf.entity.User;
import com.gf.service.FileUploadService;
import com.gf.service.UserService;
import com.tmsps.ne4spring.orm.model.DataModel;


@Controller
@RequestMapping(value="/file")
public class FileuploadController extends ProjBaseAction{
	
	private static final Logger log = LoggerFactory.getLogger(FileuploadController.class);

	@Autowired
	FileUploadService fileUploadService;
	
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@RequestMapping("/index")
    public String file_index(Model model) {
    	return "file/list";
    }
	@RequestMapping("/downlIndex")
	public String downlList(Model model) {
		return "file/downList";
	}
	
	@RequestMapping("/upload")
	public String upload_index(Model model) {
		return "file/uploadIndex";
	}
	
	@RequestMapping(value="/upload",method = RequestMethod.POST)
	@ResponseBody
	public String fileUpload(@RequestParam(value="file", required=false) MultipartFile file) throws Exception {
		String originalFilename = file.getOriginalFilename();
		
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy");
		String year = simpleDateFormat1.format(date);//年度
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
        uploadFile.setEndName(originalFilename);
        uploadFile.setUrl(uploadDir+filename);
        uploadFile.setSize(file.getSize()+"");
        uploadFile.setType(suffix);
        uploadFile.setYear(year);
        uploadFile.setDate(format2);
        uploadFile.setUserName(name);
        uploadFile.setStatus(0);
		bs.saveObj(uploadFile);
		return	JsonTools.toJson("ok");
	}
	
	
	@RequestMapping("/downList")
    @ResponseBody
    public String downlList(Integer page, Integer limit, String searchText, HttpServletRequest request,
                           HttpServletResponse response) throws  Exception{
        //在service通过条件查询获取指定页的数据的list
        List<Map<String, Object>> list = fileUploadService.downlList(page, limit, searchText);
//        System.out.println(list.size());
        //根据查询条件，获取符合查询条件的数据总量
        Integer total = fileUploadService.pageCountDownlList(searchText);
//        System.out.println(total);
        //自己封装的数据返回类型，bootstrap-table要求服务器返回的json数据必须包含：totlal，rows两个节点(sidePagination: "server"服务端分页)
        Map<String, Object> maps = new HashMap<>();
        maps.put("rows",list);
        maps.put("total",total);
//        System.out.println(maps.size());
        return JsonTools.toJson(maps);
	
	}
	
	@RequestMapping("/list")
	@ResponseBody
	public String list(Integer page, Integer limit, String searchText, HttpServletRequest request,
			HttpServletResponse response) throws  Exception{
		//在service通过条件查询获取指定页的数据的list
		List<Map<String, Object>> list = fileUploadService.list(page, limit, searchText);
//        System.out.println(list.size());
		//根据查询条件，获取符合查询条件的数据总量
		Integer total = fileUploadService.pageCount(searchText);
//        System.out.println(total);
		//自己封装的数据返回类型，bootstrap-table要求服务器返回的json数据必须包含：totlal，rows两个节点(sidePagination: "server"服务端分页)
		Map<String, Object> maps = new HashMap<>();
		maps.put("rows",list);
		maps.put("total",total);
//        System.out.println(maps.size());
		return JsonTools.toJson(maps);
		
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
            log.warn("----------file download---" + firstfilename);
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
	
	
	@RequestMapping("/file_del")
	@ResponseBody
	public String file_del(String kid) {
		UploadFile findById = bs.findById(kid, UploadFile.class);
		findById.setStatus(1);
		FileUtil.deleteFile(findById.getUrl());
		int updateObj = bs.updateObj(findById);
		return	JsonTools.toJson(updateObj);
	}
	 
}
