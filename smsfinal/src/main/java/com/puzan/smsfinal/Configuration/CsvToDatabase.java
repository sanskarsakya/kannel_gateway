/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puzan.smsfinal.Configuration;

import com.puzan.smsfinal.Entity.Contact;
import com.puzan.smsfinal.Entity.Contact;
import com.puzan.smsfinal.Listener.JobCompletionNotificationListener;
import com.puzan.smsfinal.Processor.ContactItemProcessor;
import java.lang.annotation.Annotation;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author puzansakya
 */
@EnableBatchProcessing
@Configuration
public class CsvToDatabase {
    
    /*
    contains the following methods
    reader
    processor
    writer
    job
    step
    
    */

    private static final String OVERRIDDEN_BY_EXPRESSION = null;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Bean
    @StepScope //access the job parameter values
    public FlatFileItemReader<Contact> reader(@Value("#{jobParameters['inputFile']}") String inputFile) {        
        FlatFileItemReader<Contact> reader = new FlatFileItemReader<Contact>(); //file reader 
        reader.setResource(new ClassPathResource(inputFile)); //refer path to the file
        reader.setLineMapper(new DefaultLineMapper<Contact>() { //mapping of te lines
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[]{"msisdn"});
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<Contact>() {
                    {
                        setTargetType(Contact.class);
                    }
                });
            }
        });
        return reader;
    }

    @Bean //instantiate the oricessor
    public ContactItemProcessor processor() {
        return new ContactItemProcessor();
    }

    @Bean //write to the database
    public JdbcBatchItemWriter<Contact> writer() {
        JdbcBatchItemWriter<Contact> writer = new JdbcBatchItemWriter<Contact>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Contact>());
        writer.setSql("INSERT INTO tbl_contacts (msisdn) VALUES (:msisdn)");
        writer.setDataSource(dataSource); //write into the database
        return writer;
    }

    @Bean
    public Job importContact(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importContact")
                .incrementer(new RunIdIncrementer())
                .listener(listener) //refer listener contains before and after method for execution
                .flow(step()) //chain the steps if any is available
                .end()
                .build();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("step")
                .<Contact, Contact>chunk(100) //read the line at once
                .reader(reader(OVERRIDDEN_BY_EXPRESSION)) //reader
                .processor(processor()) //processor
                .writer(writer()) //writer
                .build();
    }

}
