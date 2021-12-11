package com.cfp.helper;

import java.util.UUID;

public class UuidHelper {
    public static String id(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
