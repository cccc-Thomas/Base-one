package cn.chen.controller;

import cn.chen.entity.Medicine;
import cn.chen.service.MedicineService;
import cn.chen.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/medicine")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    /**
     * 返回 medicine 表中不同药品名称的数量（基于 name 去重）
     * @return Result<Integer> 包装的数量结果
     */
    @GetMapping("/count")
    public Result<Integer> countDistinctMedicineNames() {
        Integer count = medicineService.countDistinctMedicineNames();
        return Result.success(count);
    }

    /**
     * 查询库存大于指定阈值（默认为 2000）的药品列表
     * 可通过 ?threshold=3000 指定不同阈值
     * @param threshold 库存阈值（可选，默认 2000）
     * @return Result<List<Medicine>> 包装的满足条件的药品列表
     */
    @GetMapping("/highStock")
    public Result<List<Medicine>> getHighStockMedicines(@RequestParam(value = "threshold", required = false) Integer threshold) {
        if (threshold == null) threshold = 2000;
        List<Medicine> list = medicineService.getMedicinesWithStockGreaterThan(threshold);
        return Result.success(list);
    }
}
