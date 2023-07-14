import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import OwnerDataStore from "../util/OwnerDataStore"
import ExampleClient from "../api/exampleClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class ExamplePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(
            [
                "handleAddTool",
                "handleBorrowTool",
                "handleAddUser",
                "renderAllTools",
                "renderAllToolsByOwner",
                "onGetTools",
                "onGetUserTools",
                "handleError"
            ],
            this
        );
        this.dataStore = new DataStore();
        this.ownerData = new OwnerDataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        this.client = new ExampleClient();
        const createUserForm = document.getElementById('add-user-form');

        document.getElementById("catalog-list").addEventListener('submit', this.onGetTools);
        document.getElementById('add-tool-form').addEventListener('submit', this.handleAddTool);
        document.getElementById('add-user-form').addEventListener('click', this.toggleFormVisibility);
        document.getElementById('add-user-form').addEventListener('submit', this.handleAddUser);
        document.getElementById('get-tools-by-user-form').addEventListener('submit', this.onGetUserTools);
        document.getElementById('borrow-tool-form').addEventListener('submit', this.handleBorrowTool);

        this.dataStore.addChangeListener(this.renderAllTools);
        this.ownerData.addChangeListener(this.renderAllToolsByOwner);
        /*this.dataStore.addChangeListener(this.renderUserTools);*/



        await this.fetchAllTools();
//        await this.fetchOwnerTools();
    }

    // Fetch methods
    // These methods fetch data from the API and update the DataStore

    async fetchAllTools() {
        const tools = await this.client.getAllTools();
/*        this.dataStore.set('tools-list', tools);*/
        this.dataStore.set('catalog-list', tools);
    }


    // Render Methods --------------------------------------------------------------------------------------------------

    // Function to toggle the visibility of the forms

    fillToolId(toolId) {
      document.getElementById('tool-id-input').value = toolId;
    }

    async renderAllTools() {
        let resultArea = document.getElementById("catalog-list");
        const tools = this.dataStore.get('catalog-list');
        /*const resultArea = document.getElementById("result-info");*/

        resultArea.innerHTML = `
        <div class = "row">
        <table class = "table-bordered">
        <thead>
        <tr>
        <th> id </th>
        <th> name </th>
        <th> description </th>
        <th> borrower </th>
        </tr>
        </thead>
        <tbody id = "toolid">`;

        if (tools && tools.length !== 0 ){
        const tableBody = document.getElementById("toolid");

        for (let tool of tools){
            let row = tableBody.insertRow();

            let toolid = row.insertCell(0);
            toolid.innerHTML = tool.toolId;
            toolid.addEventListener('click', () => this.fillToolId(tool.toolId));

            let name = row.insertCell(1);
            name.innerHTML = tool.toolName;

            let description = row.insertCell(2);
            description.innerHTML = tool.description;

            let borrower = row.insertCell(3);
            borrower.innerHTML = tool.borrower;
        }
        console.log(this.dataStore);
        }

    }

    async renderAllToolsByOwner() {
        let resultArea = document.getElementById("get-user-tools");
        const tools = this.ownerData.get('get-user-tools');
        /*const resultArea = document.getElementById("result-info");*/

        resultArea.innerHTML =`
        <div class = "row">
        <table class = "table-bordered">
        <thead>
        <tr>
        <th> owner </th>
        <th> name </th>
        <th> description </th>
        </tr>
        </thead>
        <tbody id = "usertoolsbyID">`;


        if (tools && tools.length !== 0 ){
            const tableBody = document.getElementById("usertoolsbyID");

            for (let tool of tools){
                let row = tableBody.insertRow();
                let owner = row.insertCell(0);
                owner.innerHTML = tool.owner;
                let name = row.insertCell(1);
                name.innerHTML = tool.toolName;
                let description = row.insertCell(2);
                description.innerHTML = tool.description;
            }
            console.log(this.ownerData);
        }
/*        const getUserToolsForm = document.getElementById("get-tools-by-user-form");
        getUserToolsForm.addEventListener("submit", this.onGetUserTools);*/
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onGetTools(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
        let result = await this.client.getExample(id, this.errorHandler);
        this.dataStore.set("catalog-list", result);
        if (result) {
            this.showMessage(`Got ${result.name}!`)
        } else {
            this.errorHandler("Error doing GET!  Try again...");
        }
    }

    async onGetUserTools(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let ownerId = document.getElementById("usernameID").value;

        let result = await this.client.getAllToolsByOwnerId(ownerId, this.errorHandler);
        this.ownerData.set("get-user-tools", result);
        if (result) {
            this.showMessage(`${result[0].owner}!'s Tools'`)
        } else {
            this.errorHandler("User Does Not Exist. Try Again!");
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
        try {
                const createdTool = await this.client.createTool(userCreateToolRequest, this.errorHandler);
                if (createdTool) {
                    this.showMessage(`${createdTool.toolName} Added to Catalog`);
                    await this.fetchAllTools();
                }
            } catch (error) {
                console.error('Error adding tool:', error);
            }
    }

    async handleBorrowTool(event) {
        event.preventDefault();

        const toolId = document.getElementById("tool-id-input").value;
        const username = document.getElementById("username-id-input").value;
        const password = document.getElementById("password-input").value;

        const borrowToolRequest = {
            toolId: toolId,
            username: username,
            password: password
        };

        const borrowedTool = await this.client.borrowTool(borrowToolRequest, this.errorHandler);
        if (borrowedTool) {
            this.showMessage(`${borrowedTool.borrower} Borrowed ${borrowedTool.toolName}!`)
            await this.fetchAllTools();
        }
    }

    handleError(errorMessage) {
      // Handle the error and display the custom error message TOOL UNAVAILABLE
      console.error(errorMessage);
    }

    async handleAddUser(event) {
        event.preventDefault();

        const name = document.getElementById("name").value;
        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;

        const userCreateRequest = {
            name: name,
            username: username,
            password: password
        };

        try {
            const response = await this.client.createUser(userCreateRequest, this.errorHandler);
            if (response) {
                this.showMessage(`User ${userCreateRequest.username} created!`);
                const createdUser = await response.json();
                console.log(`User created: ${JSON.stringify(createdUser)}`);
            } else {
                console.error('Error creating user:', response.statusText);
            }
        } catch (error) {
            console.error('Error creating user:', error);
        }
    }

}

/**
 * NOT USED CODE. MAY USE LATER.
 */
// async renderUserTools() {
//     let userToolsCard = document.getElementById("user-tools-card");
//
//     const ownerId = document.querySelector("#usernameID").value;
//     const tools = await this.client.getToolsByOwnerId(ownerId);
//
//     if (tools) {
//         let toolsHtml = "";
//         tools.forEach(tool => {
//             toolsHtml += `
//                     <div>ID: ${tool.id}</div>
//                     <div>Name: ${tool.name}</div>
//                     <div>Description: ${tool.description}</div>
//                     <br>
//                 `;
//         });
//         userToolsCard.innerHTML = toolsHtml;
//     } else {
//         userToolsCard.innerHTML = "No tools found.";
//     }
// }

// async renderNewTools() {
//     let newToolsCard = document.getElementById("new-tools-card");
//
//     const tools = await this.client.getAllTools();
//
//     if (tools) {
//         let toolsHtml = "";
//         tools.forEach(tool => {
//             toolsHtml += `
//                     <div>ID: ${tool.id}</div>
//                     <div>Name: ${tool.name}</div>
//                     <div>Description: ${tool.description}</div>
//                     <br>
//                 `;
//         });
//         newToolsCard.innerHTML = toolsHtml;
//     } else {
//         newToolsCard.innerHTML = "No tools found.";
//     }
// }

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

// async onGet(event) {
//     // Prevent the page from refreshing on form submit
//     event.preventDefault();
//
//     let id = document.getElementById("id-field").value;
//     this.dataStore.set("example", null);
//
//     let result = await this.client.getExample(id, this.errorHandler);
//     this.dataStore.set("example", result);
//     if (result) {
//         this.showMessage(`Got ${result.name}!`)
//     } else {
//         this.errorHandler("Error doing GET!  Try again...");
//     }
// }

// async handleDeleteTool(event) {
//     event.preventDefault();
//
//     const toolId = document.querySelector("#toolId").value;
//     const username = document.querySelector("#username").value;
//     const password = document.querySelector("#password").value;
//
//     const deletedTool = await this.client.deleteTool(toolId, username, password, this.errorHandler);
// }

// async fetchNewTools() {
//     const newTools = await this.client.g;
//     this.dataStore.set('newTools', newTools);
// }
//
//this.dataStore.addChangeListener(this.renderNewTools);
//
// "renderNewTools",
//
// async renderNewTools() {
//     const newTools = this.dataStore.get('newTools');
//     const newToolsCard = document.getElementById('new-tools-card');
//     newToolsCard.innerHTML = '';
//     newTools.forEach(tool => {
//         const toolElement = document.createElement('p');
//         toolElement.textContent = `Tool: ${tool.name}, Description: ${tool.description}`;
//         newToolsCard.appendChild(toolElement);
//     });
// }


/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const examplePage = new ExamplePage();
    examplePage.mount();
};
function toggleFormVisibility(event) {
          // Get the associated form based on the clicked heading
          const formId = event.target.dataset.formId;
          const form = document.getElementById(formId);

          // Toggle the visibility of the form by adding or removing a CSS class
          form.classList.toggle('hidden');
        }

window.addEventListener('DOMContentLoaded', main);
