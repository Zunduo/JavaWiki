package com.zunduo.wiki.service;

import com.mysql.cj.util.StringUtils;
import com.zunduo.wiki.domain.Ebook;
import com.zunduo.wiki.domain.EbookExample;
import com.zunduo.wiki.mapper.EbookMapper;
import com.zunduo.wiki.req.EbookReq;
import com.zunduo.wiki.resp.EbookResp;
import com.zunduo.wiki.util.CopyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {

    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())){
            criteria.andNameLike("%" + req.getName() + "%");
        }
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        List<EbookResp> respList = new ArrayList<>();
//        for (Ebook ebook : ebookList) {
////            EbookResp ebookResp = new EbookResp();
////            BeanUtils.copyProperties(ebook,ebookResp);
        // 对象复制
//            EbookResp ebookResp = CopyUtil.copy(ebook, EbookResp.class);
//            respList.add(ebookResp);
//        }
        //列表复制
        List<EbookResp> list = CopyUtil.copyList(ebookList, EbookResp.class);
        return list;
    }
}
