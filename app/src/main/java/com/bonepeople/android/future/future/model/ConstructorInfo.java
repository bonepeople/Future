package com.bonepeople.android.future.future.model;

import org.json.JSONObject;

/**
 * 施工队基本信息
 * <p>
 * 主要用于直选施工队界面
 * <p>
 * Created by bonepeople on 2018/1/22.
 */

public class ConstructorInfo {
    private String consId;// 施工队ID "6b4f656f34954855b7bcada1d99f2ebd"
    private String consName;// 施工队名称 "美尔雅装饰"
    private String headerIcon;// 用户头像 "http://www.shownest.com/_resources/upload/c/1459265601797.jpg"
    private double gradePraise;// 用户评分 102.30000305175781
    private int nativePlace;// 工长籍贯 1011
    private String nativePalceName;// 工长籍贯名称 "江苏省"
    private int workYear;// 从业经验(年) 27
    private int peopleNum;// 工队人数 20
    private boolean authorise;// 认证结果 true-已认证，false-未认证
    private boolean warranty;// 质保金 true-有质保金，false-无质保金
    private int bookNum;// 预约个数
    private int commentNum;// 评论个数
    private int jobNum;// 工地个数

    public ConstructorInfo(JSONObject object) {
        consId = object.optString("consId");
        consName = object.optString("consName");
        headerIcon = object.optString("headerIcon", "header_default.png");
        gradePraise = object.optDouble("gradePraise");
        nativePlace = object.optInt("nativePlace", -1);
        nativePalceName = object.optString("nativePalceName", "未知");
        workYear = object.optInt("workYear", 1);
        peopleNum = object.optInt("peopleNum", 1);
        authorise = object.optBoolean("authorise");
        warranty = object.optBoolean("warranty");
        bookNum = object.optInt("bookNum");
        commentNum = object.optInt("commentNum");
        jobNum = object.optInt("jobNum");
    }

    public String getConsId() {
        return consId;
    }

    public String getConsName() {
        return consName;
    }

    public String getHeaderIcon() {
        return headerIcon;
    }

    public double getGradePraise() {
        return gradePraise;
    }

    public int getNativePlace() {
        return nativePlace;
    }

    public String getNativePalceName() {
        return nativePalceName;
    }

    public int getWorkYear() {
        return workYear;
    }

    public int getPeopleNum() {
        return peopleNum;
    }

    public boolean isAuthorise() {
        return authorise;
    }

    public boolean isWarranty() {
        return warranty;
    }

    public int getBookNum() {
        return bookNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public int getJobNum() {
        return jobNum;
    }
}
