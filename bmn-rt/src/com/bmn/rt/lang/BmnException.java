package com.bmn.rt.lang;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class BmnException {


    public void readFile(String name) {

        try(BufferedReader reader = new BufferedReader(new FileReader(name))) {

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
