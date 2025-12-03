export async function initializeCsrfToken() {
    if(!document.cookie.includes('XSRF-TOKEN')) {
        await fetch('http://127.0.0.1:8080/api/v1/csrf', {
            method: 'GET',
            credentials: 'include'
        });
    }
}

export function getCsrfToken() {
    return document.cookie
    .split("; ")
    .find(row => row.startsWith('XSRF-TOKEN='))
    ?.split("=")[1];
}