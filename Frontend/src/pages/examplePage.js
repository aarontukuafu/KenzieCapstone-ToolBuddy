import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class ExamplePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGet', 'onCreate', 'renderExample', 'handleAddUser', 'handleAddTool'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        this.client = new ExampleClient();

        this.dataStore.addChangeListener(this.renderExample)
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderExample() {
        let resultArea = document.getElementById("result-info");

        const example = this.dataStore.get("example");

        if (example) {
            resultArea.innerHTML = `
                <div>ID: ${example.id}</div>
                <div>Name: ${example.name}</div>
            `
        } else {
            resultArea.innerHTML = "No Item";
        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onGet(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let id = document.getElementById("id-field").value;
        this.dataStore.set("example", null);

        let result = await this.client.getExample(id, this.errorHandler);
        this.dataStore.set("example", result);
        if (result) {
            this.showMessage(`Got ${result.name}!`)
        } else {
            this.errorHandler("Error doing GET!  Try again...");
        }
    }

    async handleAddTool(event) {
        event.preventDefault();

        const toolName = document.querySelector("#toolName").value;
        const description = document.querySelector("#description").value;
        const username = document.querySelector("#usernameTool").value;
        const password = document.querySelector("#passwordTool").value;

        const userCreateToolRequest = {
          toolName: toolName,
          description: description,
          username: username,
          password: password
        };

        const createdTool = await this.client.createTool(userCreateToolRequest, this.errorHandler);
      }

  async handleAddTool(event) {
    event.preventDefault();

    const toolName = document.querySelector("#toolName").value;
    const description = document.querySelector("#description").value;
    const username = document.querySelector("#usernameTool").value;
    const password = document.querySelector("#passwordTool").value;

    const userCreateToolRequest = {
      toolName: toolName,
      description: description,
      username: username,
      password: password
    };

    const createdTool = await this.client.createTool(userCreateToolRequest, this.errorHandler);
    // Handle the response as needed
  }

  async renderExample() {
    let resultArea = document.getElementById("result-info");

    const tools = await this.client.getAllTools();

    if (tools) {
      // Display the tools in the result area
      let toolsHtml = "";
      tools.forEach(tool => {
        toolsHtml += `
          <div>ID: ${tool.id}</div>
          <div>Name: ${tool.name}</div>
          <div>Description: ${tool.description}</div>
          <br>
        `;
      });
      resultArea.innerHTML = toolsHtml;
    } else {
      resultArea.innerHTML = "No tools found.";
    }
  }

}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const examplePage = new ExamplePage();
    examplePage.mount();
};

window.addEventListener('DOMContentLoaded', main);
