package com.bmn.rt.util;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/9/21.
 */
public class ArrayCopy {
    public static void main(String[] args) throws CloneNotSupportedException {

        Vo vo = new Vo();
        vo.code = 3;

        Vo copyVo = vo.clone();
        copyVo.code = 4;

        System.out.println(vo);
        System.out.println(copyVo);

        Vo[] aVo = {vo, copyVo};

        Vo[] copyaVo = aVo.clone();
        copyaVo[0] = new Vo();

        System.out.println(Arrays.toString(aVo));
        System.out.println(Arrays.toString(copyaVo));

        Vo[] copyaVo2 = aVo.clone();
        copyaVo2[0].code = 10;

        System.out.println(Arrays.toString(aVo));
        System.out.println(Arrays.toString(copyaVo2));

        Vo[] copyaVo3 = Arrays.copyOf(aVo, aVo.length);
        System.out.println(aVo == copyaVo3);

        copyaVo3[0] = new Vo();
        System.out.println(Arrays.toString(aVo));
        System.out.println(Arrays.toString(copyaVo3));

        copyaVo3[1].code = 11;
        System.out.println(Arrays.toString(aVo));
        System.out.println(Arrays.toString(copyaVo3));

    }
}
