
/*
 * This will run each individual endpoint
 * */
public class Endpoint implements Runnable{
  private String endpoint;
  public Endpoint(String e) {
    endpoint = e;
  }
  @Override
  public void run() {
    // TODO Auto-generated method stub
    
    while(true) {
   // this should run the api/websocket call to the endpoint
    }
  }

}
