/*
 * this class will include the websockets to actively listen for latency arbitage,
 * will focus use multiple threads to listen for multiple live feeds 
 * 
 * */
public class Setup {
  private String[] endpoints; // dont use list for sake of efficiency
  private EndpointManager em;
  public Setup() {
    // inits endpoints
    endpoints = new String[10];
    endpoints[0] = "";
    endpoints[1] = "";
    connectToEndpoint();
  }
  
  public void connectToEndpoint() {
    em = new EndpointManager(endpoints);
  }
  
  public void comparePrices() {
    
  }
}
