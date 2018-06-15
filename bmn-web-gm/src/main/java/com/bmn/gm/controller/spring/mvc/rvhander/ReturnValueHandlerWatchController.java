package com.bmn.gm.controller.spring.mvc.rvhander;

import org.springframework.stereotype.Controller;

/**
 * mvc 返回值解析
 * Created by Administrator on 2017/8/3.
 */
@Controller
public class ReturnValueHandlerWatchController {

    /**
     * {@linkplain org.springframework.web.servlet.mvc.method.annotation.ViewNameMethodReturnValueHandler}
     *
     * supports : void || String
     */
    public void viewNameMethodReturnValueHandlerVoid() {

    }

    /**
     * {@linkplain org.springframework.web.servlet.mvc.method.annotation.ViewNameMethodReturnValueHandler}
     *
     * supports : void || String
     */
    public String viewNameMethodReturnValueHandlerString() {

        return "good";
    }

    /**
     * {@linkplain org.springframework.web.method.annotation.MapMethodProcessor}
     *
     * support : Map
     */
    public void mapMethodProcessor() {

    }
    /**
     * {@linkplain org.springframework.web.servlet.mvc.method.annotation.ViewMethodReturnValueHandler}
     *
     * support : View
     */
    public void viewMethodReturnValueHandler() {

    }
    /**
     * {@linkplain org.springframework.web.servlet.mvc.method.annotation.ModelAndViewMethodReturnValueHandler}
     *
     * support : ModelAndView
     */
    public void modelAndViewMethodReturnValueHandler() {

    }
    /**
     * {@linkplain org.springframework.web.servlet.mvc.method.annotation.ModelAndViewResolverMethodReturnValueHandler}
     *
     * support : all
     */
    public void modelAndViewResolverMethodReturnValueHandler() {

    }
    /**
     * {@linkplain org.springframework.web.servlet.mvc.method.annotation.DeferredResultMethodReturnValueHandler}
     *
     * support : DeferredResult
     */
    public void deferredResultMethodReturnValueHandler() {

    }
    /**
     * {@linkplain org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite}
     *
     * composite
     */
    public void handlerMethodReturnValueHandlerComposite() {

    }
    /**
     * {@linkplain org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor}
     *
     * support : annotation @ResponseBody
     */
    public void requestResponseBodyMethodProcessor() {

    }
    /**
     * {@linkplain org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor}
     *
     * support : HttpEntity
     */
    public void httpEntityMethodProcessor() {

    }
    /**
     * {@linkplain org.springframework.web.servlet.mvc.method.annotation.CallableMethodReturnValueHandler}
     *
     * support : Callable
     */
    public void callableMethodReturnValueHandler() {

    }
    /**
     * {@linkplain org.springframework.web.method.annotation.ModelMethodProcessor}
     *
     * support : Model
     */
    public void modelMethodProcessor() {

    }
    /**
     * {@linkplain org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor}
     *
     * support : annotation ModelAttribute
     */
    public void ServletModelAttributeMethodProcessor() {

    }
    /**
     * {@linkplain org.springframework.web.servlet.mvc.method.annotation.AsyncTaskMethodReturnValueHandler}
     *
     * support : WebAsyncTask
     */
    public void asyncTaskMethodReturnValueHandler() {

    }

}
