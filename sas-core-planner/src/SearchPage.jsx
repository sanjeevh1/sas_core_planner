import React from 'react';
import SearchTable from './SearchTable.jsx';
import ResultsTable from './ResultsTable.jsx';
import { logout, apiUrl } from './config.js'

class SearchPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            resultCourses: []
        }
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
                logout();
            }
        }).then(courses => {
            this.setState({
                resultCourses: courses
            });
        }).catch((error) => {
            console.log("ResultsTable error", error);
            logout();
        });
    }
    render() {
        return (
            <div>
                <SearchTable onSearch={this.getCourses} />
                <ResultsTable coursesTaken={this.props.coursesTaken} resultCourses={this.state.resultCourses} onAdd={this.props.onAdd} onDelete={this.props.onDelete} />
            </div>
        );
    }
}

export default SearchPage;