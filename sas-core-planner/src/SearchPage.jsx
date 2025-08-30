import React from 'react';
import SearchTable from './SearchTable.jsx';
import ResultsTable from './ResultsTable.jsx';
import { apiUrl } from './config.js';
import logout from './Logout.jsx';
import { Navigate } from 'react-router-dom';

class SearchPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            resultCourses: [],
            redirect: false
        }
    }
    handleLogout = () => {
        logout();
        this.setState({ redirect: true });
    }
    getCourses = (cores) => {
        fetch(`${apiUrl}/courses/course-list`, {
            method: 'POST',
            headers: {
            'Content-Type': 'application/json'
            },
            body: JSON.stringify(cores)
        }).then(response => {
            if(response.ok) {
                return response.json();
            } else {
                console.log("ResultsTable not ok", response.statusText);
                this.handleLogout();
            }
        }).then(courses => {
            this.setState({
                resultCourses: courses
            });
        }).catch((error) => {
            console.log("ResultsTable error", error);
            this.handleLogout();
        });
    }
    render() {
        if (this.state.redirect) {
            return <Navigate to="/login" />;
        }
        return (
            <div>
                <SearchTable onSearch={this.getCourses} />
                <ResultsTable coursesTaken={this.props.coursesTaken} resultCourses={this.state.resultCourses} onAdd={this.props.onAdd} onDelete={this.props.onDelete} />
            </div>
        );
    }
}

export default SearchPage;