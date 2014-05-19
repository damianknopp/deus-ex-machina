package dmk.deusexmachina.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import dmk.deusexmachina.config.BootstrapConfig;
import dmk.deusexmachina.service.AntikytheraMechanism;

public class AntikytheraMechanismCLI {
	Logger logger = LoggerFactory.getLogger(AntikytheraMechanismCLI.class);
	
	AnnotationConfigApplicationContext context;
	
	public static void main(final String args[]){
		AntikytheraMechanismCLI cli = new AntikytheraMechanismCLI();
		if(args.length == 0){
			System.err.println("Usage: java " + AntikytheraMechanismCLI.class + " <login|trafficCheck|notify>");
			System.exit(1);
		}
		cli.runAntikytheraMechanism(args[0]);
		System.exit(0);
	}
	
	public AntikytheraMechanismCLI(){
		super();
		this.loadBeans();
	}
	
	public void loadBeans(){
		this.context = 
		          new AnnotationConfigApplicationContext(BootstrapConfig.class);
	}
	
	public void runAntikytheraMechanism(final String type){
		AntikytheraMechanism am = context.getBean(AntikytheraMechanism.class);
		switch(type){
			case "login":
				am.commencementOfTheDawn();
				break;
			case "trafficCheck":
				am.trafficUpdatesWeekday();
				break;
			case "notify":
				am.remindUsers();
				break;
			default:
				logger.info("unknown option " + type);
		}
	}
	
}