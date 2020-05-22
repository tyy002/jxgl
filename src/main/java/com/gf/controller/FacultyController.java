package com.gf.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.demo.base.action.ProjBaseAction;
import com.example.demo.util.json.JsonTools;
import com.gf.config.SecurityUtils;
import com.gf.entity.Faculty;
import com.gf.entity.Regulations;
import com.gf.entity.Result;
import com.gf.entity.User;
import com.gf.service.FacultyService;
import com.gf.service.RegulationsService;
import com.gf.service.UserService;
import com.tmsps.ne4spring.LayuiPage;
import com.tmsps.ne4spring.orm.model.DataModel;

@Controller
@RequestMapping("/faculty")
public class FacultyController extends ProjBaseAction{
	@Autowired
	FacultyService facultyService;
	@Autowired
	RegulationsService regulationsService;
	@Autowired
	UserService userService;
	//招生院系列表
	@RequestMapping("/enrollmentList")
    public String enrollmentList(Model model) {
    	return "faculty/enrollmentList";
    }
	//就业院系列表
	@RequestMapping("/employmentList")
	public String employmentList(Model model) {
		return "faculty/employmentList";
	}
	//招生导入数据页面
	@RequestMapping("/import_enrollmentDatabase")
	public String import_enrollmentDatabase() {
		return "faculty/import_enrollmentDatabase";
	}
	//就业导入数据页面
	@RequestMapping("/import_employmentDatabase")
	public String import_employmentDatabase() {
		return "faculty/import_employmentDatabase";
	}
	
	  
	@RequestMapping("/showList_employment")
    @ResponseBody
    public String showList_employment(Integer page, Integer limit, String searchText, HttpServletRequest request,
                           HttpServletResponse response) throws  Exception{
		User user = SecurityUtils.getUser();
		String roleName = user.getAuthorities().get(0).getName();
		List<Map<String, Object>> list;
		Integer total = 0;
		if(roleName.equals("ROLE_USER")) {
			 list = facultyService.findFacultyByPrincipal_employment(user.getKid().toString());
			 total = facultyService.findFacultyByPrincipalPageCount(user.getKid().toString());
		}else {
			 list = facultyService.employment_list(page, limit, searchText);
		     total = facultyService.pageCount(searchText);
		}
        Map<String, Object> maps = new HashMap<>();
        maps.put("rows",list);
        maps.put("total",total);
        return JsonTools.toJson(maps);
	
	}
	
	@RequestMapping("/showList_enrollment")
	@ResponseBody
	public String showList_enrollment(Integer page, Integer limit, String searchText, HttpServletRequest request,
			HttpServletResponse response) throws  Exception{
		User user = SecurityUtils.getUser();
		String roleName = user.getAuthorities().get(0).getName();
		List<Map<String, Object>> list;
		Integer total = 0;
		if(roleName.equals("ROLE_USER")) {
			list = facultyService.findFacultyByPrincipal_enrollment(user.getKid().toString());
			total = facultyService.findFacultyByPrincipalPageCount(user.getKid().toString());
		}else {
			list = facultyService.enrollment_list(page, limit, searchText);
			total = facultyService.pageCount(searchText);
		}
		Map<String, Object> maps = new HashMap<>();
		maps.put("rows",list);
		maps.put("total",total);
		return JsonTools.toJson(maps);
		
	}
	
	@RequestMapping("/faculty_addIndex")
	public String faculty_addIndex(Model model) {
		List<Map<String, Object>> findAllUser = userService.findAllUser();
		model.addAttribute("userList", findAllUser);
		return	"/faculty/add";
	}
	
	
	@RequestMapping("/faculty_add")
	@ResponseBody
	public String faculty_add(String schoolName,String facultyName,String principal) {
		List<String> nameList = new ArrayList<String>();
		List<Map<String, Object>> findAll = facultyService.findAll();
		for(Map<String,Object> map:findAll) {
			nameList.add(map.get("facultyName").toString());
		}
		if(nameList.contains(facultyName)) {
			return	JsonTools.toJson(400);
		}
		Faculty faculty = new Faculty();
		faculty.setFacultyName(facultyName);
		faculty.setSchoolName(schoolName);
		faculty.setPrincipal(principal);
		User user = bs.findById(principal, User.class);
		faculty.setPrincipalName(user.getUsername());
		faculty.setStatus(0);
		int saveObj = bs.saveObj(faculty);
		if(saveObj==1) {
			return	JsonTools.toJson(200);
		}
		return  JsonTools.toJson(500);
	}
	
	@RequestMapping("/getFacultyById")
	public String getFacultyById(String kid,Model model) {
		Faculty faculty = bs.findById(kid, Faculty.class);
		List<Map<String, Object>> findAllUser = userService.findAllUser();
		model.addAttribute("userList", findAllUser);
		model.addAttribute("faculty", faculty);
		return "/faculty/update";
	}
	
	@RequestMapping("/getRegulationsById")
	@ResponseBody
	public String getRegulationsById(String kid) {
		Regulations regulations = bs.findById(kid, Regulations.class);
		return JsonTools.toJson(regulations);
	}
	//修改
	@RequestMapping("/faculty_update")
	@ResponseBody
	public String faculty_update(String kid,String schoolName,String facultyName,String principal) {
		List<String> nameList = new ArrayList<String>();
		List<Map<String, Object>> findAll = facultyService.findAll();
		Faculty findById = bs.findById(kid, Faculty.class);
		if(facultyName.equals(findById.getFacultyName())) {
			User user = bs.findById(principal, User.class);
			findById.setPrincipal(principal);
			findById.setPrincipalName(user.getUsername());
			bs.updateObj(findById);
			return JsonTools.toJson(200);
		}
		for(Map<String,Object> map:findAll) {
			nameList.add(map.get("facultyName").toString());
		}
		if(nameList.contains(facultyName)) {
			return	JsonTools.toJson(400);
		}
		User user = bs.findById(principal, User.class);
		findById.setFacultyName(facultyName);
		findById.setSchoolName(schoolName);
		findById.setPrincipal(principal);
		findById.setPrincipalName(user.getUsername());
		int updateObj = bs.updateObj(findById);
		if(updateObj==1) {
			return	JsonTools.toJson(200);
		}
		return	JsonTools.toJson(500);
	}
	//删除
	@RequestMapping("/faculty_del")
	@ResponseBody
	public void faculty_del(String[] kids) {
		for(int i=0;i<kids.length;i++) {
			Faculty findById = bs.findById(kids[i], Faculty.class);
			findById.setStatus(1);
			int updateObj = bs.updateObj(findById);
			if(updateObj==0) {
				continue;
			}
		}
	}
	
	//招生条例列表
	@RequestMapping("/enrollmentRegulationsList")
	public String regulationsList(String kid,Model model) {
		String type="招生";
		Faculty findById = bs.findById(kid, Faculty.class);
		List<Map<String, Object>> findByRegulationsDate = regulationsService.findByRegulationsDate(type);
		String nowDate=findByRegulationsDate.get(0).get("date").toString();
    	int year=Integer.parseInt(nowDate);
    	String nextDate=year-1+"";
    	String nextsDate=year-2+"";
    	String nextNextsDate=year-3+"";
    	String nextNextNextsDate=year-4+"";
    	List<Map<String, Object>> nowDatelist=new ArrayList<Map<String, Object>>();
    	List<Map<String, Object>> nextDatelist=new ArrayList<Map<String, Object>>();
    	List<Map<String, Object>> nextsDatelist=new ArrayList<Map<String, Object>>();
    	List<Map<String, Object>> nextNextsDatelist=new ArrayList<Map<String, Object>>();
    	List<Map<String, Object>> nextNextNextsDatelist=new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> findRegulationListByFacultyId = facultyService.findRegulationListByFacultyId(kid,type);
		for(int i=0;i<findRegulationListByFacultyId.size();i++) {
			if(findRegulationListByFacultyId.get(i).get("date").toString().equals(nowDate)){
				nowDatelist.add(findRegulationListByFacultyId.get(i));
			}else if(findRegulationListByFacultyId.get(i).get("date").toString().equals(nextDate)) {
				nextDatelist.add(findRegulationListByFacultyId.get(i));
			}else if(findRegulationListByFacultyId.get(i).get("date").toString().equals(nextsDate)) {
				nextsDatelist.add(findRegulationListByFacultyId.get(i));
			}else if(findRegulationListByFacultyId.get(i).get("date").toString().equals(nextNextsDate)) {
				nextNextsDatelist.add(findRegulationListByFacultyId.get(i));
			}else if(findRegulationListByFacultyId.get(i).get("date").toString().equals(nextNextNextsDate)) {
				nextNextNextsDatelist.add(findRegulationListByFacultyId.get(i));
			}
		}
		String sql = "select * from result  where faculty_kid=? ";
		Result result = bs.findObj(sql, new Object[] { kid }, Result.class);
		model.addAttribute("result", result);
		model.addAttribute("faculty", findById);
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("nextDate", nextDate);
		model.addAttribute("nextsDate", nextsDate);
		model.addAttribute("nextNextsDate", nextNextsDate);
		model.addAttribute("nextNextNextsDate", nextNextNextsDate);
		model.addAttribute("nowDatelist", nowDatelist);
		model.addAttribute("nextDatelist", nextDatelist);
		model.addAttribute("nextsDatelist", nextsDatelist);
		model.addAttribute("nextNextsDatelist", nextNextsDatelist);
		model.addAttribute("nextNextNextsDatelist", nextNextNextsDatelist);
		return	"/faculty/enrollmentRegulationsList";
	}
	//判断是否有数据招生
	@RequestMapping("/ifenrollmentRegulationsList")
	@ResponseBody
	public String ifenrollmentRegulationsList(String kid,Model model) {
		String type="招生";
		List<Map<String, Object>> findByRegulationsDate = regulationsService.findMaxByRegulationsDate(type);
		if(findByRegulationsDate.get(0).get("date")==null) {
			return	JsonTools.toJson(500);
		}
		return	JsonTools.toJson(200);
	}
	//判断是否有数据就业
	@RequestMapping("/ifemploymentRegulationsList")
	@ResponseBody
	public String ifemploymentRegulationsList(String kid,Model model) {
		String type="就业";
		List<Map<String, Object>> findByRegulationsDate = regulationsService.findMaxByRegulationsDate(type);
		if(findByRegulationsDate.get(0).get("date")==null) {
			return	JsonTools.toJson(500);
		}
		return	JsonTools.toJson(200);
	}
	
	//就业条例列表
	@RequestMapping("/employmentRegulationsList")
	public String employmentRegulationsList(String kid,Model model) {
		String type="就业";
		Faculty findById = bs.findById(kid, Faculty.class);
		List<Map<String, Object>> findByRegulationsDate = regulationsService.findByRegulationsDate(type);
		String nowDate=findByRegulationsDate.get(0).get("date").toString();
    	int year=Integer.parseInt(nowDate);
    	String nextDate=year-1+"";
    	String nextsDate=year-2+"";
    	String nextNextsDate=year-3+"";
    	String nextNextNextsDate=year-4+"";
		List<Map<String, Object>> nowDatelist=new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> nextDatelist=new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> nextsDatelist=new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> nextNextsDatelist=new ArrayList<Map<String, Object>>();
    	List<Map<String, Object>> nextNextNextsDatelist=new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> findRegulationListByFacultyId = facultyService.findRegulationListByFacultyId(kid,type);
		for(int i=0;i<findRegulationListByFacultyId.size();i++) {
			if(findRegulationListByFacultyId.get(i).get("date").toString().equals(nowDate)){
				nowDatelist.add(findRegulationListByFacultyId.get(i));
			}else if(findRegulationListByFacultyId.get(i).get("date").toString().equals(nextDate)) {
				nextDatelist.add(findRegulationListByFacultyId.get(i));
			}else if(findRegulationListByFacultyId.get(i).get("date").toString().equals(nextsDate)) {
				nextsDatelist.add(findRegulationListByFacultyId.get(i));
			}else if(findRegulationListByFacultyId.get(i).get("date").toString().equals(nextNextsDate)) {
				nextNextsDatelist.add(findRegulationListByFacultyId.get(i));
			}else if(findRegulationListByFacultyId.get(i).get("date").toString().equals(nextNextNextsDate)) {
				nextNextNextsDatelist.add(findRegulationListByFacultyId.get(i));
			}
		}
		String sql = "select * from result  where faculty_kid=? ";
		Result result = bs.findObj(sql, new Object[] { kid }, Result.class);
		model.addAttribute("result", result);
		model.addAttribute("faculty", findById);
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("nextDate", nextDate);
		model.addAttribute("nextsDate", nextsDate);
		model.addAttribute("nextNextsDate", nextNextsDate);
		model.addAttribute("nextNextNextsDate", nextNextNextsDate);
		model.addAttribute("nowDatelist", nowDatelist);
		model.addAttribute("nextDatelist", nextDatelist);
		model.addAttribute("nextsDatelist", nextsDatelist);
		model.addAttribute("nextNextsDatelist", nextNextsDatelist);
		model.addAttribute("nextNextNextsDatelist", nextNextNextsDatelist);
		return	"/faculty/employmentRegulationsList";
	}
//	//清空数据
//	@RequestMapping("/clearRegulations")
//	@ResponseBody
//	public String clearRegulations(String kid,Model model) {
//		List<Map<String, Object>> regulationsList = facultyService.findRegulationListByFacultyId(kid);
//		for (int i = 0; i < regulationsList.size(); i++) {
//			Regulations findById = bs.findById(regulationsList.get(i).get("kid").toString(), Regulations.class);
//			findById.setStatus(1);
//			 bs.updateObj(findById);
//		}
//		return	JsonTools.toJson(200);
//	}
//	
	
	//自评
	@RequestMapping("/self_evaluation")
	@ResponseBody
	public String self_evaluation(String kid,Double self_evaluation,Model model) {
		Regulations findById = bs.findById(kid, Regulations.class);
		if(self_evaluation<0) {
			return JsonTools.toJson("400");
		}
		BigDecimal self_evaluationScore = new BigDecimal(self_evaluation);
		BigDecimal score = findById.getScore();
		if(self_evaluationScore.compareTo(score)==1) {
			return JsonTools.toJson("300");
		}
		findById.setSelf_evaluation(self_evaluationScore);
		int updateObj = bs.updateObj(findById);
		if(updateObj==1) {
			return JsonTools.toJson("200");
		}
		return JsonTools.toJson("500");
	}
	
	//专家评分
	@RequestMapping("/expert_score")
	@ResponseBody
	public String expert_score(String kid,Double expert_score,Model model) {
		Regulations findById = bs.findById(kid, Regulations.class);
		if(expert_score<0) {
			return JsonTools.toJson("400");
		}
		BigDecimal self_evaluationScore = new BigDecimal(expert_score);
		BigDecimal score = findById.getScore();
		if(self_evaluationScore.compareTo(score)==1) {
			return JsonTools.toJson("300");
		}
		User user = SecurityUtils.getUser();
		String roleName = user.getAuthorities().get(0).getName();
		if(roleName.equals("ROLE_EXPEROT1")) {
			findById.setExpert_scoreOne(new BigDecimal(expert_score));
		}else if(roleName.equals("ROLE_EXPEROT2")){
			findById.setExpert_scoreTwo(new BigDecimal(expert_score));
		}else if(roleName.equals("ROLE_EXPEROT3")){
			findById.setExpert_scoreThree(new BigDecimal(expert_score));
		}else if(roleName.equals("ROLE_EXPEROT4")){
			findById.setExpert_scoreFour(new BigDecimal(expert_score));
		}else if(roleName.equals("ROLE_EXPEROT5")){
			findById.setExpert_scoreFive(new BigDecimal(expert_score));
		}
		int updateObj = bs.updateObj(findById);
		if(updateObj==1) {
			return JsonTools.toJson("200");
		}
		return JsonTools.toJson("500");
	}
	
	//专家提交评分招生
	@RequestMapping("/enrollment_export_tijiao")
	@ResponseBody
	public String enrollment_export_tijiao(String kid,String year,Model model) {
		String type="招生";
		List<Map<String, Object>> findRegulationListByFacultyId = facultyService.findRegulationListByFacultyIdAndYear(kid,type,year);
		String sql = "select * from result  where faculty_kid=? and type='招生'";
		Result result = bs.findObj(sql, new Object[] { kid }, Result.class);
		User user = SecurityUtils.getUser();
		String roleName = user.getAuthorities().get(0).getName();
		if(roleName.equals("ROLE_EXPEROT1")) {
			for (int i = 0; i < findRegulationListByFacultyId.size(); i++) {
				if(findRegulationListByFacultyId.get(i).get("expert_scoreOne")==null) {
					return JsonTools.toJson("300");
				}
			}
			result.setExpert_statusOne(1);
		}else if(roleName.equals("ROLE_EXPEROT2")){
			for (int i = 0; i < findRegulationListByFacultyId.size(); i++) {
				if(findRegulationListByFacultyId.get(i).get("expert_scoreTwo")==null) {
					return JsonTools.toJson("300");
				}
			}
			result.setExpert_statusTwo(1);
		}else if(roleName.equals("ROLE_EXPEROT3")){
			for (int i = 0; i < findRegulationListByFacultyId.size(); i++) {
				if(findRegulationListByFacultyId.get(i).get("expert_scoreThree")==null) {
					return JsonTools.toJson("300");
				}
			}
			result.setExpert_statusThree(1);
		}else if(roleName.equals("ROLE_EXPEROT4")){
			for (int i = 0; i < findRegulationListByFacultyId.size(); i++) {
				if(findRegulationListByFacultyId.get(i).get("expert_scoreFour")==null) {
					return JsonTools.toJson("300");
				}
			}
			result.setExpert_statusFour(1);
		}else if(roleName.equals("ROLE_EXPEROT5")){
			for (int i = 0; i < findRegulationListByFacultyId.size(); i++) {
				if(findRegulationListByFacultyId.get(i).get("expert_scoreFive")==null) {
					return JsonTools.toJson("300");
				}
			}
			result.setExpert_statusFive(1);
		}
		int updateObj = bs.updateObj(result);
		if(updateObj==1) {
			return JsonTools.toJson("200");
		}
		return JsonTools.toJson("500");
	}
	
	//管理员审核就业
	@RequestMapping("/update_first_trial_employment")
	@ResponseBody
	public String update_first_trial_employment(String[] kids) {
			for(int i=0;i<kids.length;i++) {
				String sql = "select * from result  where faculty_kid=? and type='就业'";
				Result result = bs.findObj(sql, new Object[] { kids[i] }, Result.class);
				result.setFirst_trial(1);
				bs.updateObj(result);
			}
		return JsonTools.toJson("200");
	}
	
	//管理员审核招生
	@RequestMapping("/update_first_trial_enrollment")
	@ResponseBody
	public String update_first_trial_enrollment(String[] kids) {
		for(int i=0;i<kids.length;i++) {
			String sql = "select * from result  where faculty_kid=? and type='招生'";
			Result result = bs.findObj(sql, new Object[] { kids[i] }, Result.class);
			result.setFirst_trial(1);
			bs.updateObj(result);
		}
		return JsonTools.toJson("200");
	}
	
	//专家提交评分就业
	@RequestMapping("/employment_export_tijiao")
	@ResponseBody
	public String employment_export_tijiao(String kid,String year,Model model) {
		String type="就业";
		List<Map<String, Object>> findRegulationListByFacultyId = facultyService.findRegulationListByFacultyIdAndYear(kid,type,year);
		String sql = "select * from result  where faculty_kid=? and type='就业'";
		Result result = bs.findObj(sql, new Object[] { kid }, Result.class);
		User user = SecurityUtils.getUser();
		String roleName = user.getAuthorities().get(0).getName();
		if(roleName.equals("ROLE_EXPEROT1")) {
			for (int i = 0; i < findRegulationListByFacultyId.size(); i++) {
				if(findRegulationListByFacultyId.get(i).get("expert_scoreOne")==null) {
					return JsonTools.toJson("300");
				}
			}
			result.setExpert_statusOne(1);
		}else if(roleName.equals("ROLE_EXPEROT2")){
			for (int i = 0; i < findRegulationListByFacultyId.size(); i++) {
				if(findRegulationListByFacultyId.get(i).get("expert_scoreTwo")==null) {
					return JsonTools.toJson("300");
				}
			}
			result.setExpert_statusTwo(1);
		}else if(roleName.equals("ROLE_EXPEROT3")){
			for (int i = 0; i < findRegulationListByFacultyId.size(); i++) {
				if(findRegulationListByFacultyId.get(i).get("expert_scoreThree")==null) {
					return JsonTools.toJson("300");
				}
			}
			result.setExpert_statusThree(1);
		}else if(roleName.equals("ROLE_EXPEROT4")){
			for (int i = 0; i < findRegulationListByFacultyId.size(); i++) {
				if(findRegulationListByFacultyId.get(i).get("expert_scoreFour")==null) {
					return JsonTools.toJson("300");
				}
			}
			result.setExpert_statusFour(1);
		}else if(roleName.equals("ROLE_EXPEROT5")){
			for (int i = 0; i < findRegulationListByFacultyId.size(); i++) {
				if(findRegulationListByFacultyId.get(i).get("expert_scoreFive")==null) {
					return JsonTools.toJson("300");
				}
			}
			result.setExpert_statusFive(1);
		}
		int updateObj = bs.updateObj(result);
		if(updateObj==1) {
			return JsonTools.toJson("200");
		}
		return JsonTools.toJson("500");
	}
	
	//招生院系列表导出数据
	@RequestMapping("/enrollmentList_export")
    public String enrollmentList_export(Model model) {
    	return "faculty/enrollmentList_export";
    }
	
	//就业院系列表导出数据
	@RequestMapping("/employmentList_export")
	public String employmentList_export(Model model) {
		return "faculty/employmentList_export";
	}
	
	//招生导出数据页面
	@RequestMapping("/export_enrollment")
    public String export_enrollment(Model model) {
    	return "faculty/export_enrollment";
    }
	
	//就业导出数据页面
	@RequestMapping("/export_employment")
	public String export_employment(Model model) {
		return "faculty/export_employment";
	}
	
	//就业总览数据
	@RequestMapping("/showList_employmentExport")
    @ResponseBody
    public LayuiPage<?> showList_employmentExport(String year) throws  Exception{
		String type="就业";
		LayuiPage<Object> LayuiPage = new LayuiPage<Object>();
		List<Map<String, Object>> list =new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> findAll = facultyService.findAll();
		List<Map<String, Object>> findByRegulationsDate = regulationsService.findByRegulationsDate(type);
		if(findByRegulationsDate.size()!=0) {
			String nowDate=findByRegulationsDate.get(0).get("date").toString();
			if(year==null) {
				year=nowDate;
			}
		}
		Faculty faculty;
		Map<String, Object> map;
		DecimalFormat df = new DecimalFormat(".00");
		for (int i = 0; i < findAll.size(); i++) {
			map = new HashMap<String, Object>();
    		faculty = bs.findById(findAll.get(i).get("kid").toString(), Faculty.class);
    		List<Map<String, Object>> regulationList = facultyService.findRegulationListByFacultyIdAndYear(findAll.get(i).get("kid").toString(),type,year);
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
    		 Double avg=(expert_scoreOneCount+expert_scoreTwoCount+expert_scoreThreeCount+expert_scoreFourCount+expert_scoreFiveCount)/5;
    		 map.put("facultyName",faculty.getFacultyName());
    		 map.put("year",year);
    		 map.put("expert_scoreOneCount",String.format("%.2f",expert_scoreOneCount));
 			 map.put("expert_scoreTwoCount",String.format("%.2f",expert_scoreTwoCount));
 			 map.put("expert_scoreThreeCount",String.format("%.2f",expert_scoreThreeCount));
 			 map.put("expert_scoreFourCount",String.format("%.2f",expert_scoreFourCount));
 			 map.put("expert_scoreFiveCount",String.format("%.2f",expert_scoreFiveCount));
    		 map.put("avg",String.format("%.2f",avg));
    		 list.add(map);
		}
		LayuiPage.setCode(0);
		LayuiPage.setData(list);
		LayuiPage.setCount(findAll.size());
        return LayuiPage;
	
	}
	
	//招生总览数据
	@RequestMapping("/showList_enrollmentExport")
	@ResponseBody
	public LayuiPage<?> showList_enrollmentExport(String year) throws  Exception{
		String type="招生";
		LayuiPage<Object> LayuiPage = new LayuiPage<Object>();
		List<Map<String, Object>> list =new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> findAll = facultyService.findAll();
		List<Map<String, Object>> findByRegulationsDate = regulationsService.findByRegulationsDate(type);
		if(findByRegulationsDate.size()!=0) {
			String nowDate=findByRegulationsDate.get(0).get("date").toString();
			if(year==null) {
				year=nowDate;
			}
		}
		Faculty faculty;
		Map<String, Object> map;
		DecimalFormat df = new DecimalFormat(".00");
		for (int i = 0; i < findAll.size(); i++) {
			map = new HashMap<String, Object>();
			faculty = bs.findById(findAll.get(i).get("kid").toString(), Faculty.class);
			List<Map<String, Object>> regulationList = facultyService.findRegulationListByFacultyIdAndYear(findAll.get(i).get("kid").toString(),type,year);
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
			Double avg=(expert_scoreOneCount+expert_scoreTwoCount+expert_scoreThreeCount+expert_scoreFourCount+expert_scoreFiveCount)/5;
			map.put("facultyName",faculty.getFacultyName());
			map.put("year",year);
			map.put("expert_scoreOneCount",String.format("%.2f",expert_scoreOneCount));
			map.put("expert_scoreTwoCount",String.format("%.2f",expert_scoreTwoCount));
			map.put("expert_scoreThreeCount",String.format("%.2f",expert_scoreThreeCount));
			map.put("expert_scoreFourCount",String.format("%.2f",expert_scoreFourCount));
			map.put("expert_scoreFiveCount",String.format("%.2f",expert_scoreFiveCount));
			 map.put("avg",String.format("%.2f",avg));
			list.add(map);
		}
		LayuiPage.setCode(0);
		LayuiPage.setData(list);
		LayuiPage.setCount(findAll.size());
		return LayuiPage;
		
	}
	
	
	

}
