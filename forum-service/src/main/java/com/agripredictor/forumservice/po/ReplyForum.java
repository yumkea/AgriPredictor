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
 * 评论表
 * </p>
 *
 * @author yuum
 * @since 2024-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("reply_forum")
public class ReplyForum implements Serializable {

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

    @TableField("create_time")
    private Long createTime;

    @TableField("creator_id")
    private String creatorId;

    @TableField("last_updated_time")
    private Long lastUpdatedTime;

    /**
     * 帖子id
     */
    @TableField("forum_id")
    private String forumId;

    /**
     * 回复内容
     */
    @TableField("content")
    private String content;

    /**
     * 回复数
     */
    @TableField("reply_count")
    private Long replyCount;

    /**
     * 点赞数
     */
    @TableField("like_count")
    private Long likeCount;

    /**
     * 评论的评论父id
     */
    @TableField("parent_id")
    private Long parentId;


}
