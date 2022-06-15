const url = "http://localhost:8080/api/spot";

export async function getUserInfo(userId) {
    const response = await fetch(`${url}/${userId}`);
    if(response.status === 200) {
        return response.json();
    }
    return Promise.reject()
}