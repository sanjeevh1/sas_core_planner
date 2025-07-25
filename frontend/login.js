const production = false
//const apiUrl = production ? 'https://sas-core-h8a3hncpctaactgg.eastus-01.azurewebsites.net' : 'http://localhost:8080';
const apiUrl =  'http://localhost:8080' //'https://sas-core-planner-latest.onrender.com'
const usernameField = document.getElementById('username');
const passwordField = document.getElementById('password');
const loginButton = document.getElementById('login');
const errorField = document.getElementById('error');
loginButton.addEventListener('click', function() {
    const username = usernameField.value;
    const password = passwordField.value;

    if (username && password) {
        fetch(`${apiUrl}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                errorField.textContent = 'Incorrect username or password.';
            }
        }).then(data => {
            if (data) {
                localStorage.setItem('token', data.token);
                localStorage.setItem('username', username);
                window.location.href = 'search.html';
            }
        }).catch(error => {
            errorField.textContent = 'An error occurred while trying to log in.';
        });
    } else {
        errorField.textContent = 'Please enter your username and password.';
    }
});