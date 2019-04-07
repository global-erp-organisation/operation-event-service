package com.ia.operation.helper;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RenderHelper {
    public static <T> T render(T oldValue, T newValue) {
        return newValue == null ? oldValue : newValue;
    }
}
