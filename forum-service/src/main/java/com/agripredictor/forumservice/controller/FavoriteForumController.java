package com.agripredictor.forumservice.controller;


import com.agripredictor.common.constant.UserConstant;
import com.agripredictor.common.po.ForumVO;
import com.agripredictor.common.po.PageVO;
import com.agripredictor.common.po.Result;
import com.agripredictor.forumservice.po.FavoriteForum;

import com.agripredictor.forumservice.service.IFavoriteForumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 帖子收藏类
 * @author yuum
 * @since 2024-08-25
 */
@RestController
@RequestMapping("/forum/favorite")
@Slf4j
@RequiredArgsConstructor
public class FavoriteForumController {

    private final IFavoriteForumService favoriteForumService;

    /**
     * 添加收藏
     * 传递一个参数即可 Requestbody中的 forumId
     * 后端保证幂等性
     * @param favoriteForum
     * @param userId
     * @return 该本帖子数据 (用于展示是否收藏 点赞，点赞数，收藏数)
     */
    @PostMapping("/addFavoriteForum")
    public Result<ForumVO> addFavoriteForum(@RequestBody FavoriteForum favoriteForum, @RequestHeader(UserConstant.USER_ID) String userId) {
        favoriteForum.setCreatorId(userId);
        return Result.success(favoriteForumService.saveMe(favoriteForum));
    }


    /**
     * 取消收藏
     * @param favoriteForum 传递一个参数即可 Requestbody中的 forumId
     * @param userId
     * @return 该本帖子数据 (用于展示是否收藏 点赞)
     */
    @DeleteMapping("/deleteFavoriteForum")
    public Result<ForumVO> deleteFavoriteForum(@RequestBody FavoriteForum favoriteForum, @RequestHeader(UserConstant.USER_ID) String userId) {
        favoriteForum.setCreatorId(userId);
        return Result.success(favoriteForumService.deleteMe(favoriteForum));
    }


    /**
     * 查看本人收藏的帖子
     * 可以条件查询
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @param title
     * @param summary
     * @param status
     * @return
     */
    @GetMapping("/pageFavoriteForumsByUser")
    public Result<PageVO<ForumVO>> pageFavoriteForumsByUser(
            Long pageNum,
            Long pageSize,
            String categoryId,
            String title,
            String summary,
            Integer status,
            @RequestHeader(UserConstant.USER_ID) String userId
    ){
        log.info("查看本人收藏的帖子 pageNum {} , pageSize {} , categoryId {} , title {} ，summary {}，status{}, userId {}"
                , pageNum, pageSize, categoryId, title, summary, status, userId);
        return Result.success(favoriteForumService.pageFavoriteForumsByUser(pageNum,pageSize,categoryId,title,summary,status,userId));
    }


}
