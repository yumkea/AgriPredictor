package com.agripredictor.forumservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.agripredictor.common.annotation.AutoFill;
import com.agripredictor.common.client.UserClient;
import com.agripredictor.common.enumeration.AutoFillType;
import com.agripredictor.common.po.ForumVO;
import com.agripredictor.common.po.PageVO;
import com.agripredictor.common.po.UserVO;
import com.agripredictor.forumservice.mapper.FavoriteForumMapper;
import com.agripredictor.forumservice.mapper.LikeForumMapper;
import com.agripredictor.forumservice.po.FavoriteForum;
import com.agripredictor.forumservice.po.Forum;
import com.agripredictor.forumservice.mapper.ForumMapper;
import com.agripredictor.forumservice.po.LikeForum;
import com.agripredictor.forumservice.service.IForumService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 论坛帖子表 服务实现类
 *
 * @author yuum
 * @since 2024-08-25
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ForumServiceImpl extends ServiceImpl<ForumMapper, Forum> implements IForumService {

    private final ForumMapper forumMapper;
    private final LikeForumMapper likeForumMapper;
    private final FavoriteForumMapper favoriteForumMapper;
    private final UserClient userClient;

    @Override
    @AutoFill(AutoFillType.CREATE)
    public ForumVO add(Forum forum) {
        forumMapper.insert(forum);
        forum = forumMapper.selectById(forum.getId());
        ForumVO forumVO = BeanUtil.copyProperties(forum, ForumVO.class);
        forumVO.setAlreadyLiked(0);
        forumVO.setAlreadyFavorite(0);
        /**
         * 作者信息
         */
        UserVO userVO = userClient.getUserVOByUserIds(List.of(forum.getCreatorId())).getData().get(0);
        forumVO.setAuthorName(userVO.getName());
        forumVO.setAuthorImgUrl(userVO.getImgUrl());
        return forumVO;
    }

    @Override
    @AutoFill(AutoFillType.UPDATE)
    public ForumVO updateMe(Forum forum) {
        if (forum.getId() == null) {
            return null;
        }
        forumMapper.updateById(forum);
        ForumVO forumVO = BeanUtil.copyProperties(forum, ForumVO.class);
        Long count = likeForumMapper.selectCount(new LambdaQueryWrapper<LikeForum>()
                .eq(LikeForum::getCreatorId, forumVO.getCreatorId())
                .eq(LikeForum::getForumId, forumVO.getId())
        );

        Long countF = favoriteForumMapper.selectCount(new LambdaQueryWrapper<FavoriteForum>()
                .eq(FavoriteForum::getCreatorId, forumVO.getCreatorId())
                .eq(FavoriteForum::getForumId, forumVO.getId())
        );
        forumVO.setAlreadyLiked(count > 0 ? 1 : 0);
        forumVO.setAlreadyFavorite(countF > 0 ? 1 : 0);

        /**
         * 作者信息
         */
        UserVO userVO = userClient.getUserVOByUserIds(List.of(forum.getCreatorId())).getData().get(0);
        forumVO.setAuthorName(userVO.getName());
        forumVO.setAuthorImgUrl(userVO.getImgUrl());

        return forumVO;
    }

    /**
     * 根据id查帖子
     *
     * @param id
     * @return
     */
    @Override
    public ForumVO getForumById(Long id, String userId) {
        Forum forum = forumMapper.selectById(id);
        ForumVO forumVO = BeanUtil.copyProperties(forum, ForumVO.class);
        forumVO.setAlreadyLiked(
                likeForumMapper.selectCount(new LambdaQueryWrapper<LikeForum>()
                        .eq(LikeForum::getForumId, id)
                        .eq(LikeForum::getCreatorId, userId))
                        > 0 ? 1 : 0
        );

        forumVO.setAlreadyFavorite(
                favoriteForumMapper.selectCount(new LambdaQueryWrapper<FavoriteForum>()
                        .eq(FavoriteForum::getForumId, id)
                        .eq(FavoriteForum::getCreatorId, userId))
                        > 0 ? 1 : 0
        );
        /**
         * 作者信息
         */
        UserVO userVO = userClient.getUserVOByUserIds(List.of(forum.getCreatorId())).getData().get(0);
        forumVO.setAuthorName(userVO.getName());
        forumVO.setAuthorImgUrl(userVO.getImgUrl());

        return forumVO;
    }

    @Override
    public PageVO<ForumVO> pageMe(Long pageNum, Long pageSize, String categoryId, String creatorId, String title, String summary, Integer status, String userId, List<Long> ids) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        Page<Forum> pageData = page(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<Forum>()
                        .eq(categoryId != null && !"".equals(categoryId), Forum::getCategoryForumId, categoryId)
                        .eq(creatorId != null && !"".equals(creatorId), Forum::getCreatorId, creatorId)
                        .eq(status != null, Forum::getStatus, status)
                        .like(title != null && !"".equals(title), Forum::getTitle, title)
                        .like(summary != null && !"".equals(summary), Forum::getSummary, summary)
                        .in(ids != null && !ids.isEmpty(), Forum::getId, ids)
                        .orderByDesc(Forum::getPriorityScore)
        );

        //帖子id集合
        List<Long> forumIds = new ArrayList<>();
        //ForumVO集合
        List<ForumVO> forumVO = new ArrayList<>();
        //帖子作者集合
        List<String> creatorIds = new ArrayList<>();
        pageData.getRecords().forEach(data -> {
            forumIds.add(data.getId());
            ForumVO forumVOTemp = BeanUtil.copyProperties(data, ForumVO.class);
            forumVOTemp.setAlreadyFavorite(0);
            forumVOTemp.setAlreadyLiked(0);
            forumVO.add(forumVOTemp);
            creatorIds.add(data.getCreatorId());
        });

        //点赞、收藏信息

        if (userId != null) {
            //          点赞
            List<LikeForum> likeForums = likeForumMapper.selectList(
                    new LambdaQueryWrapper<LikeForum>()
                            .in(LikeForum::getForumId, forumIds)
                            .eq(LikeForum::getCreatorId, userId)
            );
            //转为map, 防止O(n^2)
            Map<Long, Integer> likeMap = new HashMap<Long, Integer>();
            likeForums.forEach(x -> {
                likeMap.put(Long.valueOf(x.getForumId()), 1);
            });


            //          收藏
            List<FavoriteForum> favoriteForums = favoriteForumMapper.selectList(
                    new LambdaQueryWrapper<FavoriteForum>()
                            .in(FavoriteForum::getForumId, forumIds)
                            .eq(FavoriteForum::getCreatorId, userId)
            );
            //转为map, 防止O(n^2)
            Map<Long, Integer> favoriteForumMap = new HashMap<Long, Integer>();
            favoriteForums.forEach(x -> {
                favoriteForumMap.put(Long.valueOf(x.getForumId()), 1);
            });

            //
            if (favoriteForumMap.size() > 0 || likeMap.size() > 0) {
                forumVO.forEach(x -> {
                    if (likeMap.size() > 0 && likeMap.get(x.getId()) != null && likeMap.get(x.getId()) == 1) {
                        x.setAlreadyLiked(1);
                    }

                    if (favoriteForumMap.size() > 0 && favoriteForumMap.get(x.getId()) != null && favoriteForumMap.get(x.getId()) == 1) {
                        x.setAlreadyFavorite(1);
                    }
                });
            }
        }
        /**
         * 作者信息添加
         */
        List<UserVO> userVOList = userClient.getUserVOByUserIds(creatorIds).getData();
        //转为map，防止0(n^2)
        Map<Long, UserVO> userVOMap = new HashMap<>();
        userVOList.forEach(userVO -> {
            userVOMap.put(Long.valueOf(userVO.getId()),userVO);
        });

        forumVO.forEach(x->{
            x.setAuthorImgUrl(userVOMap.get(Long.valueOf(x.getCreatorId())).getImgUrl());
            x.setAuthorName(userVOMap.get(Long.valueOf(x.getCreatorId())).getName());
        });


        return new PageVO<ForumVO>(pageNum, pageSize, pageData.getTotal(), forumVO);
    }
}