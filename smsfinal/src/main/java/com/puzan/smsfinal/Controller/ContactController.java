/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puzan.smsfinal.Controller;

import com.puzan.smsfinal.Entity.Contact;
import com.puzan.smsfinal.Entity.Message;
import com.puzan.smsfinal.Repository.ContactRepository;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author puzansakya
 */
@RestController
@RequestMapping("/")
@CrossOrigin
public class ContactController {

    //required for webscokets
    private final SimpMessagingTemplate template;

    private int totalPage = 5;
    private int size = 1000;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Path rootLocation = Paths.get("src/main/resources/");

    @Autowired
    JobLauncher jobLauncher;

    //referenceing the import csv job
    @Autowired
    Job importContact;

    //referencing the job for sending message
    @Autowired
    Job exportUserJob;

    @Autowired
    public ContactController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @GetMapping
    public String index() throws Exception {
        return "Health check status ok";
    }

    //handles the send message functionality that runs the export user job batch
    @GetMapping("/send")
    public String send(Message message) throws Exception {
        //executing the job
        try {
            JobParameters jobParameters = new JobParametersBuilder().addString("message","check").addLong("time", System.currentTimeMillis()).toJobParameters();
            jobLauncher.run(exportUserJob, jobParameters);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return "execute get request success";                
    }

    @GetMapping(value = "/ds")
    public String deliveryReport(HttpServletRequest request) throws Exception {
        return request.getRequestURI();
    }

    //endpoint to handle csv file upload
    @PostMapping("/post")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            //delete if the file of same name exist
            Files.deleteIfExists(this.rootLocation.resolve(file.getOriginalFilename()));
            //then save the file to the storage
            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
            //return the string of the file name that inturn calls the /job endpoint and triggers the importContact batch job
            return ResponseEntity.status(HttpStatus.OK).body(file.getOriginalFilename());
        } catch (Exception e) {
            message = "FAIL to upload " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    //this endpoint takes the filename as parameter provided by uploading the csv file
    @GetMapping(value = "/job", params = {"filename"})
    public String upload(@RequestParam(value = "filename", required = false) String filename) throws Exception {
        try {
            JobParameters jobParameters = new JobParametersBuilder().addString("inputFile", filename).addLong("time", System.currentTimeMillis()).toJobParameters();
            jobLauncher.run(importContact, jobParameters);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }

        return "{\"message\":\"job done\"}";
    }

    //socket testing endpoint
    @MessageMapping("/send/message")
    public void onReceivedMessage(String message) {
        System.out.println(message);
        this.template.convertAndSend("/chat", new SimpleDateFormat("HH:mm:ss").format(new Date()) + "- " + message);
    }

}
