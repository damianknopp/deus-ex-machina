package dmk.deusexmachina.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.jasypt.spring31.properties.EncryptablePropertyPlaceholderConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
//@PropertySource("classpath:/deus-ex-machina.properties")
public class PropertyPlaceholderConfig {
	Logger logger = LoggerFactory.getLogger(PropertyPlaceholderConfig.class);

//	@Bean
//	public PropertySourcesPlaceholderConfigurer placeHolderConfigurer(){
//		return new PropertySourcesPlaceholderConfigurer();
//	}
	
	final Resource[] locations = { new ClassPathResource("deus-ex-machina.properties") };
	
	@Bean
	public EnvironmentStringPBEConfig environmentStringPBEConfig(){
		EnvironmentStringPBEConfig tmp =  new EnvironmentStringPBEConfig();
		tmp.setAlgorithm("PBEWithMD5AndDES");
		final String envName = "DEUSEXMACHINA_ENCRYPTION_PASSWORD";
//		tmp.setPasswordEnvName(envName);
		String pass = System.getProperty(envName);
		tmp.setPasswordCharArray(pass.toCharArray());
		System.setProperty(envName, "");
		pass = null;
		return tmp;
	}
	
	@Bean
	public StandardPBEStringEncryptor configurationEncryptor(){
		StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
		standardPBEStringEncryptor.setConfig(environmentStringPBEConfig());
		return standardPBEStringEncryptor;
	}
	
	@Bean
	public EncryptablePropertyPlaceholderConfigurer encryptablePropertyPlaceholderConfigurer(){
		EncryptablePropertyPlaceholderConfigurer configurer =  new EncryptablePropertyPlaceholderConfigurer(configurationEncryptor());
		configurer.setLocations(locations);
		return configurer;
	}
	
//	@Bean
//	public StrongPasswordEncryptor strongPasswordEncryptor(){
//		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
//		return passwordEncryptor;
//	}
}