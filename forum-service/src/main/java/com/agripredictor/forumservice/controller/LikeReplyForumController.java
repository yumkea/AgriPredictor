package com.agripredictor.forumservice.controller;


import com.agripredictor.common.constant.UserConstant;
import com.agripredictor.common.po.ReplyForumVO;
import com.agripredictor.common.po.Result;
import com.agripredictor.forumservice.po.LikeReplyForum;
import com.agripredictor.forumservice.service.ILikeReplyForumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 回复点赞表
 * @author yuum
 * @since 2024-08-25
 */
@RestController
@RequestMapping("/forum/likeReply")
@Slf4j
@RequiredArgsConstructor
public class LikeReplyForumController {

    private final ILikeReplyForumService likeReplyForumService;


    /**
     * 回复点赞添加
     * 保证幂等性
     * @param likeReplyForum 只传递 replyForumId 即可
     * @return 返回被点赞的评论数据 用于 展示是否点赞、点赞数更新
     */
    @PostMapping("/addLike")
    public Result<ReplyForumVO> addLike(@RequestBody LikeReplyForum likeReplyForum, @RequestHeader(UserConstant.USER_ID) String userId) {
        likeReplyForum.setCreatorId(userId);
        ReplyForumVO res = likeReplyForumService.saveMe(likeReplyForum);
        return res != null ? Result.success(res) : Result.error("like add failed");
    }

    /**
     * 取消点赞
     * @param likeReplyForum 只传递 id 即可
     * @return 返回被删除点赞的评论数据 用于 展示是否点赞、点赞数更新
     */
    @DeleteMapping("/deleteLike")
    public Result<ReplyForumVO> deleteLike(@RequestBody LikeReplyForum likeReplyForum, @RequestHeader(UserConstant.USER_ID) String userId) {
        likeReplyForum.setCreatorId(userId);
        log.info("取消点赞 {}", likeReplyForum);
        ReplyForumVO res = likeReplyForumService.deleteMe(likeReplyForum);
        return res != null ? Result.success(res) : Result.error("delete like failed");
    }


}
