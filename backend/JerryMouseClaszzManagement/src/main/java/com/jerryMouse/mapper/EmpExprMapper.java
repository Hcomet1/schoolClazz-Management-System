package com.jerryMouse.mapper;

import com.jerryMouse.pojo.EmpExpr;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmpExprMapper {
    //鎻掑叆鏈夋椂澶氭潯鏈夋椂鏃狅紝鍔ㄦ€丼QL锛岀敤Mapper
    void insertBatch(List<EmpExpr> exprList);

    void deleteByEmpIds(List<Integer> ids);

}
