package com.jerryMouse.mapper;

import com.jerryMouse.pojo.Student;
import com.jerryMouse.pojo.StudentQueryParm;
import com.jerryMouse.pojo.StudentStageStat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface StudentMapper {
    List<Student> page(StudentQueryParm studentQueryParm);

    void deleteByIds(List<Integer> ids);

    void insert(Student student);

    @Select("select * from student where id = #{id}")
    Student selectById(Integer id);

    void updateById(Student student);

    @Update("""
            update student
            set violation_count = violation_count + 1,
                violation_score = violation_score + #{score}
            where id = #{id}
            """)
    void updateViolation(Integer id, Integer score);

    List<StudentStageStat> getStudentStageStat();
}
