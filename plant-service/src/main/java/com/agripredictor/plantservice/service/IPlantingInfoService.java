package com.agripredictor.plantservice.service;

import com.agripredictor.common.po.PageVO;
import com.agripredictor.plantservice.po.PlantingInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 种植信息表 服务类
 * </p>
 *
 * @author yuum
 * @since 2024-08-24
 */
public interface IPlantingInfoService extends IService<PlantingInfo> {

    PlantingInfo savePlantingInfo(PlantingInfo plantingInfo);

    PlantingInfo updateMe(PlantingInfo plantingInfo);

    PageVO<PlantingInfo> pageMe(Long pageNum, Long pageSize, String userId);
}
