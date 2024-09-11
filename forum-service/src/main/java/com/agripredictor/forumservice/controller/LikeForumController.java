package com.agripredictor.forumservice.controller;


import com.agripredictor.common.constant.UserConstant;
import com.agripredictor.common.po.ForumVO;
import com.agripredictor.common.po.PageVO;
import com.agripredictor.common.po.Result;

import com.agripredictor.forumservice.po.LikeForum;
import com.agripredictor.forumservice.service.ILikeForumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 帖子点赞类
 *
 * @author yuum
 * @since 2024-08-25
 */
@RestController
@RequestMapping("/forum/like")
@Slf4j
@RequiredArgsConstructor
public class LikeForumController {

    private final ILikeForumService likeForumService;

    /**
     * 添加点赞
     * 传递一个参数即可 Requestbody中的 forumId
     * 后端保证幂等性
     * @param likeForum
     * @param userId
     * @return 该本帖子数据 (用于展示是否收藏 点赞，点赞数，收藏数)
     */
    @PostMapping("/addFavoriteForum")
    public Result<ForumVO> addFavoriteForum(@RequestBody LikeForum likeForum, @RequestHeader(UserConstant.USER_ID) String userId) {
        likeForum.setCreatorId(userId);
        return Result.success(likeForumService.saveMe(likeForum));
    }


    /**
     * 取消点赞
     * @param likeForum 传递一个参数即可 Requestbody中的 forumId
     * @param userId
     * @return 该本帖子数据 (用于展示是否收藏 点赞)
     */
    @DeleteMapping("/deleteFavoriteForum")
    public Result<ForumVO> deleteFavoriteForum(@RequestBody LikeForum likeForum, @RequestHeader(UserConstant.USER_ID) String userId) {
        likeForum.setCreatorId(userId);
        return Result.success(likeForumService.deleteMe(likeForum));
    }


    /**
     * 查看本人点赞的帖子
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
        log.info("查看本人点赞的帖子 pageNum {} , pageSize {} , categoryId {} , title {} ，summary {}，status{}, userId {}"
                , pageNum, pageSize, categoryId, title, summary, status, userId);
        return Result.success(likeForumService.pageLikeForumByUser(pageNum,pageSize,categoryId,title,summary,status,userId));
    }

}
