package firefly.ujnfix.user.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Phineas on 2015/7/26.
 */
public class FixEvent extends BmobObject {
    private Fixer solvedFixer;
    private User   submitUser;

    private String title;
    private String location;
    private String fixScene;
    private String fixType;
    private String details;

    private BmobFile imageFile1;
    private BmobFile imageFile2;
    private BmobFile imageFile3;


    private int solvedState;     //三个状态：1.未接单；2.已接单；3.已解决

    private int vaAttitude;//态度评分
    private int vaSpeed;//速度评分
    private int vaLevel;//程度评分



    public int getVaAttitude() {
        return vaAttitude;
    }

    public void setVaAttitude(int vaAttitude) {
        this.vaAttitude = vaAttitude;
    }

    public int getVaSpeed() {
        return vaSpeed;
    }

    public void setVaSpeed(int vaSpeed) {
        this.vaSpeed = vaSpeed;
    }

    public int getVaLevel() {
        return vaLevel;
    }

    public void setVaLevel(int vaLevel) {
        this.vaLevel = vaLevel;
    }

    public Fixer getSolvedFixer() {
        return solvedFixer;
    }

    public void setSolvedFixer(Fixer solvedFixer) {
        this.solvedFixer = solvedFixer;
    }


    public User getSubmitUser() {
        return submitUser;
    }

    public void setSubmitUser(User submitUser) {
        this.submitUser = submitUser;
    }



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFixScene() {
        return fixScene;
    }

    public void setFixScene(String fixScene) {
        this.fixScene = fixScene;
    }

    public String getFixType() {
        return fixType;
    }

    public void setFixType(String fixType) {
        this.fixType = fixType;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getSolvedState() {
        return solvedState;
    }

    public void setSolvedState(int solvedState) {
        this.solvedState = solvedState;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BmobFile getImageFile1() {
        return imageFile1;
    }

    public void setImageFile1(BmobFile imageFile1) {
        this.imageFile1 = imageFile1;
    }

    public BmobFile getImageFile2() {
        return imageFile2;
    }

    public void setImageFile2(BmobFile imageFile2) {
        this.imageFile2 = imageFile2;
    }

    public BmobFile getImageFile3() {
        return imageFile3;
    }

    public void setImageFile3(BmobFile imageFile3) {
        this.imageFile3 = imageFile3;
    }
}
