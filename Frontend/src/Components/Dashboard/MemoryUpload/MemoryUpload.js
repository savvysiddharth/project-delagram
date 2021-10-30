import React, { Component } from 'react'
import AuthContext from '../../AuthContext'
import memDefault from './memDefault.png'
import { clarendon, 
        gingham,
        moon,
        lark,
        reyes,
        juno,
        slumber,
        crema,
        ludwig,
        aden,
        perpetua,
        sierra,
        applyPresetOnImage } from 'instagram-filters';
import Loader from '../../Loader/Loader';

/**
 * MemoryUpload component, allows user to upload image and apply filters on their image then create a new memory.
 */
export class MemoryUpload extends Component {
  static contextType = AuthContext;

  state = {
    imageToUpload: memDefault, // base64 string
    caption: "",
    imageAdded: false,
    originalImage: null,
    filterProcessing: false,
  }

  /**
   * Picks up the file selected by user and converts it to base64 image and updates the state of component accordingly.
   * @method
   * @param {Object} event Event object 
   */
  fileSelectedHandler = (event) => {
    const reader = new FileReader();
    let uploadedFile = event.target.files[0]
    if(!uploadedFile) return;  // in case user cancels the upload
    
    this.setState({imageHTML: uploadedFile});
    
    reader.readAsDataURL(uploadedFile);
    reader.onload = () => {
      if(reader.readyState === 2) {
        this.setState({imageToUpload: reader.result, imageAdded: true, originalImage: reader.result});
      }
    }
  }

  /**
   * Once the user is happy with their image, caption and filters this function fires up as soon as they click "Create a memory". Further, the memory data is sent to the backend.
   * @method
   */
  uploadHandler = () => {
    if(!this.state.imageAdded) {
      alert("No image uploaded here...");
      return;
    }
    this.setState({uploading: true});
    const {username, jwt} = this.context.profile;
    const request = {
      method: "POST",
      headers: {'Content-Type':'application/json', 'authorization': jwt},
      body: JSON.stringify({
        memory_img: this.state.imageToUpload,
        mem_caption: this.state.caption,
        user_id: username
      })
    }

    // console.log("Request is:");
    // console.log(request);
    let url = this.context.url+"upload-api/upload-memory";
    fetch(url, request)
    .then(response => response.json())
    .then(data => {
      console.log(data);
      this.context.initUser();
      this.setState({
        uploading: false,
        imageAdded: false,
        caption: ""
      });
      setTimeout(() => {}, 2000);
    })
    .catch(error => {
      console.log('error occurred: '+error);
    })
  }

  /**
   * This function simply makes a click to actual Input Element.
   * @method
   */
  onImageContainerClick = () => {
    document.querySelector("#imgUploaderInput").click()
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
   * This function simply converts the Blob data given as input to a base64 string.
   * @method
   * @param {Object} blob 
   * @returns {Promise} Promise on the converted base64
   */
  blobToBase64 = (blob) => {
    return new Promise((resolve, _) => {
      const reader = new FileReader();
      reader.onloadend = () => resolve(reader.result);
      reader.readAsDataURL(blob);
    });
  }

  /**
   * This function applies filter to the uploaded image, and updates the state of the component accordingly.
   * @method
   * @param {event} event Event object
   */
  filterHandler = (event) => {
    console.log(event.target.value);
    const selectedFilter = event.target.value;
    if(selectedFilter === "0") {
      this.setState({imageToUpload: this.state.originalImage});
      return;
    }
    if(!this.state.imageAdded) {
      event.target.value = "0";
      alert("No image uploaded here...");
      return;
    }
    this.setState({filterProcessing: true});

    const filterFuncs = {
      clarendon,
      gingham,
      moon,
      lark,
      reyes,
      juno,
      slumber,
      crema,
      ludwig,
      aden,
      perpetua,
      sierra,
    };

    const image = document.querySelector('#uploadImgOriginal');
    const blob = applyPresetOnImage(image, filterFuncs[selectedFilter]());
    // console.log(blob);
    blob.then(data => {
      // console.log(data);
      this.blobToBase64(data)
      .then((base64str) => {
        // console.log(base64str);
        this.setState({imageToUpload: base64str, filterProcessing: false});
      });
    });
  }

  /**
   * Renders the input form based on current state of the component.
   */
  render() {
    const {imageToUpload, caption, originalImage, filterProcessing} = this.state;
    const {name} = this.context.profile;
    const today = new Date();
    let tdate = today.getDate();
    let tmonth = today.toLocaleString('en-US', {month: 'short'});
    let tyear = today.getFullYear();
    let time = tdate + '-' + tmonth + '-' + tyear;
    return (
      <div>
        <div className="animate__animated animate__fadeInDown">
          <h1>Upload Memory</h1>
        </div>
        <br />
        
        <div className="image-card animate__animated animate__fadeInUp">
          <div className="card-head">
            <span className="head-left animate__animated animate__tada animate__delay-1s">
              <input autoComplete="off" onChange={this.textInputHandler} name="caption" value={caption} maxLength="50" placeholder="type your caption here.." className="caption-input" type="text" />
            </span>
            <span className="head-right"> {time.slice(0,11)}</span>
          </div>
          <div className="card-img mem-upload-ing">
            <img onClick={this.onImageContainerClick} src={imageToUpload} alt="" />
            <img id="uploadImgOriginal" src={originalImage} style={{display:"none"}} alt="" />
          </div>
          <div className="card-menu-bar">
            <span className="mem-author" style={{paddingTop:"0px"}}>By {name}</span>
            <br/>
          </div>
        </div>

        <input id="imgUploaderInput" onChange={this.fileSelectedHandler} type="file" accept="image/*"/>
        {/* <img src={this.state.imageToUpload} width="100px" height="auto" alt="" /> */}
        
        {this.state.uploading ? 
        <button disabled className="btn btn-cyan"><Loader />Uploading..</button>
        :
        <button className="btn btn-cyan animate__animated animate__fadeInUp animate__delay-1s" onClick={this.uploadHandler}>Create Memory</button> 
        }&nbsp;
        
        {/* <br /><br /> */}
        <div className="filter-select animate__animated animate__fadeInUp animate__delay-1s" onChange={this.filterHandler}>
          <select>
            <option value="0">No Magic</option>
            <option value="clarendon">Clarendon</option>
            <option value="gingham">Gingham</option>
            <option value="moon">Moon</option>
            <option value="lark">Lark</option>
            <option value="reyes">Reyes</option>
            <option value="juno">Juno</option>
            <option value="slumber">Slumber</option>
            <option value="crema">Crema</option>
            <option value="ludwig">Ludwig</option>
            <option value="aden">Aden</option>
            <option value="perpetua">Perpetua</option>
            <option value="sierra">Sierra</option>
          </select>
          <br/>
        </div>
        {filterProcessing ?
        <div className="filter-loader animate__animated animate__fadeInDown">
          <Loader/> Processing..
        </div>
        :
        ""
        }
        <br /><br /><br />
      </div>
    )
  }
}

export default MemoryUpload
