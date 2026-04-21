package cn.chen.mapper;

import cn.chen.entity.Medicine;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Mapper 接口：用于访问 medicine 表
 * 提供了统计不同药品名称数量的方法
 */
@Mapper
public interface MedicineMapper extends BaseMapper<Medicine> {
    /**
     * 统计 medicine 表中不同药品名称（name）的数量（去重统计）
     * 注：如果需要按 name+specification 去重，请修改 SQL 或新增方法
     * @return 不同药品名称的数量（Integer）
     */
    @Select("SELECT COUNT(DISTINCT name) FROM medicine")
    Integer countDistinctMedicineNames();
}
