const url = "http://localhost:8080/api/trail";

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
