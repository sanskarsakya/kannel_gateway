/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puzan.smsfinal.Listener;

import com.puzan.smsfinal.Entity.Contact;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**
 *
 * @author puzansakya
 */
@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;
    private Long startTime, endTIme;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = System.nanoTime();
    }        

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");
            endTIme = System.nanoTime();
            log.info("start: " + startTime + " and end: " + endTIme);
//            List<Contact> results = jdbcTemplate.query("SELECT msisdn FROM tbl_contacts", new RowMapper<Contact>() {
//                @Override
//                public Contact mapRow(ResultSet rs, int row) throws SQLException {
//                    return new Contact(rs.getString(1));
//                }
//            });
//
//            for (Contact contact : results) {
//                log.info("Found <" + contact + "> in the database.");
//            }
        }
    }
}
