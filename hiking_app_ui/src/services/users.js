import { apiurl } from "../helpers/url";

const api_url = apiurl + 'api/user/';
const register_url = apiurl+ 'register';


export async function findByEmail(email){
    if(email){
        let contents = {
            username: email
        }
        const init = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(contents)
        };
        const response = await fetch(api_url + 'getinfo', init);
        if(response.ok){
            return await response.json();
        }

        return Promise.reject();
    }
}

export async function getId(email){
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
        };
        const response = await fetch(api_url + 'id', init);
        if(response.ok){
            return await response.json();
        }

        return Promise.reject();
    }
}

export async function create(user, pkey, encryption){


        if(!user){
            return Promise.reject();
        }

        


        const newUser = {
            "username": user.username,
            "password": encryption(pkey, user.password)
        }



        const register = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newUser)
        };



        const registerResponse = await fetch(register_url, register);
        if(registerResponse.status===201){
            return registerResponse.json();
        }
        const errors = registerResponse.json();
        return Promise.reject(errors);
}

export async function addUserInfo(user){

    const registerInfo = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('jwt')}`
        },
        body: JSON.stringify(user)
    };

    const registerInfoResponse = fetch(api_url, registerInfo);

    if(registerInfoResponse.status ===400) {
        const errors = registerInfoResponse.json();
        return Promise.reject(errors);
    }
}

export async function editUserInfo(user){
    
        const registerInfo = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('jwt')}`
            },
            body: JSON.stringify(user)
        };

        const updateInfoResponse = fetch(api_url, registerInfo);

        if(updateInfoResponse.status ===400) {
            const errors = updateInfoResponse.json();
            return Promise.reject(errors);
        }

        return Promise.resolve();
}