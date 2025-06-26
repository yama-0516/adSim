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
	<script>
  // CVRのデフォルト値（％）
  const CVR_IMPRESSION = 0.2;
  const CVR_CLICK = 2.5;

  function getCurrentCVR() {
    var focusNodes = document.getElementsByName("focus");
    for (var i = 0; i < focusNodes.length; i++) {
      if (focusNodes[i].checked) {
        return focusNodes[i].value === "impression" ? CVR_IMPRESSION : CVR_CLICK;
      }
    }
    return null;
  }

  function runSimulation(event) {
    event.preventDefault();
    var focusNodes = document.getElementsByName("focus");
    var focus = null;
    for (var i = 0; i < focusNodes.length; i++) {
      if (focusNodes[i].checked) {
        focus = focusNodes[i];
        break;
      }
    }
    var budgetInput = document.getElementById("budget");
    var unitPriceInput = document.getElementById("unitPrice");
    var resultDiv = document.getElementById("sim-result");

    // CVRは自動設定
    var convRate = getCurrentCVR();

    if (!focus || !budgetInput.value || !unitPriceInput.value) {
      resultDiv.innerHTML = "<span style='color:#b71c1c;'>すべての項目を入力してください。</span>";
      document.getElementById("consult-support").classList.remove("active");
      return;
    }
    var budget = parseInt(budgetInput.value, 10);
    var unitPrice = parseInt(unitPriceInput.value, 10);

    // シミュレーション用の基本数値
    var impressions = 0;
    var clicks = 0;
    var ctr = 0.015; // デフォルトCTR1.5%
    var cpm = 800;   // 1,000impあたり800円
    var cpc = 120;   // 1クリックあたり120円

    if (focus.value === "impression") {
      impressions = Math.floor((budget / cpm) * 1000);
      clicks = Math.floor(impressions * ctr);
    } else {
      clicks = Math.floor(budget / cpc);
      impressions = Math.floor(clicks / ctr);
    }
    // 売上シミュレーション
    var conversions = Math.floor(clicks * (convRate / 100.0)); // 成約数
    var sales = conversions * unitPrice;             // 売上
    var roi = budget > 0 ? (sales / budget) : 0;     // 費用対効果（ROI）

    var focusLabel = (focus.value === "impression") ? "インプレッション重視" : "クリック重視";

    resultDiv.innerHTML =
      "<div class='simulation-result'>" +
      "<strong>" + focusLabel + "の概算</strong><br>" +
      "想定インプレッション数：<b>" + impressions.toLocaleString() + "</b><br>" +
      "想定クリック数：<b>" + clicks.toLocaleString() + "</b><br>" +
      "想定コンバージョン数：<b>" + conversions.toLocaleString() + "</b>（CVR=" + convRate.toFixed(2) + "%）<br>" +
      "<span class='emphasize-label'>売上見込</span><span class='emphasize-main'>" + sales.toLocaleString() + "円</span>" +
      "<span class='emphasize-label'>ROI（売上/費用）</span><span class='emphasize-main'>" + roi.toFixed(2) + "</span><br>" +
      "<span style='font-size:0.9em;'>（客単価：" + unitPrice.toLocaleString() + "円、CTR：" + (ctr*100).toFixed(1) + "%、CPM：" + cpm.toLocaleString() + "円、CPC：" + cpc.toLocaleString() + "円）</span><br>" +
      "<span style='font-size:0.9em;color:#888;'>※CVR（成約率）は選択指標に応じて自動設定：" + convRate.toFixed(2) + "%</span>" +
      "</div>";
    document.getElementById("consult-support").classList.add("active");
  }

  // ラジオボタン変更時にCVRの注釈を即反映（任意）
  function updateCvrNote() {
    var cvrNote = document.getElementById("cvr-note");
    var cvr = getCurrentCVR();
    if (cvr !== null) {
      cvrNote.textContent = "※このシミュレーションではCVR（成約率）は" + cvr.toFixed(2) + "%として計算します";
    } else {
      cvrNote.textContent = "※まず指標を選択してください";
    }
  }

	</script>
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
    style="margin-left: 1em;  background:#E53935; color:#fff; padding:0.5em 1.2em;border-radius:6px text-decoration:none">
    この広告で費用対効果シミュレーション
	</a>

</body>
</html>