package com.wangxiaoqi.crm.settings.web.controller;

import com.wangxiaoqi.crm.commons.contants.Contants;
import com.wangxiaoqi.crm.commons.domain.ReturnObj;
import com.wangxiaoqi.crm.commons.utils.DateUtils;
import com.wangxiaoqi.crm.settings.domain.User;
import com.wangxiaoqi.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        System.out.println("toLogin执行了");
        return "settings/qx/user/login";
    }

    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response, HttpSession session){

        String ip = request.getRemoteAddr();
        System.out.println("Ip地址为："+ip);

        Map<String,Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);

        User user = userService.selectUserByLoginActAndPwd(map);
        ReturnObj returnObj = new ReturnObj();

        if (user == null){
            //登录失败，用户名或密码错误
            returnObj.setCode(Contants.RETURN_OBJ_FAIL);
            returnObj.setMessage("登录失败，账户或密码错误！");
        }else { //用户名和密码正确，但仍需验证其他条件
            //验证账号是否过期

            String nowDate = DateUtils.formateDateTime(new Date());

            //如果当前时间比过期时间大，证明账号已过期
            if (nowDate.compareTo(user.getExpireTime())>0){
                //登录失败，账号已过期
                returnObj.setCode(Contants.RETURN_OBJ_FAIL);
                returnObj.setMessage("登录失败，账号已过期");

            }else if ("0".equals(user.getLockState())){
                //登录失败，账号已锁定
                returnObj.setCode(Contants.RETURN_OBJ_FAIL);
                returnObj.setMessage("登录失败，账号已锁定");

            }else if (!user.getAllowIps().contains(request.getRemoteAddr())){
                //登录失败，IP地址受限
                returnObj.setCode(Contants.RETURN_OBJ_FAIL);
                returnObj.setMessage("登录失败，IP地址受限");

            }else {
                //登录成功
                returnObj.setCode(Contants.RETURN_OBJ_SUCCESS);
                //将当前用户放入到session作用域中
                session.setAttribute(Contants.SESSION_USER,user);

                //如果选中记住密码，则往cookie中写入账户密码
                if ("true".equals(isRemPwd)){

                    Cookie c1 = new Cookie("loginAct",user.getLoginAct());
                    c1.setMaxAge(10*24*60*60);
                    c1.setPath("/");
                    response.addCookie(c1);

                    Cookie c2 = new Cookie("loginPwd",user.getLoginPwd());
                    c2.setMaxAge(10*24*60*60);
                    c2.setPath("/");
                    response.addCookie(c2);

                //如果不选中记住密码，则销毁cookie
                }else {
                    Cookie c1 = new Cookie("loginAct","1");
                    c1.setMaxAge(0);
                    c1.setPath("/");
                    response.addCookie(c1);

                    Cookie c2 = new Cookie("loginPwd","1");
                    c2.setMaxAge(0);
                    c2.setPath("/");
                    response.addCookie(c2);
                }

            }
        }
        return returnObj;
    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response, HttpSession session){
        //安全退出，删除cookie、销毁session
        Cookie c1 = new Cookie("loginAct","1");
        c1.setMaxAge(0);
        c1.setPath("/");
        response.addCookie(c1);

        Cookie c2 = new Cookie("loginPwd","1");
        c2.setMaxAge(0);
        c2.setPath("/");
        response.addCookie(c2);


        //销毁session
        session.invalidate();

        return "redirect:/";
    }

}
