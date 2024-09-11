package com.agripredictor.forumservice.service;

import com.agripredictor.common.po.PageVO;
import com.agripredictor.forumservice.po.CategoryForum;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 种植信息表 服务类
 * </p>
 *
 * @author yuum
 * @since 2024-08-25
 */
public interface ICategoryForumService extends IService<CategoryForum> {

    CategoryForum saveMe(CategoryForum categoryForum);

    CategoryForum updateMe(CategoryForum categoryForum);

    PageVO<CategoryForum> pageMe(Long pageNum, Long pageSize);
}
