//package com.gf.mapper;
//
//import com.gf.entity.Role;
//import com.gf.entity.User;
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Param;
//import org.apache.ibatis.annotations.Select;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//
//@Repository
//public interface RoleMapper {
//
//  //  @Select( "SELECT A.id,A.name FROM role A LEFT JOIN user_role B ON A.id=B.role_id WHERE B.user_id=${userId}" )
//    List<Role> getRolesByUserId(@Param("userId") Integer integer);
//
//}
