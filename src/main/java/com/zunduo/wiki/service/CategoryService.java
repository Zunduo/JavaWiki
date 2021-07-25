package com.zunduo.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zunduo.wiki.domain.Category;
import com.zunduo.wiki.domain.CategoryExample;
import com.zunduo.wiki.mapper.CategoryMapper;
import com.zunduo.wiki.req.CategoryQueryReq;
import com.zunduo.wiki.req.CategorySaveReq;
import com.zunduo.wiki.resp.CategoryQueryResp;
import com.zunduo.wiki.resp.PageResp;
import com.zunduo.wiki.util.CopyUtil;
import com.zunduo.wiki.util.UuidUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger((CategoryService.class));

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private UuidUtils uuidUtils;

    public PageResp<CategoryQueryResp> list(CategoryQueryReq req) {

        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        PageHelper.startPage(req.getPage(),req.getSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        LOG.info("总行数:{}", pageInfo.getTotal());
        LOG.info("总页数:{}", pageInfo.getPages());
        List<CategoryQueryResp> respList = new ArrayList<>();
//        for (Category category : categoryList) {
////            CategoryResp categoryResp = new CategoryResp();
////            BeanUtils.copyProperties(category,categoryResp);
        // 对象复制
//            CategoryResp categoryResp = CopyUtil.copy(category, CategoryResp.class);
//            respList.add(categoryResp);
//        }
        //列表复制
        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        PageResp<CategoryQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public List<CategoryQueryResp> all() {

        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        //列表复制
        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);

        return list;
    }
/**
 * 保存
 */
        public void save(CategorySaveReq req){
            Category category = CopyUtil.copy(req,Category.class);
            if(ObjectUtils.isEmpty(req.getId())){
                //新增
                category.setId(uuidUtils.getId());
                categoryMapper.insert(category);
            } else {
                //更新
                categoryMapper.updateByPrimaryKey(category);
            }

        }

    /**
     * delete
     */
    public void delete(Long id){
       categoryMapper.deleteByPrimaryKey(id);
    }

}
