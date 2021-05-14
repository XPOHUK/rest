window.onload = function() {
    isAdmin();
};

function logout(){
    fetch('http://localhost:8080/logout', {
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then();
}

function isAdmin() {
    fetch('http://localhost:8080/rest/user').then(response => response.json()).then(json => testAdmin(json));
}

function testAdmin(json){
        var admin = false;
        for (var j = 0; j < json.authorities.length; j++){
            if(json.authorities[j].role.substr(5) == "ADMIN"){
                console.log("admin пришёл!");
                getUsers();
                document.getElementById("createUserForm").addEventListener("submit", createUser);
                document.getElementById("editUserForm").addEventListener("submit", saveUser);
                let selector = document.getElementById("selectRoles")
                addRolesSelector(selector);
                let editSelector = document.getElementById("editSelectRoles");
                addRolesSelector(editSelector);
                admin = true;
                break;
            }
        }
        if (!admin){
            document.getElementById("pills-user-tab").click();
        }
    }


function getUsers(){
    fetch('http://localhost:8080/rest/users').then(response => response.json()).then(json => fill_table(json));
}

function fill_table(json){
    let table = document.getElementById("tbody");
    document.getElementsByTagName("tbody").item(0).innerHTML = "";
    for (var i = 0; i < json.length; i++){
        var tr = table.insertRow(-1);
        tr.insertCell(-1).innerHTML = json[i].id;
        tr.insertCell(-1).innerHTML = json[i].username;
        tr.insertCell(-1).innerHTML = json[i].firstName;
        tr.insertCell(-1).innerHTML = json[i].lastName;
        tr.insertCell(-1).innerHTML = json[i].age;
        var roles = json[i].authorities;
        var rolesTxt = "";
        for (var j = 0; j < roles.length; j++){
            rolesTxt = rolesTxt + roles[j].role.substr(5) + ", ";
        }
        tr.insertCell(-1).innerHTML = rolesTxt.substr(0, rolesTxt.length - 2);
        var editButton = document.createElement("button");
        editButton.innerHTML = "EDIT";
        editButton.setAttribute("type", "button");
        editButton.setAttribute("class", "btn btn-primary");
        editButton.setAttribute("data-bs-toggle", "modal");
        editButton.setAttribute("data-bs-target", "#editUserModal");
        editButton.setAttribute("data-bs-whatever", i.toString());
        tr.insertCell(-1).appendChild(editButton);
        var delButton = document.createElement("button");
        delButton.innerHTML = "DELETE";
        delButton.setAttribute("onClick", "delete_user(" + json[i].id + ")");
        tr.insertCell(-1).appendChild(delButton);
        let modal = document.getElementById("editUserModal");
        modal.addEventListener("show.bs.modal", function (event){
            console.log("json: ", json);
            let userIndex = parseInt(event.relatedTarget.getAttribute("data-bs-whatever"));
            console.log("json user", json[userIndex]);
            document.getElementById("editFirstName").value = json[userIndex].firstName;
            document.getElementById("editLastName").value = json[userIndex].lastName;
            document.getElementById("editAge").value = json[userIndex].age;
            document.getElementById("editEmail").value = json[userIndex].username;
            document.getElementById("editId").value = json[userIndex].id;
            let selector = document.getElementById("editSelectRoles");
            // теперь надо в этом селекторе найти имеющиеся роли юзера и отметить их
            let userRoles = json[userIndex].authorities;
            for (let r = 0; r < userRoles.length; r++){
                let optionId = selector.id + userRoles[r].role;
                console.log("optionId: ", optionId);
                document.getElementById(optionId).selected = true;
            }
        })
        //Возможно, что это событие обработается до обработки результата сабмита формы...
        // Тогда надо будет после обработки сабмита самому очистить селектор, тем более, что есть его id
        modal.addEventListener("hide.bs.modal", function (event){
            let selector = document.getElementById("editSelectRoles");
            [...selector.options].forEach(option => option.selected = false);
        })
    }
}

function delete_user(id){
    fetch("http://localhost:8080/rest/users/" + id, {method: 'delete'}).then(getUsers);

}

function saveUser(event){
    event.preventDefault();
    let formData = new FormData(event.target);
    let selector = document.getElementById("editSelectRoles");
    let obj = {};
    formData.forEach(((value, key) => obj[key] = value));
    obj["roles"] = [...selector.options].filter(option => option.selected).map(option => option.value);
    console.log("Obj: ", obj);
    fetch('http://localhost:8080/rest/users/' + obj.id, {
        method: 'put',
        body: JSON.stringify(obj),
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => console.log(response.json())).then(getUsers);
    // Надо вручную закрыть модалку
    document.querySelector("#closeModalBtn").click();
    
}

function createUser(event){
    event.preventDefault();
    let formData = new FormData(event.target);
    console.log("Form Data: ", formData);
    let selector = document.getElementById("selectRoles");
    console.log("Roles: ", [...selector.options].filter(option => option.selected).map(option => option.value))
    let obj = {};
    formData.forEach(((value, key) => obj[key] = value));
    obj["roles"] = [...selector.options].filter(option => option.selected).map(option => option.value);
    console.log("Obj: ", obj);
    fetch('http://localhost:8080/rest/users', {
        method: 'post',
        body: JSON.stringify(obj),
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => console.log(response.json())).then(getUsers);
    document.getElementById("createUserForm").reset();
    document.getElementById("nav-users-tab").click();
}

function addRolesSelector(selector){
    fetch('http://localhost:8080/rest/roles').then(response => response.json()).then(json => {
        console.log("Roles: ", json);
        for (var i = 0; i < json.length; i++){
            let opt = document.createElement("option");
            opt.value = json[i].role;
            opt.innerText = json[i].role.substr(5);
            opt.id = selector.id + opt.value;
            selector.options.add(opt);
        }
    })
}