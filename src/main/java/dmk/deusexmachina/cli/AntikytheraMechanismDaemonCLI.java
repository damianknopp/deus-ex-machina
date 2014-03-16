package dmk.deusexmachina.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import dmk.deusexmachina.config.BootstrapConfig;

public class AntikytheraMechanismDaemonCLI {
	Logger logger = LoggerFactory.getLogger(AntikytheraMechanismDaemonCLI.class);
	
	AnnotationConfigApplicationContext context;
	
	public static void main(final String args[]){
		AntikytheraMechanismDaemonCLI cli = new AntikytheraMechanismDaemonCLI();
	}
	
	public AntikytheraMechanismDaemonCLI(){
		super();
		this.loadBeans();
	}
	
	public void loadBeans(){
		this.context = 
		          new AnnotationConfigApplicationContext(BootstrapConfig.class);
	}
}