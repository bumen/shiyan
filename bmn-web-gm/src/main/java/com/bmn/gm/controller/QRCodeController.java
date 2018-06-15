package com.bmn.gm.controller;

import com.bmn.gm.FaceContext;
import com.bmn.gm.doman.LoginConnect;
import com.bmn.gm.util.SessionUtil;
import com.bmn.gm.vo.JsonResult;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/26.
 */
@Controller
public class QRCodeController {


    @RequestMapping("qrcode/show")
    public void show(@RequestParam Integer size, HttpSession session, HttpServletResponse response) {

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 0);
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().
                    encode("hello", BarcodeFormat.QR_CODE, size, size, hints);

            response.setContentType("image/png");

           // MatrixToImageWriter.writeToStream(bitMatrix, "png", response.getOutputStream());

            long loginId = SessionUtil.getLoginId(session);
            LoginConnect connect = FaceContext.getLoginApp().getLoginConnect(loginId);
            connect.setQrcodeRefreshTime(System.currentTimeMillis());
            connect.setQrcode(201);     //等待扫码
            FaceContext.getLoginApp().saveLoginConnect(loginId, connect);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private static final int validTime = 1 * 60 * 1000;  //1分钟
    @RequestMapping("qrcode/check")
    @ResponseBody
    public String check(HttpSession session) {
        long loginId = SessionUtil.getLoginId(session);
        LoginConnect connect = FaceContext.getLoginApp().getLoginConnect(loginId);
        long lastRefreshTime = connect.getQrcodeRefreshTime();

        int code = connect.getQrcode();
        if(code == 201) {
            if (lastRefreshTime + validTime < System.currentTimeMillis()) {
                connect.setQrcode(203);       // //手机1分钟内没有扫码，二维码失效
                code = 203;
                FaceContext.getLoginApp().saveLoginConnect(loginId, connect);
            }
        }

        JsonResult result = new JsonResult();
        result.putValue("code", code);
        return result.build();
    }

}
