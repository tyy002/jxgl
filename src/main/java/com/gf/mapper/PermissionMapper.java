//package com.gf.mapper;
//
//import com.gf.entity.Permisson;
//import com.gf.entity.RolePermisson;
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Select;
//import org.springframework.stereotype.Repository;
//import java.util.List;
//
//
//@Repository
//public interface PermissionMapper {
//
//   // @Select( "SELECT A.NAME AS roleName,C.url FROM role AS A LEFT JOIN role_permission B ON A.id=B.role_id LEFT JOIN permission AS C ON B.permission_id=C.id" )
//    List<RolePermisson> getRolePermissions();
//    
////    @Select("SELECT DISTINCT\n" + 
////    		"				m.*\n" + 
////    		"			FROM\n" + 
////    		"				( SELECT * FROM USER ) u\n" + 
////    		"				INNER JOIN ( SELECT * FROM user_role ) ur ON u.id = ur.user_id AND u.id = #{userId}\n" + 
////    		"				INNER JOIN ( SELECT * FROM role ) r ON r.id = ur.role_id\n" + 
////    		"				INNER JOIN ( SELECT * FROM role_permission ) rm ON rm.role_id = r.id\n" + 
////    		"				INNER JOIN ( SELECT * FROM permission ) m ON rm.permission_id = m.id")
//    List<Permisson> getMenusByUserId(Integer userId);
//
//}
