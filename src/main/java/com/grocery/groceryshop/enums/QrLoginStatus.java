package com.grocery.groceryshop.enums;

public enum QrLoginStatus {
    WAITING,    // 等待扫码
    SCANNED,    // 已扫码，等待确认
    CONFIRMED,  // 已确认，登录成功
    EXPIRED     // 已过期
}
