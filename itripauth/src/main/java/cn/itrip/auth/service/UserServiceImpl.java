package cn.itrip.auth.service;

import beans.pojo.ItripUser;
import cn.itrip.auth.exception.UserLoginFailedException;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.MD5;
import cn.itrip.common.RedisAPI;
import dao.user.ItripUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理接口的实现
 *
 * @author hduser
 */
@Service("useService")
public class UserServiceImpl implements UserService {

    @Resource
    private ItripUserMapper itripUserMapper;
    @Resource
    private MailService mailService; //邮件发送接口

    @Resource
    private RedisAPI redisAPI; //缓存
    @Resource
    private SmsService smsService; //手机短信发送接口

    /**
     * 添加用户信息
     *
     * @param user
     */
    @Override
    public void itriptxCreateUser(ItripUser user) throws Exception {
        String activationCode = MD5.getMd5(new Date().toLocaleString(), 32);//生成激活码
        //user.getUserCode()用户编号
        mailService.sendActivationMail(user.getUserCode(), activationCode);//发送激活码
        itripUserMapper.insertItripUser(user);

    }

    /**
     * 修改用户
     *
     * @param user
     * @throws Exception
     */
    @Override
    public void updateUser(ItripUser user) throws Exception {

    }

    /**
     * 删除用户
     *
     * @param userId
     * @throws Exception
     */
    @Override
    public void deleteUser(Long userId) throws Exception {

    }

    /**
     * 修改密码
     *
     * @param userId
     * @param newPassword
     * @throws Exception
     */
    @Override
    public void changePassword(Long userId, String newPassword) throws Exception {

    }

    @Override
    public ItripUser findOne(Long userId) throws Exception {
        return null;
    }

    @Override
    public List<ItripUser> findAll() throws Exception {
        return null;
    }

    @Override
    public ItripUser findByUsername(String username) throws Exception {
        Map<String, Object> param = new HashMap();
        param.put("userCode", username);
        List<ItripUser> list = itripUserMapper.getItripUserListByMap(param);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 登录
     *
     * @param name
     * @param password
     * @return
     * @throws Exception
     */
    @Override
    public ItripUser login(String name, String password) throws Exception {
        ItripUser user = this.findByUsername(name);
        if (null != user && user.getUserPassword().equals(password)) {
            if (user.getActivated() != 1) {
                throw new UserLoginFailedException("用户未激活");
            }
            return user;
        } else {
            return null;
        }

    }

    /**
     *
     * @param email 用户注册邮箱
     * @param code 激活码
     * @return
     * @throws Exception
     */
    @Override
    public boolean activate(String email, String code) throws Exception {
        String key="activation:"+email;
        if(redisAPI.exist(key))
            if(redisAPI.get(key).equals(code)){
                ItripUser user=this.findByUsername(email);
                if(EmptyUtils.isNotEmpty(user))//判断是否为空
                {
                   //logger.debug("激活用户"+email);
                    user.setActivated(1);//激活用户0未激活1激活
                    user.setUserType(0);//自注册用户
                    user.setFlatID(user.getId());
                    itripUserMapper.updateItripUser(user);
                    return true;
                }
            }

        return false;
    }

    @Override
    public void itriptxCreateUserByPhone(ItripUser user) throws Exception {
        //发送短信验证码
        String code=String.valueOf(MD5.getRandomCode());//生成验证码
        smsService.send(user.getUserCode(), "1", new String[]{code,String.valueOf(1)});//使用号码1发送
        //缓存验证码
        String key="activation:"+user.getUserCode();
        redisAPI.set(key, 1*60, code);
        //保存用户信息
        itripUserMapper.insertItripUser(user);
    }

    /**
     * 短信注册激活
     * @param phoneNumber
     * @param code
     * @return
     * @throws Exception
     */
    @Override
    public boolean validatePhone(String phoneNumber, String code) throws Exception {
        String key="activation:"+phoneNumber;
        if(redisAPI.exist(key))
            if(redisAPI.get(key).equals(code)){
                ItripUser user=this.findByUsername(phoneNumber);
                if(EmptyUtils.isNotEmpty(user))
                {
                   // logger.debug("用户手机验证已通过："+phoneNumber);
                    user.setActivated(1);//激活用户
                    user.setUserType(0);//自注册用户
                    user.setFlatID(user.getId());
                    itripUserMapper.updateItripUser(user);
                    return true;
                }
            }
        return false;
    }
}
