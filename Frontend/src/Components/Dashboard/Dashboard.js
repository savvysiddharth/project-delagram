import AuthContext from '../AuthContext';
import React, { Component } from 'react'
import {
  Switch,
  Route,
  NavLink,
  Redirect
} from "react-router-dom";
import { getAuth, signOut } from "firebase/auth";
import MemoryUpload from './MemoryUpload/MemoryUpload';
import Profile from './Profile/Profile';
import Search from './Search/Search';
import Feed from './Feed/Feed';
import Gossip from './Gossip/Gossip';

/**
 * Dashboard component provides routes to user related components such as Gossip, Search, Feed etc.
 */
export class Dashboard extends Component {
  static contextType = AuthContext;
  state = {
    profile: {
      username: "fetching",
      name: "fetching",
      email: "fetching",
      jwt: "fetching",
    },
    loggedIn: true
  }

  constructor(props) {
    super()
  }

  /**
   * As soon as this component is loaded. This code checks whether the user is logged in or not.
   * @method
   */
  componentDidMount() {
    const {initUser} = this.context;
    initUser(
      () => { // on success
        const {profile} = this.context;
        this.setState ({
          profile
        });
      }, 
      () => { // on failure
        this.setState({loggedIn: false});
      });
  }

  /**
   * Simply calls the signOut function from Firebase SDK
   * @method
   */
  signOutUser = () => {
    const auth = getAuth();
    signOut(auth).then(() => {
      // Sign-out successful.
    }).catch((error) => {
      console.log(error);
      });
  }

  render() {
    if(!this.state.loggedIn)
      return <Redirect to="/" />;
    
    return (
      <>
      <div className="animate__animated animate__fadeIn">
        <ul className="theNav">
          <NavLink activeClassName="activeMenu" to="/dashboard" exact>
            <li title="Me!"><span><ion-icon name="person-outline"></ion-icon></span></li>
          </NavLink>
          <NavLink activeClassName="activeMenu" to="/dashboard/feed" exact>
            <li title="Feed"><span><ion-icon name="albums-outline"></ion-icon></span></li>
          </NavLink>
          <NavLink activeClassName="activeMenu" to="/dashboard/gossip" exact>
            <li title="Gossip"><span><ion-icon name="chatbubbles-outline"></ion-icon></span></li>
          </NavLink>
          <NavLink activeClassName="activeMenu" to="/dashboard/memoryupload" exact>
            <li title="Memory Upload"><span><ion-icon name="cloud-upload-outline"></ion-icon></span></li>
          </NavLink>
          <NavLink activeClassName="activeMenu" to="/dashboard/search" exact>
            <li title="Add friend"><span><ion-icon name="person-add-outline"></ion-icon></span></li>
          </NavLink>
        </ul>
      </div>
      <div>
        <button 
          onClick={this.signOutUser}
          className="btn btn-red sign-out-btn animate__animated animate__fadeInRight"
          style={{
              paddingLeft:"15px",
              paddingRight:"13px",
              paddingBottom:"6px",
            fontSize:"22px",
          }}
          title="Log Out"
        >
          <ion-icon name="log-out-outline"></ion-icon></button>
      </div>
      <div className="main-content">
        <Switch>
          <Route exact path="/dashboard" component={Profile} />
          <Route exact path="/dashboard/memoryupload" component={MemoryUpload}/>
          <Route exact path="/dashboard/feed" component={Feed}/>
          <Route exact path="/dashboard/search" component={Search}/>
          <Route exact path="/dashboard/gossip" component={Gossip}/>
        </Switch>
      </div>
    </>
    )
  }
}

export default Dashboard