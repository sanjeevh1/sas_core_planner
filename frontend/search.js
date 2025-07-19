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
                    let courseDiv = document.createElement('div');
                    let courseNumber = document.createElement('p');
                    courseNumber.textContent = course.courseNumber;
                    courseDiv.appendChild(courseNumber);
                    let courseTitle = document.createElement('p');
                    courseTitle.textContent = course.courseTitle;
                    courseDiv.appendChild(courseTitle);
                    let credits = document.createElement('p');
                    credits.textContent = course.credits;
                    courseDiv.appendChild(credits);
                    let coreCodes = document.createElement('p');
                    coreCodes.textContent = course.coreCodes.join(", ");
                    courseDiv.appendChild(coreCodes);
                    let subject = document.createElement('p');
                    subject.textContent = course.subject;
                    courseDiv.appendChild(subject);
                    let addButton = document.createElement('button');
                    addButton.textContent = '+';
                    addButton.addEventListener('click', function() {
                        fetch(`${apiUrl}/user/add/${course.id}`, {
                            method: 'POST',
                            headers: {
                                'Authorization': `Bearer ${localStorage.getItem('token')}`
                            },
                        }).then(response => {
                            if (response.ok) {
                                alert('Course added successfully!');
                            } else {
                                alert('Failed to add course.');
                            }
                        });
                    });
                    courseDiv.appendChild(addButton);
                    courseList.appendChild(courseDiv);
                });
         });
    }
});