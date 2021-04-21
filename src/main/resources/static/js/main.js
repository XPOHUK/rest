window.onload = function() {
    console.log("blia");
    getUsers();
    document.getElementById("createUserForm").addEventListener("submit", createUser);
    addRolesSelector();
};

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
        var delButton = document.createElement("BUTTON");
        delButton.innerHTML = "DELETE";
        delButton.setAttribute("onClick", "delete_user(" + json[i].id + ")");
        tr.insertCell(-1).appendChild(delButton);
    }
}

function delete_user(id){
    fetch("http://localhost:8080/rest/users/" + id, {method: 'delete'}).then(getUsers);

}

function createUser(event){
    event.preventDefault();
    let formData = new FormData(event.target);
    console.log("Form Data: ", formData);
    let selector = document.getElementById("selectRoles");
    console.log("Roles: ", [...selector.options].filter(option => option.selected).map(option => option.value))
    let obj = {};
    /*obj["age"] = formData.get("age");
    obj["firstName"] = formData.get("firstName");
    obj["lastName"] = formData.get("lastName");
    obj["age"] = formData.get("age");
    obj["age"] = formData.get("age");*/
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

function addRolesSelector(){
    fetch('http://localhost:8080/rest/roles').then(response => response.json()).then(json => {
        console.log("Roles: ", json);
        let selector = document.getElementById("selectRoles")
        for (var i = 0; i < json.length; i++){
            let opt = document.createElement("option");
            opt.value = json[i].role;
            opt.innerText = json[i].role.substr(5);
            selector.options.add(opt);
        }
    })
}