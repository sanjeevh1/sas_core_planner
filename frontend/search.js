const apiUrl = 'http://localhost:8080';
if(localStorage.getItem('token') === null) {
    window.location.href = 'login.html';
}
window.addEventListener('storage', function(event) {
    location.reload();
});
const usernameField = document.getElementById('username');
const logoutButton = document.getElementById('logout');
const searchButton = document.getElementById('search');
const courseList = document.getElementById('course-list');
const username = localStorage.getItem('username');
const coreField = document.getElementById('core');
usernameField.textContent = username;
logoutButton.addEventListener('click', function() {
    localStorage.removeItem('username');
    localStorage.removeItem('token');
    window.location.href = 'login.html';
});
searchButton.addEventListener('click', function() {
    const core = coreField.value;
    if (core) {
        fetch(`${apiUrl}/courses/course-list`, {
            method: 'POST',
            body: JSON.stringify([ core ])
            }).then(response => {
                courseList.textContent = response.json();

            });
    }
});