import { useEffect, useRef, useState } from "react";

const Chat = ({ messages, sendMessage, currentUserId }) => {
  const [message, setMessage] = useState("");
  const messagesEndRef = useRef(null);

  // Auto-scroll à chaque nouveau message
  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (message.trim()) {
      sendMessage(message.trim());
      setMessage("");
    }
  };

  return (
    <div className="flex flex-col  bg-gray-900 p-4 h-full pb-8 rounded-xl w-full">
      <div className="flex-1 overflow-y-auto mb-4">
        {messages.map((msg, index) => (
          <div
            key={index}
            className={`mb-2 p-2 rounded ${
              msg.userId === currentUserId
                ? "bg-yellow-400 text-black ml-auto"
                : "bg-gray-800 text-white"
            }`}
            style={{ maxWidth: "80%" }}
          >
            <div className="text-sm opacity-75">
              {msg.userId === currentUserId ? "You" : "Opponent"}
            </div>
            <div className="break-words">{msg.text}</div>
            {msg.timestamp && (
              <div className="text-xs opacity-50">
                {new Date(msg.timestamp).toLocaleTimeString()}
              </div>
            )}
          </div>
        ))}
        <div ref={messagesEndRef} />
      </div>

      <form onSubmit={handleSubmit} className="flex gap-2">
        <input
          type="text"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          className="flex-1 bg-gray-800 text-white p-2 rounded"
          placeholder="Message"
        />
      </form>
    </div>
  );
};

export default Chat;
