package com.agripredictor.forumservice.mapper;

import com.agripredictor.forumservice.po.LikeReplyForum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 回复点赞表 Mapper 接口
 * </p>
 *
 * @author yuum
 * @since 2024-08-25
 */
public interface LikeReplyForumMapper extends BaseMapper<LikeReplyForum> {
    void deleteDataInOneAndChildrenByReplyId(long replyId);
}
