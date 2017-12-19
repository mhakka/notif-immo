/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mha.notif.immo.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author mehdi
 */
@Controller
@EnableAutoConfiguration
public class SampleController {
    
    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }
}
