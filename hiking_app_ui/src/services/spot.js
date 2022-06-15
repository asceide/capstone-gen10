const url = "http://localhost:8080/api/spot";

export async function findById(id) {
    const response = await fetch(`${url}/${id}`);
    if (response.status === 200) {
        return response.json();
    }
    return Promise.reject();
}

export async function addSpot(spot, user) {
    const init = {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${user}`
        },
        body: JSON.stringify(spot)
    }

    const response = await fetch(url, init);
    if(response.status === 400) {
        const errors = await response.json();
        return Promise.reject(errors);
    } else if (!response.ok) {
        return Promise.reject([`Request failed. ${response.status}`])
    }
}

export async function updateSpot(spot, user) {
    const init = {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${user}`
        },
        body: JSON.stringify(spot)
    }

    const response = await fetch(`${url}/${spot.spotId}`, init);
    if(response.status === 400) {
        const errors = await response.json();
        return Promise.reject(errors);
    } else if (!response.ok) {
        return Promise.reject([`Request failed. ${response.status}`])
    }
}

export async function deleteSpot(spotId, user) {
    const init = {
        method: "DELETE",
        headers: {
            "Authoirzation": `Bearer ${user}`
        }
    }

    const response = await fetch(`${url}/${spotId}`, init);
    if (!response.ok) {
        throw new Error("Delete was not 204");
    }
}