package com.bmn.sdk.weixin.pay;

import com.bmn.sdk.weixin.WeixinPayServices;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.xml.sax.InputSource;


/**
 * Servlet implementation class ScreenInsertMsg
 */
@WebServlet("/payback")
public class WXPayCallback extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public WXPayCallback() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        InputStream inputStream = request.getInputStream();
        InputSource inputSource = new InputSource(inputStream);
        inputSource.setEncoding("utf-8");

        String r = WeixinPayServices.getInstance().payCallback(inputSource);

        inputStream.close();

        response.setCharacterEncoding("utf-8");
        response.setContentType("text/xml;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println(r);
            out.flush();
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
