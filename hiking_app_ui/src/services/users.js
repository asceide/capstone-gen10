const api_url = process.env.REACT_APP_API_URL + 'api/user/';

export async function findByEmail(email){
    if(email){
        let contents = {
            "username": email
        }
        const init = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(contents)
        }
        const response = await fetch(api_url + 'getinfo', init);
        if(response.ok){
            return await response.json();
        }

        return Promise.reject();
    }
}