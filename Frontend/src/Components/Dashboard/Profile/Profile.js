import React, { Component } from 'react'
import AuthContext from '../../AuthContext';
import Loader from '../../Loader/Loader'
import MemoryCard from '../Cards/MemoryCard';
import ProfileCard from '../Cards/ProfileCard';

/**
 * Profile component shows a profile card of currently logged in users, and lists the memories of their memories in interactive card format.
 */
export class Profile extends Component {
  static contextType = AuthContext;
  state = {
    profile: {}, // will store the profile information in this state
    loggedIn: true // if loggedOut user gets redirected to Login Page
  }

  /**
   * As the component is loaded, the user data is initialized using the functions from AuthContext, and state is updated accordingly.
   */
  componentDidMount() {
    const {initUser, userLoggedIn} = this.context;
    if(userLoggedIn) {
      const {profile} = this.context;
      this.setState ({
        profile,
        loggedIn: true,
      });
      return;
    }
    initUser(() => { // on success
      const {profile} = this.context;
      this.setState ({
        profile,
        loggedIn: true,
      });
    }, () => { // on failure
      this.setState({loggedIn: false});
    });
  }

  /**
   * Renders the input form based on current state of the component.
   */
  render() {
    const {memories, name, username, email, profilePic, bio} = this.context.profile;
    return (
      <>
      {memories && profilePic ? 
        <div className="">
          <div className="profile-center animate__animated animate__fadeInDown">
            <ProfileCard name={name} username={username} email={email} profilePic={profilePic} bio={bio}/>
          </div>
          <div className="animate__animated animate__fadeInUp">
            {
              memories.length === 0 ? <center>No memories have been uploaded yet.</center> :
              memories.map(item => {
                return <MemoryCard 
                        key={item.memId}
                        image={item.imageURI} 
                        caption={item.memCaption}
                        id={item.memId}
                        stars={item.listOfStars}
                        time={item.timeStamp}
                        comments={item.comments}
                        name={name}
                        username={username}
                      />
              })
            }
          </div>
        </div>
        :
        <div className="loader-center">
          <Loader /> Loading..
        </div>
      }
      </>
    )
  }
}

export default Profile
