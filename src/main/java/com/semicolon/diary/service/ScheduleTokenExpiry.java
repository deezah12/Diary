package com.semicolon.diary.service;

import com.semicolon.diary.service.inter.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

    @Configuration
    @EnableScheduling
    @RequiredArgsConstructor
    public class ScheduleTokenExpiry {
        @Autowired
        TokenService tokenService;


        @Scheduled(cron = "0 10 0 * * *")
        public void tokenExpiredAt(){
            //System.out.println("Deleted");
            tokenService.deleteExpiredToken();
        }
}
