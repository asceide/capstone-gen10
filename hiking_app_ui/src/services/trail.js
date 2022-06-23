import { apiurl } from "../helpers/url";

const url = apiurl+"api/trail";

export async function findById(id) {
    const response = await fetch(`${url}/${id}`);
    if (response.status === 200) {
        return response.json();
    }
    return Promise.reject();
}

export async function findAll() {
    const response = await fetch(url);
    if(response.status === 200) {
        return response.json();
    }
    return Promise.reject(["Unable to fetch trails"])
}

export async function addTrail(trail) {
    const init = {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${localStorage.getItem("jwt")}`
        },
        body: JSON.stringify(trail)
    }
    const response = await fetch(url, init);
    if(response.status === 201) {
        return response.json();
    }
    if(response.status === 400) {
        const errors = await response.json();
        return Promise.reject(errors);
    } else if (!response.ok) {
        return Promise.reject([`Request failed. ${response.status}`])
    }
}

export async function updateTrail(trail) {
    const init = {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${localStorage.getItem("jwt")}`
        },
        body: JSON.stringify(trail)
    }

    const response = await fetch(`${url}/${trail.trailId}`, init);

    debugger;
    if(response.status === 400) {
        const errors = await response.json();
        return Promise.reject(errors);
    } else if (!response.ok) {
        return Promise.reject([`Request failed. ${response.status}`])
    };
}

export async function deleteTrail(trailId) {
    const init = {
        method: "DELETE",
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("jwt")}`
        }
    };
    const response = await fetch(`${url}/${trailId}`, init);
    if (!response.ok) {
        throw new Error("Delete was not 204");
    };
}



