package com.agripredictor.forumservice.controller;


import cn.hutool.core.bean.BeanUtil;
import com.agripredictor.common.constant.UserConstant;
import com.agripredictor.common.po.ForumVO;
import com.agripredictor.common.po.PageVO;
import com.agripredictor.common.po.Result;
import com.agripredictor.forumservice.po.Forum;
import com.agripredictor.forumservice.service.IForumService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 论坛帖子
 * @author yuum
 * @since 2024-08-25
 */
@RestController
@RequestMapping("/forum")
@RequiredArgsConstructor
@Slf4j
public class ForumController {

    private final IForumService forumService;


    /**
     * 添加论坛帖子
     * 返回未点赞数据
     * @param forum
     * @param userId
     * @return
     */
    @PostMapping("/addForum")
    public Result<ForumVO> addForum(@RequestBody Forum forum, @RequestHeader(UserConstant.USER_ID) String userId) {
        forum.setCreatorId(userId);
        forum.setLastOperatorId(userId);
        log.info("添加论坛帖子 {}",forum);
        return Result.success(forumService.add(forum));
    }

    /**
     * 修改帖子
     * @param forum
     * @param userId
     * @return
     */
    @PutMapping("/updateForum")
    public Result<ForumVO> updateForum(@RequestBody Forum forum, @RequestHeader(UserConstant.USER_ID) String userId) {
        forum.setLastOperatorId(userId);
        log.info("修改帖子 {}",forum);
        forumService.updateMe(forum);
        Forum temp = forumService.getById(forum.getId());
        ForumVO forumVO = BeanUtil.copyProperties(temp, ForumVO.class);
        return forumVO == null ? Result.error("update failed") : Result.success(forumVO);
    }


    /**
     * 删除帖子 byId
     * @param ids
     * @param userId
     * @return
     */
    @DeleteMapping("/deleteForum")
    public Result deleteForum(@RequestBody List<String> ids, @RequestHeader(UserConstant.USER_ID) String userId) {
        log.info("删除帖子 byId ids{} userId {}", ids, userId);
        forumService.remove(new LambdaQueryWrapper<Forum>()
                .in(Forum::getId, ids)
                .eq(Forum::getCreatorId, userId));
        return Result.success();
    }

    /**
     * 条件分页查询帖子
     * 该接口允许未登录的用户查询 返回未点赞状态
     * 登录查看 返回具体点赞、收藏状态
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @param title 标题 模糊查询
     * @param summary 摘要 模糊查询
     * @return
     */
    @GetMapping("/pageForum")
    public Result<PageVO<ForumVO>> pageForum(
            Long pageNum,
            Long pageSize,
            String categoryId,
            String creatorId,
            String title,
            String summary,
            Integer status,
            @RequestHeader(UserConstant.USER_ID) String userId
    ) {
        log.info("条件分页查询帖子 pageNum {} ,pageSize {} ,categoryId {} creatorId {}, title {} , summary {} , userId {} status {}"
                ,pageNum,pageSize,categoryId,creatorId,title,summary,userId,status);
        return Result.success(forumService.pageMe(pageNum,pageSize,categoryId,creatorId,title,summary,status,userId,null));
    }


}
