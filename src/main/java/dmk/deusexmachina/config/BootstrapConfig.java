package dmk.deusexmachina.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import dmk.mail.MailSenderService;
import dmk.mail.MailSenderServiceDefaultImpl;
 
@Configuration
@Import({
	PropertyPlaceholderConfig.class,
	SchedulerConfig.class
})
@ComponentScan(basePackages="dmk.deusexmachina")
public class BootstrapConfig {
	Logger logger = LoggerFactory.getLogger(BootstrapConfig.class);

	protected String emailAddress;
	protected String emailPass;
	
	@Value("${dmk.service.mail.address}")
	public void setEmailAddr(final String emailAddr){
		this.emailAddress = emailAddr;
	}

	@Value("${dmk.service.mail.pass}")
	public void setEmailPass(final String emailPass){
		this.emailPass = emailPass;
	}

	@Bean
	public MailSenderService mailSenderService(){
		MailSenderServiceDefaultImpl mailer = new MailSenderServiceDefaultImpl();
		mailer.setEmailAddr(this.emailAddress);
		mailer.setPass(this.emailPass);
		return mailer;
	}
}