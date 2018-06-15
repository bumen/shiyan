package com.bmn.gm.vo;

import org.springframework.util.MultiValueMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/7/24.
 */
public class LoginModel implements MultiValueMap<String, String>{
    private int loginType;
    private String loginname;
    private String loginpwd;

    private int authcode;


    public int getLoginType() {
        return loginType;
    }

    public LoginModel setLoginType(int loginType) {
        this.loginType = loginType;
        return this;
    }

    public String getLoginname() {
        return loginname;
    }

    public LoginModel setLoginname(String loginname) {
        this.loginname = loginname;
        return this;
    }

    public String getLoginpwd() {
        return loginpwd;
    }

    public LoginModel setLoginpwd(String loginpwd) {
        this.loginpwd = loginpwd;
        return this;
    }

    public int getAuthcode() {
        return authcode;
    }

    public LoginModel setAuthcode(int authcode) {
        this.authcode = authcode;
        return this;
    }

    @Override
    public String getFirst(String key) {
        return null;
    }

    @Override
    public void add(String key, String value) {

    }

    @Override
    public void set(String key, String value) {

    }

    @Override
    public void setAll(Map<String, String> values) {

    }

    @Override
    public Map<String, String> toSingleValueMap() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public List<String> get(Object key) {
        return null;
    }

    @Override
    public List<String> put(String key, List<String> value) {
        return null;
    }

    @Override
    public List<String> remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends List<String>> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Collection<List<String>> values() {
        return null;
    }

    @Override
    public Set<Entry<String, List<String>>> entrySet() {
        return null;
    }
}
