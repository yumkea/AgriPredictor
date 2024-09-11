package com.agripredictor.forumservice.service;

import com.agripredictor.common.po.ForumVO;
import com.agripredictor.common.po.PageVO;
import com.agripredictor.forumservice.po.FavoriteForum;
import com.agripredictor.forumservice.po.LikeForum;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 点赞表 服务类
 * </p>
 *
 * @author yuum
 * @since 2024-08-25
 */
public interface ILikeForumService extends IService<LikeForum> {

    ForumVO saveMe(LikeForum likeForum);

    ForumVO deleteMe(LikeForum likeForum);

    PageVO<ForumVO> pageLikeForumByUser(Long pageNum, Long pageSize, String categoryId, String title, String summary, Integer status, String userId);
}
