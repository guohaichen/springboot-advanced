package com.chen.common_service.service.imlp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.common_service.entity.Photography;
import com.chen.common_service.mapper.PhotographyMapper;
import com.chen.common_service.service.IPhotographyService;
import org.springframework.stereotype.Service;

/**
 * @author cgh
 * @create 2023-03-30
 */
@Service
public class PhotographyServiceImpl extends ServiceImpl<PhotographyMapper, Photography> implements IPhotographyService {
}
