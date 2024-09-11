package com.agripredictor.forumservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.agripredictor.common.annotation.AutoFill;
import com.agripredictor.common.client.UserClient;
import com.agripredictor.common.enumeration.AutoFillType;
import com.agripredictor.common.po.ForumVO;
import com.agripredictor.common.po.PageVO;
import com.agripredictor.common.po.ReplyForumVO;
import com.agripredictor.common.po.UserVO;
import com.agripredictor.forumservice.mapper.ForumMapper;
import com.agripredictor.forumservice.mapper.LikeReplyForumMapper;
import com.agripredictor.forumservice.po.Forum;
import com.agripredictor.forumservice.po.LikeReplyForum;
import com.agripredictor.forumservice.po.ReplyForum;
import com.agripredictor.forumservice.mapper.ReplyForumMapper;
import com.agripredictor.forumservice.service.IForumService;
import com.agripredictor.forumservice.service.IReplyForumService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 评论类
 *
 * @author yuum
 * @since 2024-08-25
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ReplyForumServiceImpl extends ServiceImpl<ReplyForumMapper, ReplyForum> implements IReplyForumService {

    private final ReplyForumMapper replyForumMapper;
    private final ForumMapper forumMapper;
    private final IForumService forumService;
    private final UserClient userClient;
    private final LikeReplyForumMapper likeReplyForumMapper;

    /**
     * 添加评论
     *
     * @param replyForum
     * @return
     */
    @Override
    @AutoFill(value = AutoFillType.CREATE)
    public Object addMe(ReplyForum replyForum) {
        log.info("添加评论 {}", replyForum);
        Boolean flag = null; // true 帖子的评论   or  false 评论的评论

        //数据校验
        if (replyForum.getForumId() != null && !"".equals(replyForum.getForumId())) {
            flag = true;
        } else if (replyForum.getParentId() != null && !"".equals(replyForum.getParentId())) {
            flag = false;
        } else {
            return null;
        }

        if (flag) { // 帖子的评论 返回帖子
            if (forumMapper.update(null, new LambdaUpdateWrapper<Forum>()
                    .setSql("reply_count = reply_count + 1")
                    .eq(Forum::getId, replyForum.getForumId())) > 0
            ) {
                replyForumMapper.insert(replyForum);

                return forumService.getForumById(Long.valueOf(replyForum.getForumId()), replyForum.getCreatorId());
            } else {
                return null;
            }
        } else { // 评论的评论 返回父评论
            if (replyForumMapper.update(null, new LambdaUpdateWrapper<ReplyForum>()
                    .setSql("reply_count = reply_count + 1")
                    .eq(ReplyForum::getId, replyForum.getParentId())) > 0
            ) {
                replyForumMapper.insert(replyForum);
                return getReplyForumVOById(replyForum.getParentId(), replyForum.getCreatorId());
            }
        }
        return null;
    }

    /**
     * 删除评论
     * 评论只能单条删除
     * 评论删除 ， 该评论附带的所有子评论也会被删除
     * 只有评论人 和  帖子作者能删除
     * 评论人删除： 传递 reply的  id 即可
     * 帖子作者删除: 传递 reply的  id 并且将status 传递为 1
     * <p>
     * 删除点赞数据
     *
     * @param replyForum
     * @return 若帖子的评论 返回当前帖子数据； 若评论的评论  返回父评论
     */
    @Override
    public Object deleteMe(ReplyForum replyForum) {
        if (replyForum.getId() == null) {
            return null;
        }
        ReplyForum deletingData = replyForumMapper.selectOne(
                new LambdaQueryWrapper<ReplyForum>()
                        .eq(ReplyForum::getId, replyForum.getId())
                        .eq(replyForum.getStatus() == null || replyForum.getStatus() != 1, ReplyForum::getCreatorId, replyForum.getCreatorId())
        );
        if (deletingData == null) {
            return null;
        }
        if (deletingData.getForumId() != null && !"".equals(deletingData.getForumId())) { //帖子的评论
            forumMapper.update(null, new LambdaUpdateWrapper<Forum>()
                    .setSql("reply_count = reply_count - 1")
                    .ge(Forum::getReplyCount, 0)
                    .eq(Forum::getId, deletingData.getForumId()));
//            replyForumMapper.deleteById(deletingData.getId());


            //相关点赞数据删除
            likeReplyForumMapper.deleteDataInOneAndChildrenByReplyId(deletingData.getId());
            //数据库层面递归删除
            replyForumMapper.deleteDataInOneAndChildrenById(deletingData.getId());


            return forumMapper.selectById(deletingData.getForumId());
        } else if (deletingData.getParentId() != null) { //评论的评论
            replyForumMapper.update(null, new LambdaUpdateWrapper<ReplyForum>()
                    .setSql("reply_count = reply_count - 1")
                    .gt(ReplyForum::getReplyCount, 0)
                    .eq(ReplyForum::getId, deletingData.getParentId()));

            //相关点赞数据删除
            likeReplyForumMapper.deleteDataInOneAndChildrenByReplyId(deletingData.getId());
            //数据库层面递归删除
            replyForumMapper.deleteDataInOneAndChildrenById(deletingData.getId());


            return getReplyForumVOById(replyForum.getParentId(), replyForum.getCreatorId());
        }
        return null;
    }

    @Override
    public PageVO<ReplyForumVO> pageMe(Long pageNum, Long pageSize, Long parentId, String forumId, String userId) {

        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;

        if ((/*同时为空 */ parentId == null && (forumId == null || "".equals(forumId)))
                || /*同时不为空*/ (parentId != null && (forumId != null && !"".equals(forumId)))) {
            log.error("评论参数传递错误");
            return null;
        }

        Page<ReplyForum> page = page(new Page<>(pageNum, pageSize), new LambdaQueryWrapper<ReplyForum>()
                .eq(parentId != null, ReplyForum::getParentId, parentId)
                .eq(forumId != null && !"".equals(forumId), ReplyForum::getForumId, forumId));

        List<ReplyForumVO> replyForumVOList = new ArrayList<>();
        //CreatorIds
        List<String> creatorIds = new ArrayList<>();
        //replyIds
        List<Long> replyIds = new ArrayList<>();
        page.getRecords().forEach(record -> {
            ReplyForumVO replyForumVO = BeanUtil.copyProperties(record, ReplyForumVO.class);
            replyForumVOList.add(replyForumVO);
            creatorIds.add(replyForumVO.getCreatorId());
            replyIds.add(replyForumVO.getId());
        });

        /**
         * 获取userMap
         */
        Map<String, List<UserVO>> userMap = userClient.getUserVOByUserIds(creatorIds).getData()
                .stream().collect(Collectors.groupingBy(UserVO::getId));

        /**
         * 获取评论点赞表
         */
        Map<String, List<LikeReplyForum>> likeReplyForumMap;
        if (userId != null && !"".equals(userId)) {
            likeReplyForumMap = likeReplyForumMapper.selectList(new LambdaQueryWrapper<LikeReplyForum>()
                            .in(LikeReplyForum::getReplyForumId, replyIds)
                            .eq(LikeReplyForum::getCreatorId, userId))
                    .stream().collect(Collectors.groupingBy(LikeReplyForum::getReplyForumId));
        } else {
            likeReplyForumMap = null;
        }

        /**
         * 添加
         */
        replyForumVOList.forEach(x -> {
            x.setAuthorName(userMap.get(x.getCreatorId()).get(0).getName());
            x.setAuthorImgUrl(userMap.get(x.getCreatorId()).get(0).getImgUrl());

            if (likeReplyForumMap != null && !likeReplyForumMap.isEmpty()) {
                if (likeReplyForumMap.get(x.getId()).get(0) != null) {
                    x.setAlreadyLiked(1);
                } else {
                    x.setAlreadyLiked(0);
                }
            }
        });

        return new PageVO<>(pageNum, pageSize, page.getTotal(), replyForumVOList);
    }

    @Override
    public ReplyForumVO getReplyForumVOById(Long id, String userId) {
        ReplyForumVO replyForumVO = BeanUtil.copyProperties(replyForumMapper.selectById(id), ReplyForumVO.class);
        /**
         * 作者信息
         */
        UserVO userVO = userClient.getUserVOByUserIds(List.of(userId)).getData().get(0);
        replyForumVO.setAuthorName(userVO.getName());
        replyForumVO.setAuthorImgUrl(userVO.getImgUrl());

        /**
         * 点赞信息
         */
        if (likeReplyForumMapper.selectCount(new LambdaQueryWrapper<LikeReplyForum>()
                .eq(LikeReplyForum::getReplyForumId, replyForumVO.getId())
                .eq(LikeReplyForum::getCreatorId, userId)) > 0) {
            replyForumVO.setAlreadyLiked(1);
        } else {
            replyForumVO.setAlreadyLiked(0);
        }

        return replyForumVO;

    }


}
