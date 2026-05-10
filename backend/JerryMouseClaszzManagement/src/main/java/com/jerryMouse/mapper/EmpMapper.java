package com.jerryMouse.mapper;

import com.jerryMouse.pojo.Emp;
import com.jerryMouse.pojo.EmpQueryParm;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface EmpMapper {

    List<Emp> list(EmpQueryParm empQueryParm);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("""
            insert into emp(username, name, gender, phone, job, salary, image, entry_date, create_time, update_time)
            values(#{username}, #{name}, #{gender}, #{phone}, #{job}, #{salary}, #{image}, #{entryDate}, #{createTime}, #{updateTime})
            """)
    void insert(Emp emp);

    void deleteByIds(List<Integer> ids);

    void updateById(Emp emp);

    Emp selectById(Integer id);

    List<Map<String, Object>> countEmpJob();

    List<Map<String, Object>> countEmpGender();

    @Select("select * from emp where job = 1")
    List<Emp> selectAll();

    @Select("select id, username, name from emp where username = #{username} and password = #{password}")
    Emp selectByUsernamePassword(Emp emp);

    Emp findById(Integer id);

    void updatePassword(Integer id, String password);
}
