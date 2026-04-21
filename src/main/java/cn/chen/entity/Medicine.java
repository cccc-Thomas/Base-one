package cn.chen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 实体：Medicine（药品）
 * 对应数据库表：medicine
 * 字段说明：
 * - id: 主键，自增
 * - name: 药品名称
 * - specification: 规格（如 0.2g/片、100mg/粒）
 * - stock: 库存数量
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("medicine")
public class Medicine {
    /**
     * 主键ID（自增）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 药品名称
     */
    private String name;

    /**
     * 药品规格
     */
    private String specification;

    /**
     * 当前库存数量
     */
    private Integer stock;

    // 可以根据需要再添加字段，如 code 等
}
