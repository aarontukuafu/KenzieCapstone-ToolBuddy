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
                "handleAddTool",
                "handleBorrowTool",
                "handleAddUser",
                "renderAllTools",
                "renderAllToolsByOwner",
                "onGetTools"
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

        document.getElementById("tools-list").addEventListener('submit', this.onGetTools);
        document.getElementById('add-tool-form').addEventListener('submit', this.handleAddTool);
        //document.getElementById('borrow-tool-form').addEventListener('submit', this.handleBorrowTool);
        document.getElementById('add-user-form').addEventListener('submit', this.handleAddUser);
        document.getElementById('get-user-tools').addEventListener('submit', this.getAllToolsByOwnerId);


        this.dataStore.addChangeListener(this.renderAllTools);
        this.dataStore.addChangeListener(this.renderAllToolsByOwner);
        /*this.dataStore.addChangeListener(this.renderUserTools);*/


        await this.fetchAllTools();
//        await this.fetchOwnerTools();
    }

    // Fetch methods
    // These methods fetch data from the API and update the DataStore

    async fetchAllTools() {
        const tools = await this.client.getAllTools();
        this.dataStore.set('tools-list', tools);
    }

    /*async fetchOwnerTools() {
        const userTools = await this.client.getAllToolsByOwnerId();
        this.dataStore.set('get-user-tools', tools);
    }*/

    // Render Methods --------------------------------------------------------------------------------------------------

//    async renderUserTools() {
//        const userTools = this.dataStore.get('userTools');
//        const userToolsCard = document.getElementById('user-tools-card');
//        userToolsCard.innerHTML = '';
//        userTools.forEach(tool => {
//            const toolElement = document.createElement('p');
//            toolElement.textContent = `Tool: ${tool.name}, Description: ${tool.description}`;
//            userToolsCard.appendChild(toolElement);
//        });
//    }

    async renderAllTools() {
        let resultArea = document.getElementById("tools-list");
        const tools = this.dataStore.get('tools-list');
        /*const resultArea = document.getElementById("result-info");*/

        resultArea.innerHTML = `
        <div class = "row">
        <table class = "table-bordered">
        <thead>
        <tr>
        <th> id </th>
        <th> name </th>
        <th> description </th>
        </tr>
        </thead>
        <tbody id = "toolid">`;

        if (tools && tools.length !== 0 ){
        const tableBody = document.getElementById("toolid");

        for (let tool of tools){
            let row = tableBody.insertRow();
            let toolid = row.insertCell(0);
            toolid.innerHTML = tool.toolId;

            let name = row.insertCell(1);
            name.innerHTML = tool.toolName;

            let description = row.insertCell(2);
            description.innerHTML = tool.description;
        }
        console.log(this.dataStore);
        }

    }

    async renderAllToolsByOwner() {
        let resultArea = document.getElementById("get-user-tools");
        const tools = this.dataStore.get('get-user-tools');
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
                let usertoolsbyID = row.insertCell(0);
                usertoolsbyID.innerHTML = tool.owner;

                let name = row.insertCell(1);
                name.innerHTML = tool.toolName;

                let description = row.insertCell(2);
                description.innerHTML = tool.description;
            }

            console.log(this.dataStore);
        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onGetTools(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
        let result = await this.client.getExample(id, this.errorHandler);
        this.dataStore.set("tools-list", result);
        if (result) {
            this.showMessage(`Got ${result.name}!`)
        } else {
            this.errorHandler("Error doing GET!  Try again...");
        }
    }

    async onGetUserTools(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
        let result = await this.client.getAllToolsByOwnerId(this.renderAllToolsByOwner(), this.errorHandler);
        this.dataStore.set("get-user-tools", result);
        if (result) {
            this.showMessage(`Got ${result.ownerId}!`)
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
        if (createdTool) {
            await this.fetchAllTools();
        }
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
        if (borrowedTool) {
            await this.fetchAllTools();
        }
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

            const response = await this.client.createUser(userCreateRequest, this.errorHandler);

            console.log("handleAddUser called with request:", userCreateRequest);

            if (!response) {
                console.error('Error creating user:', response.statusText);
            } else {
                const createdUser = await response.json();
                console.log(`User created: ${JSON.stringify(createdUser)}`);
            }
    }

    errorHandler(error) {
        console.error("An error occurred:", error);
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

window.addEventListener('DOMContentLoaded', main);
