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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author puzansakya
 */
public class SendItemProcessor implements ItemProcessor<Contact, Contact> {

    private static final Logger log = LoggerFactory.getLogger(SendItemProcessor.class);

    //required for report inserting
    @Autowired
    private ReportRepository rr;

    //delegate server update to browswer via socket
    @Autowired
    private SimpMessagingTemplate template;

    //conversion logic happens here during the batch job execution
    @Override
    public Contact process(final Contact contact) throws Exception {

        String url = "http://192.168.100.23:13013/cgi-bin/sendsms?smsc=FAKE&username=rapidsms&password=CHANGE-ME&from=puzan&text=this-is-test&to=" + contact.getMsisdn();

        //executing the get call to ubuntu server with above url
        RestTemplate restTemplate = new RestTemplate();
        String post = restTemplate.getForObject(url, String.class); //get 0. Accepted for delivery
        log.info("sending message to " + contact.getMsisdn());
        this.template.convertAndSend("/chat", "sending message to " + contact.getMsisdn() + " , status: ");

        //perform report insert
        Report report = new Report();
        Date date = new Date();

        report.setMsisdn(contact.getMsisdn());
        report.setReportReceivedTime(date);
        report.setSentTime(date);
        report.setMessage(contact.getMessage());
        report.setDeliveryReport(post.toString());

        rr.save(report);
        //return the processed contact
        return contact;
    }

}
