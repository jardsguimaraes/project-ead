package com.ead.authuser.services.impl;

import org.springframework.stereotype.Service;

import com.ead.authuser.repositories.UserCourseRepository;
import com.ead.authuser.services.UserCourseService;

@Service
public class UserCourseImpl implements UserCourseService{

    final UserCourseRepository UsercourseRepository;

    public UserCourseImpl(UserCourseRepository usercourseRepository) {
        UsercourseRepository = usercourseRepository;
    }
}
