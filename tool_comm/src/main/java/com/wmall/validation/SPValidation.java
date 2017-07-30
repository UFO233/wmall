package com.wmall.validation;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.validation.Validation;
import com.alibaba.dubbo.validation.Validator;

/**
 * Created by Iven on 2015-02-11.
 */
public class SPValidation implements Validation {

    @Override
    public Validator getValidator(URL url) {
        return new ServiceParamValidator(url);
    }
}
