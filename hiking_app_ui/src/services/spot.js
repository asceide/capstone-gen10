const url = "http://localhost:8080/api/spot";

export async function findById(id) {
    const response = await fetch(`${url}/${id}`);
    if (response.status === 200) {
        return response.json();
    }
    return Promise.reject();
}

export async function findByTrail(trailId) {
    const response = await fetch(`${url}?trail-id=${trailId}`);
    if(response.status === 200) {
        return response.json();
    }
    return Promise.reject();
}

export async function addSpot(spot) {
    const init = {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${localStorage.getItem("jwt")}`
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

export async function updateSpot(spot) {
    const init = {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${localStorage.getItem("jwt")}`
        },
        body: JSON.stringify(spot)
    }

    const response = await fetch(`${url}/${spot.spotId}`, init);
    if(response.status === 400) {
        const errors = await response.json();
        return Promise.reject(errors);
    } else if (!response.ok) {
        return Promise.reject([`Request failed. ${response.status}`])
    };
}

export async function deleteSpot(spotId) {
    const init = {
        method: "DELETE",
        headers: {
            "Authoirzation": `Bearer ${localStorage.getItem("jwt")}`
        }
    };

    const response = await fetch(`${url}/${spotId}`, init);
    if (!response.ok) {
        throw new Error("Delete was not 204");
    };
}

export async function rateSpot(spotId, rating) {
    const init = {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("jwt")}`
        }
    };

    const response = await fetch(`${url}/rate/${spotId}?rating=${rating}`, init);
    if(response.status === 200) {
        return response.json();
    };
    return Promise.reject();
}