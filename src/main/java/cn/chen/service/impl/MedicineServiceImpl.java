package cn.chen.service.impl;

import cn.chen.entity.Medicine;
import cn.chen.mapper.MedicineMapper;
import cn.chen.service.MedicineService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service 实现：Medicine 相关业务实现
 */
@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {

    private final MedicineMapper medicineMapper;

    /**
     * 调用 Mapper 查询不同药品名称的数量
     *
     * @return 不同药品名称数（Integer）
     */
    @Override
    public Integer countDistinctMedicineNames() {
        return medicineMapper.countDistinctMedicineNames();
    }

    /**
     * 查询库存大于指定阈值的药品列表
     *
     * @param threshold 库存阈值（大于该值的药品将被返回）
     * @return 满足条件的药品列表
     */
    @Override
    public List<Medicine> getMedicinesWithStockGreaterThan(Integer threshold) {
        LambdaQueryWrapper<Medicine> query = new LambdaQueryWrapper<>();
        query.gt(Medicine::getStock, threshold);
        return medicineMapper.selectList(query);
    }
}
