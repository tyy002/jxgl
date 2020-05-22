package com.gf.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.util.tree.TreeTools;
import com.gf.config.SecurityUtils;
import com.gf.entity.User;
import com.gf.service.AnnouncementService;
import com.gf.service.FileUploadService;
import com.gf.service.PermissionService;

@Controller
public class MainController {

	@Autowired
	PermissionService permissonService;
	@Autowired
	FileUploadService fileUploadService;
	@Autowired
	AnnouncementService announcementService;
	
    @RequestMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @RequestMapping("/index")
    public String index() {
        return "index2";
    }
    @RequestMapping("/showindex")
    public String showindex(Model model) {
    	List<Map<String, Object>> findAllAdmin = fileUploadService.findAllAdmin();
    	List<Map<String, Object>> announcementIndex = announcementService.announcementIndex();
    	if(announcementIndex.size()>0){
    		if(announcementIndex.size()==1) {
    			model.addAttribute("announcement1", announcementIndex.get(0));
    		}else if(announcementIndex.size()==2){
    			model.addAttribute("announcement1", announcementIndex.get(0));
    			model.addAttribute("announcement2", announcementIndex.get(1));
    		}else if(announcementIndex.size()==3){
    			model.addAttribute("announcement1", announcementIndex.get(0));
    			model.addAttribute("announcement2", announcementIndex.get(1));
    			model.addAttribute("announcement3", announcementIndex.get(2));
    		}else if(announcementIndex.size()==4){
    			model.addAttribute("announcement1", announcementIndex.get(0));
    			model.addAttribute("announcement2", announcementIndex.get(1));
    			model.addAttribute("announcement3", announcementIndex.get(2));
    			model.addAttribute("announcement4", announcementIndex.get(3));
    		}else if(announcementIndex.size()==5){
    			model.addAttribute("announcement1", announcementIndex.get(0));
    			model.addAttribute("announcement2", announcementIndex.get(1));
    			model.addAttribute("announcement3", announcementIndex.get(2));
    			model.addAttribute("announcement4", announcementIndex.get(3));
    			model.addAttribute("announcement5", announcementIndex.get(4));
    		}
    	}
    	return "index";
    }
    

    @RequestMapping("/login")
    public String login() {
        return "login";
    }
    @RequestMapping("/loginOut")
    public String loginOut() {
    	SecurityUtils.logout();
    	 return "redirect:login";
    }

   
    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute( "loginError"  , true);
        return "login";
    }

    @GetMapping("/401")
    public String accessDenied() {
        return "401";
    }

   
    @GetMapping("/role/index")
    public String role_index() {
    	return "role/list";
    }
  

    @RequestMapping(value="/permission/index")
    public String permission_index() {
   
       return "permission/list";
    }
    @GetMapping("/user/update")
    public String update() {
    	return "user/update";
    }
    @GetMapping("/user/password")
    public String password() {
    	return "user/password";
    }
    


}
