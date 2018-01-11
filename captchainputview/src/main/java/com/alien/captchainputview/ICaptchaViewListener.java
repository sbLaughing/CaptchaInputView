package com.alien.captchainputview;

/**
 * 描述:
 * <p>
 * Created by Alien on 2018/1/10.
 */

public interface ICaptchaViewListener {
    void onContentChanged(String content);
    void onComplete(String content);
}
