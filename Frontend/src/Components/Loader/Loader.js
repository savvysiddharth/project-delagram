import React, { Component } from 'react'

/**
 * This component consist of a Loading Animation, which is used throughout the web app.
 */
export class Loader extends Component {
  /**
   * Renders the loading ring.
   */
  render() {
    return (
        <div className="lds-ring"><div></div><div></div><div></div><div></div></div>
    )
  }
}

export default Loader
