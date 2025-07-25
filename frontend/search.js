const production = false;
const apiUrl =  'https://sas-core-planner-latest.onrender.com';
if(localStorage.getItem('token') === null) {
    window.location.href = 'login.html';
}
const coresTable = document.getElementById('cores');
const cores = ['CCD', 'CCO', 'NS', 'SCL', 'HST', 'AHo', 'AHp', 'AHq', 'AHr', 'WCr', 'WCd', 'WC', 'QQ', 'QR'];
let userCourses;

function logout() {
    localStorage.removeItem('username');
    localStorage.removeItem('token');
    window.location.href = 'login.html';
}

function loadUsername() {
    const usernameField = document.getElementById('username');
    const username = localStorage.getItem('username');
    usernameField.textContent = username;
}

function parseResponse(response) {
    if (response.ok) {
        return response.json();
    } else {
        window.location.href = 'login.html';
    }
}

function createCoreSelect() {
    const coreSelect = document.createElement('select');
    for(const core of cores) {
        const option = document.createElement('option');
        option.value = core;
        option.textContent = core;
        coreSelect.appendChild(option);
    }
    return coreSelect;
}

function addCoreSelect(coreGroup, deleteButton) {
    const andCell = coreGroup.insertCell(coreGroup.cells.length - 2);
    andCell.textContent = 'AND';
    const selectCell = coreGroup.insertCell(coreGroup.cells.length - 2);
    selectCell.appendChild(createCoreSelect());
    deleteButton.disabled = false;
}

function createAndButton(coreGroup, deleteButton) {
    const andButton = document.createElement('button');
    andButton.textContent = 'AND';
    andButton.addEventListener('click', function() {
        addCoreSelect(coreGroup, deleteButton);
    });
    return andButton;
}

function createDeleteButton(coreGroup) {
    const deleteButton = document.createElement('button');
    deleteButton.textContent = '-';
    deleteButton.disabled = true;
    deleteButton.addEventListener('click', function() {
        coreGroup.deleteCell(coreGroup.cells.length - 3);
        coreGroup.deleteCell(coreGroup.cells.length - 3);
        if(coreGroup.cells.length < 5) {
            deleteButton.disabled = true;
        }
    });
    return deleteButton;
}

function addCoreGroup(firstTime = false) {
    const coreGroup = coresTable.insertRow(-1);
    coreGroup.className = 'core-group';
    if(!firstTime) {
        const orCell = coreGroup.insertCell(0)
        orCell.textContent = 'OR';
    }
    const selectCell = coreGroup.insertCell(-1);
    selectCell.appendChild(createCoreSelect());
    createAndButton(coreGroup);
    const deleteButton = createDeleteButton(coreGroup);
    const andButton = createAndButton(coreGroup, deleteButton);
    const andButtonCell = coreGroup.insertCell(-1);
    andButtonCell.appendChild(andButton);
    const deleteCell = coreGroup.insertCell(-1);
    deleteCell.appendChild(deleteButton);
}

function toggleCourse(addButton, courseId) {
    let methodType, endpoint, newText, color;
    if(addButton.textContent === '+') {
        methodType = 'POST';
        endpoint = 'add';
        newText = '-';
    } else {
        methodType = 'DELETE';
        endpoint = 'remove';
        newText = '+';
    }
    fetch(`${apiUrl}/user/${endpoint}/${courseId}`, {
        method: methodType,
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
    }).then(response => {
        if (response.ok) {
            addButton.textContent = newText;
        } else {
            logout();
        }
    }).catch(error => {
        logout();
    });
}

const courseList = document.getElementById('course-list');
function addCourseRow(course) {
    let courseRow = courseList.insertRow(-1);
    let courseNumber = courseRow.insertCell(-1);
    courseNumber.textContent = course.courseNumber;
    let courseTitle = courseRow.insertCell(-1);
    courseTitle.textContent = course.courseTitle;
    let credits = courseRow.insertCell(-1);
    credits.textContent = course.credits;
    let coreCodes = courseRow.insertCell(-1);
    coreCodes.textContent = course.coreCodes.join(", ");
    let subject = courseRow.insertCell(-1);
    subject.textContent = course.subject;
    let addButton = document.createElement('button');
    addButton.textContent = userCourses.some(c => c.id === course.id) ? '-' : '+';
    addButton.addEventListener('click', function() {
        toggleCourse(addButton, course.id);
    });
    let buttonCell = courseRow.insertCell(-1);
    buttonCell.appendChild(addButton);
}

function loadCourses(courses) {
    clearCourseList();
    fetch(`${apiUrl}/user/courses`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
    }).then(response => {
        if (response.ok) {
            return response.json();
        } else {
            logout();
        }
    }).then(takenCourses => {
        userCourses = takenCourses;
        courses.forEach(addCourseRow);
    }).catch(error => {
        logout();
    });
}

function clearCourseList() {
    courseList.innerHTML = `
        <tr>
            <th>Course No.</th>
            <th>Title</th>
            <th>Credits</th>
            <th>Core Codes</th>
            <th>Subject</th>
            <th>Add/Remove</th>
        </tr>`;
}

addCoreGroup(true);
loadUsername();

const searchButton = document.getElementById('search');
searchButton.addEventListener('click', function () {
   const cores = Array.from(coresTable.rows).map(row => Array.from(row.querySelectorAll('select')).map(select => select.value));
   fetch(`${apiUrl}/courses/course-list`, {
       method: 'POST',
       headers: {
           'Content-Type': 'application/json'
       },
       body: JSON.stringify(cores)
   }).then(response => response.json()).then(loadCourses).catch(error => {
       location.reload();
    });
});

const logoutButton = document.getElementById('logout');
logoutButton.addEventListener('click', logout);

const deleteRowButton = document.getElementById('delete-row');
deleteRowButton.addEventListener('click', function() {
    coresTable.deleteRow(-1);
    if(coresTable.rows.length === 1) {
        deleteRowButton.disabled = true;
    }
});

const orButton = document.getElementById('or-button');
orButton.addEventListener('click', function() {
    addCoreGroup();
    deleteRowButton.disabled = false;
});
