/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puzan.smsfinal.Processor;

import com.puzan.smsfinal.Entity.Contact;
import com.puzan.smsfinal.Entity.Report;
import com.puzan.smsfinal.Repository.ReportRepository;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author puzansakya
 */
@StepScope
public class ContactItemProcessor implements ItemProcessor<Contact, Contact> {

    private static final Logger log = LoggerFactory.getLogger(ContactItemProcessor.class);
    
    @Autowired
    private SimpMessagingTemplate template;

//    @Value("#{jobParameters['message']}")
//    String message;

    @Override
    public Contact process(final Contact contact) throws Exception {

        log.info("processing " + contact.toString());
        this.template.convertAndSend("/chat", "processing " + contact.toString());

        return contact;
    }

}
