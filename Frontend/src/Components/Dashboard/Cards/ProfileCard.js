import React, { Component } from 'react';
import AuthContext from '../../AuthContext';

/**
 * ProfileCard component, that displays user's details in small card format. This component is currently used "Profile" and "Add a Buddy".
 */
export class ProfileCard extends Component {
  static contextType = AuthContext;
  constructor(props) {
    super(props);
  }

  /**
   * Renders the input form based on props of the component.
   */
  render() {
    const {name, username, email, profilePic, bio} = this.props;
    return (
      <div className="profile-card">
        <h1>{name}</h1>
        <div className="dp-profile-parent">
          <div className="dp-container-profile">
            <img id="dp-prev" alt="Dp" className="dp-image-profile" src={profilePic}/>
          </div>
        </div>
        <div className="profile-data">
          <b><p>{username}</p></b>
          <p>{bio}</p>
          <p style={{color: 'gray'}}>({email})</p>
        </div>
      </div>
    )
  }
}

export default ProfileCard
