/*
 * This class will handle all data, manipulate, and do calculations
 * Expect to create
 * */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParseDataPoints {
  double[] priceDP;
  double[] originalPriceDP;
  double mean=-1, volatility=-1, momentum=-1, pearsonsCoefficient=-1;
  List<int[]> interceptsList;
  public void setup() throws IOException {

    getDataPoints();
    priceDP = standardizeData(priceDP);


    getMean();
    getIntercepts();
    for(int i = 0; i < interceptsList.size(); i++){
      int[] z = interceptsList.get(i);
      System.out.println(z[0]+" "+z[1]+" "+z[2]);
    }

//    integrateIntercepts(interceptsList);


    System.out.println(mean);
  }

  public void getDataPoints() throws IOException {
    @SuppressWarnings("deprecation")

    URL url = new URL("https://api.coingecko.com/api/v3/coins/bitcoin/market_chart?vs_currency=usd&days=1");
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");

    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    String inputLine;
    StringBuilder response = new StringBuilder();

    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();


    JSONObject json = new JSONObject(response.toString());
    JSONArray prices = json.getJSONArray("prices");

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"));

    System.out.println("Timestamp\t\t\tPrice (USD)");
    System.out.println("--------------------------------------------------");


    priceDP = new double[prices.length()];
    double price;


    for (int i = 0; i < prices.length(); i++) {
      JSONArray entry = prices.getJSONArray(i);
      price = entry.getDouble(1);
      priceDP[i] = price;
      String formattedTime = formatter.format(Instant.ofEpochMilli(entry.getLong(0)));
      System.out.println(formattedTime + "\t" + price);
    }
    System.out.println("Response: " + response.toString());

  }
  /*
   * This method is used to translate a standardized form of percentage change rather than magnitude
   * this will help parse the data first, this NEEDs to be called before manipulating the the priceDP array
   * */
  public double[] standardizeData(double[] temp) {
    double[] z = new double[temp.length - 1];

    double accumulatedNegatives = 0.0;
    double accumulatedPositives = 0.0;

    for (int i = 0; i < temp.length - 1; i++) {
      z[i] = ((temp[i + 1] - temp[i]) / temp[i]) * 100;

      System.out.println(i + ": " + z[i] + "%");

      if (z[i] > 0) {
        accumulatedPositives += z[i];
      } else {
        accumulatedNegatives += z[i];
      }
    }

    System.out.println("accumulatedPositives: " + accumulatedPositives +
            "\naccumulatedNegatives: " + accumulatedNegatives +
            "\nNet difference: " + (accumulatedPositives - accumulatedNegatives));

    return z;
  }



  public double getMean(){
    double z = 0.0;
    for(int i =0; i<priceDP.length; i++){
      z+=priceDP[i];
    }
    mean = z/=(1.0*priceDP.length);
    return mean;
  }

  /*
   * This will get us the index position of where residual will be higher/lower than mean
   * will show quadrant indices
   * temp.get(0) -> quadrant -> [+-,x0, x1]
   * temp.get(1) -> quadrant -> [+-,x1, x2]
   * tells us the indices of priceDp of which sections are below the mean (denoted by indices), and above the mean
   */
  public List<int[]> getIntercepts(){
    List<int[]> temp = new ArrayList<int[]>();
    int[] indices = null;
    int start = 0;
    boolean isNegDirection = priceDP[0] < mean;

    for(int i = 1; i < priceDP.length; i++){
      boolean curDirection = priceDP[i] < mean;
      if(isNegDirection != curDirection){
        // Include the transition point (index i) in the current segment
        indices = new int[]{(isNegDirection ? -1 : 1), start, i-1};
        temp.add(indices);
        // Start the next segment at the transition point (index i)
        start = i;
        isNegDirection = curDirection;
      }
    }

    // Add the final segment
    indices = new int[]{isNegDirection ? -1 : 1, start, priceDP.length - 1};
    temp.add(indices);

    interceptsList = temp;
    return temp;
  }
  /*
   * using the indecies of the intercepts, i integrate the quadrants to see which one is more.
   * Skewed data implies a lot
   *
   * will create an array of this data
   * */
  public void integrateIntercepts(List<int[]>segments){
    int posCount = 0, negCount = 0;
    double posArea = 0, negArea = 0;
    boolean isPos;
    int start;
    int end;
    for(int i = 0; i<segments.size(); i++){
      System.out.println(segments.get(i)[0]+" "+segments.get(i)[1]+" "+segments.get(i)[2]);
      isPos = segments.get(i)[0] > 0;
      start = segments.get(i)[1];
      end = segments.get(i)[2];
      for(int j = start; j<=end; j++){
        if(isPos){
          posCount++;
          posArea+=priceDP[j];
        } else {
          negCount++;
          negArea+=priceDP[j];
        }
      }

    }

    System.out.println("posCount: "+posCount
            +"\nnegCount: "+negCount
            +"\nposArea: "+posArea
            +"\nnegArea: "+negArea);



  }


  public double getMeanAversion(){
    return 0;
  }

  public void displayDoubleArr() {
    String z ="{";
    for(int i = 0; i<priceDP.length; i++) {
      z+=priceDP[i]+", ";
    }
    z+="}";
    System.out.println(z);
  }

  public static void main(String[] args) throws IOException {
    ParseDataPoints p = new ParseDataPoints();
    p.setup();


  }
}