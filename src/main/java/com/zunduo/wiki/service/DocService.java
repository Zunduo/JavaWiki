package com.zunduo.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zunduo.wiki.domain.Doc;
import com.zunduo.wiki.domain.DocExample;
import com.zunduo.wiki.mapper.DocMapper;
import com.zunduo.wiki.req.DocQueryReq;
import com.zunduo.wiki.req.DocSaveReq;
import com.zunduo.wiki.resp.DocQueryResp;
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
public class DocService {

    private static final Logger LOG = LoggerFactory.getLogger((DocService.class));

    @Resource
    private DocMapper docMapper;

    @Resource
    private UuidUtils uuidUtils;

    public PageResp<DocQueryResp> list(DocQueryReq req) {

        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();
        PageHelper.startPage(req.getPage(),req.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);
        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("总行数:{}", pageInfo.getTotal());
        LOG.info("总页数:{}", pageInfo.getPages());
        List<DocQueryResp> respList = new ArrayList<>();
//        for (Doc doc : docList) {
////            DocResp docResp = new DocResp();
////            BeanUtils.copyProperties(doc,docResp);
        // 对象复制
//            DocResp docResp = CopyUtil.copy(doc, DocResp.class);
//            respList.add(docResp);
//        }
        //列表复制
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);
        PageResp<DocQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public List<DocQueryResp> all() {

        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);
        //列表复制
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);

        return list;
    }
/**
 * 保存
 */
        public void save(DocSaveReq req){
            Doc doc = CopyUtil.copy(req,Doc.class);
            if(ObjectUtils.isEmpty(req.getId())){
                //新增
                doc.setId(uuidUtils.getId());
                docMapper.insert(doc);
            } else {
                //更新
                docMapper.updateByPrimaryKey(doc);
            }

        }

    /**
     * delete
     */
    public void delete(Long id){
       docMapper.deleteByPrimaryKey(id);
    }

}