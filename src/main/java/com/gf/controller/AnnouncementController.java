package com.gf.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.demo.base.action.ProjBaseAction;
import com.example.demo.util.json.JsonTools;
import com.gf.config.SecurityUtils;
import com.gf.entity.Announcement;
import com.gf.entity.User;
import com.gf.service.AnnouncementService;

@Controller
@RequestMapping("/announcement")
public class AnnouncementController extends ProjBaseAction{
	
	@Autowired
	AnnouncementService announcementService;
	
	
	@RequestMapping("/index")
    public String announcement_index(Model model) {
    	return "announcement/list";
    }
	  
	
	@RequestMapping("/list")
    @ResponseBody
    public String list(Integer page, Integer limit, String searchText, HttpServletRequest request,
                           HttpServletResponse response) throws  Exception{
        //在service通过条件查询获取指定页的数据的list
        List<Map<String, Object>> list = announcementService.list(page, limit, searchText);
//        System.out.println(list.size());
        //根据查询条件，获取符合查询条件的数据总量
        Integer total = announcementService.pageCount(searchText);
//        System.out.println(total);
        //自己封装的数据返回类型，bootstrap-table要求服务器返回的json数据必须包含：totlal，rows两个节点(sidePagination: "server"服务端分页)
        Map<String, Object> maps = new HashMap<>();
        maps.put("rows",list);
        maps.put("total",total);
//        System.out.println(maps.size());
        return JsonTools.toJson(maps);
	
	}
	
	
	@RequestMapping("/announcement_add")
    public String announcement_add() {
    	return "announcement/add";
    }
	
	//发布公告
	@RequestMapping("/announcement_release")
	public String announcement_release(String title,String context,Model model) {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = simpleDateFormat.format(date);
		User user = SecurityUtils.getUser();
    	String name=user.getUsername();
		Announcement announcement = new Announcement();
		announcement.setTitle(title);
		announcement.setContext(context);
		announcement.setDate(format);
		announcement.setUserName(name);
		announcement.setAnnouncement_status(1);
		announcement.setIs_top(0);
		announcement.setStatus(0);
		int saveObj = bs.saveObj(announcement);
		if(saveObj==1) {
			model.addAttribute("result", "发布成功！");
		}else {
			model.addAttribute("result", "发布失败，请重新发布！！");
		}
		return "announcement/add";
	}
	
	//修改公告
	@RequestMapping("/announcement_update")
	@ResponseBody
	public String announcement_update(String kid,String title,String context) {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = simpleDateFormat.format(date);
		Announcement announcement = bs.findById(kid, Announcement.class);
		announcement.setTitle(title);
		announcement.setContext(context);
		announcement.setDate(format);
		int updateObj = bs.updateObj(announcement);
		if(updateObj==1) {
			return JsonTools.toJson("200");
		}
		return JsonTools.toJson("500");
	}
	
	
	
	@RequestMapping("/getAnnouncementById")
	public String getAnnouncementById(String kid,Model model) {
		Announcement announcement = bs.findById(kid, Announcement.class);
		model.addAttribute("announcement", announcement);
		return	"/announcement/update";
	}
	
	@RequestMapping("/announcement_del")
	@ResponseBody
	public String announcement_del(String kid) {
		Announcement findById = bs.findById(kid, Announcement.class);
		findById.setStatus(1);
		int updateObj = bs.updateObj(findById);
		return	JsonTools.toJson(updateObj);
	}
	
	@RequestMapping("/announcement_top")
	@ResponseBody
	public void announcement_top(String kid) {
		Announcement announcement = bs.findById(kid, Announcement.class);
		int is_top = announcement.getIs_top();
		if(is_top==0) {
			announcement.setIs_top(1);
		}else if(is_top==1) {
			announcement.setIs_top(0);
		}
		 bs.updateObj(announcement);
	}
	@RequestMapping("/announcement_switch")
	@ResponseBody
	public void announcement_switch(String kid) {
		Announcement announcement = bs.findById(kid, Announcement.class);
		int announcement_status = announcement.getAnnouncement_status();
		if(announcement_status==0) {
			announcement.setAnnouncement_status(1);
		}else if(announcement_status==1) {
			announcement.setAnnouncement_status(0);
		}
		 bs.updateObj(announcement);
	}

}
