package com.agripredictor.forumservice.service.impl;

import com.agripredictor.common.po.ForumVO;
import com.agripredictor.common.po.PageVO;
import com.agripredictor.forumservice.mapper.ForumMapper;
import com.agripredictor.forumservice.po.Forum;
import com.agripredictor.forumservice.po.LikeForum;
import com.agripredictor.forumservice.mapper.LikeForumMapper;
import com.agripredictor.forumservice.service.IForumService;
import com.agripredictor.forumservice.service.ILikeForumService;
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
 * <p>
 * 点赞表 服务实现类
 * </p>
 *
 * @author yuum
 * @since 2024-08-25
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class LikeForumServiceImpl extends ServiceImpl<LikeForumMapper, LikeForum> implements ILikeForumService {
    private final LikeForumMapper likeForumMapper;
    private final ForumMapper forumMapper;
    private final IForumService forumService;

    /**
     * 将帖子 点赞++
     *
     * @param likeForum
     */
    @Override
    public ForumVO saveMe(LikeForum likeForum) {

        likeForum.setCreateTime(System.currentTimeMillis());
        log.info("添加点赞 {}", likeForum);

        /**
         * 幂等处理
         */
        Long count = likeForumMapper.selectCount(
                new LambdaQueryWrapper<LikeForum>()
                        .eq(LikeForum::getForumId, likeForum.getForumId())
                        .eq(LikeForum::getCreatorId, likeForum.getCreatorId())
        );
        if(count == 0){
            likeForumMapper.insert(likeForum);
            forumMapper.update(null, new LambdaUpdateWrapper<Forum>()
                    .setSql("like_count  = like_count + 1")
                    .eq(Forum::getId, likeForum.getForumId()));
        }

        return forumService.getForumById(Long.valueOf(likeForum.getForumId()), likeForum.getCreatorId());
    }

    /**
     * 取消点赞
     *
     * @param likeForum 已知 forumId CreatorId
     * @return
     */
    @Override
    public ForumVO deleteMe(LikeForum likeForum) {
        log.info("取消点赞 {}", likeForum);
        if (likeForumMapper.delete(new LambdaQueryWrapper<LikeForum>()
                .eq(LikeForum::getCreatorId, likeForum.getCreatorId())
                .eq(LikeForum::getForumId, likeForum.getForumId())
        ) > 0) {
            forumMapper.update(null, new LambdaUpdateWrapper<Forum>()
                    .setSql("like_count  = like_count + 1")
                    .eq(Forum::getId, likeForum.getForumId()));
        }
        return forumService.getForumById(Long.valueOf(likeForum.getForumId()), likeForum.getCreatorId());
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
    public PageVO<ForumVO> pageLikeForumByUser(Long pageNum, Long pageSize, String categoryId, String title, String summary, Integer status, String userId) {
        List<LikeForum> likeForums = likeForumMapper.selectList(
                new LambdaQueryWrapper<LikeForum>()
                        .select(LikeForum::getForumId)
                        .eq(LikeForum::getCreatorId, userId)
        );
        //forumIds
        List<Long> forumIds = new ArrayList<>();
        likeForums.forEach(x->{
            forumIds.add(Long.valueOf(x.getForumId()));
        });

        log.info("{} 搜藏的帖子 ids {}",userId,forumIds);
        if(forumIds.isEmpty()) {
            return new PageVO<ForumVO>(0L,0L,0L,null);
        }

        return forumService.pageMe(pageNum,pageSize,categoryId,null,title,summary,status,userId,forumIds);
    }
}
