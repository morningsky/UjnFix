package firefly.ujnfix.user.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Phineas on 2015/8/6.
 */
public class FeedBack extends BmobObject{
    private User submitUser;
    private String title;
    private String details;

    public User getSubmitUser() {
        return submitUser;
    }

    public void setSubmitUser(User submitUser) {
        this.submitUser = submitUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
