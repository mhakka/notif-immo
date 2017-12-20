/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mha.notif.immo;

import com.mha.notif.immo.service.SeLogerClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mehdi
 */
public class Run {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Logger logger = LoggerFactory.getLogger(Run.class);
        logger.info("Program started");
        
        SeLogerClientService seLogerClientService = new SeLogerClientService();
        seLogerClientService.start();
        
        logger.info("End of program");
    }
    
}
