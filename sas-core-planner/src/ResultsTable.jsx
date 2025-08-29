import React from 'react';

class ResultRow extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        let handler, buttonText;
        if(this.props.taken) {
            handler = () => {this.props.onDelete(this.props.course)};
            buttonText = '-';
        } else {
            handler = () => {this.props.onAdd(this.props.course)};
            buttonText = '+';
        }
        return (
            <tr>
                <td>{this.props.course.courseNumber}</td>
                <td>{this.props.course.courseTitle}</td>
                <td>{this.props.course.credits}</td>
                <td>{this.props.course.coreCodes.join(", ")}</td>
                <td>{this.props.course.subject}</td>
                <td><button onClick={handler}>{buttonText}</button></td>
            </tr>
        )
    }
}

class ResultsTable extends React.Component {
    constructor(props) {
        super(props);
    }
    
    render() {
        const resultRows = this.props.resultCourses.map(course => (
            <ResultRow key={course.id} course={course} onAdd={this.props.onAdd} onDelete={this.props.onDelete} taken={this.props.coursesTaken.some(takenCourse => takenCourse.id === course.id)} />
        ));
        return (
            <table>
                <thead>
                    <tr>
                        <th>Course No.</th>
                        <th>Title</th>
                        <th>Credits</th>
                        <th>Core Codes</th>
                        <th>Subject</th>
                    </tr>
                </thead>
                <tbody>
                    {resultRows}
                </tbody>
            </table>
        );
    }
}

export default ResultsTable;