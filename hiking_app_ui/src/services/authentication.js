const url = process.env.REACT_APP_API_URL + 'authenticate';
const refreshURL = process.env.REACT_APP_API_URL + 'refresh_token';

// In this function we create a user by using the jwt_token to gather some information.
function makeUser(body) {
    const token = body.jwt_token; // We get the jwt_token from the body.
    const sections = token.split('.'); // We split the jwt_token into its respectives sections
    const payload = atob(sections[1]); // And we grab the second part from the jwt_token, which is the payload that contains the sub, iss, exp, etc.
    const user = JSON.parse(payload); // We parse that payload into a JSON object and set it to the user
    localStorage.setItem('jwt',token); // We set the token to localStorage so we can use it later.
    return user;
}

export async function authenticate(credentials) {
    // Authenticates the user
    // Credentials is an object that contains two properties:
    // username and password
    const init = { // Creates the request object
        method : 'POST',
        headers : {
            'Content-Type' : 'application/json'
        },
        body : JSON.stringify(credentials) // converts the credentials to a JSON string
    }

    const response = await fetch (url, init); // Creates a fetch request using the authenticate URL and the request object represented by init

    if (response.ok) { // If the response is 0k/200, then we return make a user and return it (see Login.js)
        return makeUser(await response.json());
    }

    return Promise.reject(); // Otherwise we just reject
}

export async function refresh(){

    if(!localStorage.getItem('jwt')){
        return Promise.reject();
    }

    const init = {
        method : 'POST',
        headers : {
            "Authorization" : `Bearer ${localStorage.getItem('jwt')}`
        }
    };

    const response = await fetch(refreshURL, init);
    if(response.ok){
        return makeUser(await response.json());
    }

    return Promise.reject();

}