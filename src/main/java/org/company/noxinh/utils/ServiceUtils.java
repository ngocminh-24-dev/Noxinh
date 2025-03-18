package org.company.noxinh.utils;

import org.company.noxinh.entity.Account;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.module.ResolutionException;

public class ServiceUtils {
    public static Integer getId(){
        try {
            return ((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getId();
        } catch (Exception e){
            throw new ResolutionException("Could not get id from user");
        }
    }
}
