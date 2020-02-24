package ru.otus.pw02.controller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.pw02.service.OtpService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RestController {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final  String MESSAGE_OTP_EXPIRE_OR_INVALID = "The one-time password that you submitted is not valid";
    private static final  String MESSAGE_UNAUTHORIZED = "401 Unauthorized";
    private static final  String ATTR_OTP = "otp";

    private static final  String TEMPLATE_NAME_ERROR_PAGE = "errorPage";
    private static final  String TEMPLATE_NAME_ADMIN_PAGE = "adminPage";
    private static final  String TEMPLATE_NAME_LOGIN_PAGE = "loginPage";
    private static final  String REDIRECT_TO_HOME = "redirect:/";
    private static final  String REDIRECT_TO_LOGIN_PAGE = "redirect:/login";
    private static final  String REDIRECT_TO_ADMIN_PAGE = "redirect:/admin/page";

    @Autowired
    private OtpService otpService;


    @GetMapping(path="/")
    public String index(HttpServletRequest request) {
        if (isUnAuthorized(request)) {
            return REDIRECT_TO_LOGIN_PAGE;
        }
        return REDIRECT_TO_ADMIN_PAGE;
    }

    @GetMapping(path = "/login")
    public String showLoginPage() {
        return TEMPLATE_NAME_LOGIN_PAGE;
    }

    @GetMapping(path = "/admin/page")
    public String showAdminPage(HttpServletRequest request) {
        if (isUnAuthorized(request)) {
            request.setAttribute("errorMessage",  MESSAGE_UNAUTHORIZED);
            return TEMPLATE_NAME_ERROR_PAGE;
        }
        return TEMPLATE_NAME_ADMIN_PAGE;
    }

    @GetMapping(path = "/logout")
    public String doLogout(HttpServletRequest request) {
        request.removeAttribute(ATTR_OTP);
        request.getSession(true).invalidate();
        return REDIRECT_TO_HOME;
    }

    @PostMapping(path = "/login")
    public String doLogin(HttpServletRequest request, @RequestParam(defaultValue = "") String otp) {
        logger.debug("doLogin otp:{}",otp);
        if (otpService.getOtp(otp)>0) {
            request.getSession().setAttribute(ATTR_OTP, otp);
            return REDIRECT_TO_HOME;
        } else {
            request.setAttribute("errorMessage",  MESSAGE_OTP_EXPIRE_OR_INVALID);
            return TEMPLATE_NAME_ERROR_PAGE;
        }
    }

    @GetMapping("/error/page")
    public String errorPageView(@ModelAttribute("errorMessage") String errorMessage ) {
        return TEMPLATE_NAME_ERROR_PAGE;
    }

    private boolean isUnAuthorized(HttpServletRequest request) {
        String otp = (String) request.getSession().getAttribute(ATTR_OTP);
        return otp == null || otp.isEmpty();
    }
}
