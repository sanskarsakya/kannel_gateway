/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puzan.smsfinal.Configuration;

import com.puzan.smsfinal.Entity.Contact;
import com.puzan.smsfinal.Listener.JobCompletionNotificationListener;
import com.puzan.smsfinal.Processor.ContactItemProcessor;
import com.puzan.smsfinal.Processor.SendItemProcessor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.hibernate.cfg.Environment;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.H2PagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author puzansakya
 */
@EnableBatchProcessing
@Configuration
public class DatabaseJobConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Bean
    @StepScope
    public JdbcCursorItemReader<Contact> databaseReader(@Value("#{jobParameters['message']}") String message) {
        JdbcCursorItemReader<Contact> reader = new JdbcCursorItemReader<Contact>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT id,msisdn FROM tbl_contacts");
        reader.setRowMapper(new RowMapper<Contact>() {
            @Override
            public Contact mapRow(ResultSet rs, int i) throws SQLException {
                return new Contact(rs.getInt(1),rs.getString(2),message);
            }
        });

        return reader;
    }

    @Bean
    ItemProcessor<Contact, Contact> databaseCsvItemProcessor() {
        return new SendItemProcessor();
    }

//    @Bean
//    ItemWriter<Contact> databaseCsvItemWriter() {
//        FlatFileItemWriter<Contact> csvFileWriter = new FlatFileItemWriter<>();
//
////        String exportFileHeader = environment.getRequiredProperty(PROPERTY_CSV_EXPORT_FILE_HEADER);              
////
////        String exportFilePath = environment.getRequiredProperty(PROPERTY_CSV_EXPORT_FILE_PATH);
////        csvFileWriter.setResource(new FileSystemResource(exportFilePath));
//        return csvFileWriter;
//    }
    @Bean
    public Job exportUserJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("exportUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1").<Contact, Contact>chunk(10)
                .reader(databaseReader(null))
                .processor(databaseCsvItemProcessor())
                //                .writer(writer())
                .build();
    }

}
