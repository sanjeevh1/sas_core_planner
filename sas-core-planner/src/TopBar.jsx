import React from 'react';
import logout from './Logout.jsx';
import { Navigate } from 'react-router-dom';

class TopBar extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            redirect: false
        };
    }

    handleLogout = () => {
        logout();
        this.setState({ redirect: true });
    }

    render() {
        if (this.state.redirect) {
            return <Navigate to="/login" />;
        }
        return (
            <div className="top-bar">
                <p>{this.props.username}</p>
                <div className="nav-actions">
                    <button onClick={this.props.onChange}>{this.props.isSearch ? "My Courses" : "Search"}</button>
                    <button onClick={this.handleLogout}>Logout</button>
                </div>
            </div>
        );
    }
}

export default TopBar;