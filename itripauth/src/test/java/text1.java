import beans.pojo.ItripUser;
import cn.itrip.auth.service.MailService;
import cn.itrip.auth.service.SmsService;
import cn.itrip.auth.service.TokenService;
import cn.itrip.auth.service.UserService;
import cn.itrip.common.MD5;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

public class text1 {
    public static void main(String[] args) {
//        ApplicationContext ctx = new ClassPathXmlApplicationContext(
//                "applicationContext.xml");
//        SmsService tokenService = ctx.getBean(SmsService.class);
//        String code=String.valueOf(MD5.getRandomCode());//生成验证码
//        ItripUser user=new ItripUser();
//        user.setUserName("1");
//        user.setUserCode("13469263167");
//        user.setUserPassword("123456");
//        user.setUserType(0);
//        try {
//            tokenService.send("13469263167", "1", new String[]{code,String.valueOf(1)});//使用号码1发送
//        }catch (Exception e){
//            e.printStackTrace();;
//        }
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                "applicationContext.xml");
        MailService mailService = ctx.getBean(MailService.class);
        mailService.sendActivationMail("2514579443@qq.com", MD5.getMd5(new Date().toLocaleString(), 32));

    }
}
