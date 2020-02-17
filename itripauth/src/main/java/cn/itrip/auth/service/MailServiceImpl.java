package cn.itrip.auth.service;

import cn.itrip.common.RedisAPI;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 发送邮件的实现
 */
@Service("mailService")
public class MailServiceImpl implements MailService {

    @Resource
    private SimpleMailMessage activationMailMessage;
    @Resource
    private MailSender mailSender;
    @Resource
    private RedisAPI redisAPI;

    @Override
    public void sendActivationMail(String mailTo, String activationCode) {
        activationMailMessage.setTo(mailTo);//地址
        activationMailMessage.setText("注册邮箱："+mailTo +"  激活码："+activationCode);//发送邮箱的内容
        mailSender.send(activationMailMessage);//发送
        this.saveActivationInfo("activation:"+mailTo,activationCode);//存储到内存中
    }
    private void saveActivationInfo(String key,String value){
        redisAPI.set(key,30*60,value);//存入Redis缓存
    }
}
