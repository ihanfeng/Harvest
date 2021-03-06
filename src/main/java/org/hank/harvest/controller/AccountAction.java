package org.hank.harvest.controller;

import org.hank.harvest.domain.Authority;
import org.hank.harvest.domain.User;
import org.hank.harvest.service.AuthorityService;
import org.hank.harvest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Administrator on 2016/4/23.
 */
@Controller
@RequestMapping("/action")
public class AccountAction {

    private UserService userService;
    private AuthorityService authorityService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAuthorityService(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String doLogin(@ModelAttribute User user,
                          RedirectAttributes redirect, HttpSession httpSession, HttpServletResponse response) {
        List<User> userList = userService.findIndirect(user);
        User currentUser = userList.size() > 0 ? userList.get(0) : null;
        if (currentUser == null) {
            redirect.addFlashAttribute("loginError", "登录失败：邮箱地址或者密码不正确！");
            redirect.addFlashAttribute("email", user.getEmail());
            redirect.addFlashAttribute("password", user.getPassword());
            return "redirect:/login";
        } else {
            httpSession.setAttribute("currentUser", currentUser);
            Cookie cookie = new Cookie("currentUserID", currentUser.getId().toString());
            cookie.setPath("/");
            response.addCookie(cookie);
            return "redirect:/management/password";
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String doLogout(HttpSession httpSession) {
        httpSession.removeAttribute("currentUser");
        return "redirect:/index";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String doRegister(@ModelAttribute User user, Integer type,
                             RedirectAttributes redirect, HttpSession httpSession, HttpServletResponse response) {
        Authority authority = authorityService.findOne(type);
        user.setAuthority(authority);
        User currentUser = userService.saveOne(user);
        httpSession.setAttribute("currentUser", currentUser);
        Cookie cookie = new Cookie("currentUserID", currentUser.getId().toString());
        cookie.setPath("/");
        response.addCookie(cookie);
        redirect.addFlashAttribute("hasRegistered", true);
        return "redirect:/register";
    }

}
