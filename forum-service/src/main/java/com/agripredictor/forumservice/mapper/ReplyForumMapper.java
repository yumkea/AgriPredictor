package com.agripredictor.forumservice.mapper;

import com.agripredictor.forumservice.po.ReplyForum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 评论表 Mapper 接口
 * </p>
 *
 * @author yuum
 * @since 2024-08-25
 */
public interface ReplyForumMapper extends BaseMapper<ReplyForum> {

    void deleteDataInOneAndChildrenById(Long id);

}
