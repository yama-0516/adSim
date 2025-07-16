package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class AiChatServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        
        System.out.println("POSTリクエスト受信");

        try (BufferedReader reader = request.getReader()) {
            JsonObject jsonRequest = JsonParser.parseReader(reader).getAsJsonObject();
            String userMessage = jsonRequest.get("message").getAsString();

            // OpenAIに送信 → 返答取得（ここは別メソッドで実装してください）
            String aiReply = callOpenAIAPI(userMessage);

            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("reply", aiReply);
            response.getWriter().write(jsonResponse.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"reply\":\"エラーが発生しました。\"}");
        }
    }

    private String callOpenAIAPI(String message) throws IOException {
        String apiKey = System.getenv("OPENAI_API_KEY");
        System.out.println("環境変数 OPENAI_API_KEY = " + apiKey);
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("OpenAI APIキーが設定されていません。");
        }

        System.out.println("送信メッセージ: " + message);
        
        int maxRetries = 3;
        int retryDelayMillis = 2000; // 2秒待つ

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL("https://api.openai.com/v1/chat/completions");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + apiKey);
                conn.setDoOutput(true);

                // リクエストボディ作成
                JsonObject requestBody = new JsonObject();
                requestBody.addProperty("model", "gpt-3.5-turbo");

                JsonArray messages = new JsonArray();
                JsonObject userMsg = new JsonObject();
                userMsg.addProperty("role", "user");
                userMsg.addProperty("content", message);
                messages.add(userMsg);
                requestBody.add("messages", messages);

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = requestBody.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int statusCode = conn.getResponseCode();

                if (statusCode == 429) {
                    System.out.println("429 Too Many Requests - 再試行中...");
                    Thread.sleep(retryDelayMillis); // リトライまで待機
                    continue; // リトライ
                } else if (statusCode >= 400) {
                    throw new IOException("APIエラー: " + statusCode);
                }

                // レスポンス読み込み
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }
                    
                    System.out.println("OpenAIレスポンス: " + response.toString());

                    JsonObject json = JsonParser.parseString(response.toString()).getAsJsonObject();
                    return json.getAsJsonArray("choices")
                               .get(0).getAsJsonObject()
                               .getAsJsonObject("message")
                               .get("content").getAsString();
                }

            } catch (IOException | InterruptedException e) {
                if (attempt == maxRetries) {
                    throw new IOException("OpenAI API呼び出しが失敗しました（リトライ済）", e);
                }
            } finally {
                if (conn != null) conn.disconnect();
            }
        }

        throw new IOException("OpenAI API呼び出しに失敗しました（全リトライ失敗）");
    }
}