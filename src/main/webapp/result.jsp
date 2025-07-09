<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="service.AdSuggestionService" %>
<%@ page import="java.util.List" %>

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

    <%
		AdSuggestionService.SuggestionResult suggestionResult = (AdSuggestionService.SuggestionResult) request.getAttribute("suggestionResult");
		String suggestion = suggestionResult.getSuggestion();
		String reason = suggestionResult.getReason();
		List<String> evidenceUrls = suggestionResult.getEvidenceUrls();
		String encodedAdType = (String) request.getAttribute("encodedAdType");
	%>

    <div class="suggestion">
        <%= (suggestion != null && !suggestion.isEmpty()) ? suggestion : "診断結果が取得できませんでした。" %>
    </div>

    <div class="reason">
        理由：
        <%= (reason != null && !reason.isEmpty()) ? reason : "理由が設定されていません。" %>
    </div>

	<% if (evidenceUrls != null && !evidenceUrls.isEmpty()) { %>
    	<div class="evidence">
			参考データ:
			<ul>
				<% for (String url : evidenceUrls) { %>
					<li><a href="<%= url %>" target="_blank"><%= url %></a></li>
            <% } %>
        </ul>
    </div>
<% } %>

    <a href="Survey">もう一度診断する</a>

    <% if (encodedAdType != null && !encodedAdType.isEmpty()) { %>
        <a class="custom-button"
           href="simulation.jsp?adType=<%= encodedAdType %>"
           style="margin-left: 1em; background:#E53935; color:#fff; padding:0.5em 1.2em;border-radius:6px; text-decoration:none;">
            この広告で費用対効果シミュレーション
        </a>
    <% } %>
</div>
</body>
</html>