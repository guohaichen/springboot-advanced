package com.chen.common_service.controller;

import com.chen.common_service.dto.Result;
import com.chen.common_service.entity.Photography;
import com.chen.common_service.mapper.PhotographyMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cgh
 * @create 2023-03-30
 */
@RestController
@RequestMapping("/share")
@Slf4j
public class PhotographyController {

    @Autowired
    private PhotographyMapper photographyMapper;

    @GetMapping("/photography")
    public Result<?> testOK(){
        return Result.OK("ok");
    }


    /*@GetMapping("/photography")
    public Result<?> getPhotography(@RequestBody Photography photography,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    @RequestParam(name = "pageNo", defaultValue = "2") Integer pageNo) {
        IPage<Photography> page = new Page<>(pageNo, pageSize);
        log.info("pageNo:{},\tpageSize:{}", pageNo, pageSize);
        LambdaQueryWrapper<Photography> queryWrapper = new LambdaQueryWrapper<>();
        log.info("photography:{}", photography);
        //photography => 参数 待实现
        return Result.OK(photographyMapper.selectPage(page, queryWrapper));
    }*/

    @PostMapping("/photography")
    public Result<?> addPhotography(@RequestBody Photography photography) {
        int result = photographyMapper.insert(photography);
        if (result < 0) {
            return Result.error("插入失败");
        }
        return Result.OK("插入成功");
    }
}
