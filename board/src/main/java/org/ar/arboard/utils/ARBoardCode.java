package org.ar.arboard.utils;

/**
 * Created by liuxiaozhong on 2018/9/29.
 */
public enum ARBoardCode {
    ParameterEmpty(3000),//参数为空
    ParameterError(3009),//参数为空
    NoNet(30001),//没网络
    ImageLoaderIsNull(40000),//图片加载器为空
    SessionPastDue(201),//Session过期
    DeveloperInfoError(202),//开发者信息错误
    DeveloperArrearage(203),//欠费
    DeverloperNotOpen(206),//未开通画板功能
    DatabaseError(301);//数据库异常

    public final int code;

    private ARBoardCode(int code) {
        this.code = code;
    }
}
