<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>広告媒体提案</title>
    <link rel="stylesheet" href="style.css">
    <style>
      body {
        margin-left: 5px;
        margin-right: 5px;
      }
    </style>
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
<div class="container">
    <h1>診断結果</h1>

    <div class="suggestion">
        <%= ((service.AdSuggestionService.SuggestionResult)request.getAttribute("result")).getSuggestion() %>
    </div>

    <div class="reason">
        理由：<%= ((service.AdSuggestionService.SuggestionResult)request.getAttribute("result")).getReason() %>
    </div>

    <%
        String url = ((service.AdSuggestionService.SuggestionResult)request.getAttribute("result")).getEvidenceUrl();
        if (url != null && !url.isEmpty()) {
    %>
        <div class="evidence">
            参考データ: <a href="<%= url %>" target="_blank"><%= url %></a>
        </div>
    <%
        }
    %>

    <a href="Survey">もう一度診断する</a>

    <% String adType = ((service.AdSuggestionService.SuggestionResult)request.getAttribute("result")).getSuggestion(); %>
    <a class="custom-button" href="simulation.jsp?adType=<%= java.net.URLEncoder.encode(adType, "UTF-8") %>"
       style="margin-left: 1em; background:#E53935; color:#fff; padding:0.5em 1.2em;border-radius:6px; text-decoration:none;">
        この広告で費用対効果シミュレーション
    </a>
</div>
</body>
</html>