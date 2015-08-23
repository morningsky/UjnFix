package firefly.ujnfix.user.model;

import cn.bmob.v3.BmobUser;

/**
 * Created by 清晨 on 2015/7/26.
 */
public class Fixer extends BmobUser {
    // 父类中已经存在的属性
    // private String id;
    // private String username;
    // private String password;
    // private String email;


    private boolean isFixer;//是否是维修员
    private boolean onDuty = true;//是否在线
    private String     jobNumber;//工号
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

    public void setFixer(boolean isFixer) {
        this.isFixer = isFixer;
    }

    public boolean isOnDuty() {
        return onDuty;
    }

    public void setOnDuty(boolean isOnDuty) {
        this.onDuty = isOnDuty;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }
}
