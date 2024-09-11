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
 * 论坛帖子表
 * </p>
 *
 * @author yuum
 * @since 2024-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("forum")
public class Forum implements Serializable {

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

    private Long createTime;

    private String creatorId;

    private Long lastUpdatedTime;

    private String lastOperatorId;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 类型id
     */
    private String categoryForumId;

    /**
     * 帖子摘要
     */
    private String summary;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Long likeCount;

    /**
     * 回复数
     */
    private Long replyCount;

    /**
     * 收藏数
     */
    private Long favoriteCount;

    /**
     * 展示优先度 0 ~ 1000
     */
    private Integer priorityScore;


}
