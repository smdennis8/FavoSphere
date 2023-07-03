const API_URL = 'http://localhost:8080/email';

export async function findAllEmails() {
    const response = await fetch(API_URL);
    if (response.status === 200) {
        return response.json();
    }
}

export async function refreshAllEmails() {
    const response = await fetch(`${API_URL}/refresh`);
    if (response.status === 200) {
        return response.json();
    }
}

export async function refreshEmailsByUserId(appUserId) {
    const response = await fetch(`${API_URL}/refresh/${appUserId}`);
    if (response.status === 200) {
        return response.json();
    }
}

// TODO: Backend method not implemented
export async function findEmailById(emailId) {
    const response = await fetch(`${API_URL}/${emailId}`);
    if (response.status === 200) {
        return response.json();
    } 
    else {
        return Promise.reject(`Email: ${emailId} was not found.`);
    }
}

export async function deleteEmailById(emailId) {
    const jwtToken = localStorage.getItem('jwt_token');
    const init = {
    method: 'DELETE',
    headers: {
        'Authorization': `Bearer ${jwtToken}`
    },
    }
    const response = await fetch(`${API_URL}/${emailId}`, init);

    if (response.status === 404) {
        return Promise.reject(`Email: ${emailId} was not found.`);
    } 
    else if (response.status === 403) {
        return Promise.reject('Unauthorized');
    }
}