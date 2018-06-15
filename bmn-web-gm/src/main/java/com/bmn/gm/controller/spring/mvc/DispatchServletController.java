package com.bmn.gm.controller.spring.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/8/3.
 */
@Controller
@RequestMapping("/spring/mvc/servlet")
public class DispatchServletController {


    /**
     * @see #jump
     * @return
     */
    @RequestMapping("/show")
    public String show() {

        /**
         * 1. @see #processRequest()
         *      看Local
         *      看RequestAttributes
         *      看WebAsyncManager
         *      @see #doService()
         * 2. @see #doService() 设置request属性（spring处理相关的东西）
         *      看FlashMap ( 有input 和output )
         *      @see #doDispatch()
         *
         * 3. @see #doDispatch()
         *      看Multipart
         *      看HandlerExecutionChain
         *      看HandlerMapping
         *      看HandlerAdapter
         *      看ServletWebRequest
         *      看WebDataBinderFactory
         *      看ModelFactory
         *      看ServletInvocableHandlerMethod
         *      看ModelAndViewContainer
         * 4.
         */
        return "spring/mvc/show";
    }

    private void jump() {

    }
}
