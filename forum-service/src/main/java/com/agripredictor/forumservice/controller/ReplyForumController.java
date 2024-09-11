package com.agripredictor.forumservice.controller;


import com.agripredictor.common.constant.UserConstant;
import com.agripredictor.common.po.PageVO;
import com.agripredictor.common.po.ReplyForumVO;
import com.agripredictor.common.po.Result;
import com.agripredictor.forumservice.po.ReplyForum;
import com.agripredictor.forumservice.service.IReplyForumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 评论类
 *
 * @author yuum
 * @since 2024-08-25
 */
@RestController
@RequestMapping("/forum/reply")
@Slf4j
@RequiredArgsConstructor
public class ReplyForumController {

    private final IReplyForumService replyForumService;

    /**
     * 评论添加
     * 帖子的评论 传递 forumId , parentId "不"传
     * 评论的评论 传递 parentId , forumId "不"传
     * replyForum
     * Forum 中的replyCount 只统计帖子的直接评论  不会统计间接评论
     * @param replyForum
     * @return 若帖子的评论 返回当前帖子数据； 若评论的评论  返回父评论
     */
    @PostMapping("/addReplyForum")
    public Result<Object> addReplyForum(@RequestBody ReplyForum replyForum , @RequestHeader(UserConstant.USER_ID) String userId) {
        replyForum.setCreatorId(userId);
        Object res = replyForumService.addMe(replyForum);
        return res == null ? Result.error("add failed") : Result.success(res);
    }


    /**
     * 删除评论
     * 评论只能单条删除
     * 评论删除 ， 该评论附带的所有子评论也会被删除
     * 只有评论人 和  帖子作者能删除
     * 评论人删除： 传递 reply的  id 即可
     * 帖子作者删除: 传递 reply的  id 并且将status 传递为 1
     * @param replyForum
     * @param userId
     * @return 若帖子的评论 返回当前帖子数据； 若评论的评论  返回父评论
     */
    @DeleteMapping("/deleteReply")
    public Result<Object> deleteReplyForum(@RequestBody ReplyForum replyForum ,@RequestHeader(UserConstant.USER_ID) String userId) {
        replyForum.setCreatorId(userId);
        log.info("删除评论 {} ", replyForum);
        Object res = replyForumService.deleteMe(replyForum);
        return res == null ? Result.error("delete failed") : Result.success(res);
    }


    /**
     * 分页查询评论
     * forumId  parentId 必须二选一
     * 帖子的直接评论 传递 forumId ，  parentId 不传
     * 评论的评论 传递 parentId ，  forumId 不传
     * @param pageNum 不传 默认1
     * @param pageSize 不传 默认10
     * @param parentId 若 b是 a的评论 要查询 b ，将a的id放在parentId中传递
     * @param forumId
     * @return
     */
    @GetMapping("/pageReplyForum")
    public Result<PageVO<ReplyForumVO>> pageReplyForum(
            Long pageNum,
            Long pageSize,
            Long parentId,
            String forumId,
            @RequestHeader(UserConstant.USER_ID) String userId
    ) {
        log.info("分页查询评论 pageNum {} , pageSize {} , parentId {} forumId {} userId {}", pageNum, pageSize, parentId,forumId,userId);

        PageVO<ReplyForumVO> res = replyForumService.pageMe(pageNum, pageSize, parentId, forumId, userId);
        return res == null ? Result.error("query failed ,check parameters") : Result.success(res);
    }


}
