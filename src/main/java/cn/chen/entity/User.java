package cn.chen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")  // 对应数据库表名
public class User {
    @TableId(value = "id", type = IdType.AUTO)  // 主键、自增
    private Long id;

    @TableField("username")
    private String userName;

    private String password;
    @TableField("real_name")

    private String realName;

    private String role;
}
