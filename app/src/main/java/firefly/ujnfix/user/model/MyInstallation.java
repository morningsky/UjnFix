package firefly.ujnfix.user.model;

import android.content.Context;

import cn.bmob.v3.BmobInstallation;

/**
 * Created by 清晨 on 2015/8/4.
 */
public class MyInstallation extends BmobInstallation {
    private String username;
    private boolean fixer;

    public MyInstallation(Context context) {
        super(context);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isFixer() {
        return fixer;
    }

    public void setFixer(boolean fixer) {
        this.fixer = fixer;
    }

}
