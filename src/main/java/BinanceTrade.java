import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

public class BinanceTrade {
    static final String API_KEY = "";
    static final String API_SECRET = "";  // base64 encoded


    static final String BASE_URL = "";
    static final String REQUEST_PATH = "";
    static final String HTTP_METHOD = "";
    static final String BODY = "";

    public static void main(String[] args) throws Exception {

        String timestamp = String.valueOf(Instant.now().getEpochSecond());

        String prehash = timestamp + HTTP_METHOD + REQUEST_PATH + BODY;
        System.out.println("Prehash string: " + prehash);

        // generate HMAC-SHA256 signature
        byte[] secretDecoded = Base64.getDecoder().decode(API_SECRET);
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        hmacSha256.init(new SecretKeySpec(secretDecoded, "HmacSHA256"));
        byte[] hmac = hmacSha256.doFinal(prehash.getBytes(StandardCharsets.UTF_8));
        String signature = Base64.getEncoder().encodeToString(hmac);

        // debug values
        System.out.println("Timestamp: " + timestamp);
        System.out.println("Signature: " + signature);

        //setup request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + REQUEST_PATH))
                .GET()
                .header("CB-ACCESS-KEY", API_KEY)
                .header("CB-ACCESS-SIGN", signature)
                .header("CB-ACCESS-TIMESTAMP", timestamp)
                .header("Content-Type", "application/json")
                .build();

        // request
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());
        System.out.println("Response Body:\n" + response.body());
    }
}
