package com.bunnybear.suanhu.master.oss;

import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCustomSignerCredentialProvider;
import com.alibaba.sdk.android.oss.common.utils.OSSUtils;

public class OSSConfig {
    // Access Key id 问后台要
    public static final String AK = "LTAIuMfFKxjWxUyk";
    // SecretKeyId 问后台要
    public static final String SK = "ncoTXQ81q1FcEfP7NPfKlCQ8A2tswo";

    public static OSSCredentialProvider newCustomSignerCredentialProvider() {
        return new OSSCustomSignerCredentialProvider() {
            @Override
            public String signContent(String content) {
                return OSSUtils.sign(AK, SK, content);
            }
        };
    }
}
