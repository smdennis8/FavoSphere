const API_URL = 'http://localhost:8080/appuser';

export async function findAllUsers() {
    const response = await fetch(API_URL);
    if (response.status === 200) {
        return response.json();
    }
}

export async function findUserByUsername(username) {
    const response = await fetch(`${API_URL}/${username}`);
    if (response.status === 200) {
        return response.json();
    } 
    else {
        return Promise.reject(`User: ${appUserId} was not found.`);
    }
}

export async function createAppUser(appUser) {

    const init = makeFavoriteInit('POST', appUser);
    const response = await fetch(API_URL, init);
    if (response.status === 201) {
        return response.json();
    } 
    else if (response.status === 403) {
        return Promise.reject('Unauthorized');
    } 
    else {
        const errors = await response.json();
        return Promise.reject(errors);
    }
}

// export async function updateAppUser(favorite) {

//     const init = makeFavoriteInit('PUT', favorite);
//     const response = await fetch(`${API_URL}/${favorite.favoriteId}`, init);
//     console.log(init);

//     if (response.status === 404) {
//         return Promise.reject(`Favorite: ${favorite.favoriteId} was not found.`);
//     } 
//     else if (response.status === 400) {
//         const errors = await response.json();
//         return Promise.reject(errors);
//     } 
//     else if (response.status === 409) {
//         return Promise.reject('Oopsie');
//     } 
//     else if (response.status === 403) {
//         return Promise.reject('Unauthorized');
//     }
// }

function makeUserInit(method, appUser) {
    const jwtToken = localStorage.getItem('jwt_token');

    const init = {
    method: method,
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${jwtToken}`
    },
        body: JSON.stringify(appUser)
    };

    return init;
}

// export async function deleteUser(favoriteId) {
//     const jwtToken = localStorage.getItem('jwt_token');
//     const init = {
//     method: 'DELETE',
//     headers: {
//         'Authorization': `Bearer ${jwtToken}`
//     },
//     }
//     const response = await fetch(`${API_URL}/${favoriteId}`, init);

//     if (response.status === 404) {
//         return Promise.reject(`Favorite: ${favoriteId} was not found.`);
//     } 
//     else if (response.status === 403) {
//         return Promise.reject('Unauthorized');
//     }
// }