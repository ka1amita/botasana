async function sendMessage() {
  const userMessage = document.getElementById('user-message').value;

  if (userMessage.trim() === '') {
    return;
  }

  const chatMessages = document.getElementById('chat-messages');

  // User's question
  const userBubble = document.createElement('div');
  userBubble.className = 'message user-message';
  userBubble.innerHTML = '<div class="label user-label" th:text="#{label.user}"><span class="user-icon"></span>You</div>'
      + userMessage;
  chatMessages.appendChild(userBubble);

  // Make HTTP request to the API
  try {
    const response = await fetch('http://127.0.0.1:8080/namesana', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({prompt: userMessage}),
    });
    console.log('After fetch');

    if (!response.ok) {
      throw new Error('HTTP request failed');
    }

    const result = await response.json();

    // Simulate a response
    const botMessage = document.createElement('div');
    botMessage.className = 'message bot-message';
    botMessage.innerHTML = '<div class="label bot-label" th:text="#{label.bot}"><span class="bot-icon"></span>Botasana</div>'
        + result.completion;
    botMessage.onclick = function () {
      copyTextToClipboard(botMessage.lastChild.textContent);
    };
    chatMessages.appendChild(botMessage);
  } catch (error) {
    console.error('Error:', error);
    // Handle error, e.g., display a default bot message
  }

  // Clear the user input
  document.getElementById('user-message').value = '';

  // Scroll to the bottom to show the latest messages
  chatMessages.scrollTop = chatMessages.scrollHeight;
}

function copyTextToClipboard(text) {
  // Create a temporary textarea to hold the text to be copied
  const textarea = document.createElement('textarea');
  textarea.value = text;
  document.body.appendChild(textarea);

  // Select and copy the text
  // TODO replace with current solution
  textarea.select();
  document.execCommand('copy');

  // Remove the temporary textarea
  document.body.removeChild(textarea);

  alert('Text copied to clipboard!');
}