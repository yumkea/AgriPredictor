package com.agripredictor.forumservice.service;

import com.agripredictor.common.po.ReplyForumVO;
import com.agripredictor.forumservice.po.LikeReplyForum;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 回复点赞表 服务类
 * </p>
 *
 * @author yuum
 * @since 2024-08-25
 */
public interface ILikeReplyForumService extends IService<LikeReplyForum> {

    ReplyForumVO saveMe(LikeReplyForum likeReplyForum);

    ReplyForumVO deleteMe(LikeReplyForum likeReplyForum);
}
