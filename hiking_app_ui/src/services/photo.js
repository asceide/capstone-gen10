import { apiurl } from "../helpers/url";
const url = apiurl + "api/photo";


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

export async function addPhoto(param, photo) {
    const init = {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${localStorage.getItem("jwt")}`
        },
        body: JSON.stringify(photo)
    };

    const response = await fetch(`${url}?${param}`, init);
    if(response.status === 400) {
        const errors = await response.json();
        return Promise.reject(errors);
    } else if (!response.ok) {
        return Promise.reject([`request failed. ${response.status}`])
    }
}

export async function updatePhoto(photo) {
    const init = {
        method: "PUT",
        headers: {
            "Content-Type": "application.json",
            "Authorization": `Bearer ${localStorage.getItem("jwt")}`
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

export async function deletePhoto(photoId) {
    const init = {
        method: "DELETE",
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("jwt")}`
        }
    };

    const response = await fetch(`${url}/${photoId}`, init);

    if (!response.ok) {
        throw new Error("Delete was not 204");
    }
}

export async function bucketUpload(bucketUrl, photo) {
    
    const init = {
        method: "PUT",
        headers: {
            "Content-Type": "multipart/form-data"
        },
        body: photo
    }

    const response = await fetch(bucketUrl, init);

    if (!response.ok) {
        return Promise.reject(["Unable to upload to photo"]);
    }

}
