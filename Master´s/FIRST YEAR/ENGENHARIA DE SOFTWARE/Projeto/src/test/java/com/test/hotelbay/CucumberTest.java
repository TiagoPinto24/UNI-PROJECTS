package com.test.hotelbay;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources", glue = "com.test.hotelbay", plugin = {
		"pretty",
		"json:target/cucumber.json",
		"html:target/cucumber-reports"
})
public class CucumberTest {
}