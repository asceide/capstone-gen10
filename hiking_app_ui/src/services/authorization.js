import { apiurl } from "../helpers/url";

const url = apiurl + "api/user/authorities";

export async function findAll() {
    const response = await fetch(url);
    if (response.status === 200) {
        return response.json();
    }
    return Promise.reject();
}

export async function updateRoles(user) {
    const init = {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${localStorage.getItem("jwt")}`
        },
        body: JSON.stringify(user)
    }

    const response = await fetch(`${url}`, init);

    if(response.status === 202) {
        return Promise.resolve();
    }
    if (response.status === 400) {
        const errors = await response.json();
        return Promise.reject(errors);
    }else if (!response.status!==202) {
        return Promise.reject([`Request failed. ${response.status}`])
    }

}