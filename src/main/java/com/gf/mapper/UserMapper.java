//package com.gf.mapper;
//
//import com.gf.entity.User;
//
//import java.util.List;
//import org.apache.ibatis.annotations.Param;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface UserMapper {
//
//   // @Select( "select id , username , password from user where username = #{username}" )
//    User loadUserByUsername(@Param("username") String username);
//
//	List<User> list(Integer pageSize, Integer pageNumber,@Param(value="searchText") String searchText);
//
//	int pageCount(@Param(value="searchText") String searchText);
//
//	User getUserById(Integer id);
//
//	int updateUser(Integer id, String username);
//
//}
