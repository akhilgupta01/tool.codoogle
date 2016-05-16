package gupta.akhil.tools.indexer.code;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import gupta.akhil.tools.indexer.CodeIndexer;

public class ApplicationBootstrap implements ServletContextListener{
	
	private Timer timer = new Timer();
	
	private TimerTask refreshTask = new TimerTask(){
		private boolean running;
		
		@Override
		public void run() {
			try {
				if(!running){
					running = true;
					CodeIndexer.getInstance().refreshIndex();
					running = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};


	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0){
		timer.schedule(refreshTask, 0, 4*60*60*1000);
	}
}
