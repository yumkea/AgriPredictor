package com.agripredictor.forumservice.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 回复点赞表
 * </p>
 *
 * @author yuum
 * @since 2024-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("like_reply_forum")
public class LikeReplyForum implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    @TableField("tags")
    private String tags;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;

    @TableField("extension")
    private String extension;

    /**
     * 点赞时间
     */
    @TableField("create_time")
    private Long createTime;

    /**
     * 点赞人id
     */
    @TableField("creator_id")
    private String creatorId;

    /**
     * 评论id
     */
    @TableField("reply_forum_id")
    private String replyForumId;


}
