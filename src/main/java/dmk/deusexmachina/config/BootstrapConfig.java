package dmk.deusexmachina.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import dmk.deusexmachina.service.AntikytheraMechanism;
import dmk.deusexmachina.service.AntikytheraMechanismImpl;
import dmk.deusexmachina.service.PortalLoginService;
import dmk.deusexmachina.service.PortalLoginServiceImpl;
import dmk.mail.MailSenderService;
import dmk.mail.MailSenderServiceDefaultImpl;
import dmk.mapquest.conf.MapQuestConf;
import dmk.mapquest.service.MapQuestTrafficService;
import dmk.twilio.sms.PhoneSmsService;
import dmk.twilio.sms.conf.TwilioSmsConf;
 
@Configuration
@Import({
	PropertyPlaceholderConfig.class,
	SchedulerConfig.class,
	MapQuestConf.class,
	TwilioSmsConf.class
})
@ComponentScan(basePackages="dmk.deusexmachina")
public class BootstrapConfig {
	Logger logger = LoggerFactory.getLogger(BootstrapConfig.class);

	@Value("${dmk.service.mail.address}")
	protected String emailAddress;
	@Value("${dmk.service.mail.pass}")
	protected String emailPass;
	
	@Value("${dmk.service.portal.login.portalUser}")
	protected String portalUser;
	@Value("${dmk.service.portal.login.portalPass:defaultPass}")
	protected String portalPass;
	
	@Value("${dmk.service.twilio.sms.authtoken}")
	protected String authToken;
	@Value("${dmk.service.twilio.sms.accountsid}")
	protected String authSid;
	
	@Bean public String authToken(){
		return this.authToken;
	}
	
	@Bean public String accountSid(){
		return this.authSid;
	}
	
	@Value("${dmk.mapquest.host}")
	protected String mapquestHost;
	@Value("${dmk.mapquest.key}")
	protected String mapquestKey;

	@Bean public String mapquestHost(){
		return this.mapquestHost;
	}
	
	@Bean public String mapquestKey(){
		return this.mapquestKey;
	}
	
	@Value("${dmk.antikytheramechanism.emailRecipient}")
	protected String emailRecipient;
	@Value("${dmk.antikytheramechanism.email:true}")
	protected Boolean sendEmail;
	@Value("${dmk.antikytheramechanism.sms:false}")
	protected Boolean sendSms;
	
	@Value("${dmk.antikytheramechanism.sms.to}")
	protected String smsTo;
	@Value("${dmk.antikytheramechanism.sms.from}")
	protected String smsFrom;
	
	@Autowired
	public PhoneSmsService phoneSmsService;
	@Autowired
	public MapQuestTrafficService mapQuestTrafficService;
	
	@Bean
	public MailSenderService mailSenderService(){
		MailSenderServiceDefaultImpl mailer = new MailSenderServiceDefaultImpl();
		mailer.setEmailAddr(this.emailAddress);
		mailer.setPass(this.emailPass);
		return mailer;
	}
	
	@Bean
	public PortalLoginService portalLoginService(){
		PortalLoginServiceImpl ps = new PortalLoginServiceImpl();
		ps.setPass(this.portalPass);
		ps.setUser(this.portalUser);
		return ps;
	}
	
	@Bean
	public AntikytheraMechanism antikytheraMechanism(){
		AntikytheraMechanismImpl am = new AntikytheraMechanismImpl();
		am.setMailSenderService(this.mailSenderService());
		am.setPortalLoginService(this.portalLoginService());
		am.setMapQuestTrafficService(mapQuestTrafficService);
		am.setPhoneSmsService(phoneSmsService);
		am.setEmailRecipient(this.emailRecipient);
		am.setShouldEmail(this.sendEmail);
		am.setShouldSms(this.sendSms);
		am.setSmsFrom(this.smsFrom);
		am.setSmsTo(this.smsTo);
		return am;
	}
}