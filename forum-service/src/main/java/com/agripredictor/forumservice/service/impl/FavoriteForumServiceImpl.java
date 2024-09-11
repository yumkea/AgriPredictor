package com.agripredictor.forumservice.service.impl;

import com.agripredictor.common.client.UserClient;
import com.agripredictor.common.po.ForumVO;
import com.agripredictor.common.po.PageVO;
import com.agripredictor.common.po.Result;
import com.agripredictor.common.po.UserVO;
import com.agripredictor.forumservice.mapper.ForumMapper;
import com.agripredictor.forumservice.po.FavoriteForum;
import com.agripredictor.forumservice.mapper.FavoriteForumMapper;
import com.agripredictor.forumservice.po.Forum;
import com.agripredictor.forumservice.service.IFavoriteForumService;
import com.agripredictor.forumservice.service.IForumService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏表 服务实现类
 *
 * @author yuum
 * @since 2024-08-25
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class FavoriteForumServiceImpl extends ServiceImpl<FavoriteForumMapper, FavoriteForum> implements IFavoriteForumService {

    private final FavoriteForumMapper favoriteForumMapper;
    private final ForumMapper forumMapper;
    private final IForumService forumService;
    private final UserClient userClient;

    /**
     * 将帖子 收藏++
     *
     * @param favoriteForum
     */
    @Override
    public ForumVO saveMe(FavoriteForum favoriteForum) {

        favoriteForum.setCreateTime(System.currentTimeMillis());
        log.info("添加收藏 {}", favoriteForum);
        /**
         * 幂等处理
         */
        Long count = favoriteForumMapper.selectCount(
                new LambdaQueryWrapper<FavoriteForum>()
                        .eq(FavoriteForum::getForumId, favoriteForum.getForumId())
                        .eq(FavoriteForum::getCreatorId, favoriteForum.getCreatorId())
        );
        if(count == 0){
            favoriteForumMapper.insert(favoriteForum);
            forumMapper.update(null, new LambdaUpdateWrapper<Forum>()
                    .setSql("favorite_count  = favorite_count + 1")
                    .eq(Forum::getId, favoriteForum.getForumId()));
        }

        return forumService.getForumById(Long.valueOf(favoriteForum.getForumId()), favoriteForum.getCreatorId());
    }

    /**
     * 取消收藏
     *
     * @param favoriteForum 已知 forumId CreatorId
     * @return
     */
    @Override
    public ForumVO deleteMe(FavoriteForum favoriteForum) {
        log.info("取消收藏 {}", favoriteForum);
        if (favoriteForumMapper.delete(new LambdaQueryWrapper<FavoriteForum>()
                .eq(FavoriteForum::getCreatorId, favoriteForum.getCreatorId())
                .eq(FavoriteForum::getForumId, favoriteForum.getForumId())
        ) > 0) {
            forumMapper.update(null, new LambdaUpdateWrapper<Forum>()
                    .setSql("favorite_count  = favorite_count - 1")
                    .eq(Forum::getId, favoriteForum.getForumId()));
        }
        return forumService.getForumById(Long.valueOf(favoriteForum.getForumId()), favoriteForum.getCreatorId());
    }


    /**
     * 查看本人收藏的帖子
     *
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @param title
     * @param summary
     * @param status
     * @param userId
     * @return
     */
    @Override
    public PageVO<ForumVO> pageFavoriteForumsByUser(Long pageNum, Long pageSize, String categoryId, String title, String summary, Integer status, String userId) {

        Result<List<UserVO>> userVOByUserIds = userClient.getUserVOByUserIds(List.of("1", "2"));
        log.error("UserClint测试 {}",userVOByUserIds);

        List<FavoriteForum> favoriteForums = favoriteForumMapper.selectList(
                new LambdaQueryWrapper<FavoriteForum>()
                        .select(FavoriteForum::getForumId)
                        .eq(FavoriteForum::getCreatorId, userId)
        );
        //forumIds
        List<Long> forumIds = new ArrayList<>();
        favoriteForums.forEach(x->{
            forumIds.add(Long.valueOf(x.getForumId()));
        });
        log.info("{} 搜藏的帖子 ids {}",userId,forumIds);
        if(forumIds.isEmpty()) {
            return new PageVO<ForumVO>(0L,0L,0L,null);
        }

        return forumService.pageMe(pageNum,pageSize,categoryId,null,title,summary,status,userId,forumIds);
    }


}
