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
  static double[] priceDP;
  public static double mean=-1, volatility=-1, momentum=-1, pearsonsCoefficient=-1;
  public static void setup() throws IOException {
    getDataPoints();
    getMean();

  }
  public static void getDataPoints() throws IOException {
    @SuppressWarnings("deprecation")
    // THIS URL IS PUBLIC 
    URL url = new URL("https://api.coingecko.com/api/v3/coins/bitcoin/market_chart?vs_currency=usd&days=7");
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
//    System.out.println("Response: " + response.toString());
    
  }

  public static double getMean(){
    double z = 0.0;
    for(int i =0; i<priceDP.length; i++){
      z+=priceDP[i];
    }
    mean = z/=(1.0*priceDP.length);
    return mean;
  }

  /*
   * This will get us the index position of where residual will be higher/lower than mean
   * will show quadrant indicies
   * temp.get(0) -> quadrant -> [+-,x0, x1]
   * temp.get(1) -> quadrant -> [+-,x1, x2]
   * tells us the indicies of priceDp of which sections are below the mean (denoted by indecies), and above the mean
   * */
  public static List<int[]> getIntercepts(){
    List<int[]> temp = new ArrayList<int[]>();
    int[] indecies = null;

    //create pointers
    int start = 0;
    int end = 0;
    // this variable indicates if the initial price is negative
    boolean isNegDirection = priceDP[0] < mean;

    for(int i = 1; i < priceDP.length; i++){
      end = i;
      boolean curDirection = priceDP[i] < mean;

      if(isNegDirection != curDirection){
        // direction changed, thus add temp
        indecies = new int[]{start,end-1};
        temp.add(indecies);

        start = end;
        isNegDirection = curDirection;
      }
    }
    indecies = new int[]{start, priceDP.length - 1};
    temp.add(indecies);

    return temp;
  }
  public static double getMeanAversion(){
    if(mean==-1){
      return -1;
    }



    return 0;
  }

  public static void displayDoubleArr() {
    String z ="{";
    for(int i = 0; i<priceDP.length; i++) {
      z+=priceDP[i]+", ";
    }
    z+="}";
    System.out.println(z);
  }
  // this will be for testing methods
  public static void main(String[] args) throws IOException {
//    getDataPoints();
//    priceDP= new double[]{96984.40777110745, 97041.44181128644, 96738.11613131239, 96685.63911259717, 96932.91347341705, 96730.10192500369, 96523.60435697486, 96585.86635562367, 96399.3103307854, 96537.85554466437, 96380.04392946887, 96226.48114418039, 96219.45399678136, 96358.32950974852, 96278.3950554348, 95964.52767965854, 95976.4505745279, 96173.42460959752, 96321.3677258507, 96480.99719442788, 96425.31179505517, 96208.48935554299, 96183.72181610571, 96178.88564981271, 96392.32633421359, 96358.9081323945, 96279.18995625683, 96206.39898420339, 95911.57974109074, 95895.53297681686, 95915.88640420574, 95922.38712221736, 95806.54957964174, 95780.01377709549, 96064.91190994653, 95913.60802998614, 95922.80323598157, 95625.17286104144, 95523.33996950623, 95446.2718625732, 95458.83076597172, 95563.77480239843, 95378.85274818254, 95608.48318552892, 95525.92857480767, 95450.75277975168, 95430.42950592804, 95398.8923422279, 95578.93519558122, 95732.77578576689, 95492.48013580107, 94990.11188430124, 94303.45616731183, 94665.39195249154, 93904.08029713292, 94283.45228200975, 94081.95126956061, 94388.68665108354, 94662.83806875185, 94728.12709624284, 94736.51570501548, 94552.9528661461, 94588.80144204857, 94133.1803452243, 94088.5057159754, 94280.48803833337, 94365.43018781069, 93831.61281503776, 93724.80501461533, 94207.40183338308, 94488.70233451825, 94732.07505209661, 94381.49765604567, 94236.88089735083, 94712.89911153041, 95036.71686518363, 94719.67050036856, 94553.5512061966, 94311.76641275226, 94288.01718818802, 94409.2116200293, 94559.93816867477, 94372.28734827018, 94461.28594893696, 94408.13275180699, 94330.85226969632, 94175.29764102303, 93991.7353270022, 93846.01249611624, 93860.7540418781, 93903.87572706376, 94186.6533764946, 94551.00991495204, 94307.86688268195, 94534.4462606627, 95022.10980979039, 94954.2912644009, 94742.59542490661, 95136.89593991211, 96288.68626769505, 96815.80313214885, 97454.99383963464, 97205.98482172143, 96547.14987059082, 96556.62015334367, 96491.500021853, 96354.63080106633, 96538.03334234984, 96925.96210528578, 97003.63646893593, 96986.15290872475, 96986.48455649219, 97017.36703189345, 96968.99773463995, 97041.28165831216, 96946.47681288401, 96941.07608759051, 96863.98868038424, 96212.81842184125, 96306.502609734, 96148.7571218679, 96623.98416478802, 97096.195573126, 97277.49264928911, 97035.80181467332, 97921.8716635437, 97950.20260938423, 98712.46826631304, 99037.21087966081, 99095.08778875701, 98684.10898485525, 98990.04978777783, 99109.17249310363, 99645.29105191333, 99763.50394686138, 99810.2089598869, 99572.56913879959, 99380.82080975642, 99445.01176371268, 99883.30449934515, 101783.94854609821, 101397.34207827682, 101112.06155180026, 101327.33984258387, 101156.28787384083, 102715.70496819356, 102898.7842378539, 102946.60364931433, 103155.55403424997, 102755.01416938983, 102981.92071790314, 102602.6120856411, 102431.38593811914, 102735.23542016957, 102936.7074812753, 103654.43206932151, 103775.85199443038, 102941.5958896605, 102847.4715321015, 102942.94358657954, 102928.84977016224, 103635.59449072609, 103081.79586234059, 102895.00436299198, 102560.61383641983, 103192.42776451155, 103241.92605940854, 103068.76246094644};
//    setup();
    priceDP = new double[]{0,2,1,2,3,10,7,6,4,4,4,4,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5};
    System.out.println(getMean());
    List<int[]> z = getIntercepts();
    System.out.println(z.size());
    System.out.println(z.get(0)[0]+" "+z.get(0)[1]);
    System.out.println(z.get(1)[0]+" "+z.get(1)[1]);
    System.out.println(z.get(2)[0]+" "+z.get(2)[1]);
    System.out.println(z.get(3)[0]+" "+z.get(3)[1]);
  }
}
