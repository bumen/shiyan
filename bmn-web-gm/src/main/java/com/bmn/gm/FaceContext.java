package com.bmn.gm;


import com.bmn.gm.app.AuthorityApp;
import com.bmn.gm.app.LoginApp;
import com.bmn.gm.manager.ExplorerManager;
import com.bmn.gm.manager.IdGenerator;

/**
 * Created by Administrator on 2017/7/27.
 */
public class FaceContext {

    private static IdGenerator idGenerator;
    private static LoginApp loginApp;
    private static ExplorerManager explorerManager;
    private static AuthorityApp authorityApp;

    public static IdGenerator getIdGenerator() {
        return idGenerator;
    }

    public void setIdGenerator(IdGenerator idGenerator) {
        FaceContext.idGenerator = idGenerator;
    }

    public static LoginApp getLoginApp() {
        return loginApp;
    }

    public void setLoginApp(LoginApp loginApp) {
        FaceContext.loginApp = loginApp;
    }

    public static ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    public void setExplorerManager(ExplorerManager explorerManager) {
        FaceContext.explorerManager = explorerManager;
    }

    public static AuthorityApp getAuthorityApp() {
        return authorityApp;
    }

    public void setAuthorityApp(AuthorityApp authorityApp) {
        FaceContext.authorityApp = authorityApp;
    }
}
