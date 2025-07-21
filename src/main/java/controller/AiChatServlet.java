package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AiChatServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        System.out.println("AIチャットリクエスト受信 (Gemini)");

        try (BufferedReader reader = request.getReader()) {
            JsonObject jsonRequest = JsonParser.parseReader(reader).getAsJsonObject();
            String userMessage = jsonRequest.get("message").getAsString();

            if (userMessage == null || userMessage.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"reply\":\"メッセージが空です。\"}");
                return;
            }

            String aiReply = callGeminiAPI(userMessage);

            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("reply", aiReply);
            response.getWriter().write(jsonResponse.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"reply\":\"エラーが発生しました。\"}");
        }
    }

    private String callGeminiAPI(String message) throws IOException {
        String apiKey = System.getenv("GEMINI_API_KEY");
        System.out.println("=== Geminiチャットデバッグ情報 ===");
        System.out.println("APIキー設定状況: " + (apiKey != null ? "設定済み" : "未設定"));
        if (apiKey != null) {
            System.out.println("APIキー長: " + apiKey.length() + "文字");
            System.out.println("APIキー先頭: " + apiKey.substring(0, Math.min(10, apiKey.length())) + "...");
        }
        if (apiKey == null || apiKey.isEmpty()) {
            System.out.println("エラー: Gemini APIキーが設定されていません");
            return "申し訳ございません。AI機能が現在利用できません。環境設定をご確認ください。\n\n設定方法:\n1. CMDで「set GEMINI_API_KEY=your_api_key」を実行\n2. サーバーを再起動してください。";
        }

        System.out.println("送信メッセージ: " + message);

        String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(endpoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000); // 10秒
            conn.setReadTimeout(30000);    // 30秒

            // Gemini用リクエストボディ
            JsonObject requestBody = new JsonObject();
            JsonArray contents = new JsonArray();
            JsonObject content = new JsonObject();
            JsonArray parts = new JsonArray();
            JsonObject part = new JsonObject();
            part.addProperty("text", message);
            parts.add(part);
            content.add("parts", parts);
            contents.add(content);
            requestBody.add("contents", contents);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int statusCode = conn.getResponseCode();
            System.out.println("Gemini APIレスポンスコード: " + statusCode);

            if (statusCode >= 400) {
                String errorResponse = readErrorResponse(conn);
                System.out.println("Gemini APIエラー " + statusCode + ": " + errorResponse);
                return "申し訳ございません。Gemini APIでエラーが発生しました。\n\n" + errorResponse;
            }

            String responseBody = readResponse(conn);
            System.out.println("Geminiレスポンス: " + responseBody);

            JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonArray candidates = json.getAsJsonArray("candidates");
            if (candidates == null || candidates.size() == 0) {
                return "申し訳ございません。Gemini APIから有効なレスポンスが返されませんでした。";
            }
            JsonObject contentObj = candidates.get(0).getAsJsonObject().getAsJsonObject("content");
            if (contentObj == null) {
                return "申し訳ございません。Gemini APIのレスポンス形式が不正です。";
            }
            JsonArray partsArr = contentObj.getAsJsonArray("parts");
            if (partsArr == null || partsArr.size() == 0) {
                return "申し訳ございません。Gemini APIのレスポンスに回答が含まれていません。";
            }
            String result = partsArr.get(0).getAsJsonObject().get("text").getAsString();
            System.out.println("Gemini AI回答: " + result);
            return result;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private String readResponse(HttpURLConnection conn) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    private String readErrorResponse(HttpURLConnection conn) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getErrorStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }
}