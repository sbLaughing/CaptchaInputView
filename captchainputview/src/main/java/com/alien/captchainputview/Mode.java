package com.alien.captchainputview;

import android.support.annotation.IntDef;

import static com.alien.captchainputview.Mode.border;
import static com.alien.captchainputview.Mode.borderSparse;
import static com.alien.captchainputview.Mode.underline;

/**
 * 描述:
 * <p>
 * Created by Alien on 2018/1/10.
 */

@IntDef({underline,border,borderSparse})
public @interface Mode {
    int underline = 0;
    int border = 1;
    int borderSparse = 2;
}
