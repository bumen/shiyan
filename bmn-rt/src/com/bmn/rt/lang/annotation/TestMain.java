package com.bmn.rt.lang.annotation;

import java.lang.annotation.*;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

/**
 * Created by Administrator on 2017/9/7.
 */
public class TestMain {
    public static void main(String[] args) {
        Annotation annotation;

        ElementType elementType;

        RetentionPolicy retentionPolicy;

        ClassDefinition classDefinition;

        Instrumentation instrumentation;

        ClassFileTransformer classFileTransformer;
    }
}
