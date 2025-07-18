if(localStorage.getItem('token') === null) {
    window.location.href = 'login.html';
}
const usernameField = document.getElementById('username');
const logoutButton = document.getElementById('logout');
const username = localStorage.getItem('username');
usernameField.textContent = username;
logoutButton.addEventListener('click', function() {
    localStorage.removeItem('username');
    localStorage.removeItem('token');
    window.location.href = 'login.html';
});