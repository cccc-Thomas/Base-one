package cn.chen.service;

import java.util.List;

/**
 * Service 接口：药品相关业务逻辑（Medicine）
 */
public interface MedicineService {
    /**
     * 返回 medicine 表中不同药品名称的数量（基于 name 去重统计）
     *
     * @return 不同药品名称数量（Integer）
     */
    Integer countDistinctMedicineNames();

    /**
     * 查询库存大于指定阈值的药品列表
     *
     * @param threshold 库存阈值（大于该值的药品将被返回）
     * @return 满足条件的药品列表
     */
    List<cn.chen.entity.Medicine> getMedicinesWithStockGreaterThan(Integer threshold);
}
