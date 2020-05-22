package com.gf.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.base.action.ProjBaseAction;
import com.example.demo.util.FileUtil;
import com.example.demo.util.json.JsonTools;
import com.gf.entity.Faculty;
import com.gf.entity.Regulations;
import com.gf.entity.Result;
import com.gf.entity.User;
import com.gf.service.FacultyService;
import com.gf.service.RegulationsService;
import com.gf.service.UserService;
import com.tmsps.ne4spring.orm.model.DataModel;

@Controller
@RequestMapping("/execl")
public class ExeclController extends ProjBaseAction{
	
	@Autowired
	FacultyService facultyService;
	@Autowired
	RegulationsService regulationsService;
	 
	@Value("${upload.path}")
	private String uploadPath;
	
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";
	
    //导出招生统计数据
    @RequestMapping(value = "/export_enrollmentExcelCount")
    @ResponseBody
    public String export_excelCount(String year,HttpServletResponse response) throws Exception{
    	String type="招生";
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("统计表");
        createTitleCount(workbook,sheet);
        //设置日期格式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
    	List<Map<String, Object>> findAll = facultyService.findAll();
    	Faculty faculty;
    	//新增数据行，并且设置单元格数据
        int rowNum=1;
    	for (int i = 0; i < findAll.size(); i++) {
    		faculty = bs.findById(findAll.get(i).get("kid").toString(), Faculty.class);
    		List<Map<String, Object>> regulationList = facultyService.findRegulationListByFacultyIdAndYear(findAll.get(i).get("kid").toString(),type,year);
    		HSSFRow row = sheet.createRow(rowNum);
    		row.createCell(0).setCellValue(i+1+"");
    		row.createCell(1).setCellValue(findAll.get(i).get("facultyName").toString());
    		row.createCell(2).setCellValue(year);
    		Double expert_scoreOneCount = 0.00;
			Double expert_scoreTwoCount = 0.00;
			Double expert_scoreThreeCount = 0.00;
			Double expert_scoreFourCount = 0.00;
			Double expert_scoreFiveCount = 0.00;
    		 for(int j=0;j<regulationList.size();j++){
    			 if(regulationList.get(j).get("expert_scoreOne")!=null) {
    				 expert_scoreOneCount=expert_scoreOneCount+Double.valueOf(regulationList.get(j).get("expert_scoreOne").toString());
    			 }
    			 if(regulationList.get(j).get("expert_scoreTwo")!=null) {
    				 expert_scoreTwoCount=expert_scoreTwoCount+Double.valueOf(regulationList.get(j).get("expert_scoreTwo").toString());
    			 }
    			 if(regulationList.get(j).get("expert_scoreThree")!=null) {
    				 expert_scoreThreeCount=expert_scoreThreeCount+Double.valueOf(regulationList.get(j).get("expert_scoreThree").toString());
    			 }
    			 if(regulationList.get(j).get("expert_scoreFour")!=null) {
    				 expert_scoreFourCount=expert_scoreFourCount+Double.valueOf(regulationList.get(j).get("expert_scoreFour").toString());
    			 }
    			 if(regulationList.get(j).get("expert_scoreFive")!=null) {
    				 expert_scoreFiveCount=expert_scoreFiveCount+Double.valueOf(regulationList.get(j).get("expert_scoreFive").toString());
    			 }
    	        }
			 row.createCell(3).setCellValue(String.format("%.2f",expert_scoreOneCount));
    		 row.createCell(4).setCellValue(String.format("%.2f",expert_scoreTwoCount));
    		 row.createCell(5).setCellValue(String.format("%.2f",expert_scoreThreeCount));
    		 row.createCell(6).setCellValue(String.format("%.2f",expert_scoreFourCount));
    		 row.createCell(7).setCellValue(String.format("%.2f",expert_scoreFiveCount));
    		 Double avg=(expert_scoreOneCount+expert_scoreTwoCount+expert_scoreThreeCount+expert_scoreFourCount+expert_scoreFiveCount)/5;
    		 row.createCell(8).setCellValue(String.format("%.2f",avg));
    		 rowNum++;
    		 
		}
        String fileName = year+"招生总数据.xls";
        //生成excel文件
        buildExcelFile(fileName, workbook);
        //浏览器下载excel
        buildExcelDocument(fileName,workbook,response);
        return "download excel";
    }
    
    //导出就业统计数据
    @RequestMapping(value = "/export_employmentExcelCount")
    @ResponseBody
    public String export_employmentExcelCount(String year,HttpServletResponse response) throws Exception{
    	String type="就业";
    	HSSFWorkbook workbook = new HSSFWorkbook();
    	HSSFSheet sheet = workbook.createSheet("统计表");
    	createTitleCount(workbook,sheet);
    	//设置日期格式
    	HSSFCellStyle style = workbook.createCellStyle();
    	style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
    	List<Map<String, Object>> findAll = facultyService.findAll();
    	Faculty faculty;
    	//新增数据行，并且设置单元格数据
    	int rowNum=1;
    	for (int i = 0; i < findAll.size(); i++) {
    		faculty = bs.findById(findAll.get(i).get("kid").toString(), Faculty.class);
    		List<Map<String, Object>> regulationList = facultyService.findRegulationListByFacultyIdAndYear(findAll.get(i).get("kid").toString(),type,year);
    		HSSFRow row = sheet.createRow(rowNum);
    		row.createCell(0).setCellValue(i+1+"");
    		row.createCell(1).setCellValue(findAll.get(i).get("facultyName").toString());
    		row.createCell(2).setCellValue(year);
    		Double expert_scoreOneCount = 0.00;
			Double expert_scoreTwoCount = 0.00;
			Double expert_scoreThreeCount = 0.00;
			Double expert_scoreFourCount = 0.00;
			Double expert_scoreFiveCount = 0.00;
    		for(int j=0;j<regulationList.size();j++){
    			if(regulationList.get(j).get("expert_scoreOne")!=null) {
    				expert_scoreOneCount=expert_scoreOneCount+Double.valueOf(regulationList.get(j).get("expert_scoreOne").toString());
    			}
    			if(regulationList.get(j).get("expert_scoreTwo")!=null) {
    				expert_scoreTwoCount=expert_scoreTwoCount+Double.valueOf(regulationList.get(j).get("expert_scoreTwo").toString());
    			}
    			if(regulationList.get(j).get("expert_scoreThree")!=null) {
    				expert_scoreThreeCount=expert_scoreThreeCount+Double.valueOf(regulationList.get(j).get("expert_scoreThree").toString());
    			}
				if(regulationList.get(j).get("expert_scoreFour")!=null) {
					expert_scoreFourCount=expert_scoreFourCount+Double.valueOf(regulationList.get(j).get("expert_scoreFour").toString());
				}
				if(regulationList.get(j).get("expert_scoreFive")!=null) {
					expert_scoreFiveCount=expert_scoreFiveCount+Double.valueOf(regulationList.get(j).get("expert_scoreFive").toString());
				}
    		}
    		row.createCell(3).setCellValue(String.format("%.2f",expert_scoreOneCount));
    		row.createCell(4).setCellValue(String.format("%.2f",expert_scoreTwoCount));
    		row.createCell(5).setCellValue(String.format("%.2f",expert_scoreThreeCount));
    		row.createCell(6).setCellValue(String.format("%.2f",expert_scoreFourCount));
    		row.createCell(7).setCellValue(String.format("%.2f",expert_scoreFiveCount));
    		Double avg=(expert_scoreOneCount+expert_scoreTwoCount+expert_scoreThreeCount+expert_scoreFourCount+expert_scoreFiveCount)/5;
    		row.createCell(8).setCellValue(String.format("%.2f",avg));
    		rowNum++;
    		
    	}
    	String fileName = year+"就业总数据.xls";
    	//生成excel文件
    	buildExcelFile(fileName, workbook);
    	//浏览器下载excel
    	buildExcelDocument(fileName,workbook,response);
    	return "download excel";
    }
    
    //表头
    public void createTitleCount(HSSFWorkbook workbook,HSSFSheet sheet){
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(1,22*256);
        sheet.setColumnWidth(2,22*256);
        sheet.setColumnWidth(3,22*256);
        sheet.setColumnWidth(4,22*256);
        sheet.setColumnWidth(5,22*256);
        sheet.setColumnWidth(6,22*256);
        sheet.setColumnWidth(7,22*256);
        sheet.setColumnWidth(8,22*256);

        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("编号");
        cell.setCellStyle(style);


        cell = row.createCell(1);
        cell.setCellValue("学院名称");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("年度");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("专家一总分");
        cell.setCellStyle(style);
        
        cell = row.createCell(4);
        cell.setCellValue("专家二总分");
        cell.setCellStyle(style);
        
        cell = row.createCell(5);
        cell.setCellValue("专家三总分");
        cell.setCellStyle(style);
        
        cell = row.createCell(6);
        cell.setCellValue("专家四总分");
        cell.setCellStyle(style);
        
        cell = row.createCell(7);
        cell.setCellValue("专家五总分");
        cell.setCellStyle(style);
        
        cell = row.createCell(8);
        cell.setCellValue("平均分");
        cell.setCellStyle(style);
        
    }
    
    //导出招生单个数据
    @RequestMapping(value = "/export_excelOne")
    @ResponseBody
    public String export_excelOne(HttpServletResponse response,String kid,String year) throws Exception{
    	String type="招生";
    	HSSFWorkbook workbook = new HSSFWorkbook();
    	HSSFSheet sheet = workbook.createSheet("统计表");
    	createTitle(workbook,sheet);
    	
//    	Date date = new Date();
//    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
//    	String format = simpleDateFormat.format(date);
    	
    	Faculty faculty = bs.findById(kid, Faculty.class);
    	List<Map<String, Object>> regulationList = facultyService.findRegulationListByFacultyIdAndYear(kid,type,year);
    	//设置日期格式
    	HSSFCellStyle style = workbook.createCellStyle();
    	style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
    	
    	//新增数据行，并且设置单元格数据
    	int rowNum=1;
    	for(int i=0;i<regulationList.size();i++){
    		HSSFRow row = sheet.createRow(rowNum);
    		row.createCell(0).setCellValue(i+1+"");
    		row.createCell(1).setCellValue(regulationList.get(i).get("name").toString());
    		row.createCell(2).setCellValue(regulationList.get(i).get("context").toString());
    		row.createCell(3).setCellValue(regulationList.get(i).get("score").toString());
    		row.createCell(4).setCellValue(regulationList.get(i).get("description").toString());
    		row.createCell(5).setCellValue(regulationList.get(i).get("expert_scoreOne")==null ? "" :regulationList.get(i).get("expert_scoreOne").toString());
    		row.createCell(6).setCellValue(regulationList.get(i).get("expert_scoreTwo")==null ? "" :regulationList.get(i).get("expert_scoreTwo").toString());
    		row.createCell(7).setCellValue(regulationList.get(i).get("expert_scoreThree")==null ? "" :regulationList.get(i).get("expert_scoreThree").toString());
    		row.createCell(8).setCellValue(regulationList.get(i).get("expert_scoreFour")==null ? "" :regulationList.get(i).get("expert_scoreFour").toString());
    		row.createCell(9).setCellValue(regulationList.get(i).get("expert_scoreFive")==null ? "" :regulationList.get(i).get("expert_scoreFive").toString());
//            HSSFCell cell = row.createCell(3);
//            cell.setCellValue(regulation.get("createDate")==null ? "" : regulation.get("createDate")+"");
//            cell.setCellStyle(style);
    		rowNum++;
    	}
    	
    	String fileName = year+faculty.getFacultyName()+"招生数据.xls";
    	//生成excel文件
    	buildExcelFile(fileName, workbook);
    	//浏览器下载excel
    	buildExcelDocument(fileName,workbook,response);
    	return "download excel";
    }
    
    //导出就业单个数据
    @RequestMapping(value = "/export_employmentExcelOne")
    @ResponseBody
    public String export_employmentExcelOne(HttpServletResponse response,String kid,String year) throws Exception{
    	String type="就业";
    	HSSFWorkbook workbook = new HSSFWorkbook();
    	HSSFSheet sheet = workbook.createSheet("统计表");
    	createTitle(workbook,sheet);
    	
    	Date date = new Date();
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
    	String format = simpleDateFormat.format(date);
    	
    	Faculty faculty = bs.findById(kid, Faculty.class);
    	List<Map<String, Object>> regulationList = facultyService.findRegulationListByFacultyIdAndYear(kid,type,year);
    	//设置日期格式
    	HSSFCellStyle style = workbook.createCellStyle();
    	style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
    	
    	//新增数据行，并且设置单元格数据
    	int rowNum=1;
    	for(int i=0;i<regulationList.size();i++){
    		HSSFRow row = sheet.createRow(rowNum);
    		row.createCell(0).setCellValue(i+1+"");
    		row.createCell(1).setCellValue(regulationList.get(i).get("name").toString());
    		row.createCell(2).setCellValue(regulationList.get(i).get("context").toString());
    		row.createCell(3).setCellValue(regulationList.get(i).get("score").toString());
    		row.createCell(4).setCellValue(regulationList.get(i).get("description").toString());
    		row.createCell(5).setCellValue(regulationList.get(i).get("expert_scoreOne")==null ? "" :regulationList.get(i).get("expert_scoreOne").toString());
    		row.createCell(6).setCellValue(regulationList.get(i).get("expert_scoreTwo")==null ? "" :regulationList.get(i).get("expert_scoreTwo").toString());
    		row.createCell(7).setCellValue(regulationList.get(i).get("expert_scoreThree")==null ? "" :regulationList.get(i).get("expert_scoreThree").toString());
    		row.createCell(8).setCellValue(regulationList.get(i).get("expert_scoreFour")==null ? "" :regulationList.get(i).get("expert_scoreFour").toString());
    		row.createCell(9).setCellValue(regulationList.get(i).get("expert_scoreFive")==null ? "" :regulationList.get(i).get("expert_scoreFive").toString());
//            HSSFCell cell = row.createCell(3);
//            cell.setCellValue(regulation.get("createDate")==null ? "" : regulation.get("createDate")+"");
//            cell.setCellStyle(style);
    		rowNum++;
    	}
    	
    	String fileName = year+faculty.getFacultyName()+"就业数据.xls";
    	//生成excel文件
    	buildExcelFile(fileName, workbook);
    	//浏览器下载excel
    	buildExcelDocument(fileName,workbook,response);
    	return "download excel";
    }
    //表头
    public void createTitle(HSSFWorkbook workbook,HSSFSheet sheet){
    	HSSFRow row = sheet.createRow(0);
    	//设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
    	sheet.setColumnWidth(1,12*256);
    	sheet.setColumnWidth(3,17*256);
    	
    	//设置为居中加粗
    	HSSFCellStyle style = workbook.createCellStyle();
    	HSSFFont font = workbook.createFont();
    	font.setBold(true);
    	style.setFont(font);
    	
    	HSSFCell cell;
    	cell = row.createCell(0);
    	cell.setCellValue("编号");
    	cell.setCellStyle(style);
    	
    	
    	cell = row.createCell(1);
    	cell.setCellValue("项目名称");
    	cell.setCellStyle(style);
    	
    	cell = row.createCell(2);
    	cell.setCellValue("项目内容");
    	cell.setCellStyle(style);
    	
    	cell = row.createCell(3);
    	cell.setCellValue("分数");
    	cell.setCellStyle(style);
    	
    	cell = row.createCell(4);
    	cell.setCellValue("评分说明");
    	cell.setCellStyle(style);
    	
    	cell = row.createCell(5);
    	cell.setCellValue("专家一评分");
    	cell.setCellStyle(style);
    	
    	cell = row.createCell(6);
    	cell.setCellValue("专家二评分");
    	cell.setCellStyle(style);
    	
    	cell = row.createCell(7);
    	cell.setCellValue("专家三评分");
    	cell.setCellStyle(style);
    	
    	cell = row.createCell(8);
    	cell.setCellValue("专家四评分");
    	cell.setCellStyle(style);
    	
    	cell = row.createCell(9);
    	cell.setCellValue("专家五评分");
    	cell.setCellStyle(style);
    }
    
    //生成excel文件
    public void buildExcelFile(String filename,HSSFWorkbook workbook) throws Exception{
        FileOutputStream fos = new FileOutputStream(filename);
        workbook.write(fos);
        fos.flush();
        fos.close();
    }

    //浏览器下载excel
    public void buildExcelDocument(String filename,HSSFWorkbook workbook,HttpServletResponse response) throws Exception{
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(filename, "utf-8"));
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }
    
    //导入单个数据
    @RequestMapping(value = "/import_execlOne")
    @ResponseBody
    public String import_execlOne(@RequestParam(value="file", required=false) MultipartFile  file,String type,String kid,HttpServletRequest request) throws Exception{
    	String st;
    	String originalFilename = file.getOriginalFilename();
        String suffix = file.getOriginalFilename().substring(originalFilename.lastIndexOf("."));
        String filename = System.currentTimeMillis() + suffix;
        String pathname= FileUtil.getPath()+filename;
        String fullPath = uploadPath + pathname;
        FileUtil.saveFile(file, fullPath);
        List<Regulations> execlToList = execlToList(new File(fullPath),suffix);
        List<Map<String, Object>> findAll = facultyService.findAll();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String format = simpleDateFormat.format(date);
        if(execlToList.size()!=0) {
        		for(int i=0;i<execlToList.size();i++) {
        			execlToList.get(i).setDate(format);
        			execlToList.get(i).setFaculty_id(kid);
        			execlToList.get(i).setType(type);
        			execlToList.get(i).setStatus(0);
        			execlToList.get(i).setNumber_returns(0); 
        			bs.saveObj(execlToList.get(i));
        		}
        }
        st="导入成功！";
        FileUtil.deleteFile(fullPath);
        return JsonTools.toJson(st);
    }
    
    
    //导入所有数据
    @RequestMapping(value = "/import_execl")
    @ResponseBody
    public String import_execl(@RequestParam(value="file", required=false) MultipartFile file,String type,String year,HttpServletRequest request) throws Exception{
    	if(year==null||year.equals("")) {
    		return JsonTools.toJson("导入失败，请选择导入数据年份!");
    	}
    	List<String> dateList = new ArrayList<String>();
     	List<Map<String, Object>> findByRegulationsDate = regulationsService.findByRegulationsDate(type);
     	for(int i=0;i<findByRegulationsDate.size();i++) {
     		dateList.add(findByRegulationsDate.get(i).get("date").toString());
     	}
     	if(dateList.contains(year)) {
     		return JsonTools.toJson("导入失败，该年份已有数据!");
     	}
    	String originalFilename = file.getOriginalFilename();
    	//String webPath=request.getServletContext().getRealPath("");
//		String filePath = "\\static\\upload_files\\excel\\";
    	String suffix = file.getOriginalFilename().substring(originalFilename.lastIndexOf("."));
    	String filename = System.currentTimeMillis() + suffix;
    	String pathname= FileUtil.getPath()+filename;
//        String str = url.replaceAll("\\\\", "/");
    	//       String path = FileUtil.getPath();
//    	File serverFile = new File(uploadPath);
//    	if(!serverFile.exists()) {
//        	serverFile.getParentFile().mkdir();
//       }
    	String fullPath = uploadPath + pathname;
    	FileUtil.saveFile(file, fullPath);
//        File serverFile = new File(str);
//        System.out.println();
//        if(!serverFile.exists()) {
//        	serverFile.getParentFile().mkdir();
//        }
//        System.out.println("file"+file);
//        System.out.println("serverFile"+serverFile);
//       // serverFile.createNewFile();
//        file.transferTo(serverFile);
    	List<Regulations> execlToList = execlToList(new File(fullPath),suffix);
//    	System.out.println("execlToList"+execlToList);
    	List<Map<String, Object>> clearResult = facultyService.clearResult(type); //将结果表清空
    	for (int i = 0; i < clearResult.size(); i++) {
			bs.deleteObjById(clearResult.get(i).get("kid").toString(), Result.class);
		}
    	List<Map<String, Object>> findAll = facultyService.findAll();
    	if(findAll.size()==0) {
    		return JsonTools.toJson("导入失败，请先添加院系数据！");
    	}
    	Regulations regulations;
    	Result result;
    	if(execlToList.size()!=0) {
    		for(int j=0;j<findAll.size();j++) {
    			 List<Map<String, Object>> findRegulationsByFacultyId = facultyService.findRegulationsByFacultyId(findAll.get(j).get("kid").toString(),type,year);
    			if(findRegulationsByFacultyId.size()!=0) {
    				continue;
    			}
    			for(int i=0;i<execlToList.size();i++) {
    				regulations=new Regulations();
    				regulations.setSerial_number(i+1);
    				regulations.setName(execlToList.get(i).getName());
    				regulations.setContext(execlToList.get(i).getContext());
    				regulations.setScore(new BigDecimal(execlToList.get(i).getScore().toString()));
    				regulations.setDescription(execlToList.get(i).getDescription());
    				regulations.setDate(year);
    				regulations.setFaculty_id(findAll.get(j).get("kid").toString());
    				regulations.setType(type);
    				regulations.setNumber_returns(0);
    				regulations.setStatus(0);
    				bs.saveObj(regulations);
    			}
    			result = new Result();//生成每年的结果表
				result.setFaculty_kid(findAll.get(j).get("kid").toString());
				result.setFirst_trial(0);
				result.setExpert_statusOne(0);
				result.setExpert_statusTwo(0);
				result.setExpert_statusThree(0);
				result.setExpert_statusFour(0);
				result.setExpert_statusFive(0);
				result.setType(type);
				result.setStatus(0);
				bs.saveObj(result);
    		}
    	}
    	FileUtil.deleteFile(fullPath);
    	return JsonTools.toJson("导入成功！");
    }
    
    
    
    public List<Regulations> execlToList(File file,String suffix){
    	List<Regulations> list = new ArrayList<Regulations>();
    	Regulations regulations=null;
    	Workbook wb = null;
    	try {
    		FileInputStream fis = new FileInputStream(file);
//			POIFSFileSystem fs = new POIFSFileSystem(fileInputStream);
			if(suffix.equals("xls")) {
				 wb = new HSSFWorkbook(fis);
			}else if(suffix.equals("xlsx")) {
				wb =new XSSFWorkbook(fis);
			}
    		 wb = new HSSFWorkbook(fis);
			Sheet sheet = wb.getSheetAt(0);
			if(sheet!=null) {
				for(int i=1;i<sheet.getLastRowNum()+1;i++) {
					Row row = sheet.getRow(i);
					if(row==null) {
						continue;
					}
					regulations=new Regulations();
					String name = String.valueOf(row.getCell(1).getStringCellValue());
					String content = String.valueOf(row.getCell(2).getStringCellValue());
					String score = String.valueOf(row.getCell(3).getNumericCellValue());
					String description = String.valueOf(row.getCell(4).getStringCellValue());
					regulations.setName(name);
					regulations.setContext(content);
					regulations.setScore(new BigDecimal(score));
					regulations.setDescription(description);
					list.add(regulations);
				}
			}
			
		} catch (FileNotFoundException e) {
			logger.warn("没有文件！");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
    }
    


    public static void checkFile(MultipartFile file) throws IOException{
        //判断文件是否存在
        if(null == file){
            throw new FileNotFoundException("文件不存在！");
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if(!fileName.endsWith(xls) && !fileName.endsWith(xlsx)){
            throw new IOException(fileName + "不是excel文件");
        }
    }

    

}
