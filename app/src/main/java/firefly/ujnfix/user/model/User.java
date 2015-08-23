package firefly.ujnfix.user.model;

import cn.bmob.v3.BmobUser;

/**
 * Created by 清晨 on 2015/7/26.
 */
public class User extends BmobUser {
    // 父类中已经存在的属性
    // private String id;
    // private String username;
    // private String password;
    // private String email;

    private boolean isFixer;
    private boolean isOnline = false;

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public boolean isFixer() {
        return isFixer;
    }

    public void setFixer(boolean fixer) {
        isFixer = fixer;
    }
}
