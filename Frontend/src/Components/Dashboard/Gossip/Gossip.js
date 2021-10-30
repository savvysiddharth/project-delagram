import React, { Component } from 'react'
import AuthContext from '../../AuthContext'

/**
 * Gossip component is basically a place to hangout with buddies on Delagram with one to one conversation mode.
 */
export class Gossip extends Component {
  static contextType = AuthContext;

  state = {
    messages: [], // all messages with current buddy
    currentMessage: "", // text in the input box
    buddy: null // the person, whom with user is chatting right now
  }

  /**
   * As soon as this component is loaded the, the selection list of buddies is filled up according to circle of currently logged in user.
   */
  componentDidMount() {
    const {circle} = this.context.profile;
    let select_elem = document.querySelector('#selectBuddy');
    if(circle) {
      circle.forEach((element, index) => { // ref: https://stackoverflow.com/questions/6601028/how-to-populate-the-options-of-a-select-element-in-javascript
        let option_elem = document.createElement('option');              
        option_elem.value = element;// Add index to option_elem
        option_elem.textContent = element; // Add element HTML
        select_elem.appendChild(option_elem); // Append option_elem to select_elem
      });
    }
    this.interval = setInterval(this.fetchMessages, 1000);
  }

  /**
   * Executes at each keystroke of user for any input text field in the component. As soon as it receives any keystroke from user, this method updates the state of the component accordingly.
   * @method
   * @param {Object} event Event object
   */
  textInputHandler = (event) => {
    const {name, value} = event.target;
    this.setState({[name]:value});
  }

  /**
   * Sends the text message to the backend. Before sending, this function preprocesses the the message string by removing unncessary spaces. Also, blank messages are ignored.
   * @method
   * @param {Object} event Event object
   */
  doSend = (event) => {
    event.preventDefault();
    let {messages, currentMessage} = this.state;
    currentMessage = currentMessage.trim();
    if(currentMessage === "" || currentMessage === " ") {
      return;
    }
    const currMsgObject = {
      timestamp: Date.now(),
      text: currentMessage,
      sender: this.context.profile.username
    }
    const updatedMessages = messages;
    updatedMessages.push(currMsgObject);
    this.setState({
      messeges: updatedMessages,
      currentMessage: ""
    });

    const {username, jwt} = this.context.profile;

    const url = this.context.url+"chat-api/sendMessage"
    const request = {
      method: "POST",
      headers: {'Content-Type':'application/json', 'authorization': jwt},
      body: JSON.stringify({
        sender: username,
        receiver: this.state.buddy,
        text: currentMessage,
      })
    }
    fetch(url, request)
    .then(response => response.json())
    .then(data => {
      console.log(data);
    })
    .catch(error => {
      console.log(error);
    });
  }

  /**
   * Fetches latest messages from the backend and updates the state of the component. This function is scheduled to run at every 100ms.
   * @method
   */
  fetchMessages = () => {
    const selectedBuddy = this.state.buddy;
    if(!selectedBuddy) {
      return;
    }
    const {username, jwt} = this.context.profile;
    const url = this.context.url+"chat-api/getMessages"
    const request = {
      method: "POST",
      headers: {'Content-Type':'application/json', 'authorization': jwt},
      body: JSON.stringify({
        user: username,
      })
    }
    fetch(url, request)
    .then(response => response.json())
    .then(data => {
      // console.log("messages:")
      // console.log(data);
      const newMessages = [];
      for(let item of data) {
        if(item.receiver === selectedBuddy || item.sender === selectedBuddy) {
          newMessages.push(item);
        }
      }

      this.setState({messages: newMessages});
    })
    .catch(error => {
      console.log(error);
    });
  }

  /**
   * As the user selects a buddy to chat with from the selection list, the chat box is updated accordingly.
   * @param {Object} event Event object
   */
  selectBuddy = (event) => {
    const selectedBuddy = event.target.value;
    console.log(selectedBuddy);
    if(selectedBuddy === "0") {
      console.log("here?")
      this.setState({buddy: null});
      return;
    }
    this.setState({buddy: selectedBuddy}, this.fetchMessages);
  }

  /**
   * As the user chats with their buddy, the messages are bound to cross the size of chat box element. So, for the convenience of user, this function does that automatically, so that user won't have to scroll down to see the latest messages.
   * @method
   */
  scrollToBottom = () => {
    const mydiv = document.querySelector("div.messages-area");
    mydiv.scrollTo({ left: 0, top: mydiv.scrollHeight });
  }

  /**
   * As soon as new messages are arrived, scrollToBottom() function is called.
   */
  componentDidUpdate() {
    if(this.state.buddy) {
      this.scrollToBottom();
    }
  }

  /**
   * When the user leaves this component, the fetchMessages() schedule is stopped, so that it doesn't run in background while user is exploring other components.
   */
  componentWillUnmount() {
    clearInterval(this.interval);
  }

  /**
   * Renders the input form based on state of the component.
   */
  render() {
    const {currentMessage, messages, buddy} = this.state;
    const {username} = this.context.profile;
    // console.log(messages)
    return (
      <div>
        <h1 className="animate__animated animate__fadeInDown">Gossip Room</h1>
        <div className="gossip-select animate__animated animate__fadeInUp">
          <select id="selectBuddy" onChange={this.selectBuddy}>
            <option value="0">Select a buddy to gossip with..</option>       
          </select>
        </div>
        { buddy ? 
          <div className="chat-box animate__animated animate__fadeInUp">
          <div className="messages-area">
            {
              messages.length !== 0 ?
              messages.map((item) => {
                if(item.sender === username) {
                  return (
                    <div key={item.timestamp + item.receiver + item.sender} className="animate__animated animate__zoomIn animate__fast">
                      <div className="message-text-box">
                        <div className="message-bubble bubble-right">
                          {item.text}
                        </div>
                      </div>
                    </div>
                  )
                } else {
                  return (
                    <div key={item.timestamp + item.receiver + item.sender} className="animate__animated animate__zoomIn animate__fast">
                      <div className="message-text-box">
                        <div className="message-bubble bubble-left">
                          {item.text}
                        </div>
                      </div>
                    </div>
                  )
                }
              })
              :
              <center>
                No messages to display
              </center>
            }
          
          </div>
          <form onSubmit={this.doSend}>
            <input maxLength="60" autoComplete="off" value={currentMessage} name="currentMessage" onChange={this.textInputHandler} 
            placeholder="Type your message here.. (Hit enter to send)" 
            className="chat-input" type="text" />
          </form>
        </div>
        :
        <div className="comment-author animate__animated animate__fadeInUp animate__slow">
          <p>Select a buddy first, to start chatting.</p>
        </div>
        }
        
      </div>
    )
  }
}

export default Gossip


{/* <div className="animate__animated animate__zoomIn animate__fast">
  <div className="message-text-box">
    <div className="message-bubble bubble-right">
      {item}
    </div>
  </div>
</div> */}