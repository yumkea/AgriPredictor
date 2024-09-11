package com.agripredictor.forumservice.service.impl;

import com.agripredictor.common.po.ReplyForumVO;
import com.agripredictor.forumservice.mapper.ReplyForumMapper;
import com.agripredictor.forumservice.po.LikeReplyForum;
import com.agripredictor.forumservice.mapper.LikeReplyForumMapper;
import com.agripredictor.forumservice.po.ReplyForum;
import com.agripredictor.forumservice.service.ILikeReplyForumService;
import com.agripredictor.forumservice.service.IReplyForumService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 回复点赞表 服务实现类
 *
 * @author yuum
 * @since 2024-08-25
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LikeReplyForumServiceImpl extends ServiceImpl<LikeReplyForumMapper, LikeReplyForum> implements ILikeReplyForumService {

    private final LikeReplyForumMapper likeReplyForumMapper;
    private final ReplyForumMapper replyForumMapper;
    private final IReplyForumService replyForumService;

    /**
     * 添加评论点赞
     * 注意 like_count
     *
     * @param likeReplyForum
     * @return
     */
    @Override
    public ReplyForumVO saveMe(LikeReplyForum likeReplyForum) {
        likeReplyForum.setCreateTime(System.currentTimeMillis());
        log.info("添加评论点赞 {}", likeReplyForum);
        if (likeReplyForum.getReplyForumId() == null || "".equals(likeReplyForum.getReplyForumId())) {
            return null;
        }

        /**
         * 幂等处理
         */
        Long count = likeReplyForumMapper.selectCount(new LambdaQueryWrapper<LikeReplyForum>()
                .eq(LikeReplyForum::getReplyForumId, likeReplyForum.getReplyForumId())
                .eq(LikeReplyForum::getCreatorId, likeReplyForum.getCreatorId()));


        if ( count == 0 && replyForumMapper.update(null, new LambdaUpdateWrapper<ReplyForum>()
                .setSql("like_count  = like_count + 1")
                .eq(ReplyForum::getId, likeReplyForum.getReplyForumId())) == 1
        ) {
            likeReplyForumMapper.insert(likeReplyForum);
            return replyForumService.getReplyForumVOById(Long.valueOf(likeReplyForum.getReplyForumId()),likeReplyForum.getCreatorId());
        } else {
            return null;
        }
    }

    @Override
    public ReplyForumVO deleteMe(LikeReplyForum likeReplyForum) {
        if(likeReplyForum.getId() == null) {
            return null;
        }

        LikeReplyForum deletingLikeReplyForum = likeReplyForumMapper.selectOne(new LambdaQueryWrapper<LikeReplyForum>()
                .eq(LikeReplyForum::getId, likeReplyForum.getId())
                .eq(LikeReplyForum::getCreatorId, likeReplyForum.getCreatorId()));

        if (deletingLikeReplyForum != null &&
            replyForumMapper.update(null,new LambdaUpdateWrapper<ReplyForum>()
                    .setSql("like_count  = like_count - 1")
                    .eq(ReplyForum::getId,deletingLikeReplyForum.getReplyForumId())
                    .gt(ReplyForum::getLikeCount,0))  > 0 ) {
            likeReplyForumMapper.deleteById(deletingLikeReplyForum.getId());
            return replyForumService.getReplyForumVOById(Long.valueOf(deletingLikeReplyForum.getReplyForumId()),likeReplyForum.getCreatorId());
        }  else
        {
            return null;
        }
    }
}
