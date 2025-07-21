<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <div id="chatbot">
    <div id="chatHistory"></div>
    <input id="chatInput" type="text" placeholder="メッセージを入力..." autocomplete="off" />
  </div>
  <button id="toggleChat">チャット</button>

  <script>
    const contextPath = "<%= request.getContextPath() %>";
  </script>
  <script src="chatbot.js"></script>

  <style>
    #chatbot {
      position: fixed;
      bottom: 80px;
      right: 20px;
      width: 300px;
      height: 350px;
      border: 1px solid #ccc;
      background: #fff;
      display: none;
      flex-direction: column;
      box-shadow: 0 0 8px rgba(0, 0, 0, 0.2);
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
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
      transition: background-color 0.3s;
      z-index: 10000;
    }

    #toggleChat:hover {
      background-color: #e64a19;
    }
  </style>