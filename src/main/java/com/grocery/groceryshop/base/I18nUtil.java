package com.grocery.groceryshop.base;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Locale;

/** 基于 Spring {@link MessageSource} 的国际化文案获取工具。 */
@Component
public class I18nUtil implements InitializingBean {

    @Resource private MessageSource messageSource;

    private static MessageSource staticMessageSource;

    @Override
    public void afterPropertiesSet() {
        staticMessageSource = messageSource;
    }

    /** 按当前请求 Locale 取国际化文案；若 key 不存在则回退为 key 本身。 */
    public static String get(String key, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return staticMessageSource.getMessage(key, args, key, locale);
    }
}
