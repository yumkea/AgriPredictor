package com.agripredictor.plantservice.service.impl;

import com.agripredictor.common.annotation.AutoFill;
import com.agripredictor.common.enumeration.AutoFillType;
import com.agripredictor.common.po.PageVO;
import com.agripredictor.plantservice.po.PlantingInfo;
import com.agripredictor.plantservice.mapper.PlantingInfoMapper;
import com.agripredictor.plantservice.service.IPlantingInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 种植信息表 服务实现类
 * </p>
 *
 * @author yuum
 * @since 2024-08-24
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PlantingInfoServiceImpl extends ServiceImpl<PlantingInfoMapper, PlantingInfo> implements IPlantingInfoService {
    private final PlantingInfoMapper plantingInfoMapper;
    @Override
    @AutoFill(value = AutoFillType.CREATE)
    public PlantingInfo savePlantingInfo(PlantingInfo plantingInfo) {
        log.info("测试 {}", plantingInfo);
        plantingInfoMapper.insert(plantingInfo);
        return plantingInfoMapper.selectById(plantingInfo.getId());
    }

    @Override
    @AutoFill(value = AutoFillType.UPDATE)
    public PlantingInfo updateMe(PlantingInfo plantingInfo) {
        plantingInfoMapper.updateById(plantingInfo);
        return plantingInfoMapper.selectById(plantingInfo.getId());
    }

    @Override
    public PageVO<PlantingInfo> pageMe(Long pageNum, Long pageSize, String userId) {

        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        log.info("种植信息 数据库查询参数 pageNum:{}, pageSize:{}, userId:{}", pageNum, pageSize,userId);
        Page<PlantingInfo> page = plantingInfoMapper.selectPage(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<PlantingInfo>()
                        .eq(userId != null, PlantingInfo::getUserId, userId)
                        .orderByDesc(PlantingInfo::getCreateTime)
        );
        return new PageVO<PlantingInfo>(pageSize,pageNum, page.getTotal(), page.getRecords());
    }
}
