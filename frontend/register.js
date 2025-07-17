const usernameField = document.getElementById('username');
const passwordField = document.getElementById('password');
const registerButton = document.getElementById('register');
const apiUrl = 'http://localhost:8080';
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
                alert('Registration successful! You can now log in.');
                window.location.href = '/login';
            } else {
                alert('Registration failed');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while trying to register.');
        });
    } else {
        alert('Please enter both username and password.');
    }
});