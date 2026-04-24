package com.ead.authuser.services.impl;

import org.springframework.stereotype.Service;

import com.ead.authuser.repositories.UserCourseRepository;
import com.ead.authuser.services.UserCourseService;

@Service
public class UserCourseServiceImpl implements UserCourseService{

    final UserCourseRepository UsercourseRepository;

    public UserCourseServiceImpl(UserCourseRepository usercourseRepository) {
        UsercourseRepository = usercourseRepository;
    }
}
