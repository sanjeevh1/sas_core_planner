import React from 'react';
import { createRoot } from "react-dom/client";
import TopBar from './TopBar.jsx';
import CourseTable from './CourseTable.jsx';
import SearchPage from './SearchPage.jsx';
import { logout, apiUrl, cores } from './config.js';

class CoursesPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isSearch: true,
            username: localStorage.getItem('username'),
            token: localStorage.getItem('token'),
            codesTaken: Object.fromEntries(cores.map(core => [core, 0])),
            coursesTaken: [],
            categoriesTaken: {
                "CCD/CCO": 0,
                "SCL/HST": 0,
                "AHo/AHp/AHq/AHr": 0,
                "WCr/WCd": 0,
                "QQ/QR": 0
            },
            ahCodesTaken: 0
        };
    }

    addCourse = (course) => {
        fetch(`${apiUrl}/user/add/${course.id}`, {
            method: "POST",
            headers: {
            'Authorization': `Bearer ${this.state.token}`
            }
        }).then(response => {
            if(response.ok) {
                this.setState(state => ({
                    coursesTaken: [...state.coursesTaken, course]
                }));
                this.addCores(course);
            } else {
                console.log("CoursesPage addCourse status", response.statusText);
                logout();
            }
        }).catch((error) => {
            console.log("CoursesPage addCourse error", error);
            logout();
        });
    }

    addCores = (course) => {
        const newCodesTaken = { ...this.state.codesTaken };
        const newCategoriesTaken = { ...this.state.categoriesTaken };
        let newAhCodesTaken = this.state.ahCodesTaken;
        course.coreCodes.forEach(code => {
            if (code.startsWith("AH") && newCodesTaken[code] === 0) {
                newAhCodesTaken += 1;
            }
            newCodesTaken[code] = (newCodesTaken[code] || 0) + 1;
        });
        Object.keys(newCategoriesTaken).forEach(category => {
            const codes = category.split("/");
            if (course.coreCodes.some(code => codes.includes(code))) {
                newCategoriesTaken[category] += 1;
            }
        });
        this.setState({
            codesTaken: newCodesTaken,
            categoriesTaken: newCategoriesTaken,
            ahCodesTaken: newAhCodesTaken
        });
    };

    removeCourse = (course) => {
        fetch(`${apiUrl}/user/remove/${course.id}`, {
            method: "DELETE",
            headers: {
            'Authorization': `Bearer ${this.state.token}`
            }
        }).then(response => {
            if(response.ok) {
                this.setState(state => ({
                    coursesTaken: state.coursesTaken.filter(c => c.id !== course.id)
                }));
                this.removeCores(course);
            } else {
                console.log("CoursesPage removeCourse status", response.statusText);
                logout();
            }
        }).catch((error) => {
            console.log("CoursesPage removeCourse error", error);
            logout();
        });
    }

    removeCores = (course) => {
        const newCodesTaken = { ...this.state.codesTaken };
        const newCategoriesTaken = { ...this.state.categoriesTaken };
        let newAhCodesTaken = this.state.ahCodesTaken;
        course.coreCodes.forEach(code => {
            if (newCodesTaken[code] > 0) {
                newCodesTaken[code] -= 1;
                if (code.startsWith("AH") && newCodesTaken[code] === 0) {
                    newAhCodesTaken -= 1;
                }
            }
        });
        Object.keys(newCategoriesTaken).forEach(category => {
            const codes = category.split("/");
            if (course.coreCodes.some(code => codes.includes(code))) {
                newCategoriesTaken[category] -= 1;
            }
        });
        this.setState({
            codesTaken: newCodesTaken,
            categoriesTaken: newCategoriesTaken,
            ahCodesTaken: newAhCodesTaken
        });
    };

    setCoresTaken = () => {
        const newCodesTaken = Object.fromEntries(cores.map(core => [core, 0]));
        const newCategoriesTaken = {
            "CCD/CCO": 0,
            "SCL/HST": 0,
            "AHo/AHp/AHq/AHr": 0,
            "WCr/WCd": 0,
            "QQ/QR": 0
        };
        let newAhCodesTaken = 0;
        this.state.coursesTaken.forEach(course => {
            course.coreCodes.forEach(code => {
                if (code.startsWith("AH") && newCodesTaken[code] === 0) {
                    newAhCodesTaken += 1;
                }
                newCodesTaken[code] += 1;
            });
            Object.keys(newCategoriesTaken).forEach(category => {
                const codes = category.split("/");
                if (course.coreCodes.some(code => codes.includes(code))) {
                    newCategoriesTaken[category] += 1;
                }
            });
        });
        this.setState({
            codesTaken: newCodesTaken,
            categoriesTaken: newCategoriesTaken,
            ahCodesTaken: newAhCodesTaken
        });
    };

    componentDidMount() {
        fetch(`${apiUrl}/user/courses`, {
            headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
        })
        .then(res => res.json())
        .then(data => {
            this.setState({ coursesTaken: data }, () => {
                this.setCoresTaken();
            });
        })
        .catch(err => {
            console.error("Failed to fetch courses:", err);
        });
    }

    toggleState = () => {
        this.setState(state => ({
            isSearch: !state.isSearch
        }));
    }
    
    render() {
        let table;
        if(this.state.isSearch) {
            table = (<SearchPage
                token={this.state.token} 
                coursesTaken={this.state.coursesTaken} 
                onAdd={this.addCourse} 
                onDelete={this.removeCourse} 
            />);
        } else {
            table = (<CourseTable 
                token={this.state.token} 
                coursesTaken={this.state.coursesTaken} 
                onDelete={this.removeCourse} 
                categoriesTaken={this.state.categoriesTaken} 
                codesTaken={this.state.codesTaken} 
                ahCodesTaken={this.state.ahCodesTaken} 
            />);
        }
        return (
            <div>
                <TopBar isSearch={this.state.isSearch} username={this.state.username} onChange={this.toggleState} />
                <h1>{this.state.isSearch ? "Search" : "My Courses"}</h1>
                {table}
            </div>
        );
    }
}

const root = createRoot(document.getElementById('root'));
root.render(<CoursesPage />);