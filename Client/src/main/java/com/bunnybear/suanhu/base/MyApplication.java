package com.bunnybear.suanhu.base;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

public class MyApplication extends TinkerApplication{


    public MyApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.bunnybear.suanhu.base.MyApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
}
