package com.gf.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.demo.base.action.ProjBaseAction;
import com.example.demo.util.json.JsonTools;
import com.example.demo.util.tree.TreeTools;
import com.gf.config.MyInvocationSecurityMetadataSourceService;
import com.gf.entity.Role;
import com.gf.entity.Role_Permission;
import com.gf.service.PermissionService;
import com.gf.service.RoleService;
import com.tmsps.ne4spring.orm.model.DataModel;

@Controller
@RequestMapping("/role")
public class RoleController extends ProjBaseAction{
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	PermissionService permissionService;
	
	@Autowired
	MyInvocationSecurityMetadataSourceService myInvocationSecurityMetadataSourceService;
	
	
	@RequestMapping("/showList")
    @ResponseBody
    public String list(Integer page, Integer limit, String searchText, HttpServletRequest request,
                           HttpServletResponse response) throws  Exception{
        //在service通过条件查询获取指定页的数据的list
        List<Map<String, Object>> list = roleService.list(page, limit, searchText);
//        System.out.println(list.size());
        //根据查询条件，获取符合查询条件的数据总量
        Integer total = roleService.pageCount(searchText);
//        System.out.println(total);
        //自己封装的数据返回类型，bootstrap-table要求服务器返回的json数据必须包含：totlal，rows两个节点(sidePagination: "server"服务端分页)
        Map<String, Object> maps = new HashMap<>();
        maps.put("rows",list);
        maps.put("total",total);
//        System.out.println(maps.size());
        return JsonTools.toJson(maps);
	
	}
	
	@RequestMapping("/role_add")
	public String role_add(Role role) {
		String rust;
		Role role1 = new Role();
		role1.setName(role.getName());
		role1.setExplanation(role.getExplanation());
		role1.setStatus(0);
		int saveObj = bs.saveObj(role1);
		if(saveObj==1) {
			return	"/role/list";
		}
	    return  rust="添加失败";
	}
	
	@RequestMapping("/getUserById")
	@ResponseBody
	public String getUserById(String kid) {
		Role role = bs.findById(kid, Role.class);
		return JsonTools.toJson(role);
	}
	@RequestMapping("/permissionList")
	public String permissionList(String kid,Model model) {
		List<Map<String, Object>> list =  permissionService.getAllpermisson();
		List<Map<String, Object>> findPermissionList = roleService.findPermissionList(kid);
		String st="";
		for(int i=0;i<findPermissionList.size();i++) {
			st+=findPermissionList.get(i).get("name").toString();
		}
//		System.out.println("1111"+findPermissionList);
		List<Map<String, Object>> turnListToTree2 = TreeTools.turnListToTree2(list,false);
		model.addAttribute("permissionAllList", turnListToTree2);
		model.addAttribute("permissionList", st);
		model.addAttribute("roleKid", kid);
		return "/role/permissionList";
	}
	
	@RequestMapping("/role_update")
	public String role_update(Role role) {
		String kid=role.getKid();
		Role findById = bs.findById(kid, Role.class);
		findById.setName(role.getName());
		findById.setExplanation(role.getExplanation());
		int updateObj = bs.updateObj(findById);
		if(updateObj==1) {
			return	"/role/list";
		}
		return	"修改失败";
	}
	
	@RequestMapping("/role_del")
	@ResponseBody
	public String role_del(String kid) {
		Role findById = bs.findById(kid, Role.class);
		findById.setStatus(1);
		int updateObj = bs.updateObj(findById);
		return	JsonTools.toJson(updateObj);
	}
	
	
	@RequestMapping("/role_authorization")
	@ResponseBody
	public String role_authorization(String kids,String rolekid) {
		String rust="200";
		String[] split = kids.split(",");
		List<Map<String, Object>> findAllpermissionByRole = roleService.findAllpermissionByRole(rolekid);
		for(int i=0;i<findAllpermissionByRole.size();i++) {
			 int deleteObjById = bs.deleteObjById(findAllpermissionByRole.get(i).get("kid").toString(), Role_Permission.class);
			 if(deleteObjById==0) {
				 rust="500";
				 break;
			 }
		}
		for(int i=0;i<split.length;i++) {
			Role_Permission role_Permission = new Role_Permission();
			role_Permission.setRole_id(rolekid);
			role_Permission.setPermission_id(split[i]);
			role_Permission.setStatus(0);
			int saveObj = bs.saveObj(role_Permission);
			if(saveObj==0) {
				 rust="500";
				 break;
			}
		}
	    myInvocationSecurityMetadataSourceService.getAllConfigAttributes();//初始化授权
	    return  JsonTools.toJson(rust);
	}
	
}
