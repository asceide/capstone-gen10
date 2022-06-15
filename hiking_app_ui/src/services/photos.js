const url = "http://localhost:8080/api/photo";

export async function findBySpot(spotId) {
    const response = await fetch(`${url}?spot-id=${spotId}`);
    if(response.status === 200) {
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

export async function addPhoto(photo, user) {
    const init = {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${user}`
        },
        body: JSON.stringify(photo)
    };

    const response = await fetch(url, init);
    if(response.status === 400) {
        const errors = await response.json();
        return Promise.reject(errors);
    } else if (!response.ok) {
        return Promise.reject([`request failed. ${response.status}`])
    }
}

export async function updatePhoto(photo, user) {
    const init = {
        method: "PUT",
        headers: {
            "Content-Type": "application.json",
            "Authorization": `Bearer ${user}`
        },
        body: JSON.stringify(photo)
    };

    const response = await fetch(`${url}/${photo.photoId}`, init);
    if(response.status === 400) {
        const errors = await response.json();
        return Promise.reject(errors);
    } else if (!response.ok) {
        return Promise.reject([`Request failed. ${response.status}`]);
    }
}

export async function deletePhoto(photoId, user) {
    const init = {
        method: "DELETE",
        headers: {
            "Authorization": `Bearer ${user}`
        }
    };

}