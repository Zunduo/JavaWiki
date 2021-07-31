package com.zunduo.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zunduo.wiki.domain.User;
import com.zunduo.wiki.domain.UserExample;
import com.zunduo.wiki.exception.BusinessException;
import com.zunduo.wiki.exception.BusinessExceptionCode;
import com.zunduo.wiki.mapper.UserMapper;
import com.zunduo.wiki.req.UserQueryReq;
import com.zunduo.wiki.req.UserResetPasswordReq;
import com.zunduo.wiki.req.UserSaveReq;
import com.zunduo.wiki.resp.UserQueryResp;
import com.zunduo.wiki.resp.PageResp;
import com.zunduo.wiki.util.CopyUtil;
import com.zunduo.wiki.util.UuidUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class  UserService {

    private static final Logger LOG = LoggerFactory.getLogger((UserService.class));

    @Resource
    private UserMapper userMapper;

    @Resource
    private UuidUtils uuidUtils;

    public PageResp<UserQueryResp> list(UserQueryReq req) {

        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getLoginName())){
            criteria.andLoginNameEqualTo(req.getLoginName());
        }
        PageHelper.startPage(req.getPage(),req.getSize());
        List<User> userList = userMapper.selectByExample(userExample);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        LOG.info("总行数:{}", pageInfo.getTotal());
        LOG.info("总页数:{}", pageInfo.getPages());
        List<UserQueryResp> respList = new ArrayList<>();
//        for (User user : userList) {
////            UserResp userResp = new UserResp();
////            BeanUtils.copyProperties(user,userResp);
        // 对象复制
//            UserResp userResp = CopyUtil.copy(user, UserResp.class);
//            respList.add(userResp);
//        }
        //列表复制
        List<UserQueryResp> list = CopyUtil.copyList(userList, UserQueryResp.class);
        PageResp<UserQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }
/**
 * 保存
 */
        public void save(UserSaveReq req){
            User user = CopyUtil.copy(req,User.class);
            if(ObjectUtils.isEmpty(req.getId())){
                //新增
                if (ObjectUtils.isEmpty(selectByLoginName(req.getLoginName()))){
                user.setId(uuidUtils.getId());
                userMapper.insert(user);
                } else {
                    // username already exist
                    throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
                }
            } else {
                //更新
                user.setLoginName(null);
                user.setPassword(null);
                userMapper.updateByPrimaryKeySelective(user);
            }
        }

    /**
     * delete
     */
    public void delete(Long id){
       userMapper.deleteByPrimaryKey(id);
    }

    public User selectByLoginName(String LoginName) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(LoginName);
        List<User> userList = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(userList)) {
            return null;
        } else {
            return userList.get(0);
        }
    }

    /**
     * 修改密码
     * @param req
     */
    public void resetPassword(UserResetPasswordReq req){
        User user = CopyUtil.copy(req,User.class);
        userMapper.updateByPrimaryKeySelective(user);

    }

}
