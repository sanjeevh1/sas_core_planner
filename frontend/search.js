const apiUrl = 'http://localhost:8080';
if(localStorage.getItem('token') === null) {
    window.location.href = 'login.html';
}
coresDiv = document.getElementById('cores');
function addCoreGroup(firstTime = false) {
    const coreGroup = document.createElement('div');
    coreGroup.className = 'core-group';
    const andButton = document.createElement('button');
    andButton.textContent = 'AND';
    const coreList = document.createElement('div');
    coreList.className = 'core-list';

    andButton.addEventListener('click', function() {
        const andPara = document.createElement('p');
        andPara.textContent = 'AND';
        coreList.appendChild(andPara);
        const coreSelect = document.createElement('select');
        coreSelect.innerHTML = `
            <option value="CCD">CCD</option>
            <option value="CCO">CCO</option>
            <option value="NS">NS</option>
            <option value="SCL">SCL</option>
            <option value="HST">HST</option>
            <option value="AHo">AHo</option>
            <option value="AHp">AHp</option>
            <option value="AHq">AHq</option>
            <option value="AHr">AHr</option>
            <option value="WCr">WCr</option>
            <option value="WCd">WCd</option>
            <option value="WC">WC</option>
            <option value="QQ">QQ</option>
            <option value="QR">QR</option>`;
        coreList.appendChild(coreSelect);
    });
    const coreSelect = document.createElement('select');
    coreSelect.innerHTML = `
        <option value="CCD">CCD</option>
        <option value="CCO">CCO</option>
        <option value="NS">NS</option>
        <option value="SCL">SCL</option>
        <option value="HST">HST</option>
        <option value="AHo">AHo</option>
        <option value="AHp">AHp</option>
        <option value="AHq">AHq</option>
        <option value="AHr">AHr</option>
        <option value="WCr">WCr</option>
        <option value="WCd">WCd</option>
        <option value="WC">WC</option>
        <option value="QQ">QQ</option>
        <option value="QR">QR</option>`;
    if(!firstTime) {
        const orPara = document.createElement('p');
        orPara.textContent = 'OR';
        coreGroup.appendChild(orPara);
    }
    coreList.appendChild(coreSelect);
    coreGroup.appendChild(coreList);
    coreGroup.appendChild(andButton);
    coresDiv.appendChild(coreGroup);
}
addCoreGroup(true);
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
            const cores = Array.from(coresDiv.querySelectorAll('.core-group')).map(group => Array.from(group.querySelectorAll('select')).map(select => select.value));
            fetch(`${apiUrl}/courses/course-list`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(cores)
            }).then(response => response.json())
             .then(courses => {
                    courseList.innerHTML = `
                        <tr>
                            <th>Course No.</th>
                            <th>Title</th>
                            <th>Credits</th>
                            <th>Core Codes</th>
                            <th>Subject</th>
                            <th>Add/Remove</th>
                        </tr>`; // Clear previous results
                    fetch(`${apiUrl}/user/courses`, {
                        method: 'GET',
                        headers: {
                            'Authorization': `Bearer ${localStorage.getItem('token')}`
                        }
                    }).then(response => {
                        if (response.ok) {
                            return response.json();
                        } else {
                            location.reload();
                        }
                    }).then(takenCourses => {
                        courses.forEach(course => {
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
                            addButton.textContent = takenCourses.some(c => c.id === course.id) ? '-' : '+';
                            addButton.addEventListener('click', function() {
                                if( addButton.textContent === '+') {
                                    fetch(`${apiUrl}/user/add/${course.id}`, {
                                        method: 'POST',
                                        headers: {
                                            'Authorization': `Bearer ${localStorage.getItem('token')}`
                                        }
                                    }).then(response => {
                                        if (response.ok) {
                                            addButton.textContent = '-';
                                        } else {
                                            location.reload();
                                        }
                                    }).catch(error => {
                                        window.location.href = 'login.html';
                                    });
                                } else {
                                    fetch(`${apiUrl}/user/remove/${course.id}`, {
                                        method: 'DELETE',
                                        headers: {
                                            'Authorization': `Bearer ${localStorage.getItem('token')}`
                                        }
                                    }).then(response => {
                                        if (response.ok) {
                                            addButton.textContent = '+';
                                        } else {
                                            location.reload();
                                        }
                                    }).catch(error => {
                                        location.reload();
                                    });
                                }
                            });
                            let buttonCell = courseRow.insertCell(-1);
                            buttonCell.appendChild(addButton);
                        });
                    }).catch(error => {
                        location.reload();
                    });
             }).catch(error => {
                location.reload();
             });
        });
}).catch(error => {
    window.location.href = 'login.html';
});
window.addEventListener('storage', function(event) {
    location.reload();
});


orButton = document.getElementById('or-button');
orButton.addEventListener('click', function() {
    addCoreGroup();
});
