// Should be renamed as CurrentUserContex
// Contains the details of current user, eg: name, username, email, memories, JWT
// Also, all authentication related functions are implemented here..
import React, { Component } from 'react';
import { getAuth, signInWithEmailAndPassword, onAuthStateChanged } from "firebase/auth";

const SERVER_URL = "https://delagram-api.herokuapp.com/";

const defaultValue = {
  profile: {
    username: "unknown",
    name: "unknown",
    email: "unknown",
    userLoggedIn: false,
  },
  url : SERVER_URL
}

const AuthContext = React.createContext(defaultValue);

/**
 * Passes down all the data in AuthContext to its children. This Context maintains, user data like name, email, memories, profile picture, circle of user.
 */
export class AuthProvider extends Component {

  state = {
    profile: {
      username: "unknown",
      name: "unknown",
      email: "unknown",
      memories: [],
      dp: null,
    },
    url: SERVER_URL,
    userLoggedIn: false,
  }

  /**
   * Makes call to Firebase SDK with given inputs.
   * @method 
   * @param {String} email Email ID of user
   * @param {String} password Password of the user
   * @returns {Promise} Promise on signInWithEmailAndPassword() from Firebase
   */
  loginUser = (email, password) => { 
    const auth = getAuth();
    return signInWithEmailAndPassword(auth, email, password);
  }

  /**
   * Fetches current user metadata (dp, bio, memories etc)
   * @method
   * @param {Function} onValid Function to run for a valid user
   * @param {Function} onInvalid Function to run for an invalid user
   */
  initUser = (onValid, onInvalid) => {
    const auth = getAuth();
    onAuthStateChanged(auth, (user) => {
      if (user) { // user is signed in
        user.getIdToken().then(jwt => {
          if(!user.emailVerified) {
            if(onInvalid) onInvalid();
            return;
          }

          const req2 = {
            method: "GET",
            headers: {'Content-Type':'application/json', 'authorization': jwt},
          }
          let url = this.state.url+"auth-api/verify-token";
          fetch(url, req2)
          .then(response => {
            return response.json();
          })
          .then(data => {
            data.uploadedMemories.reverse();
            const profile = {
              name: data.name,
              username: user.uid,
              email: user.email,
              jwt: jwt,
              memories: data.uploadedMemories,
              profilePic: data.displayPicRepresentation,
              bio: data.bio,
              circle: data.circle
            }
            // console.log("AUTH_CONTEXT: ",data);
            this.setState({profile, userLoggedIn: true});
            if(onValid) onValid();
          })
          .catch(error => {
            console.log('error occurred: '+error);
          })
        });
      } else { // user is signed out
        if(onInvalid) onInvalid();
      }
    });
  }

  /**
   * @method
   * @returns Renders AuthContext and passing it to all its children via props.
   */
  render() {
    const {profile, userLoggedIn, url} = this.state;
    const {initUser, loginUser} = this;
    return (
      // sets AuthProvider with following data to all its children
      <AuthContext.Provider value={{
        profile,
        userLoggedIn,
        url,
        initUser,
        loginUser,
      }}>
        {this.props.children}
      </AuthContext.Provider>
    )
  }
}

export default AuthContext;