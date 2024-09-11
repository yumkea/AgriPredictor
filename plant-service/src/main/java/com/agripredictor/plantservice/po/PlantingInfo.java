package com.agripredictor.plantservice.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 种植信息表
 * </p>
 *
 * @author yuum
 * @since 2024-08-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("planting_info")
public class PlantingInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String name;

    private String description;

    private String tags;

    private Integer status;

    private String extension;

    private Long createTime;

    private Long lastUpdatedTime;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 种植地点
     */
    private String plantingLocation;

    /**
     * 种植面积
     */
    private String plantingArea;

    /**
     * 作物名称
     */
    private String cropName;

    /**
     * 作物类型
     */
    private String cropType;

    /**
     * 种植开始时间
     */
    private Long plantingTimeBegin;

    /**
     * 种植周期
     */
    private Long plantingPeriod;

    /**
     * 施肥记录 json 数据
     */
    private String fertilizationRecord;

    /**
     * 预期产量
     */
    private String expectedOutput;


}
