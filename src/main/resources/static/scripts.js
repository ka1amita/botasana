async function sendMessage() {
  const userPrompt = document.getElementById('user-prompt').value;

  if (userPrompt.trim() === '') {
    return;
  }

  const chatMessages = document.getElementById('chat-messages');

  // User's question
  const userBubble = document.createElement('div');
  userBubble.className = 'message user-message';
  userBubble.innerHTML = '<div class="label user-label" th:text="#{label.user}"><span class="user-icon"></span>You</div>';
  chatMessages.appendChild(userBubble);

  // Simulate typing effect for user's message
  await simulateTyping(userPrompt, userBubble);

  const inputElement = document.getElementById('user-prompt')
  inputElement.scrollIntoView({behavior: "smooth"})

  // Make HTTP request to the API
  try {
    const response = await fetch('http://127.0.0.1:8080/botasana', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({prompt: userPrompt}),
    });

    const result = await response.json();

    // Simulate a response
    const botMessage = document.createElement('div');
    botMessage.className = 'message bot-message';
    botMessage.innerHTML = '<div class="label bot-label" th:text="#{label.bot}"><span class="bot-icon"></span>Botasana</div>';
    botMessage.onclick = function () {
      copyTextToClipboard(botMessage.lastChild.textContent);
    };
    chatMessages.appendChild(botMessage);

    await simulateTyping(result.completion, botMessage);

    // Clear the user input and error message
    document.getElementById('user-prompt').value = '';
  } catch (error) {
    console.error('Error:', error);
    // Handle error, e.g., display a default bot message
    const botMessage = document.createElement('div');
    botMessage.className = 'message bot-message';
    botMessage.innerHTML = '<div class="label bot-label" th:text="#{label.bot}"><span class="bot-icon"></span>Botasana</div>Oupsasana, something went wrongasana';
    botMessage.onclick = function () {
      copyTextToClipboard(botMessage.lastChild.textContent);
    }
    chatMessages.appendChild(botMessage);
  } finally {
    // Scroll to the input to show the latest messages
    inputElement.scrollIntoView({ behavior: "smooth"})
  }
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

// TODO add smooth typing of the response and also some arbitrary text during waiting for the response from the API
// Function to simulate a typing effect by revealing each letter gradually
async function simulateTyping(text, messageContainer) {
  const delay = 100; // Adjust the delay between each letter
  for (let i = 0; i < text.length; i++) {
    messageContainer.innerHTML += text.charAt(i);
    await sleep(delay);
  }
}

// Function to introduce a delay
function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}
