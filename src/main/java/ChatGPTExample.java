import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ChatGPTExample {
    public static void main(String[] args) throws Exception {
        // 環境変数からAPIキーを取得
        String apiKey = System.getenv("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("ERROR: 環境変数OPENAI_API_KEYが設定されていません。");
            return;
        }

        String jsonPayload = """
        {
          "model": "gpt-4o-mini",
          "messages": [
            {"role": "user", "content": "広告の改善案を教えてください"}
          ]
        }
        """;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
    }
}