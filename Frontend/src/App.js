import './App.css';
import { Login } from './Components/Login/Login';
import { Dashboard } from './Components/Dashboard/Dashboard';
import { SignUp } from './Components/SignUp/SignUp';
import React, { Component } from 'react'
import {
  HashRouter,
  BrowserRouter as Router,
  Switch,
  Route
} from "react-router-dom";

import { AuthProvider } from './Components/AuthContext';

import firebaseConfig from "./firebaseStuff" // just to initialize firebase app
import { initializeApp } from "firebase/app";
initializeApp(firebaseConfig);


/** 
 * Root component, Starting point of this SPA (Single Page application). This component also manages some basic routes for the app. That is "/", "/signup" and "/dashboard". For any other routes it renders an error message.  
*/
export class App extends Component {
  /**
   * Renders Basic Router.
   * @returns Root Component
   */
  render() {
    return (
      <AuthProvider>
        <Router basename="/project-delagram/Frontend/build">
          <Switch>
            <Route exact path="/">
              <Login updateUser={this.updateCurrentUser} />
            </Route>
            <Route exact path="/signup">
              <SignUp />
            </Route>
            <Route path="/dashboard">
              <Dashboard updateUser={this.updateCurrentUser} userDetails={this.state}/>
            </Route>
            <Route path="*">
              <div style={{color:"rgb(209, 90, 90)", padding:"20px"}}>
                <h2>
                  (Assume some random error code here..!)
                </h2>
                <h1>
                  What are you trying to do? Huhh?
                </h1>
                <h3>
                  Just stop the mischief!
                </h3>
              </div>
            </Route>
          </Switch>
        </Router>
      </AuthProvider>
    )
  }
}

export default App