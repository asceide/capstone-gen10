const url = process.env.REACT_APP_API_URL + 'authenticate';
const refreshURL = process.env.REACT_APP_API_URL + 'refresh';

// In this function we create a user by using the jwt_token to gather some information.
function makeUser(body) {
    const token = body.jwt_token; // We get the jwt_token from the body.
    const sections = token.split('.'); // We split the jwt_token into its respectives sections
    const payload = atob(sections[1]); // And we grab
    const user = JSON.parse(payload);
    localStorage.setItem('jwt',token);
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