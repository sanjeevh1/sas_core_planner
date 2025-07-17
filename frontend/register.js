usernameField = document.getElementById('username');
passwordField = document.getElementById('password');
registerButton = document.getElementById('register');
apiUrl = 'http://localhost:8080';
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
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('Registration successful! You can now log in.');
                window.location.href = '/login';
            } else {
                alert('Registration failed: ' + data.message);
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