package com.example.javamailsender.repository;
import com.example.javamailsender.model.Request.AppUserRequest;
import com.example.javamailsender.model.entity.AppUser;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AppUserRepository {

    @Results(id = "appUserMapper", value = {
            @Result(property = "userId", column = "user_id"),
            @Result(property = "fullName", column = "full_name"),
            @Result(property = "roleName", column = "role_name")
    })
    @Select("""
                SELECT * FROM app_users
                WHERE email = #{email};
            """)
    AppUser getUserByEmail(String email);

    @Select("""
                INSERT INTO app_users
                VALUES (default, #{request.fullName}, #{request.email}, #{request.password}, #{request.roleName})
                RETURNING *
            """)
    @ResultMap("appUserMapper")
    AppUser register(@Param("request") AppUserRequest request);

    @Select("""
                SELECT * FROM app_users
                WHERE user_id = #{userId}
            """)
    @ResultMap("appUserMapper")
    AppUser getUserById(Long userId);

}