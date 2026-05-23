package com.grocery.groceryshop.service.impl;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.grocery.groceryshop.base.CustomerException;
import com.grocery.groceryshop.enums.QrLoginStatus;
import com.grocery.groceryshop.service.QrLoginService;
import com.grocery.groceryshop.vo.QrGenerateVO;
import com.grocery.groceryshop.vo.QrPollVO;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class QrLoginServiceImpl implements QrLoginService {

    // token 有效期 3 分钟
    private static final long TOKEN_TTL_MS = 3 * 60 * 1000L;

    private final ConcurrentHashMap<String, QrLoginSession> store = new ConcurrentHashMap<>();

    @Override
    public QrGenerateVO generate() throws Exception {
        String token = UUID.randomUUID().toString().replace("-", "");
        store.put(token, new QrLoginSession(token));

        QrConfig config = new QrConfig();
        config.setErrorCorrection(ErrorCorrectionLevel.H);
        BufferedImage image = QrCodeUtil.generate(token, config);
        String base64 = "data:image/png;base64," + ImgUtil.toBase64(image, ImgUtil.IMAGE_TYPE_PNG);

        QrGenerateVO vo = new QrGenerateVO();
        vo.setToken(token);
        vo.setQrCodeImage(base64);
        return vo;
    }

    @Override
    public QrPollVO poll(String token) {
        QrLoginSession session = getSession(token);
        QrPollVO vo = new QrPollVO();
        vo.setStatus(session.getStatus().name());
        if (session.getStatus() == QrLoginStatus.CONFIRMED) {
            vo.setAuthToken(session.getAuthToken());
            vo.setUserId(session.getUserId());
        }
        return vo;
    }

    @Override
    public void scan(String token, Long userId) {
        QrLoginSession session = getSession(token);
        if (session.getStatus() != QrLoginStatus.WAITING) {
            throw new CustomerException("400", "二维码已被扫描或已过期");
        }
        session.setStatus(QrLoginStatus.SCANNED);
        session.setUserId(userId);
    }

    @Override
    public String confirm(String token, Long userId) {
        QrLoginSession session = getSession(token);
        if (session.getStatus() != QrLoginStatus.SCANNED) {
            throw new CustomerException("400", "请先扫描二维码");
        }
        if (!userId.equals(session.getUserId())) {
            throw new CustomerException("400", "操作用户与扫码用户不一致");
        }
        String authToken = UUID.randomUUID().toString().replace("-", "");
        session.setAuthToken(authToken);
        session.setStatus(QrLoginStatus.CONFIRMED);
        return authToken;
    }

    private QrLoginSession getSession(String token) {
        QrLoginSession session = store.get(token);
        if (session == null) {
            throw new CustomerException("404", "二维码不存在");
        }
        // 懒过期：首次访问时检查 TTL
        if (session.getStatus() != QrLoginStatus.EXPIRED
                && System.currentTimeMillis() > session.getExpireAt()) {
            session.setStatus(QrLoginStatus.EXPIRED);
        }
        if (session.getStatus() == QrLoginStatus.EXPIRED) {
            throw new CustomerException("410", "二维码已过期");
        }
        return session;
    }

    @Data
    private static class QrLoginSession {
        private final String token;
        private QrLoginStatus status = QrLoginStatus.WAITING;
        private Long userId;
        private String authToken;
        private final long expireAt = System.currentTimeMillis() + TOKEN_TTL_MS;

        QrLoginSession(String token) {
            this.token = token;
        }
    }
}
