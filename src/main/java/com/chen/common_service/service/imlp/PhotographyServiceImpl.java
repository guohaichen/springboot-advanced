package com.chen.common_service.service.imlp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.common_service.dto.Result;
import com.chen.common_service.entity.Photography;
import com.chen.common_service.mapper.PhotographyMapper;
import com.chen.common_service.service.IPhotographyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cgh
 * @create 2023-03-30
 */
@Service
public class PhotographyServiceImpl extends ServiceImpl<PhotographyMapper, Photography> implements IPhotographyService {

    //文件保存的路径(浏览器阻止了访问本地文件资源。故使用http服务访问，已做配置)
    public static final String imgUrlPrefix = "http://localhost:9090/image/";

    @Autowired
    private PhotographyMapper photographyMapper;

    @Override
    public Result<?> listPhotography(Photography photography) {
        List<Photography> photographyList = photographyMapper.selectList(new LambdaQueryWrapper<Photography>());
        for (Photography one : photographyList) {
            String url = one.getImgUrl();
            one.setImgUrl(completeFilePath(url));
        }
        return Result.OK("查询成功", photographyList);
    }

    //拼接文件的全路径
    public String completeFilePath(String imgUrl) {
        return imgUrlPrefix + imgUrl;
    }

}
