package com.agripredictor.forumservice.service.impl;

import com.agripredictor.common.annotation.AutoFill;
import com.agripredictor.common.enumeration.AutoFillType;
import com.agripredictor.common.po.PageVO;
import com.agripredictor.forumservice.po.CategoryForum;
import com.agripredictor.forumservice.mapper.CategoryForumMapper;
import com.agripredictor.forumservice.service.ICategoryForumService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.aspectj.apache.bcel.generic.ReturnaddressType;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 * @author yuum
 * @since 2024-08-25
 */
@Service
public class CategoryForumServiceImpl extends ServiceImpl<CategoryForumMapper, CategoryForum> implements ICategoryForumService {

    @Override
    public CategoryForum saveMe(CategoryForum categoryForum) {
        save(categoryForum);
        return getById(categoryForum.getId());
    }

    @Override
    public CategoryForum updateMe(CategoryForum categoryForum) {
        if(getById(categoryForum.getId()) == null) {
            return null;
        }
        updateById(categoryForum);
        return getById(categoryForum.getId());
    }

    @Override
    public PageVO<CategoryForum> pageMe(Long pageNum, Long pageSize) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        Page<CategoryForum> page = page(new Page<>(pageNum, pageSize));
        return new  PageVO<CategoryForum>(pageSize,pageNum,page.getTotal(),page.getRecords());
    }
}
