import React, { Component } from 'react'
import AuthContext from '../../AuthContext';

/**
 * MemoryCard component creats a card view of the memories created or shared by the user. This component is repeatedly used in Profile and Feed component.
 */
export class MemoryCard extends Component {
  static contextType = AuthContext;

  state = {
    liked: false,
    stars: [], // stars fetched from database
    comment: "", // refers to current user's comment box (input box)
    comments: [] // comments fetched from database
  }

  constructor(props) {
    super(props);
  }

  /**
   * As soon as component is loaded, comment, stars data are updated immediately.
   */
  componentDidMount() {
    this.setState({stars: this.props.stars, comments: this.props.comments});
    // console.log(this.props.stars);
    if(this.props.stars.includes(this.context.profile.username)) {
      this.setState({liked: true});
    }
  }

  /**
   * Adds star for the memory. It immediately updates UI but stars gets updated in db in background asynchronously.
   * @method
   */
  doReact = () => {
    const {username, jwt} = this.context.profile;

    const reactingUserId = username;
    const reactedUserId = this.props.username;

    const url = this.context.url+"view-api/react-on-memory"
    const request = {
      method: "POST",
      headers: {'Content-Type':'application/json', 'authorization': jwt},
      body: JSON.stringify({
        mem_id: this.props.id,
        curr_user_id: reactingUserId,
        react_user_id: reactedUserId,
      })
    }
    fetch(url, request)
    .then(response => response.json())
    .then(data => {
      console.log(data);
      const newStars = this.state.stars;
      newStars.push(username);
      this.setState({liked: true, stars: newStars});
      this.context.initUser();
    })
    .catch(error => {
      console.log(error);
      const newStars = this.state.stars;
      newStars.push(username);
      this.setState({liked: true, stars: newStars});
      this.context.initUser();
    });
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
   * Adds comment for the memory. It immediately updates comment in UI but stars gets updated in db in background asynchronously.
   * @method
   * @param {Object} event Event object
   */
  doComment = (event) => {
    event.preventDefault();
    const {comment} = this.state;
    const {username, jwt} = this.context.profile;
    
    const commentingUserId = username;
    const commentedUserId = this.props.username;

    const url = this.context.url+"view-api/comment-on-memory"
    const request = {
      method: "POST",
      headers: {'Content-Type':'application/json', 'authorization': jwt},
      body: JSON.stringify({
        mem_id: this.props.id,
        curr_user_id: commentingUserId,
        react_user_id: commentedUserId,
        comment_text: comment
      })
    }
    fetch(url, request)
    .then(response => response.json())
    .then(data => {
      console.log(data);
    })
    .catch(error => {
      console.log(error);
      this.context.initUser();
    });
    const newComments = this.state.comments;
    const newComment = {
      commentText : comment,
      commentId : new Date(),
      commentorId: username,
    }
    newComments.push(newComment);
    this.setState({comments: newComments, comment: ""});
  }

  /**
   * Prompts user to add reason for report, and sends the details to backend
   * @method
   */
  doReport = () => {
    let reason = prompt("Write your report details:"); 
    console.log(reason);
    const {username, jwt} = this.context.profile;
    const url = this.context.url+"view-api/report-memory"
    const request = {
      method: "POST",
      headers: {'Content-Type':'application/json', 'authorization': jwt},
      body: JSON.stringify({
        mem_id: this.props.id,
        reason: reason,
        reported_by: username,
        user_id: this.props.username,
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
   * Renders the card view of memory using props and state of the component.
   */
  render() {
    const {image, caption, id, time, name} = this.props;
    const {liked, stars, comment, comments} = this.state;
    return (
      <>
      {
        stars ?
        <div className="image-card">
          <div className="card-head">
            <span className="head-left"> {caption} </span>
            <span className="head-right"> {time.slice(0,11)} </span>
          </div>
          <div className="card-img">
            <img src={image} alt="" />
          </div>
          <div className="card-menu-bar">
            <span className="mem-author">By {name} </span>
            {
              liked?
              <button disabled className="btn-small btn-liked">+1</button> 
              :
              <button onClick={this.doReact} className="btn-small btn-notliked">+1</button> 
            } &nbsp;
            <span>{stars.length} Stars</span>
            <span title="Report" onClick={this.doReport} className="report-link"><ion-icon name="alert-circle-outline"></ion-icon></span>
            <br/>
            {
              comments.length === 0 ?
              <p key="no-comments"></p>
              :
              comments.map((comment) => {
                return <div key={comment.commentId} className="comment-block animate__animated animate__fadeInUp">
                  <hr className="hr-thin" />
                  <span className="comment-text">{comment.commentText} <span className="comment-author"> &#8212; {comment.commentorId}</span> </span>
                </div>
              })
            }
            <form onSubmit={this.doComment}>
              <input 
                maxLength="40" 
                autoComplete="off" 
                value={comment} 
                onChange={this.textInputHandler} 
                name="comment" 
                placeholder="Say something..." 
                className="comment-input-theme" 
                type="text"/>
            </form>
          </div>
        </div>
        :
          "umm..idk"
      }
      </>
    )
  }
}

export default MemoryCard
