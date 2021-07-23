package com.zunduo.wiki.controller;

import com.zunduo.wiki.domain.Ebook;
import com.zunduo.wiki.req.EbookReq;
import com.zunduo.wiki.resp.CommonResp;
import com.zunduo.wiki.resp.EbookResp;
import com.zunduo.wiki.resp.PageResp;
import com.zunduo.wiki.service.EbookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list(EbookReq req) {
        CommonResp<PageResp<EbookResp>> resp = new CommonResp<>();
        PageResp<EbookResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }
}
