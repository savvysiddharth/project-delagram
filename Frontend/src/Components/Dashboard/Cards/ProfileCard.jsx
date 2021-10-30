import React, { Component } from 'react';
import AuthContext from '../../AuthContext';

export class ProfileCard extends Component {
  static contextType = AuthContext;
  constructor(props) {
    super(props);
  }

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
