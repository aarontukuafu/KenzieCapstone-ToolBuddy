import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class ExamplePage extends BaseClass {

      constructor() {
        super();
        this.bindClassMethods(
          [
            "onGet",
            "handleAddTool",
            "handleBorrowTool",
            "handleAddUser",
            "renderExample",
            "renderAllTools",
            "renderAllToolsByOwner",
            "renderNewTools",
            "renderUserTools",
          ],
          this
        );
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

    async renderUserTools() {
        let userToolsCard = document.getElementById("user-tools-card");

        const ownerId = document.querySelector("#usernameID").value;
        const tools = await this.client.getToolsByOwnerId(ownerId);

        if (tools) {
            let toolsHtml = "";
            tools.forEach(tool => {
                toolsHtml += `
                    <div>ID: ${tool.id}</div>
                    <div>Name: ${tool.name}</div>
                    <div>Description: ${tool.description}</div>
                    <br>
                `;
            });
            userToolsCard.innerHTML = toolsHtml;
        } else {
            userToolsCard.innerHTML = "No tools found.";
        }
    }

    async renderNewTools() {
        let newToolsCard = document.getElementById("new-tools-card");

        const tools = await this.client.getAllTools();

        if (tools) {
            let toolsHtml = "";
            tools.forEach(tool => {
                toolsHtml += `
                    <div>ID: ${tool.id}</div>
                    <div>Name: ${tool.name}</div>
                    <div>Description: ${tool.description}</div>
                    <br>
                `;
            });
            newToolsCard.innerHTML = toolsHtml;
        } else {
            newToolsCard.innerHTML = "No tools found.";
        }
    }
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

    async renderAllTools() {
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

    async renderAllToolsByOwner() {
      let resultArea = document.getElementById("result-info");

      const ownerId = document.querySelector("#usernameID").value;
      const tools = await this.client.getToolsByOwnerId(ownerId);

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

    // async renderToolById() {
    //     let resultArea = document.getElementById("result-info");
    //
    //
    //     const toolId = document.querySelector("#toolName").value; //Switch to a field
    //     const tool = await this.client.getToolById(toolId);
    //
    //     if (tool) {
    //         // Display the tool in the result area
    //         resultArea.innerHTML = `
    //         <div>ID: ${tool.id}</div>
    //         <div>Name: ${tool.name}</div>
    //         <div>Description: ${tool.description}</div>
    //     `;
    //     } else {
    //         resultArea.innerHTML = "Tool not found.";
    //     }
    // }

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
        // Handle the response as needed
    }

    async handleBorrowTool(event) {
        event.preventDefault();

        const toolId = document.querySelector("#toolId").value;
        const username = document.querySelector("#username").value;
        const password = document.querySelector("#password").value;

        const borrowToolRequest = {
            toolId: toolId,
            username: username,
            password: password
        };

        const borrowedTool = await this.client.borrowTool(borrowToolRequest, this.errorHandler);
    }

    // async handleDeleteTool(event) {
    //     event.preventDefault();
    //
    //     const toolId = document.querySelector("#toolId").value;
    //     const username = document.querySelector("#username").value;
    //     const password = document.querySelector("#password").value;
    //
    //     const deletedTool = await this.client.deleteTool(toolId, username, password, this.errorHandler);
    // }

    async handleAddUser(event) {
        event.preventDefault();

        let name = document.getElementById("name").value;
        let username = document.getElementById("username").value;
        let password = document.getElementById("password").value;

        const userCreateRequest = {
            name: name,
            username: username,
            password: password
        };

        const createdUser = await this.client.createUser(userCreateRequest, this.errorHandler);

        // Log the creation of the user to the console
        console.log(`User created: ${JSON.stringify(createdUser)}`);
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
