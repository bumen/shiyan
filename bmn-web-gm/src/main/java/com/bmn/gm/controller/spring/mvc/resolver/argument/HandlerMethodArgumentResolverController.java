package com.bmn.gm.controller.spring.mvc.resolver.argument;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.security.Principal;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * mvc 参数解析
 * Created by Administrator on 2017/8/3.
 */
@Controller
@RequestMapping("/spring/mvc/servlet/argumentResolver")
public class HandlerMethodArgumentResolverController {

    /**
     * {@linkplain org.springframework.web.method.annotation.MapMethodProcessor}
     * support : Map
     */
    public void MapMethodProcessor(Map args) {

    }

    /**
     * {@linkplain org.springframework.web.servlet.mvc.method.annotation.PathVariableMapMethodArgumentResolver}
     * support : parameter annotation PathVariable  参数是 Map
     */
    public void PathVariableMapMethodArgumentResolver(@PathVariable Map args) {

    }
    /**
     * {@linkplain org.springframework.web.method.annotation.ErrorsMethodArgumentResolver}
     * support : Errors
     */
    public void ErrorsMethodArgumentResolver(Errors errors) {

    }
    /**
     * {@linkplain org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver}
     * support : parameter annotation RequestHeader  参数不是Map
     */
    public void RequestHeaderMethodArgumentResolver(@RequestHeader String name) {

    }
    /**
     * {@linkplain org.springframework.web.method.annotation.RequestParamMethodArgumentResolver}
     * support :    parameter annotation RequestParam  || MultipartFile || javax.servlet.http.Part
     */
    public void RequestParamMethodArgumentResolver(@RequestParam("name") Map args, @RequestParam Object other, MultipartFile file, Part part, int code, boolean read) {

    }
    /**
     * {@linkplain org.springframework.web.servlet.mvc.method.annotation.ServletCookieValueMethodArgumentResolver}
     * support : parameter annotation CookieValue
     */
    public void ServletCookieValueMethodArgumentResolver(@CookieValue String name) {

    }
    /**
     * {@linkplain org.springframework.web.servlet.mvc.method.annotation.MatrixVariableMethodArgumentResolver}
     * support : parameter annotation MatrixVariable
     */
    public void MatrixVariableMethodArgumentResolver(@MatrixVariable("name") Map args, @MatrixVariable Object other) {

    }
    /**
     * {@linkplain org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver}
     * support : parameter annotation Value
     */
    public void ExpressionValueMethodArgumentResolver(@Value("name${pwd}") String name) {

    }
    /**
     * {@linkplain org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver}
     * support : parameter annotation PathVariable
     */
    public void PathVariableMethodArgumentResolver(@PathVariable("name") Map args, @PathVariable Object other) {

    }
    /**
     * {@linkplain org.springframework.web.method.annotation.RequestHeaderMapMethodArgumentResolver}
     * support : parameter annotation RequestHeader 参数是Map
     */
    public void RequestHeaderMapMethodArgumentResolver(@RequestHeader Map args) {

    }
    /**
     * {@linkplain org.springframework.web.servlet.mvc.method.annotation.ServletResponseMethodArgumentResolver}
     * support : ServletResponse || OutputStream || Writer
     */
    public void ServletResponseMethodArgumentResolver(ServletResponse response, OutputStream outputStream, Writer writer) {

    }
    /**
     * {@linkplain org.springframework.web.method.annotation.ModelMethodProcessor}
     * support : Model
     */
    public void ModelMethodProcessor(Model model) {

    }
    /**
     * {@linkplain org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor}
     * support : parameter annotation ModelAttribute
     */
    /**
     * {@linkplain org.springframework.web.method.annotation.SessionStatusMethodArgumentResolver}
     * support : SessionStatus
     */
    public void SessionStatusMethodArgumentResolver(SessionStatus sessionStatus) {

    }
    /**
     * {@linkplain org.springframework.web.method.annotation.RequestParamMapMethodArgumentResolver}
     * support : parameter annotation RequestParam
     */
    public void RequestParamMapMethodArgumentResolver(@RequestParam Map args) {

    }
    /**
     * {@linkplain org.springframework.web.servlet.mvc.method.annotation.RequestPartMethodArgumentResolver}
     * support : parameter annotation RequestPart || MultipartFile || javax.servlet.http.Part
     */
    public void RequestPartMethodArgumentResolver(@RequestPart String name, MultipartFile file, Part part) {

    }
    /**
     * {@linkplain org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor}
     * support : parameter annotation RequestBody
     */
    /**
     * {@linkplain org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor}
     * support : HttpEntity
     */
    public void HttpEntityMethodProcessor(HttpEntity httpEntity) {

    }
    /**
     * {@linkplain org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter}
     * support : Map
     */
    public void ServletWebArgumentResolverAdapter() {

    }
    /**
     * {@linkplain org.springframework.web.servlet.mvc.method.annotation.UriComponentsBuilderMethodArgumentResolver}
     * support : UriComponentsBuilder
     */
    public void UriComponentsBuilderMethodArgumentResolver(UriComponentsBuilder uriComponentsBuilder) {

    }
    /**
     * {@linkplain org.springframework.web.servlet.mvc.method.annotation.ServletRequestMethodArgumentResolver}
     * support : WebRequest || ServletRequest || MultipartRequest
     * || HttpSession || Principal || Locale || InputStream || Reader
     */
    public void ServletRequestMethodArgumentResolver(WebRequest webRequest, ServletRequest servletRequest,
                                                     MultipartRequest multipartRequest, HttpSession session,
                                                     Principal principal, Locale locale,
                                                     InputStream inputStream, Reader reader) {

    }
    /**
     * {@linkplain org.springframework.web.method.support.HandlerMethodArgumentResolverComposite}
     * support :
     */
    public void HandlerMethodArgumentResolverComposite() {

    }
    /**
     * {@linkplain org.springframework.web.servlet.mvc.method.annotation.MatrixVariableMapMethodArgumentResolver}
     * support :  parameter annotation MatrixVariable 参数Map
     */
    public void MatrixVariableMapMethodArgumentResolver(@MatrixVariable Map args) {

    }
    /**
     * {@linkplain org.springframework.web.servlet.mvc.method.annotation.RedirectAttributesMethodArgumentResolver}
     * support : RedirectAttributes
     */
    public void RedirectAttributesMethodArgumentResolver(RedirectAttributes redirectAttributes) {

    }


}
