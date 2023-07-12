import BaseClass from "../util/baseClass";
import axios from 'axios'

/**
 * Client to call the MusicPlaylistService.
 *
 * This could be a great place to explore Mixins. Currently the client is being loaded multiple times on each page,
 * which we could avoid using inheritance or Mixins.
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Classes#Mix-ins
 * https://javascript.info/mixins
 */
export default class ExampleClient extends BaseClass {

    constructor(props = {}){
        super();
        const methodsToBind = ['createUser', 'createTool', 'getAllTools', 'getToolsByOwnerId'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
        this.initializeScrollListener();
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     * @param client The client that has been successfully loaded.
     */
    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")){
            this.props.onReady();
        }
    }

    async createUser(userCreateRequest, errorCallback) {
        console.log("createUser function called");
        console.log("createUser called with request:", userCreateRequest);
        try {
            const response = await this.client.post(`/user`, userCreateRequest);
            console.log("createUser response:", response.data);
            return response.data;
        } catch (error) {
            console.error("createUser error:", error);
            this.handleError("createUser", error, errorCallback);
        }
    }

    async createTool(userCreateToolRequest, errorCallback) {
        try {
          const response = await this.client.post(`/tools/tools`, userCreateToolRequest);
          return response.data;
        } catch (error) {
          this.handleError("createTool", error, errorCallback);
        }
      }

    async getAllTools() {
        try {
          const response = await this.client.get(`/tools`);

          return response.data;
        } catch (error) {
          this.handleError("getAllTools", error);
          return null;
        }
      }

    async getAllToolsByOwnerId(ownerId) {
        try {
            const response = await this.client.get(`/owner/${ownerId}`);
            return response.data;
        } catch (error) {
            this.handleError("getAllToolsByOwnerId", error);
            return null;
        }
    }

    // async getToolById(toolId) {
    //     try {
    //         const response = await this.client.get(`/tool/${toolId}`);
    //         return response.data;
    //     } catch (error) {
    //         this.handleError("getToolById", error);
    //         return null;
    //     }
    // }

    async borrowTool(borrowToolRequest, errorCallback) {
        try {
            const response = await this.client.put(`/borrowTool`, borrowToolRequest);
            return response.data;
        } catch (error) {
            this.handleError("borrowTool", error, errorCallback);
        }
    }

    // async deleteTool(toolId, username, password, errorCallback) {
    //     try {
    //         const response = await this.client.delete(`/toolId`, {
    //             params: {
    //                 username: username,
    //                 password: password
    //             }
    //         });
    //         return response.data;
    //     } catch (error) {
    //         this.handleError("deleteTool", error, errorCallback);
    //     }
    // }

     initializeScrollListener() {
        window.addEventListener('scroll', function() {
        var backgroundImage = document.querySelector('.background-image');
        var scrollPosition = window.scrollY;
        var windowHeight = window.innerHTML;
        // Calculate the threshold to switch the image (e.g., when 50% of the window is scrolled)
        var threshold = windowHeight
        if (scrollPosition > threshold) {
          backgroundImage.style.backgroundImage = 'url(./images/seemlesstools.png)';
          backgroundImage.style.opacity = '1';
        } else {
          backgroundImage.style.backgroundImage = 'url(./images/toolbackground.jpg)';
          backgroundImage.style.opacity = '1';
        }
        });
     }


      async getToolsByOwnerId(ownerId) {
          try {
            const response = await this.client.get(`/tools/owner/${ownerId}`);
            return response.data;
          } catch (error) {
            this.handleError("getToolsByOwnerId", error);
            return null;
          }
      }

    /**
     * Helper method to log the error and run any error functions.
     * @param method
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
    }
}
