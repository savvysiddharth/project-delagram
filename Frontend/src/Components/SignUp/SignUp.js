// import React, {useState} from 'react';
import React, { Component } from 'react'
import { Redirect } from 'react-router-dom'
import dp_placeholder from  './dp-placeholder.png'
import Loader from '../Loader/Loader';
import AuthContext from '../AuthContext';

/**
 * Sign Up component. Provides an input form to the user where user fills up their information like their profile picture, name, username, email, password. Using which they can register themselves on Delagram.
 */
export class SignUp extends Component {
  static contextType = AuthContext;
  state = {
    profileImage: dp_placeholder, // default to placeholder image
    name: "",
    username: "",
    password: "",
    password2:"",
    email: "",
    intro: "",
    error: null,
    redirectToLogin: false,
    loading: false
  }

  /**
   * This function simply makes a click to actual Input Element.
   * @method
   */
  onImageContainerClick = () => {
    document.querySelector("#signup-dp-input").click()
  }

  /**
   * Picks up the file selected by user and converts it to base64 image and updates the state of component accordingly.
   * @method
   * @param {Object} event Event object 
   */
  fileSelectedHandler = (event) => {
    const reader = new FileReader();
    let uploadedFile = event.target.files[0]
    if(!uploadedFile) return;  // in case user cancels the upload
    // this.setState({imgFileObject: uploadedFile});
    reader.readAsDataURL(uploadedFile)
    reader.onload = () => {
      if(reader.readyState === 2) {
        this.setState({profileImage: reader.result});
      }
    }
  }

  /**
   * Executes at each keystroke of user for any input text field in the component. As soon as it receives any keystroke from user, this method updates the state of the component accordingly.
   * @method
   * @param {Object} event Event object
   */
  textInputHandler = (event) => {
    const {name, value} = event.target;
    this.setState({[name]:value});
    if(this.state.error) this.setState({error: null});
  }

  /**
   * As soon as the user taps Submit button, this function fires up, and verifies if the inputs are valid or not. Various checks include, testing for "iitb.ac.in" domain name, testing for length of the password.
   * @method
   * @param {Object} event Event object
   */
  verifyInputs = (event) => {
    this.setState({loading: true});
    event.preventDefault();
    const {name, username, password, password2, email, intro, profileImage} = this.state;
    this.setState({error: null}); // resetting all errors
    console.log(this.state)
    if(email.slice(email.indexOf('@')) !== "@iitb.ac.in") {
      this.setState({error: "Only iitb.ac.in domain name is allowed!"});
      this.setState({loading: false});
      return
    }

    if(password.length < 6) {
      this.setState({error: "Password is too short!"});
      this.setState({loading: false});
      return
    }

    if(password !== password2) {
      this.setState({error: "Passwords do not match!"});
      this.setState({loading: false});
      return
    }
    this.uploadToServer({name, username, password, email, intro, profileImage});
  }

  /**
   * After input data from the form is verified, this method is called to send the user details to the backend server. Upon success, user is redirected to login page, otherwise error message is rendered.
   * @method
   * @param {Object} details Contains information about user, such as name, username, password, email etc
   */
  uploadToServer = (details) => {
    const {name, username, password, email, intro, profileImage} = details;
    const request = {
      method: "POST",
      headers: {'Content-Type':'application/json'},
      body: JSON.stringify({
        disp_pic: profileImage,
        name:name,
        user_id:username,
        password:password,
        email_id: email,
        bio:intro
      })
    }
    
    let url = this.context.url+"auth-api/signup/";
    fetch(url, request)
    .then(response => response.json())
    .then(data => {
      this.setState({loading: false});
      if(data.status === 500) {
        this.setState({error: data.message});
        return;
      }
      if(data === true) {
        console.log("YOOOOHOOOO!");
        this.setState({redirectToLogin: true});
      }
    })
    .catch(error => {
      this.setState({loading: false});
      this.setState({redirectToLogin: true});
      console.log('error occurred: '+error);
    })
  }

  /**
   * Renders the input form based on current state of the component.
   */
  render() {
    if(this.state.redirectToLogin)
      return <Redirect to="/" />;

    const {profileImage, name, username, password, password2, email, intro, error} = this.state;
    return (
      <div>
        <div className="SignUpBox">
          <form onSubmit={this.verifyInputs}>
            <div>
              <div className="dp-container animate__animated animate__backInDown" onClick={this.onImageContainerClick} title="Click to upload your pic!">
                <img id="dp-prev" alt="Dp" className="dp-preview" src={profileImage}/>
              </div>
              <input name="dp" onChange={this.fileSelectedHandler} id="signup-dp-input" type="file" accept="image/*" className="dp-input"/>
            </div>
            <div className="animate__animated animate__fadeInLeft">
              <label htmlFor="name">Name</label>
              <input value={name} onChange={this.textInputHandler} name="name" autoComplete="on" id="name" className="input-theme signup-input" type="text" placeholder="Eg: Chandler Bing" required/>
            </div>
            <div className="animate__animated animate__fadeInRight">
              <label htmlFor="username">Username <span>(only way to find you on network)</span></label>
              <input value={username} onChange={this.textInputHandler} name="username" autoComplete="on" id="username" className="input-theme signup-input" type="text" placeholder="Eg: sarcastic_transponster" required/>
            </div>
            <div className="animate__animated animate__fadeInLeft">
              <label htmlFor="email">Email <span>(only @iitb.ac.in domain is allowed)</span></label>
              <input value={email} onChange={this.textInputHandler} name="email" autoComplete="on" id="email" className="input-theme signup-input" type="email" placeholder="Eg: chandlerbing@iitb.ac.in" required/>
            </div>
            <div className="animate__animated animate__fadeInRight">
              <label htmlFor="passwd">Password <span>(minimum 6 characters)</span></label>
              <input value={password} onChange={this.textInputHandler} name="password" autoComplete="on" id="passwd" className="input-theme signup-input" type="password" placeholder="Eg: ************" required/>
            </div>
            <div className="animate__animated animate__fadeInLeft">
              <label htmlFor="passwd-2">Confirm password</label>
              <input value={password2} onChange={this.textInputHandler} name="password2" autoComplete="on" id="passwd-2" className="input-theme signup-input" type="password" placeholder="Eg: ************" required/>
            </div>
            <div className="animate__animated animate__fadeInRight">
              <label htmlFor="intro">Short intro <span>(max. 50 characters)</span></label>
              <textarea value={intro} onChange={this.textInputHandler} name="intro" id="intro" maxLength={50} className="input-theme signup-input" type="text" placeholder="Eg: I make jokes, when I'm uncomfortable." required/>
            </div>
            <div className="errorMessage-signup">
              {error ? error : ""}
            </div>
            <div style={{textAlign:"center"}} className="animate__animated animate__fadeInUp">
                {
                this.state.loading ? 
                <button disabled className="btn btn-green"><Loader />Verifying..</button> : 
                <button type="submit" className="btn btn-green">Join Now!</button>
                }
            </div>
          </form>
        </div>
      </div>
    )
  }
}

export default SignUp