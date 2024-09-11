package com.agripredictor.plantservice.controller;
import com.agripredictor.common.constant.UserConstant;
import com.agripredictor.common.po.PageVO;
import com.agripredictor.common.po.Result;
import com.agripredictor.plantservice.po.PlantingInfo;
import com.agripredictor.plantservice.service.IPlantingInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * 种植信息
 * @author yuum
 * @since 2024-08-24
 */
@RestController
@Slf4j
@RequestMapping("/plant")
@RequiredArgsConstructor
public class PlantingInfoController {
    private final IPlantingInfoService plantingInfoService;


    /**
     * 添加种植信息
     * @param plantingInfo
     * @param userId
     * @return
     */
    @PostMapping("/savePlantingInfo")
    public Result<PlantingInfo> savePlantingInfo(@RequestBody PlantingInfo plantingInfo, @RequestHeader(UserConstant.USER_ID) String userId) {
        plantingInfo.setUserId(userId);
        log.info("添加种植信息 {}", plantingInfo);
        return Result.success(plantingInfoService.savePlantingInfo(plantingInfo));
    }


    /**
     * 修改种植信息
     * @param plantingInfo
     * @param userId
     * @return
     */
    @PutMapping("updatePlantingInfo")
    public Result<PlantingInfo> updatePlantingInfo(@RequestBody PlantingInfo plantingInfo, @RequestHeader(UserConstant.USER_ID) String userId) {
        plantingInfo.setUserId(userId);
        if(plantingInfo.getId() == null || plantingInfo.getId() == ""){
            return Result.error("种植信息实体未传递");
        }
        log.info("修改种植信息 {}", plantingInfo);
        return Result.success(plantingInfoService.updateMe(plantingInfo));
    }

    /**
     * 删除种植信息
     * @param ids
     * @return
     */
    @DeleteMapping("/deletePlantingInfoByIds")
    public Result deletePlantingInfoByIds(@RequestBody List<String> ids) {
        log.info("删除种植信息 {}",ids);
        plantingInfoService.removeByIds(ids);
        return Result.success();
    }


    /**
     * 分页查询种植信息
     * GET请求，注意是请求参数，不是请求体
     * 页数，页码 默认 1 ，10
     * 按照创建时间倒叙排序
     * @param pageNum
     * @param pageSize
     * @param userId
     * @return
     */
    @GetMapping("/pagePlantingInfo")
    public Result<PageVO<PlantingInfo>> pagePlantingInfo(
            Long pageNum,
            Long pageSize,
            @RequestHeader(UserConstant.USER_ID) String userId) {
        log.info("分页查询种植信息 pageNum: {},pageSize: {}",pageNum,pageSize);
        return Result.success(plantingInfoService.pageMe(pageNum,pageSize,userId));

    }

}
