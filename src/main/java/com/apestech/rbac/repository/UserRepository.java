package com.apestech.rbac.repository;

import com.apestech.rbac.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    User findByUserId(String userId);


//    //常用 查询方法
//    @Query("select name from aut_users where beautyType = :beautyType AND time BETWEEN :startDate AND :endDate")
//    public List<User> findReservations(@Param("startDate")Date startDate, @Param("endDate")Date endDate , @Param("beautyType")String beautyType);
//
//    /**
//     * 更新是否可用标记
//     * @param id
//     * @param usable
//     */
//    @Modifying
//    @Query("update aut_users set usable = :usable where id = :id")
//    public void updateUsableFlag(@Param("id")Long id, @Param("usable")Boolean usable);
//
//    /**
//     * 删除
//     * @param ids
//     */
//    @Modifying
//    @Query("delete from aut_users where id in (:ids)")
//    public void delete(@Param("ids")Long[] ids);

}
