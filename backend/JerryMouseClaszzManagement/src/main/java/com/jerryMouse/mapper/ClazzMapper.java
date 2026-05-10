package com.jerryMouse.mapper;

import com.jerryMouse.pojo.ClassQueryParm;
import com.jerryMouse.pojo.Clazz;
import com.jerryMouse.pojo.Student;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ClazzMapper {
    List<Clazz> page(ClassQueryParm classQueryParm);

    List<Student> selectClazzStudents(Integer id);

    @Delete("delete from clazz where id = #{id}")
    void deleteById(Integer id);

    List<Clazz> list();

    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Clazz clazz);

    @Select("select * from clazz where id = #{id}")
    Clazz selectById(Integer id);

    void updateById(Clazz clazz);

    List<Map<String, Object>> getStudentCount();
}
