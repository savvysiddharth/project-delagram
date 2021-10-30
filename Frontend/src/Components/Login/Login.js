import React, { Component } from 'react'
import { Link } from 'react-router-dom'
import logo from './logo.png'
import { Redirect } from 'react-router-dom';
import { getAuth, signOut } from "firebase/auth";
import AuthContext from '../AuthContext';
import Loader from '../Loader/Loader';

/**
 * Login Component, allows user to enter their email and password and login into the Delagram.
 */
export class Login extends Component {
  static contextType = AuthContext;
  state = {
    username: "",
    password: "",
    loggedIn: false,
    loading: false,
    error: null,
  }

  // to do stuff just after component is loaded
  componentDidMount() {
    console.log("CONTEXT:",this.context);
    const {initUser} = this.context;
    this.setState({loading: true});
    initUser(() => {
      console.log(this.context);
      this.setState({loggedIn: true, loading: false});
    },
    ()=> {
      this.setState({loading: false});
    });
  }

  
  /**
   * Executes at each keystroke of user for any input text field in the component. As soon as it receives any keystroke from user, this method updates the state of the component accordingly.
   * @param {event} event Event object
   */
  InputHandler = (event) => {
    const {name, value} = event.target;
    this.setState({[name]:value});
    if(this.state.error) this.setState({error: null});
  }

  /**
   * As the user taps on "Let me in!" button, this function fires up, and attempts to login user using Firebase SDK functions, with the credentials provided by the user. On success, user is redirected to Dashboard, else error message is rendered.
   * @param {event} event Event object
   */
  proceedToLogin = (event) => {
    this.setState({loading: true});
    event.preventDefault();
    const {username, password} = this.state;
    const email = username; // renaming, bcoz login via email+password, and not username+password
    console.log(email, password);
    const {loginUser, initUser} = this.context;

    loginUser(email, password)
      .then((userCredential) => { // Signed in 
        // const user = userCredential.user;
        initUser(() => {
          console.log(this.context);
          this.setState({loading: false});
          this.setState({redirectToDashboard: true});
        },
        () => {
          this.setState({loading: false});
          this.setState({error: "Account not yet verified!"});
          const auth = getAuth();
            signOut(auth).then(() => {
              // Sign-out successful.
            }).catch((error) => {
              console.log(error);
              }); 
        });

      })
      .catch((error) => { // Unsuccesssful Sign In
        this.setState({loading: false});
        this.setState({error: "Invalid login!"});
        const errorCode = error.code;
        const errorMessage = error.message;
        console.log(errorCode, errorMessage);
      });
  }

  /**
   * Renders the Login Component using the current state of the component.
   */
  render() {
    if(this.state.loggedIn) // if already logged in, jump to dashboard
      return <Redirect to="/dashboard" />;

    let btnStyle = {
      marginBottom: "20px"
    }
  
    let joinBtnStyle = {
      marginBottom: "20px",
      fontSize: "15px",
      fontWeight: "200"
    }
  
    let hrStyle = {
      marginBottom: "20px",
      borderColor: "rgb(90,90,90)",
      width:"400px"
    }
    const {username, password, error} = this.state;
    return (
        <div className="loginBox">
          <img className="heroLogo animate__animated animate__backInDown" src={logo} alt="Delagram Logo" />
          <h1 className="heroTitle animate__animated animate__fadeInLeft">Delagram</h1>
          <p className="heroSubtitle animate__animated animate__fadeInRight">[ Exclusively for IIT Bombay ]</p>
          <form onSubmit={this.proceedToLogin}>
            <input required value={username} onChange={this.InputHandler} name="username" className="input-theme animate__animated animate__fadeInLeft animate__delay-1s" type="email" placeholder="Email" />
            <input required value={password} onChange={this.InputHandler} name="password" className="input-theme animate__animated animate__fadeInRight animate__delay-1s"  type="password" placeholder="Password" />
            
            <div className="errorMessage-signup">
              {error ? error : ""}
            </div>

            {this.state.loading ? 
            <button disabled style={btnStyle} className="btn btn-cyan animate__animated animate__fadeInUp animate__delay-1s"><Loader />Verifying..</button>
            :
            <button type="submit" style={btnStyle} className="btn btn-cyan animate__animated animate__fadeInUp animate__delay-1s">Let me in!</button> 
            }
          </form>
          <div className="animate__animated animate__fadeInUp animate__delay-2s">
            <hr style={hrStyle} />
            <p>Haven't joined the cult yet?</p> <br/>
            <Link to="/signup">
              <button style={joinBtnStyle} className="btn btn-blue">Verify your identity now!</button>
            </Link>
          </div>
        </div>
    )
  }
}

export default Login;