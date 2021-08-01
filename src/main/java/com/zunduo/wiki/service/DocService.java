package com.zunduo.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zunduo.wiki.domain.Content;
import com.zunduo.wiki.domain.Doc;
import com.zunduo.wiki.domain.DocExample;
import com.zunduo.wiki.exception.BusinessException;
import com.zunduo.wiki.exception.BusinessExceptionCode;
import com.zunduo.wiki.mapper.ContentMapper;
import com.zunduo.wiki.mapper.DocMapper;
import com.zunduo.wiki.mapper.DocMapperCust;
import com.zunduo.wiki.req.DocQueryReq;
import com.zunduo.wiki.req.DocSaveReq;
import com.zunduo.wiki.resp.DocQueryResp;
import com.zunduo.wiki.resp.PageResp;
import com.zunduo.wiki.util.CopyUtil;
import com.zunduo.wiki.util.RedisUtil;
import com.zunduo.wiki.util.RequestContext;
import com.zunduo.wiki.util.UuidUtils;
import com.zunduo.wiki.websocket.WebSocketServer;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;
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
    private ContentMapper contentMapper;

    @Resource
    private DocMapperCust docMapperCust;

    @Resource
    private UuidUtils uuidUtils;

    @Resource
    public RedisUtil redisUtil;

    @Resource
    public WsService wsService;

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

    public List<DocQueryResp> all(Long ebookId) {

        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
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
            Content content = CopyUtil.copy(req,Content.class);
            if(ObjectUtils.isEmpty(req.getId())){
                //新增
                doc.setId(uuidUtils.getId());
                doc.setViewCount(0);
                doc.setVoteCount(0);
                docMapper.insert(doc);

                content.setId(doc.getId());
                contentMapper.insert(content);
            } else {
                //更新
                docMapper.updateByPrimaryKey(doc);
                int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
                if (count == 0){
                    contentMapper.insert(content);
                }
            }

        }

    /**
     * delete
     */
    public void delete(Long id){
       docMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<String> ids){
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }
    public String findContent(Long id){
        Content content = contentMapper.selectByPrimaryKey(id);
        docMapperCust.increaseViewCount(id);
        if (ObjectUtils.isEmpty(content)) {
            return "";
        } else {
        return content.getContent();
        }
    }
    public void vote(Long id) {
        // docMapperCust.increaseVoteCount(id);
        // 远程IP+doc.id作为key，24小时内不能重复
        String ip = RequestContext.getRemoteAddr();
        if (redisUtil.validateRepeat("DOC_VOTE_" + id + "_" + ip, 5000)) {
            docMapperCust.increaseVoteCount(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }
        Doc docDb = docMapper.selectByPrimaryKey(id);
        String logId = MDC.get("LOG_ID");
        wsService.sendInfo("[" + docDb.getName() +"]被点赞！", logId);
    }

    public void updateEbookInfo() {
        docMapperCust.updateEbookInfo();
    }
}
