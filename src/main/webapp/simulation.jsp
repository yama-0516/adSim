<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>費用対効果シミュレーション</title>
    <link rel="stylesheet" href="style.css">
    <style>
          .simulation-section {
        margin-top: 2.5em;
        padding: 2em 1.5em 1em 1.5em;
        background: #f7fafc;
        border-radius: 12px;
        box-shadow: 0 2px 10px rgba(50,80,120,0.06);
      }
      .simulation-section h2 {
        text-align: center;
        color: #00796b;
        font-size: 1.3em;
        margin-bottom: 1.3em;
      }
      .simulation-section label {
        font-weight: bold;
        color: #00796b;
        margin-top: 0.7em;
        display: block;
      }
      .simulation-section input[type="number"] {
        width: 100%;
        padding: 0.6em;
        border-radius: 5px;
        border: 1px solid #bfc3d9;
        margin-bottom: 1em;
        font-size: 1em;
        background: #fff;
      }
      .simulation-annotation {
        font-size: 0.97em;
        color: #7b7c90;
        margin-bottom: 1em;
        margin-left: 1em;
      }
      .simulation-result {
        background: #eaf7f0;
        border-radius: 7px;
        padding: 1em;
        margin-top: 1em;
        color: #115342;
        text-align: center;
      }
      .emphasize-main {
        font-size: 1.5em;
        color: #00796b;
        font-weight: bold;
        display: block;
        margin: 0.3em 0 0.1em 0;
      }
      .emphasize-label {
        font-size: 1em;
        color: #00796b;
        font-weight: bold;
        margin-top: 0.5em;
        display: inline-block;
      }

      .consult-support-block {
        display: none;
      }
      .consult-support-block.active {
        display: block;
      }
      body {
    margin-left: 5px;
    margin-right: 5px;
 	 }
    
    </style>
    <script>
      // URLパラメータから広告媒体取得
      function getAdTypeFromQuery() {
        const params = new URLSearchParams(window.location.search);
        return params.get("adType") || "Google広告";
      }
      // 媒体ごとの平均値
	const MEDIA_DATA = {
		"Google広告":   { ctr: 0.015, cpm: 800,  cpc: 120, cvr_click: 2.5, cvr_impression: 0.2 },
		"Yahoo!広告":   { ctr: 0.013, cpm: 850,  cpc: 130, cvr_click: 2.0, cvr_impression: 0.18 },
		"Facebook広告": { ctr: 0.010, cpm: 700,  cpc: 150, cvr_click: 2.0, cvr_impression: 0.15 },
		"Instagram広告":{ ctr: 0.012, cpm: 900,  cpc: 160, cvr_click: 1.5, cvr_impression: 0.12 },
		"LINE広告":     { ctr: 0.009, cpm: 600,  cpc: 170, cvr_click: 1.3, cvr_impression: 0.10 },
		"Twitter広告":  { ctr: 0.008, cpm: 650,  cpc: 180, cvr_click: 1.0, cvr_impression: 0.09 }
	};
      let selectedMedia = "Google広告";

      function onMediaChange(radio) {
        selectedMedia = radio.value;
        // 必要なら入力値リセットや注釈更新
        updateCvrNote();
      }

      function getCurrentCVR() {
        var focusNodes = document.getElementsByName("focus");
        for (var i = 0; i < focusNodes.length; i++) {
          if (focusNodes[i].checked) {
            let media = MEDIA_DATA[selectedMedia];
            return focusNodes[i].value === "impression" ? media.cvr_impression : media.cvr_click;
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

        var convRate = getCurrentCVR();
        if (!focus || !budgetInput.value || !unitPriceInput.value) {
          resultDiv.innerHTML = "<span style='color:#b71c1c;'>すべての項目を入力してください。</span>";
          return;
        }
        var budget = parseInt(budgetInput.value, 10);
        var unitPrice = parseInt(unitPriceInput.value, 10);
        var media = MEDIA_DATA[selectedMedia];

        // 媒体ごとの平均値利用
        var impressions = 0;
        var clicks = 0;
        if (focus.value === "impression") {
          impressions = Math.floor((budget / media.cpm) * 1000);
          clicks = Math.floor(impressions * media.ctr);
        } else {
          clicks = Math.floor(budget / media.cpc);
          impressions = Math.floor(clicks / media.ctr);
        }
        var conversions = Math.floor(clicks * (convRate / 100.0));
        var sales = conversions * unitPrice;
        var roi = budget > 0 ? (sales / budget) : 0;

        var focusLabel = (focus.value === "impression") ? "インプレッション重視" : "クリック重視";

        resultDiv.innerHTML =
          "<div class='simulation-result'>" +
          "<strong>" + focusLabel + "の概算（" + selectedMedia + "）</strong><br>" +
          "想定インプレッション数：<b>" + impressions.toLocaleString() + "</b><br>" +
          "想定クリック数：<b>" + clicks.toLocaleString() + "</b><br>" +
          "想定コンバージョン数：<b>" + conversions.toLocaleString() + "</b>（CVR=" + convRate.toFixed(2) + "%）<br>" +
          "<span class='emphasize-label'>売上見込</span><span class='emphasize-main'>" + sales.toLocaleString() + "円</span>" +
          "<span class='emphasize-label'>ROI（売上/費用）</span><span class='emphasize-main'>" + roi.toFixed(2) + "</span><br>" +
          "<span style='font-size:0.9em;'>（客単価：" + unitPrice.toLocaleString() + "円、CTR：" + (media.ctr*100).toFixed(1) + "%、CPM：" + media.cpm.toLocaleString() + "円、CPC：" + media.cpc.toLocaleString() + "円）</span>" +
          "</div>";
      }

      function updateCvrNote() {
        var cvrNote = document.getElementById("cvr-note");
        var cvr = getCurrentCVR();
        if (cvr !== null) {
          cvrNote.textContent = "※このシミュレーションではCVR（成約率）は" + cvr.toFixed(2) + "%として計算します";
        } else {
          cvrNote.textContent = "※まず指標を選択してください";
        }
      }

      window.onload = function() {
        // 初期表示時に診断からの媒体をセット
        const adType = getAdTypeFromQuery();
        if (MEDIA_DATA[adType]) selectedMedia = adType;
        document.getElementById("ad-type").textContent = selectedMedia;
        // ラジオボタンもupdate
      };
    </script>
</head>
<body>
  <div class="container">
    <h1>費用対効果シミュレーション</h1>
    <div>
      診断でオススメされた広告媒体: <span id="ad-type"></span>
    </div>
    <form onsubmit="runSimulation(event)">
	<div>
		<label>媒体を選択：</label>
		<label><input type="radio" name="media" value="Google広告" checked onchange="onMediaChange(this)"> Google広告</label>
		<label><input type="radio" name="media" value="Yahoo!広告" onchange="onMediaChange(this)"> Yahoo!広告</label>
		<label><input type="radio" name="media" value="Facebook広告" onchange="onMediaChange(this)"> Facebook広告</label>
		<label><input type="radio" name="media" value="Instagram広告" onchange="onMediaChange(this)"> Instagram広告</label>
		<label><input type="radio" name="media" value="LINE広告" onchange="onMediaChange(this)"> LINE広告</label>
		<label><input type="radio" name="media" value="Twitter広告" onchange="onMediaChange(this)"> Twitter広告</label>
	</div><br>
	<label>重視する指標</label>
	<label><input type="radio" name="focus" value="impression" onchange="updateCvrNote()"> インプレッション重視</label>
	<label><input type="radio" name="focus" value="click" onchange="updateCvrNote()"> クリック率重視</label>
	<div id="cvr-note" class="simulation-annotation" style="margin-bottom:1em;">※まず指標を選択してください</div><br>
	<label for="budget">概算予算（円）</label>
	<input type="number" id="budget" min="1000" max="100000000" placeholder="例: 50000" required>
	<label for="unitPrice">客単価（円） <span class="simulation-annotation"><br>※1回の購入や成約あたりの平均売上金額</span></label>
	<input type="number" id="unitPrice" min="1" max="1000000" placeholder="例: 5000" required>
	<button type="submit">シミュレーションする</button><br>
    </form>
    <div id="sim-result"></div>
    <div id="consult-support" class="consult-support-block" style="margin-top:2em; text-align:center;">
		<strong>さらに最適な広告運用をご希望の方へ</strong><br>
		<a href="contact.html" class="consult-btn" style="display:inline-block; margin:1em auto; background:#00796b; color:#fff; padding:0.8em 2em; border-radius:7px; font-size:1.2em; text-decoration:none;">具体的に相談する</a>
		<div style="font-size:0.95em; color:#888; margin-top:0.7em;">
    実際の成果は媒体やターゲットによって大きく変動します。<br>
    あなたに最適な運用方法や媒体選定をアドバイスします！お気軽にご相談ください！
  </div>
  </div>
</body>
</html>