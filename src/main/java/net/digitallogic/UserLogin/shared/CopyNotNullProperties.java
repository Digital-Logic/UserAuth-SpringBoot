package net.digitallogic.UserLogin.shared;


import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.reflect.InvocationTargetException;

public class CopyNotNullProperties extends BeanUtilsBean {

    @Override
    public void copyProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException {
        if (value != null)
            super.copyProperty(bean, name, value);
    }
}
