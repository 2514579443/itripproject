package cn.itrip.auth.controller;

import beans.dto.Dto;
import beans.pojo.ItripUser;
import beans.vo.ItripTokenVO;
import cn.itrip.auth.exception.UserLoginFailedException;
import cn.itrip.auth.service.TokenService;
import cn.itrip.auth.service.UserService;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.ErrorCode;
import cn.itrip.common.MD5;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

/**
 * 登入和注销
 */
@Controller
@RequestMapping(value = "/api")
public class LoginController {
    @Resource
    private UserService userService;

    @Resource
    private TokenService tokenService;

    @RequestMapping(value = "/dologin", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Dto dologin(String name, String password, HttpServletRequest request) {

        if (!EmptyUtils.isEmpty(name) && !EmptyUtils.isEmpty(password)) {//判断非空
            ItripUser user = null;
            try {
                user = userService.login(name.trim(), MD5.getMd5(password.trim(), 32));//登入
            } catch (UserLoginFailedException e) {
                return DtoUtil.returnFail(e.getMessage(), ErrorCode.AUTH_AUTHENTICATION_FAILED);
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail(e.getMessage(), ErrorCode.AUTH_UNKNOWN);
            }
            if (EmptyUtils.isNotEmpty(user)) {//判断是否为空
                String token = tokenService.generateToken(
                        request.getHeader("user-agent"), user);
                tokenService.save(token, user);//保存用户到redis内存中

                //返回ItripTokenVO
                ItripTokenVO tokenVO = new ItripTokenVO(token,
                        Calendar.getInstance().getTimeInMillis() + TokenService.SESSION_TIMEOUT * 1000,//2h有效期
                        Calendar.getInstance().getTimeInMillis());//返回前端

                return DtoUtil.returnDataSuccess(tokenVO);
            } else {
                return DtoUtil.returnFail("用户名密码错误", ErrorCode.AUTH_AUTHENTICATION_FAILED);
            }
        } else {
            return DtoUtil.returnFail("参数错误！检查提交的参数名称是否正确。", ErrorCode.AUTH_PARAMETER_ERROR);
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET, produces = "application/json", headers = "token")
    @ResponseBody
    public Dto logout(HttpServletRequest request) {
        //验证是否存在该用户
        String token = request.getHeader("token");
        if (!tokenService.validate(request.getHeader("user-agent"), token))
            return DtoUtil.returnFail("token无效", ErrorCode.AUTH_TOKEN_INVALID);
        //删除token和信息
        try {
            tokenService.delete(token);//注销
            return DtoUtil.returnSuccess("注销成功");
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("注销失败", ErrorCode.AUTH_UNKNOWN);
        }

    }
}
