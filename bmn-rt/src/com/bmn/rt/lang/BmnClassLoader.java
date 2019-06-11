package com.bmn.rt.lang;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import javax.sound.midi.Instrument;

public class BmnClassLoader extends ClassLoader {

    public static void main(String[] args) {

        ClassLoader classLoader;

        Instrumentation instrumentation;

        ClassDefinition classDefinition;

        URL url = BmnClassLoader.class.getClassLoader()
            .getResource("E:\\project\\bmn\\shiyan\\bmn-rt\\rt-io.md");

        System.out.println("");

    }

}
