package com.bmn.ddd.structure.bridge.peer;

import java.awt.Button;
import java.awt.peer.ButtonPeer;

import javax.swing.JMenu;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Client {

    public static void main(String[] args) {
        Button button = new Button();
        button.setLabel("windows");

        ButtonPeer buttonPeer;

        JMenu menu = new JMenu();
    }
}
