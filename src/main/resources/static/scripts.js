const botsBubbleInnerHTML = `<div class="label bot-label"><span class="bot-icon"></span>${botLabel}</div>`;

const usersBubbleInnerHTML = `<div class="label user-label"><span class="user-icon"></span>${userLabel}</div>`;

const thinkingText = 'Hmmm';

const typingDelay = 100; // Adjust the delay between each letter

const erasingDelay = 150; // Adjust the delay between each letter

async function sendMessage() {
  const userPrompt = document.getElementById('user-prompt').value;

  if (userPrompt.trim() === '') {
    return;
  }

  const chatMessages = document.getElementById('chat-messages');

  // User's question
  const userBubble = document.createElement('div');
  userBubble.className = 'message user-message';
  userBubble.innerHTML = usersBubbleInnerHTML + userPrompt;
  chatMessages.appendChild(userBubble);

  document.getElementById('user-prompt').value = ' '; // erase the user's prompt but avoid displaying the placeholder

  const inputElement = document.getElementById('user-prompt')
  inputElement.scrollIntoView({behavior: "smooth"})

  const botBubble = document.createElement('div');
  botBubble.className = 'message bot-message';
  botBubble.innerHTML = botsBubbleInnerHTML;
  chatMessages.appendChild(botBubble);
  // Simulate a delay before fetching the actual response

  await simulateTyping(thinkingText, botBubble);
  // Make HTTP request to the API
  let botsText;

  try {
    const response = await fetch(apiUrl, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({prompt: userPrompt}),
    });

    const result = await response.json();

    // Simulate a response
    botBubble.onclick = function () {
      copyTextToClipboard(botBubble.lastChild.textContent);
    };
    botsText = result.completion;
    // Clear the user input and error message
  } catch (error) {
    console.error('Error:', error);
    // Handle error, e.g., display a default bot message
    botsText = 'Oupsasana, connectionsana is brokensana';
  } finally {
    await simulateRetypingBotsText(botsText, botBubble);

    document.getElementById('user-prompt').value = '';

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
  /*[- */
  // TODO replace with current solution
  /* -]*/
  textarea.select();
  document.execCommand('copy');

  // Remove the temporary textarea
  document.body.removeChild(textarea);

  alert('Text copied to clipboard!');
}

// Function to simulate a typing effect by revealing each letter gradually
async function simulateTyping(text, bubble) {
  for (let i = 0; i < text.length; i++) {
    bubble.innerHTML += text.charAt(i);
    await sleep(typingDelay);
  }
}

async function simulateRetypingBotsText(botsText, botBubble) {
  await simulateErasingOfThinkingText(botBubble);
  await simulateTypingOfBotsText(botsText, botBubble);
}

async function simulateTypingOfBotsText(text, bubble) {
  bubble.innerHTML = botsBubbleInnerHTML + text.charAt(0); // Replaces the last letter that is left behind to avoid shrinking
  await sleep(typingDelay);
  await simulateTyping(text.substring(1), bubble)
}

async function simulateErasingOfThinkingText(bubble) {
  const text = thinkingText;
  for (let i = text.length; i > 0; i--) {
    bubble.innerHTML = botsBubbleInnerHTML + text.substring(0, i);
    await sleep(erasingDelay);
  }
}

// Function to introduce a delay
function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}
