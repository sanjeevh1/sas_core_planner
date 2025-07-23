const usernameField = document.getElementById('username');
const passwordField = document.getElementById('password');
const registerButton = document.getElementById('register');
const errorField = document.getElementById('error');
const production = false
const apiUrl = production ? 'https://sas-core-h8a3hncpctaactgg.eastus-01.azurewebsites.net' : 'http://localhost:8080';
registerButton.addEventListener('click', function() {
    const username = usernameField.value;
    const password = passwordField.value;

    if (username && password) {
        fetch(`${apiUrl}/auth/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        })
        .then(response => {
            if (response.ok) {
                window.location.href = 'login.html';
            } else {
                errorField.textContent = 'Username already exists.';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            errorField.textContent = 'An error occurred while trying to register.';
        });
    } else {
        errorField.textContent = 'Please enter a username and password.';
    }
});