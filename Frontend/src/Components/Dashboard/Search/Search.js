import React, { Component } from 'react';
import AuthContext from '../../AuthContext';
import ProfileCard from '../Cards/ProfileCard';
import Loader from '../../Loader/Loader'

/**
 * Search component, allows currently logged in user to find their buddies by their exact username.
 */
export class Search extends Component {
  static contextType = AuthContext
  state = {
    searchKeyword: "",
    result: null,
    searching: false,
    adding: false,
    notFound: false
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
   * Takes the searched username string from the state of component and attempts to get the user data from the backend.
   * @method
   * @param {Object} event Event object
   */
  doSearch = (event) => {
    event.preventDefault();
    this.setState({searching: true, result: null, notFound: false});
    const {username, jwt} = this.context.profile;
    const {searchKeyword} = this.state;
    const url = this.context.url+"search-api/search-by-id"
    const request = {
      method: "POST",
      headers: {'Content-Type':'application/json', 'authorization': jwt},
      body: JSON.stringify({
        curr_user_id: username,
        user_id: searchKeyword
      })
    }
    fetch(url, request)
    .then(response => response.json())
    .then(data => {
      if(data.emailId) {
        console.log(data);
        this.setState({result: data, searching: false});
      } else {
        this.setState({searching: false, notFound: true});
      }
    })
    .catch(error => {
      console.log(error);
      this.setState({searching: false});
    });
  }

  /**
   * When user finds their buddy on the platform by username, they can click the "Add" button to add them into their circle, and this function fires up to make the relavant api calls.
   * @method
   * @param {Object} event Event object
   */
  doAdd = () => {
    const {result} = this.state;
    const {username, jwt} = this.context.profile;
    const {circle} = this.context.profile;
    if(circle.includes(result.userId)) {
      alert("They are already in your circle!");
      return;
    } else if(result.userId === username) {
      alert("Well, you can't add yourself. Let's not create a paradox!");
      return;
    }
    this.setState({adding: true});
    const url = this.context.url+"search-api/add-in-circle"
    const request = {
      method: "POST",
      headers: {'Content-Type':'application/json', 'authorization': jwt},
      body: JSON.stringify({
        curr_user_id: username,
        targ_user_id: result.userId
      })
    }
    fetch(url, request)
    .then(response => response.json())
    .then(data => {
      console.log(data);
      this.setState({adding: false});
      this.context.initUser();
    })
    .catch(error => {
      this.setState({adding: false});
      console.log(error);
    });
  }

  /**
   * Renders the input form based on current state of the component.
   */
  render() {
    const {searchKeyword, result, searching, notFound, adding} = this.state;
    return (
      <div>
        <div className="animate__animated animate__fadeInDown">
          <h1>Add Your Buddy</h1>
        </div>
        
        <div className="search-box animate__animated animate__fadeInUp">
          <form onSubmit={this.doSearch}>
            <input 
              name="searchKeyword" 
              value={searchKeyword} 
              onChange={this.textInputHandler} 
              placeholder="Search your buddy by username..."
              className="search-input" type="text" />
            <br/>
            {
              searching ?
              <button disabled className="btn btn-cyan findButton"><Loader />Searching..</button>
              :
              <button onClick={this.doSearch} className="btn btn-cyan findButton">Find</button>
            }
          </form>
        </div>
        {
          result ?
          <div className="profile-center search-result animate__animated animate__bounceIn">
            <ProfileCard 
            name={result.name} 
            username={result.userId} 
            email={result.emailId} 
            profilePic={result.displayPicRepresentation} 
            bio={result.bio} />
            {adding?
            <button disabled className="btn btn-green add-friend-btn"><Loader/>Adding..</button>
            :
            <button onClick={this.doAdd} className="btn btn-green add-friend-btn">Add</button>
            }
          </div>
          :
          ""
        }
        {
          notFound ?
          <div className="errorMessage-signup search-fail-msg">
            Sorry, probably your buddy is not on Delagram.
          </div>
          :
          ""
        }
      </div>
    )
  }
}

export default Search
