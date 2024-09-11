package com.agripredictor.forumservice.service;

import com.agripredictor.common.po.ForumVO;
import com.agripredictor.common.po.PageVO;
import com.agripredictor.forumservice.po.FavoriteForum;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 收藏表 服务类
 * </p>
 *
 * @author yuum
 * @since 2024-08-25
 */
public interface IFavoriteForumService extends IService<FavoriteForum> {

    ForumVO saveMe(FavoriteForum favoriteForum);

    ForumVO deleteMe(FavoriteForum favoriteForum);

    PageVO<ForumVO> pageFavoriteForumsByUser(Long pageNum, Long pageSize, String categoryId, String title, String summary, Integer status, String userId);
}
