package com.gf.service;


import com.example.demo.base.service.BaseService;
import com.gf.entity.Role;
import com.gf.entity.User;
import com.tmsps.ne4spring.orm.model.DataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MyUserDetailsService extends BaseService implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        //查数据库
        List<Map<String, Object>> listuser = userService.loadUserByUsername(userName);
        User user = bs.findById(listuser.get(0).get("kid"), User.class);
        if (null != user) {
        	List<Role> arrayList = new ArrayList<Role>();
            List<Map<String, Object>> list = roleService.getRolesByUserId(user.getKid());
            for (int i = 0; i < list.size(); i++) {
            	Role role = bs.findById(list.get(i).get("kid"), Role.class);
            	arrayList.add(role);
			}
            user.setAuthorities(arrayList);
        }
        return user;
    }


}
