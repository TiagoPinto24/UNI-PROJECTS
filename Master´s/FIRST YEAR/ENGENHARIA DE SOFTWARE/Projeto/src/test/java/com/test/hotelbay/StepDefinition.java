package com.test.hotelbay;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import io.cucumber.spring.CucumberContextConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HotelBayApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@CucumberContextConfiguration
public class StepDefinition {

    @Autowired
    protected MockMvc mockMvc;
}