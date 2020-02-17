package cn.itrip.auth.controller;

import beans.dto.Dto;
import beans.pojo.ItripUser;
import beans.vo.userinfo.ItripUserVO;
import cn.itrip.auth.service.UserService;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.ErrorCode;
import cn.itrip.common.MD5;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.regex.Pattern;

/**
 * 邮箱注册和手机注册
 */
@Controller
@RequestMapping(value = "/api")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 使用邮箱添加用户
     *
     * @param userVO
     * @return
     */
    @RequestMapping(value = "/doregister", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Dto doRegister(@RequestBody ItripUserVO userVO) {
        if (!validEmail(userVO.getUserCode())) { //判断邮箱格式
            return DtoUtil.returnFail("请使用正确的邮箱地址注册", ErrorCode.AUTH_ILLEGAL_USERCODE);
        } else {
            ItripUser user = new ItripUser();
            try {
                user.setUserCode(userVO.getUserCode());//赋值
                user.setUserPassword(userVO.getUserPassword());//赋值
                user.setUserType(0);
                user.setUserName(userVO.getUserName());
                if (null == userService.findByUsername(user.getUserCode())) {//查询用户是否已存在
                    user.setUserPassword(MD5.getMd5(user.getUserPassword(), 32));//密码加密
                    userService.itriptxCreateUser(user);//添加
                    return DtoUtil.returnSuccess();
                } else {
                    return DtoUtil.returnFail("用户已存在，注册失败", ErrorCode.AUTH_USER_ALREADY_EXISTS);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail(e.getMessage(), ErrorCode.AUTH_UNKNOWN);
            }

        }
    }

    /**
     * 使用手机注册用户
     *
     * @param userVO
     * @return
     */
    @RequestMapping(value = "/registerbyphone", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Dto registerByPhone(@RequestBody ItripUserVO userVO) {
        try {
            System.out.println("1");
            if (!validPhone(userVO.getUserCode())) //判断格式是否正确
                return DtoUtil.returnFail("请使用正确的手机号注册", ErrorCode.AUTH_ILLEGAL_USERCODE);

            ItripUser user = new ItripUser();
            user.setUserCode(userVO.getUserCode()); //赋值
            user.setUserPassword(userVO.getUserPassword());
            user.setUserType(0);
            user.setUserName(userVO.getUserName());
            if (null == userService.findByUsername(user.getUserCode())) { //判断是否已存在
                user.setUserPassword(MD5.getMd5(user.getUserPassword(), 32));//密码加密
                userService.itriptxCreateUserByPhone(user);//添加
                return DtoUtil.returnSuccess();
            } else {
                return DtoUtil.returnFail("用户已存在，注册失败", ErrorCode.AUTH_USER_ALREADY_EXISTS);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail(e.getMessage(), ErrorCode.AUTH_UNKNOWN);
        }
    }

    /**
     * 检查用户是否被注册
     *
     * @return
     */
    @RequestMapping(value = "/ckusr", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto checkUser(String name) {
        try {
            if (null == userService.findByUsername(name))//判断用户名是否可用
            {
                return DtoUtil.returnSuccess("用户名可用");
            } else  {
                return DtoUtil.returnFail("用户已存在，注册失败", ErrorCode.AUTH_USER_ALREADY_EXISTS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail(e.getMessage(), ErrorCode.AUTH_UNKNOWN);
        }
    }

    /**
     * 邮箱激活账户
     *
     * @param user
     * @param code
     * @return
     */
    @RequestMapping(value = "/activate", method = RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    public Dto activate(String user, String code) {
        try {
            if (userService.activate(user, code)) {
                return DtoUtil.returnSuccess("激活成功");
            } else {
                return DtoUtil.returnSuccess("激活失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("激活失败", ErrorCode.AUTH_ACTIVATE_FAILED);
        }
    }

    /**
     * 手机激活账户
     *
     * @param user
     * @param code
     * @return
     */
    @RequestMapping(value = "/validatephone", method = RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    public Dto validatePhone(String user, String code) {
        try {
            if (userService.validatePhone(user, code)) {
                return DtoUtil.returnSuccess("验证成功");
            } else {
                return DtoUtil.returnSuccess("验证失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("验证失败", ErrorCode.AUTH_ACTIVATE_FAILED);
        }
    }

    /**
     * 合法E-mail地址：
     * 1. 必须包含一个并且只有一个符号“@”
     * 2. 第一个字符不得是“@”或者“.”
     * 3. 不允许出现“@.”或者.@
     * 4. 结尾不得是字符“@”或者“.”
     * 5. 允许“@”前的字符中出现“＋”
     * 6. 不允许“＋”在最前面，或者“＋@”
     */
    private boolean validEmail(String email) {

        String regex = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        return Pattern.compile(regex).matcher(email).find();
    }

    /**
     * 验证是否合法的手机号
     *
     * @param phone
     * @return
     */
    private boolean validPhone(String phone) {
        String regex = "^1[3578]{1}\\d{9}$";
        return Pattern.compile(regex).matcher(phone).find();
    }
}
