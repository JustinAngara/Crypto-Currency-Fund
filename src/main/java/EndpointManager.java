
/*
 * This class will handle the scheduler
 * let n be the length of exchangeEndpoints array
 * each thread will be spamming api calls to each exchange
 * if a occurence of a price disparaity, a running thread will be called to run an algorithm
 * */

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class EndpointManager {
  
  public EndpointManager(String[] endpoints) {
    int coreCount = Runtime.getRuntime().availableProcessors(); 
    ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(coreCount);
    
    for(int i = 0; i<endpoints.length; i++) {
      executor.execute(new Endpoint(endpoints[i]));
    }
    
    
    // no longer point
    executor.shutdown();

  }
  public void startEndpointListening() {
    
  }
}
