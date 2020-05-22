package com.gf.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.demo.base.action.ProjBaseAction;
import com.example.demo.util.json.JsonTools;
import com.gf.config.SecurityUtils;
import com.gf.entity.User;
import com.gf.entity.User_Role;
import com.gf.service.UserService;
import com.tmsps.ne4spring.orm.model.DataModel;

@Controller
@RequestMapping("/user")
public class UserController extends ProjBaseAction{
	
	@Autowired
	UserService userService;
	
	
	@RequestMapping("/index")
    public String user_index(Model model) {
		List<Map<String, Object>> findAllRoles = userService.findAllRoles();
		model.addAttribute("roleList", findAllRoles);
        return "user/list";
    }
	
	@RequestMapping("/list")
    @ResponseBody
    public String list(Integer page, Integer limit, String searchText, HttpServletRequest request,
                           HttpServletResponse response) throws  Exception{
        //在service通过条件查询获取指定页的数据的list
        List<Map<String, Object>> list = userService.list(page, limit, searchText);
//        System.out.println(list.size());
        //根据查询条件，获取符合查询条件的数据总量
        Integer total = userService.pageCount(searchText);
//        System.out.println(total);
        //自己封装的数据返回类型，bootstrap-table要求服务器返回的json数据必须包含：totlal，rows两个节点(sidePagination: "server"服务端分页)
        Map<String, Object> maps = new HashMap<>();
        maps.put("rows",list);
        maps.put("total",total);
//        System.out.println(maps.size());
        return JsonTools.toJson(maps);
	
	}
	
	@RequestMapping("/addIndex")
    public String addIndex() {
        return "/user/add";
    }
	
	@RequestMapping("/user_add")
	@ResponseBody
	public String user_add(String username) {
		List<String> nameList = new ArrayList<String>();
		List<Map<String, Object>> findAll = userService.findAll();
		for(Map<String,Object> map:findAll) {
			nameList.add(map.get("username").toString());
		}
		if(nameList.contains(username)) {
			return	JsonTools.toJson(400);
		}
		User user = new User();
		user.setUsername(username);
		user.setPassword(DigestUtils.md5DigestAsHex("123456".toString().getBytes()));
		user.setStatus(0);
		int saveObj = bs.saveObj(user);
		if(saveObj==1) {
			return	JsonTools.toJson(200);
		}
	    return  JsonTools.toJson(500);
	}
	@RequestMapping("/allocation_role")
	public String allocation_role(String kid,String username,String role_id) {
		String rust;
		User_Role userRoles = new User_Role();
		userRoles.setUser_id(kid);
		userRoles.setRole_id(role_id);
		userRoles.setStatus(0);
		int saveObj = bs.saveObj(userRoles);
		if(saveObj==1) {
			return	"redirect:/user/index";
		}
		return  rust="添加失败";
	}
	
	@RequestMapping("/getUserById")
	@ResponseBody
	public String getUserById(String kid) {
		User user = bs.findById(kid, User.class);
		return  JsonTools.toJson(user);
	}
	
	@RequestMapping("/getUserUpdate")
	public String getUserUpdate(String kid,Model model) {
		User user = bs.findById(kid, User.class);
		model.addAttribute("user", user);
		return  "/user/update";
	}
	@RequestMapping("/resetPasswordUserById")
	@ResponseBody
	public String resetPasswordUserById(String kid) {
		String code;
		User user = bs.findById(kid, User.class);
		user.setPassword(DigestUtils.md5DigestAsHex("123456".toString().getBytes()));
		int saveObj = bs.updateObj(user);
		if(saveObj==1) {
			code="200";
		}else {
			code="500";
		}
		return JsonTools.toJson(code);
	}
	
	@RequestMapping("/user_update")
	@ResponseBody
	public String user_update(String kid,String username) {
		List<String> nameList = new ArrayList<String>();
		List<Map<String, Object>> findAll = userService.findAll();
		User findById = bs.findById(kid, User.class);
		if(username.equals(findById.getUsername())) {
			return JsonTools.toJson(200);
		}
		for(Map<String,Object> map:findAll) {
			nameList.add(map.get("username").toString());
		}
		if(nameList.contains(username)) {
			return JsonTools.toJson(400);
		}
		findById.setUsername(username);
		int updateObj = bs.updateObj(findById);
		if(updateObj==1) {
			return JsonTools.toJson(200);
		}
		return JsonTools.toJson(500);
	}
	
	@RequestMapping("/user_del")
	@ResponseBody
	public String user_del(String kid) {
		User findById = bs.findById(kid, User.class);
		findById.setStatus(1);
		int updateObj = bs.updateObj(findById);
		return	JsonTools.toJson(updateObj);
	}
	
	@RequestMapping("/updatePassword")
	public String updatePassword(String oldPassword,String newPassword,Model model) {
		String rust="";
		User user = SecurityUtils.getUser();
		User findById = bs.findById(user.getKid(), User.class);
		String md5DigestAsHex = DigestUtils.md5DigestAsHex(oldPassword.toString().getBytes());
		String password = user.getPassword();
		if(password.equals(md5DigestAsHex)) {
			findById.setPassword(DigestUtils.md5DigestAsHex(newPassword.toString().getBytes()));
			int updateObj = bs.updateObj(findById);
			if(updateObj==1) {
				rust="修改成功！";
			}
		}else {
			rust="原始密码不正确，请重新输入！";
		}
		model.addAttribute("rust", rust);
		return	"/user/password";
	}
	
	

}
