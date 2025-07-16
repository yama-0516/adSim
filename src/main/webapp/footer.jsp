<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Footer Chat</title>
<style>
  /* チャットパネル */
  #chatbot {
    position: fixed;
    bottom: 80px; /* ボタンと被らないように少し上に */
    right: 20px;
    width: 300px;
    height: 350px;
    border: 1px solid #ccc;
    background: #fff;
    display: none; /* 初期は非表示 */
    flex-direction: column;
    box-shadow: 0 0 8px rgba(0,0,0,0.2);
    border-radius: 8px;
    z-index: 9999;
  }
  #chatHistory {
    flex: 1;
    overflow-y: auto;
    padding: 10px;
    font-size: 14px;
  }
  #chatInput {
    border: none;
    border-top: 1px solid #ccc;
    padding: 10px;
    font-size: 14px;
    box-sizing: border-box;
    width: 100%;
  }

  /* チャットボタン */
  #toggleChat {
    position: fixed;
    bottom: 20px;
    right: 20px;
    background-color: #ff5722;
    color: white;
    border: none;
    border-radius: 50px;
    padding: 10px 20px;
    font-weight: bold;
    cursor: pointer;
    box-shadow: 0 4px 8px rgba(0,0,0,0.2);
    transition: background-color 0.3s;
    z-index: 10000;
  }
  #toggleChat:hover {
    background-color: #e64a19;
  }
</style>
</head>
<body>

<div id="chatbot">
  <div id="chatHistory"></div>
  <input id="chatInput" type="text" placeholder="メッセージを入力..." autocomplete="off" />
</div>
<button id="toggleChat">チャット</button>

<script>
  const chatbot = document.getElementById('chatbot');
  const toggleBtn = document.getElementById('toggleChat');
  const chatHistory = document.getElementById('chatHistory');
  const chatInput = document.getElementById('chatInput');

  toggleBtn.onclick = () => {
    chatbot.style.display = chatbot.style.display === 'flex' ? 'none' : 'flex';
  };

  chatInput.addEventListener('keydown', async (e) => {
    if (e.key === 'Enter' && chatInput.value.trim() !== '') {
      const userMsg = chatInput.value.trim();
      chatHistory.innerHTML += `<div><b>あなた:</b> ${userMsg}</div>`;
      chatInput.value = '';

      try {
        const response = await fetch('/AiChat', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ message: userMsg }),
        });
        const data = await response.json();

        console.log("サーバーの返答内容:", data);
        
        chatHistory.innerHTML += `<div><b>AI:</b> ${data.reply}</div>`;
        chatHistory.scrollTop = chatHistory.scrollHeight;

        } catch (err) {
        chatHistory.innerHTML += `<div style="color:red;">通信エラーが発生しました。</div>`;
        console.error(err);
      }
    }
  });
    document.addEventListener("DOMContentLoaded", () => {
	    const form = document.querySelector("form[action='AiChatServlet']");
	    if (form) {
	      const submitButton = form.querySelector("input[type='submit']");
	      if (submitButton) {
	        form.addEventListener("submit", function () {
	          submitButton.disabled = true;
	          submitButton.value = "送信中...";
	        });
	      }
	    }
	  });

</script>

</body>
</html>
