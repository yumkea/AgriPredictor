package com.agripredictor.forumservice.controller;
import com.agripredictor.common.po.PageVO;
import com.agripredictor.common.po.Result;
import com.agripredictor.forumservice.po.CategoryForum;
import com.agripredictor.forumservice.service.ICategoryForumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 帖子分类
 * @author yuum
 * @since 2024-08-25
 */
@RestController
@RequestMapping("/forum/category")
@RequiredArgsConstructor
@Slf4j
public class CategoryForumController {

    private final ICategoryForumService categoryForumService;
    /**
     * 帖子分类添加
     * @param categoryForum
     * @return
     */
    @PostMapping("/addCategoryForum")
    public Result<CategoryForum> addCategoryForum(@RequestBody CategoryForum categoryForum) {
        log.info("帖子分类添加 {}", categoryForum);
        return Result.success(categoryForumService.saveMe(categoryForum));
    }

    /**
     * 分类修改
     * @param categoryForum
     * @return
     */
    @PutMapping("/updateCategoryForum")
    public Result<CategoryForum> updateCategoryForum(@RequestBody CategoryForum categoryForum) {
        log.info("分类修改 {}", categoryForum);
        CategoryForum category = categoryForumService.updateMe(categoryForum);
        return category != null ? Result.success(category) : Result.error("update failed");
    }


    /**
     * 分类删除
     * @param ids
     * @return
     */
    @DeleteMapping("/deleteCategoryForum")
    public Result deleteCategoryForum(@RequestBody List<String> ids) {
        log.info("分类删除 {}",ids);
        categoryForumService.removeByIds(ids);
        return Result.success();
    }


    /**
     * 分页查询分类
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/pageAllCategoryForum")
    public Result<PageVO<CategoryForum>> pageCategoryForum(
            Long pageNum,
            Long pageSize
    ) {
        log.info("分页查询分类 pageNum:{},pageSize:{}",pageNum,pageSize);
        return Result.success(categoryForumService.pageMe(pageNum,pageSize));
    }

}
