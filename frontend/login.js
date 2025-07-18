const apiUrl = 'http://localhost:8080';
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
               localStorage.setItem('username', username);
               localStorage.setItem('token', response.json().then(data => data.token));
               window.location.href = 'search.html';
            } else {
                errorField.textContent = 'Incorrect username or password.';
            }
        })
        .catch(error => {
            errorField.textContent = 'An error occurred while trying to log in.';
        });
    } else {
        errorField.textContent = 'Please enter your username and password.';
    }
});