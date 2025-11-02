package com.fiap.techChallenge.bdd;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * Runner para executar os testes BDD com Cucumber
 * 
 * Executa todos os arquivos .feature encontrados em src/test/resources/features
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/product")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, 
        value = "pretty, html:target/cucumber-report.html, json:target/cucumber-report.json")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, 
        value = "com.fiap.techChallenge.bdd.steps")
public class CucumberTestRunner {
}

