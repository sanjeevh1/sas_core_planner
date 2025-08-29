import React from 'react';
import { cores } from './config.js';

class CoreCell extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        if(this.props.included) {
            return <td style={{backgroundColor: 'green'}}></td>;
        } else {
            return <td></td>;
        }
    }
}

class CourseRow extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        const coreCells = cores.map((core, index) => (
            <CoreCell key={core} included={this.props.course.coreCodes.includes(core)} />
        ));
        return (
            <tr>
                <td>{this.props.course.courseNumber}</td>
                {coreCells}
                <td><button onClick={() => this.props.onDelete(this.props.course)}>-</button></td>
            </tr>
        );
    }
}

class CourseTableHead extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <thead>
                <tr>
                    <th rowSpan="2">Course No.</th>
                    <th colSpan="2">CC (<span>{this.props.categoriesTaken["CCD/CCO"]}</span>/2)</th>
                    <th rowSpan="2">NS (<span>{this.props.codesTaken["NS"]}</span>/2)</th>
                    <th colSpan="2">SCL/HST (<span>{this.props.categoriesTaken["SCL/HST"]}</span>/2)</th>
                    <th colSpan="4">AH (courses: <span>{this.props.categoriesTaken["AHo/AHp/AHq/AHr"]}</span>/2, codes: <span>{this.props.ahCodesTaken}</span>/2)</th>
                    <th colSpan="2">WCr/WCd (<span>{this.props.categoriesTaken["WCr/WCd"]}</span>/2)</th>
                    <th rowSpan="2">WC (<span>{this.props.codesTaken["WC"]}</span>/1)</th>
                    <th colSpan="2">QQ/QR (<span>{this.props.categoriesTaken["QQ/QR"]}</span>/2)</th>
                    <th rowSpan="2">Remove</th>
                </tr>
                <tr>
                    <th>CCD (<span>{this.props.codesTaken["CCD"]}</span>/1)</th>
                    <th>CCO (<span>{this.props.codesTaken["CCO"]}</span>/1)</th>
                    <th>SCL (<span>{this.props.codesTaken["SCL"]}</span>/1)</th>
                    <th>HST (<span>{this.props.codesTaken["HST"]}</span>/1)</th>
                    <th>AHO (<span>{this.props.codesTaken["AHo"]}</span>)</th>
                    <th>AHP (<span>{this.props.codesTaken["AHp"]}</span>)</th>
                    <th>AHQ (<span>{this.props.codesTaken["AHq"]}</span>)</th>
                    <th>AHR (<span>{this.props.codesTaken["AHr"]}</span>)</th>
                    <th>WCR (<span>{this.props.codesTaken["WCr"]}</span>/1)</th>
                    <th>WCD (<span>{this.props.codesTaken["WCd"]}</span>/1)</th>
                    <th>QQ (<span>{this.props.codesTaken["QQ"]}</span>/1)</th>
                    <th>QR (<span>{this.props.codesTaken["QR"]}</span>/1)</th>
                </tr>
            </thead>
        );
    }
}

class CourseTable extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        const courseRows = this.props.coursesTaken.map(course => (
            <CourseRow
                key={course.id}
                course={course}
                onDelete={this.props.onDelete}
            />
        ));
        return (
            <table>
                <CourseTableHead
                    categoriesTaken={this.props.categoriesTaken}
                    codesTaken={this.props.codesTaken}
                    ahCodesTaken={this.props.ahCodesTaken}
                />
                <tbody>
                    {courseRows}
                </tbody>
            </table>
        )
    }
}

export default CourseTable;