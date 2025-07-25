document.addEventListener("DOMContentLoaded", () => {
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
            chatHistory.innerHTML += `<div><b>あなた:</b> ${userMsg ?? '(未入力)'}</div>`;
            chatInput.value = '';

            try {
                const response = await fetch(contextPath + '/AiChat', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ message: userMsg }),
                });
                const data = await response.json();

                const aiBox = document.createElement('div');
                aiBox.innerHTML = `<b>AI:</b> `;
                chatHistory.appendChild(aiBox);
                typeText(data.reply ?? '(AI応答未取得)', aiBox);

                chatHistory.scrollTop = chatHistory.scrollHeight;
            } catch (err) {
                chatHistory.innerHTML += `<div style="color:red;">通信エラーが発生しました。</div>`;
                console.error(err);
            }
        }
    });
    async function typeText(text, targetElement) {
        for (let i = 0; i < text.length; i++) {
            targetElement.innerHTML += text[i];
            await new Promise(resolve => setTimeout(resolve, 30));
            targetElement.scrollIntoView({ behavior: "auto", block: "end" });
        }
        chatHistory.scrollTop = chatHistory.scrollHeight;
    }
});
