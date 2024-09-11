package com.agripredictor.forumservice.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 收藏表
 * </p>
 *
 * @author yuum
 * @since 2024-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("favorite_forum")
public class FavoriteForum implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String description;

    private String tags;

    /**
     * 状态
     */
    private Integer status;

    private String extension;

    /**
     * 收藏时间
     */
    private Long createTime;

    /**
     * 收藏人id
     */
    private String creatorId;

    /**
     * 帖子id
     */
    private String forumId;


}
