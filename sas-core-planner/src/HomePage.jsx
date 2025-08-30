import React from 'react';
import { Link } from 'react-router-dom';

class HomePage extends React.Component {
    render() {
        return (
            <div>
                <div className="top-bar">
                    <h1>SAS Core Planner</h1>
                    <div className="top-actions">
                        <Link to="/register">Register</Link>
                    </div>
                </div>

                <p>Every Rutgers School of Arts and Sciences student has to take courses satisfying certain requirements known as the SAS Core. This website will help you plan the courses you take and track which requirements you are meeting.</p>
                <p>To get started, create an account <Link to="/register">here</Link> and then log in.</p>
                <p>Once you log in, there are two pages you can visit: a search page and a courses page.</p>
                <p>The search page lets you search for courses that satisfy certain combinations of requirements and add these courses to your list.</p>
                <p>The courses page lets you view the courses you are currently taking and track your progress in meeting each requirement.</p>
                <p>The SAS Core Requirements can be found <a href="https://sasundergrad.rutgers.edu/majors-and-core-curriculum/core/about-sas-core">here</a>.</p>
            </div>
        );
    }
}

export default HomePage;