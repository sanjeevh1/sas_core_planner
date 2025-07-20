const apiUrl = 'http://localhost:8080';
if(localStorage.getItem('token') === null) {
    window.location.href = 'login.html';
}
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
let ahCodesTaken = 0;
const ahCodes = document.getElementById("AH-codes");
fetch(`${apiUrl}/user/courses`, {
    method: 'GET',
    headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
}).then(response => {
    if (response.ok) {
        return response.json();
    } else {
        window.location.href = 'login.html';
    }
}).then(userCourses => {
        const usernameField = document.getElementById('username');
        const logoutButton = document.getElementById('logout');
        usernameField.textContent = localStorage.getItem('username');
        logoutButton.addEventListener('click', function() {
            localStorage.removeItem('username');
            localStorage.removeItem('token');
            window.location.href = 'login.html';
        });
        const courseTable = document.getElementById('course-table');
        userCourses.forEach(course => {
            const row = courseTable.insertRow(-1);
            row.id = `${course.courseNumber}-row`;
            const numberCell = row.insertCell(0);
            numberCell.textContent = course.courseNumber;
            for(const code of Object.keys(codesTaken)) {
                const cell = row.insertCell(-1);
                if(course.coreCodes.includes(code)) {
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
            for(const category of Object.keys(categoriesTaken)) {
                const codes = category.split("/");
                if (codes.some(code => course.coreCodes.includes(code))) {
                    categoriesTaken[category]++;
                    const categorySpan = document.getElementById(category);
                    categorySpan.textContent = categoriesTaken[category];
                }
            }
            const removeCell = row.insertCell(-1);
            const removeButton = document.createElement("button");
            removeButton.classList.add("remove-btn");
            removeButton.textContent = "-";
            removeCell.appendChild(removeButton);
            removeButton.addEventListener("click", () => {
                fetch(`${apiUrl}/user/remove/${course.id}`, {
                    method: 'DELETE',
                    headers: {
                        "Authorization": `Bearer ${localStorage.getItem('token')}`
                    }
                }).then(response => {
                    location.reload();
                }).catch(error => {
                    window.location.href = 'login.html';
                });
            });
        });
}).catch(error => {
    window.location.href = 'login.html';
});