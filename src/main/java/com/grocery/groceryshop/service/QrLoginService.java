package com.grocery.groceryshop.service;

import com.grocery.groceryshop.vo.QrGenerateVO;
import com.grocery.groceryshop.vo.QrPollVO;

public interface QrLoginService {

    QrGenerateVO generate() throws Exception;

    QrPollVO poll(String token);

    void scan(String token, Long userId);

    String confirm(String token, Long userId);
}
