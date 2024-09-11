package com.agripredictor.user.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *
 * @author yuum
 * @since 2024-08-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String name;

    private String description;

    private String tags;

    private Integer status;

    private String extension;

    private Long createTime;

    private Long lastUpdatedTime;

    /**
     * 头像地址
     */
    private String imgUrl;

    /**
     * 用户名
     */
    private String username;

    private String password;
}
