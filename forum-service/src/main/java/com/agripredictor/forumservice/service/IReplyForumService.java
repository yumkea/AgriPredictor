package com.agripredictor.forumservice.service;

import com.agripredictor.common.po.PageVO;
import com.agripredictor.common.po.ReplyForumVO;
import com.agripredictor.forumservice.po.ReplyForum;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author yuum
 * @since 2024-08-25
 */
public interface IReplyForumService extends IService<ReplyForum> {

    Object addMe(ReplyForum replyForum);

    Object deleteMe(ReplyForum replyForum);

    ReplyForumVO getReplyForumVOById(Long id, String userId);

    PageVO<ReplyForumVO> pageMe(Long pageNum, Long pageSize, Long parentId, String forumId ,String userId);
}
