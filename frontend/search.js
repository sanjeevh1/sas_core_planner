const apiUrl = 'http://localhost:8080';
if(localStorage.getItem('token') === null) {
    window.location.href = 'login.html';
}
fetch(`${apiUrl}/user/courses`, {
    method: 'GET',
    headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
}).then(response => {
    if (response.ok) {
        console.log('User is authenticated');
    } else {
        window.location.href = 'login.html';
    }
});
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
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify([ [core] ])
            }).then(response => response.json())
             .then(courses => {
             courseList.innerHTML = ''; // Clear previous results
             courses.forEach(course => {
             courseList.innerHTML += `
                         <div class="course-div" id="${course.number}-div">
                             <p>${course.courseNumber}</p>
                             <p>${course.courseTitle}</p>
                             <p>${course.credits}</p>
                             <p>${course.coreCodes.join(", ")}</p>
                             <p>${course.subject}</p>
                             <button class="add-btn">+</button>
                         </div>
                     `;});
            });
    }
});