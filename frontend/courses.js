const apiUrl = 'https://localhost:8080';

const codesTaken = {
    "CCD": 0,
    "CCO": 0,
    "NS": 0,
    "SCL": 0,
    "HST": 0,
    "AHo": 0,
    "AHp": 0,
    "AHq": 0,
    "AHr": 0,
    "WCr": 0,
    "WCd": 0,
    "WC": 0,
    "QQ": 0,
    "QR": 0
};

const categoriesTaken = {
    "CCD/CCO": 0,
    "SCL/HST": 0,
    "AHo/AHp/AHq/AHr": 0,
    "WCr/WCd": 0,
    "QQ/QR": 0
};

const courseTable = document.getElementById('course-table');
let ahCodesTaken = 0;
const ahCodes = document.getElementById("AH-codes");

function logout() {
    localStorage.removeItem('username');
    localStorage.removeItem('token');
    window.location.href = 'login.html';
}

function parseResponse(response) {
    if (response.ok) {
        return response.json();
    } else {
        logout();
    }
}

function addCores(row, cores) {
    for(const code of Object.keys(codesTaken)) {
       const cell = row.insertCell(-1);
       if(cores.includes(code)) {
           if(code.includes("AH") && codesTaken[code] === 0) {
               ahCodesTaken++;
               ahCodes.textContent = ahCodesTaken;
           }
           codesTaken[code]++;
           const codeSpan = document.getElementById(code);
           codeSpan.textContent = codesTaken[code];
           cell.style.backgroundColor = "green";
       }
    }
}

function updateCategories(courseCores) {
    for(const category of Object.keys(categoriesTaken)) {
       const codes = category.split("/");
       if (codes.some(code => courseCores.includes(code))) {
           categoriesTaken[category]++;
           const categorySpan = document.getElementById(category);
           categorySpan.textContent = categoriesTaken[category];
       }
    }
}

function removeCourse(courseId) {
    fetch(`${apiUrl}/user/remove/${courseId}`, {
       method: 'DELETE',
       headers: {
           "Authorization": `Bearer ${localStorage.getItem('token')}`
       }
    }).then(response => {
       location.reload();
    }).catch(error => {
       logout();
    });
}

function createRemoveButton(row, course) {
    const removeCell = row.insertCell(-1);
    const removeButton = document.createElement("button");
    removeButton.classList.add("remove-btn");
    removeButton.textContent = "-";
    removeCell.appendChild(removeButton);
    removeButton.addEventListener("click", () => {
        removeCourse(course.id);
    });
}

function addCourseRow(course) {
    const row = courseTable.insertRow(-1);
    const numberCell = row.insertCell(0);
    numberCell.textContent = course.courseNumber;
    addCores(row, course.coreCodes);
    updateCategories(course.coreCodes);
    createRemoveButton(row, course);
}

function addCourses(userCourses) {
     const usernameField = document.getElementById('username');
     const logoutButton = document.getElementById('logout');
     usernameField.textContent = localStorage.getItem('username');
     logoutButton.addEventListener('click', logout);
     const courseTable = document.getElementById('course-table');
     userCourses.forEach(addCourseRow);
}

if(localStorage.getItem('token') === null) {
    logout();
}
fetch(`${apiUrl}/user/courses`, {
    method: 'GET',
    headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
}).then(parseResponse).then(addCourses).catch(error => {logout();});