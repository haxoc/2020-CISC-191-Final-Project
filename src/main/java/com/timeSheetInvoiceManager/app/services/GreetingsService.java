/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.timeSheetInvoiceManager.app.services;

import org.springframework.stereotype.Service;
/**
 *
 * @author xaboo
 */
@Service

public class GreetingsService {

    /**
     * @param args the command line arguments
     */
        public String getWelcomeGreeting() {
            return "Welcome and have a nice day!";
        }

}
