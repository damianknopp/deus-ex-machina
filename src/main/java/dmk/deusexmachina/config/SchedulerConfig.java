package dmk.deusexmachina.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import dmk.deusexmachina.service.AntikytheraMechanism;
import dmk.deusexmachina.service.AntikytheraMechanismImpl;
import dmk.mail.MailSenderService;
 
/**
 * At new dawns time, go forth my machine and do my bidding....
 * @author dmknopp
 *
 */
@Configuration
@EnableScheduling
public class SchedulerConfig implements SchedulingConfigurer{
	
	protected MailSenderService mailSenderService;

	@Value("${dmk.antikytheramechanism.portalUser}")
	protected String portalUser;
	@Value("${dmk.antikytheramechanism.portalPass:defaultPass}")
	protected String portalPass;
	@Value("${dmk.antikytheramechanism.emailRecipient}")
	protected String emailRecipient;
	
	@Autowired
	public void setMailSenderService(final MailSenderService mService){
		this.mailSenderService = mService;
	}
	
	@Bean
	public AntikytheraMechanism antikytheraMechanism(){
		AntikytheraMechanismImpl am = new AntikytheraMechanismImpl();
		am.setMailSenderService(this.mailSenderService);
		am.setUser(this.portalUser);
		am.setPass(this.portalPass);
		am.setEmailRecipient(this.emailRecipient);
		return am;
	}
	
	@Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }

    @Bean(destroyMethod="shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(2);
    }
}