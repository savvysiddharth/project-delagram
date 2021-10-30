import React, { Component } from 'react';
import MemoryCard from '../Cards/MemoryCard';
import Loader from '../../Loader/Loader';
import AuthContext from '../../AuthContext';

/**
 * Feed Component, shows latest post of buddies.
 */
export class Feed extends Component {
  static contextType = AuthContext;

  state={
    latestPosts: null
  }

  // Fetches lates feed from backend
  /**
   * Fetches latest posts from users who are in same circle, and updates the state of the component accordingly. This function is scheduled to run every 5000ms to fetch latest content.
   @method 
   */
  fetchFeed = () => {
    this.context.initUser(() => {
      const {username, jwt} = this.context.profile;
      const req2 = {
        method: "POST",
        headers: {'Content-Type':'application/json', 'authorization': jwt},
        body: JSON.stringify({
          user_id: username
        })
      }
      let url = this.context.url+"view-api/fetch-feed";
      fetch(url, req2)
      .then(response => {
        return response.json();
      })
      .then(data => {
        const dataReversed = data.reverse();
        this.setState({latestPosts: dataReversed});
      })
      .catch(error => {
        console.log('error occurred: '+error);
      })
    });
  }

  /**
   * Sets fetchFeed() function to call every 5000ms
   */
  componentDidMount() {
    this.fetchFeed();
    this.interval = setInterval(this.fetchFeed, 5000);
  }

  /**
   * When the user leaves this component, the fetchFeed() schedule is stopped, so that it doesn't run in background while user is exploring other components.
   */
  componentWillUnmount() {
    clearInterval(this.interval);
  }

  /**
   * Renders the input form based on state of the component.
   */
  render() {
    const {latestPosts} = this.state;
    return (
      <div>
        <div className="animate__animated animate__fadeInDown">
          <h1 style={{marginBottom:"50px"}}>What's happening..</h1>
        </div>
        {latestPosts ? 
          <div className="animate__animated animate__fadeInUp">
            {
              latestPosts.length === 0 ? <center>No memories to share at the moment.</center> :
              latestPosts.map(item => {
                return <MemoryCard 
                        key={item.memId}
                        image={item.imageURI} 
                        caption={item.memCaption}
                        id={item.memId}
                        stars={item.listOfStars}
                        time={item.timeStamp}
                        name={item.name}
                        username={item.userId}
                        comments={item.comments}
                      />
              })
            }
          </div>
          :
          <div className="loader-center">
            <Loader /> Fetching..
          </div>
        }
      </div>
    )
  }
}

export default Feed
