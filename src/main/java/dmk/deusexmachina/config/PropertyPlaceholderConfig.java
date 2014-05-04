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
public class PropertyPlaceholderConfig {
	Logger logger = LoggerFactory.getLogger(PropertyPlaceholderConfig.class);

	final static Resource[] locations = { new ClassPathResource("deus-ex-machina.properties") };
	
	@Bean
	public static EnvironmentStringPBEConfig environmentStringPBEConfig(){
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
	public static StandardPBEStringEncryptor configurationEncryptor(){
		StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
		standardPBEStringEncryptor.setConfig(environmentStringPBEConfig());
		return standardPBEStringEncryptor;
	}
	
	@Bean
	public static EncryptablePropertyPlaceholderConfigurer encryptablePropertyPlaceholderConfigurer(){
		EncryptablePropertyPlaceholderConfigurer configurer =  new EncryptablePropertyPlaceholderConfigurer(configurationEncryptor());
		configurer.setLocations(locations);
		return configurer;
	}
	
}