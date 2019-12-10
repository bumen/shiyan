package com.bmn.ddd.structure.facade;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Client {

    private FacadeClient client = new FacadeClient();

    public void active() {
        client.active();
    }

    public void deactivate() {
        client.deactivate();
    }

    public static void main(String[] args) {
        Client client = new Client();

        client.active();

        client.deactivate();

    }
}
