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
		cli.runAntikytheraMechanism();
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
	
	public void runAntikytheraMechanism(){
		AntikytheraMechanism am = context.getBean(AntikytheraMechanism.class);
		am.commencementOfTheDawn();
	}
	
}