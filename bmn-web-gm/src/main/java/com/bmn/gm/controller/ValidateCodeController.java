package com.bmn.gm.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ValidateCodeController {
    private static final Logger logger = LoggerFactory.getLogger(ValidateCodeController.class);

    private final int width = 90;
    private final int height = 33;

    private int codeCount = 4;
    private int lineCount = 50;

    private final char[] codeSequence = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    @RequestMapping("verify")
    @ResponseBody
    public void image(HttpSession session, HttpServletResponse response) {
        // 图像buffer
        BufferedImage buff = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        String code = create(buff);

        if(logger.isDebugEnabled()) {
            logger.debug("create one authcode : {}", code);
        }

        response.setContentType("image/png");
        response.setHeader("pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        OutputStream os = null;
        try {
            os = response.getOutputStream();
            ImageIO.write(buff, "png", os);

           /* long loginId = SessionUtil.getAttribute(session, SessionKey.LOGIN_ID, 0l);
            LoginConnect connect = FaceContext.getLoginApp().getLoginConnect(loginId);
            if(connect != null) {
                connect.setAuthCode(code);
                FaceContext.getLoginApp().saveLoginConnect(loginId, connect);
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String create(BufferedImage buffImg) {
        int x = 0, fontHeight=0, codeY=0;
        int red = 0, green = 0, blue = 0;

        x = width / (codeCount +2);//每个字符的宽度
        fontHeight = height - 2;//字体的高度
        codeY = height - 4;

        Graphics2D g = buffImg.createGraphics();
        // 生成随机数
        Random random = new Random();
        // 将图像填充为白色
        g.setColor(new Color(157, 157, 157));
        g.fillRect(0, 0, width, height);

        // 创建字体
        Font font = new Font("Atlantic Inline", Font.PLAIN, fontHeight);
        g.setFont(font);

        // randomCode记录随机产生的验证码
        StringBuffer randomCode = new StringBuffer();
        // 随机产生codeCount个字符的验证码。
        for (int i = 0; i < codeCount; i++) {
            String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
            // 产生随机的颜色值，让输出的每个字符的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            g.setColor(new Color(45, 45, 45));
            g.drawString(strRand, (i + 1) * x, codeY);
            // 将产生的四个随机数组合在一起。
            randomCode.append(strRand);
        }

        for (int i = 0; i < lineCount; i++) {
            int xs = random.nextInt(width);
            int ys = random.nextInt(height);
            int xe = xs+random.nextInt(width/8);
            int ye = ys+random.nextInt(height/8);
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawLine(xs, ys, xe, ye);
        }

        // 将四位数字的验证码保存到Session中。
        return randomCode.toString();
    }


}
