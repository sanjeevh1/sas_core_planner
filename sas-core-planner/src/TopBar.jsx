import React from 'react';
import { logout } from './config.js';

class TopBar extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        return (
            <div className="top-bar">
                <p>{this.props.username}</p>
                <div className="nav-actions">
                    <button onClick={this.props.onChange}>{this.props.isSearch ? "My Courses" : "Search"}</button>
                    <button onClick={logout}>Logout</button>
                </div>
            </div>
        );
    }
}

export default TopBar;