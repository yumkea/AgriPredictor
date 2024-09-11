package com.agripredictor.forumservice.service;

import com.agripredictor.common.po.ForumVO;
import com.agripredictor.common.po.PageVO;
import com.agripredictor.forumservice.po.Forum;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 论坛帖子表 服务类
 * </p>
 *
 * @author yuum
 * @since 2024-08-25
 */
public interface IForumService extends IService<Forum> {

    ForumVO add(Forum forum);

    ForumVO updateMe(Forum forum);

    ForumVO getForumById(Long id ,String userId);

    PageVO<ForumVO> pageMe(Long pageNum, Long pageSize, String categoryId, String creatorId,String title, String summary,Integer status, String userId, List<Long> ids);
}
