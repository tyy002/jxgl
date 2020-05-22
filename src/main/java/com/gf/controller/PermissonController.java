package com.gf.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.base.action.ProjBaseAction;
import com.example.demo.util.ChkTools;
import com.example.demo.util.json.JsonTools;
import com.example.demo.util.tree.TreeTools;
import com.gf.config.SecurityUtils;
import com.gf.entity.Permission;
import com.gf.entity.Role;
import com.gf.entity.User;
import com.gf.service.PermissionService;
import com.tmsps.ne4spring.orm.model.DataModel;

@Controller
@RequestMapping(value="/permission")
public class PermissonController extends ProjBaseAction{
	
	@Autowired
	PermissionService permissonService;
	
	@RequestMapping(value = "/list",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String list(HttpServletRequest request) {
	   User user = SecurityUtils.getUser();
	   List<Map<String, Object>> list =  permissonService.getMenusByUserId(user.getKid());
	   
//       List<Map<String, Object>> tree = new ArrayList<Map<String, Object>>();
//		for (int i = 0; i < list.size(); i++) {
//			Map<String, Object> hashMap = new HashMap<String,Object>();
//			hashMap.put("id",list.get(i).getId().toString());
//			hashMap.put("mid",list.get(i).getMid());
//			hashMap.put("icon",list.get(i).getIcon());
//			hashMap.put("name",list.get(i).getName());
//			hashMap.put("type",list.get(i).getType());
//			hashMap.put("code",list.get(i).getCode());
//			hashMap.put("url",list.get(i).getUrl());
//			tree.add(hashMap);
//		}
		return TreeTools.turnListToTree(list);
    }
	
	@RequestMapping("/permission_add")
	public String permission_add(String kid,String name,String url) {
		String rust;
		int saveObj;
		 int ran2 = (int) (Math.random()*900)+100; 
		String endCode=ran2+"";
		if(ChkTools.isNull(kid)) { //父级
			Permission permisson = new Permission();
			permisson.setName(name);
			permisson.setUrl(url);
			permisson.setMid("0");
			permisson.setIcon("");
			permisson.setCode(endCode);
			permisson.setType("1");
			permisson.setStatus(0);
			saveObj=bs.saveObj(permisson);
		}else {//子级
			Permission findById = bs.findById(kid, Permission.class);
			Permission permisson = new Permission();
			permisson.setName(name);
			permisson.setUrl(url);
			permisson.setMid(findById.getCode());
			permisson.setIcon("");
			permisson.setCode(findById.getCode()+endCode);
			permisson.setType("2");
			permisson.setStatus(0);
			saveObj=bs.saveObj(permisson);
		}
		if(saveObj==1) {
			return	"/permission/list";
		}
	    return  rust="添加失败";
	}
	
	@RequestMapping("/getPermissionById")
	@ResponseBody
	public String getPermissionById(String kid) {
		Permission permission = bs.findById(kid, Permission.class);
		return JsonTools.toJson(permission);
	}
	
	@RequestMapping("/permission_update")
	public String permisson_update(Permission permission) {
		String kid=permission.getKid();
		Permission findById = bs.findById(kid, Permission.class);
		findById.setName(permission.getName());
		findById.setUrl(permission.getUrl());
		int updateObj = bs.updateObj(findById);
		if(updateObj==1) {
			return	"/permission/list";
		}
		return	"修改失败";
	}
	
	@RequestMapping("/permission_del")
	@ResponseBody
	public String permission_del(String kid) {
		Permission findById = bs.findById(kid, Permission.class);
		findById.setStatus(1);
		int updateObj = bs.updateObj(findById);
		return	JsonTools.toJson(updateObj);
	}
	@RequestMapping("/permission_showTree")
	@ResponseBody
	public String permission_showTree() {
  	   List<Map<String, Object>> list =  permissonService.getAllpermisson();
  	   List<Map<String, Object>> turnListToTree2 = TreeTools.turnListToTree2(list,false);
		return	JsonTools.toJson(turnListToTree2);
	}
	
	
	
}
